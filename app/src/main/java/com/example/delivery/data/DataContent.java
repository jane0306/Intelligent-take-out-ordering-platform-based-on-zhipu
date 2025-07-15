package com.example.delivery.data;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.example.delivery.entity.Food;
import com.example.delivery.entity.Store;
import com.example.delivery.sqlite.BusinessResult;
import com.example.delivery.sqlite.FoodDB;
import com.example.delivery.sqlite.StoreDB;
import com.example.delivery.utils.AppUtils;
import com.example.delivery.utils.ImagePreStorageUtils;
import com.example.delivery.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataContent {
    /**
     * 注意！！
     * 注意！！
     * 注意！！
     * 当前数据编辑以后，需要卸载app，重新安装才能生效
     */
    public static void init(){
        // 初始化数据
        BusinessResult<List<Store>> result = StoreDB.selectAll();
        if (result.isSuccess() && !result.getData().isEmpty()) {
            return;
        }

        List<Store> storeAssetsList = getStoreList();
        List<Food> foodAssetsList = getFoodList();
        for (Store store : storeAssetsList) {
            Integer storeId = StoreDB.insert(store).getData().getId();
            //打乱菜品顺序
            for (int i = 0; i < foodAssetsList.size(); i++) {
                int randomIndex = (int) (Math.random() * foodAssetsList.size());
                Food temp = foodAssetsList.get(i);
                foodAssetsList.set(i, foodAssetsList.get(randomIndex));
                foodAssetsList.set(randomIndex, temp);
            }
            for (Food food : foodAssetsList) {
                food.setStoreId(storeId);
                FoodDB.insert(food);
            }
        }
    }

    /**
     * 使用gson从assets中读取store.json的数据
     * 获取店铺列表
     */
    private static List<Store> getStoreList() {
        //读取assets中的store.json文件
        String json = getJson("store.json");
        //将json转换为List<Store>
        List<Store> storeList = JsonUtils.parse(json, new TypeToken<List<Store>>() {
        });
        for (Store store : storeList) {
            String url = ImagePreStorageUtils.saveAssetsImage(store.getImg(), store.getName());
            store.setImg(url);
        }
        return storeList;
    }

    /**
     * 获取菜品列表
     */
    public static List<Food> getFoodList() {
        String json = getJson("food.json");
        List<Food> foodList = JsonUtils.parse(json, new TypeToken<List<Food>>() {
        });
        for (Food food : foodList) {
            String foodUrl = ImagePreStorageUtils.saveAssetsImage(food.getImg(), food.getName());
            food.setImg(foodUrl);
        }
        return foodList;
    }

    private static String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = AppUtils.getApplication().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));

            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
