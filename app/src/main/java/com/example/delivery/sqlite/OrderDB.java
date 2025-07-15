package com.example.delivery.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.delivery.entity.Order;
import com.example.delivery.utils.SqliteUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderDB {

    public static BusinessResult<Order> add(Integer userId, Order order) {
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("pay_time", order.getPayTime());
        values.put("total_price", order.getTotalPrice());
        long insert = db.insert("_order", null, values);
        if (insert == -1) {
            return new BusinessResult<>(false, "添加失败", null);
        } else {
            return new BusinessResult<>(true, "添加成功", order);
        }
    }

    public static BusinessResult<List<Order>> queryByUserId(Integer userId) {
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        Cursor cursor = db.query("_order", null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, null);
        List<Order> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Order order = new Order();
            order.setUserId(cursor.getInt(1));
            order.setPayTime(cursor.getString(2));
            order.setTotalPrice(cursor.getFloat(3));
            list.add(order);
        }
        cursor.close();
        return new BusinessResult<>(true, "查询成功", list);
    }
}
