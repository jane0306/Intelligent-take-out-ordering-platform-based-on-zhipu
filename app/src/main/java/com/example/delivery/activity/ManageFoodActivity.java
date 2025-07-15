package com.example.delivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delivery.R;
import com.example.delivery.adapter.ManageFoodAdapter;
import com.example.delivery.data.DataContent;
import com.example.delivery.entity.Food;
import com.example.delivery.entity.Store;
import com.example.delivery.entity.User;
import com.example.delivery.sqlite.FoodDB;
import com.example.delivery.utils.TitleBarUtils;

public class ManageFoodActivity extends AppCompatActivity {

    private TextView tvStore,tvAdd;

    private RecyclerView rvFood;

    private ImageView ivStore;

    private Store store;

    private ManageFoodAdapter manageFoodAdapter;

    /**
     * 界面跳转回调
     */
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    boolean isDelete = result.getData().getBooleanExtra("isDelete", false);
                    if (isDelete) {
                        finish();
                        return;
                    }
                    //编辑店铺成功
                    store = (Store) result.getData().getSerializableExtra("store");
                    Glide.with(ManageFoodActivity.this).load(store.getImg()).into(ivStore);
                    tvStore.setText(store.getName());
                }
            }
        });

        TitleBarUtils.setTitle(this, "店铺详情");
        TitleBarUtils.setBack(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TitleBarUtils.setRight(this, "编辑店铺", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageFoodActivity.this, StoreEditActivity.class);
                intent.putExtra("store", store);
                activityResultLauncher.launch(intent);
            }
        });
        bindView();
        initData();
        initView();
    }

    private void bindView() {
        tvStore = findViewById(R.id.tv_store);
        rvFood = findViewById(R.id.rv_manage_food);
        ivStore = findViewById(R.id.iv_store);
        tvAdd = findViewById(R.id.tv_add);
    }

    private void initData() {
        store = (Store) getIntent().getSerializableExtra("store");
        manageFoodAdapter = new ManageFoodAdapter();
        manageFoodAdapter.setOnItemClickListener(new ManageFoodAdapter.OnItemClickListener() {
            @Override
            public void onItem(int position, Food item) {
                Intent intent = new Intent(ManageFoodActivity.this, FoodEditActivity.class);
                intent.putExtra("food", item);
                intent.putExtra("store", store);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        Glide.with(this).load(store.getImg()).into(ivStore);
        tvStore.setText(store.getName());
        rvFood.setLayoutManager(new LinearLayoutManager(this));
        rvFood.setAdapter(manageFoodAdapter);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageFoodActivity.this, FoodEditActivity.class);
                intent.putExtra("store", store);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageFoodAdapter.setList(FoodDB.selectByStoreId(store.getId()).getData());
    }
}
