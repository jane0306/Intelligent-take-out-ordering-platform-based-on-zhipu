package com.example.delivery.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delivery.R;
import com.example.delivery.adapter.FoodAdapter;
import com.example.delivery.data.DataContent;
import com.example.delivery.entity.Cart;
import com.example.delivery.entity.Food;
import com.example.delivery.entity.Store;
import com.example.delivery.sqlite.BusinessResult;
import com.example.delivery.sqlite.CartDB;
import com.example.delivery.sqlite.FoodDB;
import com.example.delivery.utils.CurrentUserUtils;
import com.example.delivery.utils.TitleBarUtils;

public class FoodActivity extends AppCompatActivity {

    private TextView tvStore;

    private RecyclerView rvFood;

    private ImageView ivStore;

    private Store store;

    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        TitleBarUtils.setTitle(this, "菜单");
        TitleBarUtils.setBack(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindView();
        initData();
        initView();
    }

    private void bindView() {
        tvStore = findViewById(R.id.tv_store);
        rvFood = findViewById(R.id.rv_food);
        ivStore = findViewById(R.id.iv_store);
    }

    private void initData() {
        store = (Store) getIntent().getSerializableExtra("store");
        foodAdapter = new FoodAdapter();
        foodAdapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onAdd(Food item) {
                BusinessResult<Cart> result = CartDB.add(item, CurrentUserUtils.getCurrentUser().getId());
                Toast.makeText(FoodActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        Glide.with(this).load(store.getImg()).into(ivStore);
        tvStore.setText(store.getName());
        rvFood.setLayoutManager(new LinearLayoutManager(this));
        rvFood.setAdapter(foodAdapter);
        foodAdapter.setList(FoodDB.selectByStoreId(store.getId()).getData());
    }
}
