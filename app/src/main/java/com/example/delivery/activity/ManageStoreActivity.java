package com.example.delivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.adapter.ManageStoreAdapter;
import com.example.delivery.adapter.StoreAdapter;
import com.example.delivery.entity.Store;
import com.example.delivery.sqlite.StoreDB;
import com.example.delivery.utils.TitleBarUtils;

public class ManageStoreActivity extends AppCompatActivity {

    private ManageStoreAdapter storeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_store);
        TitleBarUtils.setTitle(this, "店铺管理");
        TitleBarUtils.setBack(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TitleBarUtils.setRight(this, "添加", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageStoreActivity.this, StoreEditActivity.class);
                startActivity(intent);
            }
        });
        //绑定控件
        RecyclerView rvStore = findViewById(R.id.rv_store);
        storeAdapter = new ManageStoreAdapter();
        storeAdapter.setOnItemClickListener(new ManageStoreAdapter.OnItemClickListener() {
            @Override
            public void onClick(Store item) {
                Intent intent = new Intent(ManageStoreActivity.this, ManageFoodActivity.class);
                intent.putExtra("store", item);
                startActivity(intent);
            }
        });
        rvStore.setAdapter(storeAdapter);
        rvStore.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        storeAdapter.setList(StoreDB.selectAll().getData());
    }
}
