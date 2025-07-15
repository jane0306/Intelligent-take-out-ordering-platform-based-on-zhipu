package com.example.delivery.sqlite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.delivery.entity.Food;
import com.example.delivery.utils.SqliteUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressLint("Range")
public class FoodDB {

    /**
     * 根据店铺id查询菜品
     */
    public static BusinessResult<List<Food>> selectByStoreId(Integer storeId) {
        BusinessResult<List<Food>> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from _food where store_id=? order by _id desc", new String[]{String.valueOf(storeId)});
        List<Food> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int foodId = cursor.getInt(cursor.getColumnIndex("_id"));
            Food food = new Food();
            food.setId(foodId);
            food.setStoreId(cursor.getInt(cursor.getColumnIndex("store_id")));
            food.setName(cursor.getString(cursor.getColumnIndex("name")));
            food.setImg(cursor.getString(cursor.getColumnIndex("img")));
            food.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            food.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
            list.add(food);
        }
        cursor.close();
        result.setSuccess(true);
        result.setMessage("查询成功");
        result.setData(list);
        return result;
    }

    /**
     * 随机查询5个菜品
     */
    public static BusinessResult<List<Food>> selectRandom() {
        BusinessResult<List<Food>> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from _food order by random() limit 5", null);
        List<Food> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int foodId = cursor.getInt(cursor.getColumnIndex("_id"));
            Food food = new Food();
            food.setId(foodId);
            food.setStoreId(cursor.getInt(cursor.getColumnIndex("store_id")));
            food.setName(cursor.getString(cursor.getColumnIndex("name")));
            food.setImg(cursor.getString(cursor.getColumnIndex("img")));
            food.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            food.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
            list.add(food);
        }
        cursor.close();
        result.setSuccess(true);
        result.setMessage("查询成功");
        result.setData(list);
        return result;
    }

    /**
     * 添加菜品
     */
    public static BusinessResult<Void> insert(Food food) {
        if (TextUtils.isEmpty(food.getName())) {
            return new BusinessResult<>(false, "菜品名称不能为空", null);
        }
        if (food.getPrice() == null) {
            return new BusinessResult<>(false, "菜品价格不能为空", null);
        }
        if (TextUtils.isEmpty(food.getImg())) {
            return new BusinessResult<>(false, "菜品图片不能为空", null);
        }
        if (TextUtils.isEmpty(food.getDescription())) {
            return new BusinessResult<>(false, "菜品描述不能为空", null);
        }

        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.execSQL("insert into _food(store_id,name,img,description,price) values(?,?,?,?,?)",
                new Object[]{food.getStoreId(), food.getName(), food.getImg(), food.getDescription(), food.getPrice()});
        result.setSuccess(true);
        result.setMessage("添加成功");
        return result;
    }

    /**
     * 修改菜品
     */
    public static BusinessResult<Void> update(Food food) {
        if (TextUtils.isEmpty(food.getName())) {
            return new BusinessResult<>(false, "菜品名称不能为空", null);
        }
        if (food.getPrice() == null) {
            return new BusinessResult<>(false, "菜品价格不能为空", null);
        }
        if (TextUtils.isEmpty(food.getImg())) {
            return new BusinessResult<>(false, "菜品图片不能为空", null);
        }
        if (TextUtils.isEmpty(food.getDescription())) {
            return new BusinessResult<>(false, "菜品描述不能为空", null);
        }
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.execSQL("update _food set store_id=?,name=?,img=?,description=?,price=? where _id=?",
                new Object[]{food.getStoreId(), food.getName(), food.getImg(), food.getDescription(), food.getPrice(), food.getId()});
        result.setSuccess(true);
        result.setMessage("修改成功");
        return result;
    }

    /**
     * 删除菜品
     */
    public static BusinessResult<Void> delete(Integer id) {
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.execSQL("delete from _food where _id=?", new Object[]{id});
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }

    public static BusinessResult<Food> getById(Integer foodId) {
        BusinessResult<Food> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from _food where _id=?", new String[]{String.valueOf(foodId)});
        Food food = null;
        if (cursor.moveToNext()) {
            food = new Food();
            food.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            food.setStoreId(cursor.getInt(cursor.getColumnIndex("store_id")));
            food.setName(cursor.getString(cursor.getColumnIndex("name")));
            food.setImg(cursor.getString(cursor.getColumnIndex("img")));
            food.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            food.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
        }
        cursor.close();
        if (food == null) {
            result.setSuccess(false);
            result.setMessage("菜品不存在");
        } else {
            result.setSuccess(true);
            result.setMessage("查询成功");
            result.setData(food);
        }
        return result;
    }
}
