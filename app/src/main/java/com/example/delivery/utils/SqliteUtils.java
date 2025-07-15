package com.example.delivery.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteUtils extends SQLiteOpenHelper {

    public SqliteUtils() {
        super(AppUtils.getApplication(), "delivery.db", null, 1);
    }

    /**
     * 创建并获取单例
     */
    public static SqliteUtils getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*
        用户表(_user)：
        _id       integer  用户id
        username  varchar  用户名
        password  varchar  密码
         */
        sqLiteDatabase.execSQL("CREATE TABLE _user(_id INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR(20) ,password VARCHAR(20))");
        /*
        店铺表(_store)：
        _id          integer  店铺id
        name         varchar  店铺名称
        img          varchar  店铺图片url
        description  varchar  店铺描述
         */
        sqLiteDatabase.execSQL("CREATE TABLE _store(_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20),img VARCHAR(200),description VARCHAR(200))");
        /*
        菜品表(_food)：
        _id          integer  菜品id
        store_id     integer  店铺id
        name         varchar  菜品名称
        img          varchar  菜品图片url
        description  varchar  菜品描述
        price        real     菜品价格
         */
        sqLiteDatabase.execSQL("CREATE TABLE _food(_id INTEGER PRIMARY KEY AUTOINCREMENT,store_id INTEGER,name VARCHAR(20),img VARCHAR(200),description VARCHAR(200),price REAL)");
        /*
        购物车表(_cart)：
        _id          integer  购物车id
        user_id      integer  用户id
        food_id      integer  菜品id
        count        integer  菜品数量
         */
        sqLiteDatabase.execSQL("CREATE TABLE _cart(_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,food_id INTEGER,count INTEGER)");
        /*
        订单表(_order)：
        _id          integer  订单id
        user_id      integer  用户id
        pay_time     text     支付时间
        total_price  real     总价
         */
        sqLiteDatabase.execSQL("CREATE TABLE _order(_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER,pay_time TEXT,total_price REAL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    private static final class InstanceHolder {
        /**
         * 单例
         */
        static final SqliteUtils instance = new SqliteUtils();
    }
}
