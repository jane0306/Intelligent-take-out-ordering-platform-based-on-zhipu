package com.example.delivery.sqlite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.delivery.entity.Store;
import com.example.delivery.utils.SqliteUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class StoreDB {

    /**
     * 查询所有店铺
     */
    public static BusinessResult<List<Store>> selectAll() {
        BusinessResult<List<Store>> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        List<Store> list = new ArrayList<>();
        //根据id倒序查询
        Cursor cursor = db.rawQuery("select * from _store order by _id desc", null);
        while (cursor.moveToNext()) {
            int storeId = cursor.getInt(cursor.getColumnIndex("_id"));
            Store store = new Store();
            store.setId(storeId);
            store.setName(cursor.getString(cursor.getColumnIndex("name")));
            store.setImg(cursor.getString(cursor.getColumnIndex("img")));
            store.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            list.add(store);
        }
        cursor.close();
        result.setSuccess(true);
        result.setMessage("查询成功");
        result.setData(list);
        return result;
    }

    /**
     * 根据店铺名称查询店铺
     */
    public static BusinessResult<List<Store>> selectByName(String name) {
        BusinessResult<List<Store>> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getReadableDatabase();
        //根据id倒序查询,模糊查询
        Cursor cursor = db.rawQuery("select * from _store where name like ? order by _id desc", new String[]{"%" + name + "%"});
        List<Store> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int storeId = cursor.getInt(cursor.getColumnIndex("_id"));
            Store store = new Store();
            store.setId(storeId);
            store.setName(cursor.getString(cursor.getColumnIndex("name")));
            store.setImg(cursor.getString(cursor.getColumnIndex("img")));
            store.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            list.add(store);
        }
        cursor.close();
        result.setSuccess(true);
        result.setMessage("查询成功");
        result.setData(list);
        return result;
    }

    /**
     * 添加店铺
     */
    public static BusinessResult<Store> insert(Store store) {
        if (TextUtils.isEmpty(store.getName())) {
            return new BusinessResult<>(false, "店铺名称不能为空", null);
        }
        if (TextUtils.isEmpty(store.getImg())) {
            return new BusinessResult<>(false, "店铺图片不能为空", null);
        }
        if (TextUtils.isEmpty(store.getDescription())) {
            return new BusinessResult<>(false, "店铺描述不能为空", null);
        }
        BusinessResult<Store> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.execSQL("insert into _store(name,img,description) values(?,?,?)", new Object[]{store.getName(), store.getImg(), store.getDescription()});
        result.setSuccess(true);
        result.setMessage("添加成功");
        //回填_id
        Cursor cursor = db.rawQuery("select last_insert_rowid() from _store", null);
        if (cursor.moveToFirst()) {
            store.setId(cursor.getInt(0));
        }
        cursor.close();
        result.setData(store);
        return result;
    }

    /**
     * 修改店铺
     */
    public static BusinessResult<Store> update(Store store) {
        if (TextUtils.isEmpty(store.getName())) {
            return new BusinessResult<>(false, "店铺名称不能为空", null);
        }
        if (TextUtils.isEmpty(store.getImg())) {
            return new BusinessResult<>(false, "店铺图片不能为空", null);
        }
        if (TextUtils.isEmpty(store.getDescription())) {
            return new BusinessResult<>(false, "店铺描述不能为空", null);
        }
        BusinessResult<Store> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.execSQL("update _store set name=?,img=?,description=? where _id=?", new Object[]{store.getName(), store.getImg(), store.getDescription(), store.getId()});
        result.setSuccess(true);
        result.setMessage("修改成功");
        result.setData(store);
        return result;
    }

    /**
     * 删除店铺
     */
    public static BusinessResult<Void> delete(Integer id) {
        BusinessResult<Void> result = new BusinessResult<>();
        SQLiteDatabase db = SqliteUtils.getInstance().getWritableDatabase();
        db.execSQL("delete from _store where _id=?", new Object[]{id});
        result.setSuccess(true);
        result.setMessage("删除成功");
        return result;
    }

}
