package com.example.delivery.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.delivery.entity.Cart;
import com.example.delivery.entity.Food;
import com.example.delivery.utils.CurrentUserUtils;
import com.example.delivery.utils.SqliteUtils;

import java.util.ArrayList;
import java.util.List;

public class CartDB {

    /**
     * 添加购物车中的菜品
     */
    public static BusinessResult<Cart> add(Food food, Integer userId) {
        if (userId == null) {
            return new BusinessResult<>(false, "用户id不能为空", null);
        }
        BusinessResult<Cart> getByFoodIdAndUserId = getByFoodIdAndUserId(food.getId(), userId);
        if (getByFoodIdAndUserId.isSuccess()) {
            Cart result = getByFoodIdAndUserId.getData();
            result.setCount(result.getCount() + 1);
            BusinessResult<Food> update = updateCount(result.getId(), result.getCount());
            if (update.isSuccess()) {
                return new BusinessResult<>(true, "添加成功", result);
            } else {
                return new BusinessResult<>(false, "添加失败", null);
            }
        } else {
            return insert(food);
        }
    }

    /**
     * 减少购物车中的菜品
     */
    public static BusinessResult<Cart> minus(Food food, Integer userId) {
        BusinessResult<Cart> getByFoodIdAndUserId = getByFoodIdAndUserId(food.getId(), userId);
        if (getByFoodIdAndUserId.isSuccess()) {
            Cart result = getByFoodIdAndUserId.getData();
            if (result.getCount() > 1) {
                result.setCount(result.getCount() - 1);
                BusinessResult<Food> update = updateCount(result.getId(), result.getCount());
                if (update.isSuccess()) {
                    return new BusinessResult<>(true, "减少成功", result);
                } else {
                    return new BusinessResult<>(false, "减少失败", null);
                }
            } else {
                return delete(food.getId());
            }
        } else {
            return new BusinessResult<>(false, "菜品不存在", null);
        }
    }

    /**
     * 根据购物车id删除
     */
    private static BusinessResult<Cart> delete(Integer cartId) {
        if (cartId == null) {
            return new BusinessResult<>(false, "购物车id不能为空", null);
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        String sql = "DELETE FROM _cart WHERE _id=?";
        String[] args = new String[]{String.valueOf(cartId)};
        db.execSQL(sql, args);
        return new BusinessResult<>(true, "删除成功", null);
    }

    /**
     * 添加
     */
    private static BusinessResult<Cart> insert(Food food) {
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        Cart cart = new Cart();
        cart.setUserId(CurrentUserUtils.getCurrentUser().getId());
        cart.setFoodId(food.getId());
        cart.setFood(food);
        ContentValues values = new ContentValues();
        values.put("user_id", cart.getUserId());
        values.put("food_id", cart.getFoodId());
        values.put("count", 1);
        long result = db.insert("_cart", null, values);
        if (result == -1) {
            return new BusinessResult<>(false, "添加失败", null);
        }
        return new BusinessResult<>(true, "添加成功", cart);
    }

    /**
     * 更新
     */
    private static BusinessResult<Food> updateCount(Integer cartId, Integer count) {
        if (cartId == null) {
            return new BusinessResult<>(false, "购物车id不能为空", null);
        }
        if (count == null) {
            return new BusinessResult<>(false, "数量不能为空", null);
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        String sql = "UPDATE _cart SET count=? WHERE _id=?";
        String[] args = new String[]{String.valueOf(count), String.valueOf(cartId)};
        db.execSQL(sql, args);
        return new BusinessResult<>(true, "更新成功", null);
    }

    /**
     * 根据Food的name和userId查询
     */
    public static BusinessResult<Cart> getByFoodIdAndUserId(Integer foodId, Integer userId) {
        if (foodId == null || userId == null) {
            return new BusinessResult<>(false, "菜品id和用户id不能为空", null);
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        String sql = "SELECT * FROM _cart WHERE food_id=? AND user_id=?";
        String[] args = new String[]{String.valueOf(foodId), String.valueOf(userId)};
        Cart result = null;
        try (Cursor cursor = db.rawQuery(sql, args)) {
            if (cursor.moveToNext()) {
                result = new Cart();
                result.setId(cursor.getInt(0));
                result.setUserId(cursor.getInt(1));
                result.setFoodId(cursor.getInt(2));
                result.setCount(cursor.getInt(3));
            }
        }
        if (result == null) {
            return new BusinessResult<>(false, "菜品不存在", null);
        }
        return new BusinessResult<>(true, "菜品已存在", result);
    }

    public static BusinessResult<List<Cart>> queryByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        String sql = "SELECT * FROM _cart WHERE user_id=?";
        String[] args = new String[]{String.valueOf(userId)};
        List<Cart> result = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, args)) {
            while (cursor.moveToNext()) {
                Cart cart = new Cart();
                cart.setId(cursor.getInt(0));
                cart.setUserId(cursor.getInt(1));
                cart.setFoodId(cursor.getInt(2));
                cart.setCount(cursor.getInt(3));
                BusinessResult<Food> byId = FoodDB.getById(cart.getFoodId());
                if (byId.isSuccess()) {
                    cart.setFood(byId.getData());
                }else{
                    deleteById(cart.getId());
                    continue;
                }
                result.add(cart);
            }
        }
        return new BusinessResult<>(true, "查询成功", result);
    }

    public static void deleteByUserId(Integer userId) {
        if (userId == null) {
            return;
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        String sql = "DELETE FROM _cart WHERE user_id=?";
        String[] args = new String[]{String.valueOf(userId)};
        db.execSQL(sql, args);
    }

    public static void deleteById(Integer cartId) {
        if (cartId == null) {
            return;
        }
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        String sql = "DELETE FROM _cart WHERE _id=?";
        String[] args = new String[]{String.valueOf(cartId)};
        db.execSQL(sql, args);
    }
}
