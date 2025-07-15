package com.example.delivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.delivery.R;
import com.example.delivery.entity.Food;
import com.example.delivery.entity.Store;
import com.example.delivery.sqlite.BusinessResult;
import com.example.delivery.sqlite.FoodDB;
import com.example.delivery.utils.AlbumUtils;
import com.example.delivery.utils.TitleBarUtils;

public class FoodEditActivity extends AppCompatActivity {

    private ImageView ivImg;

    private TextView tvSelect;

    private EditText etName, etDesc, etPrice;

    private Button btnSubmit;

    private Food food;

    private Store store;

    private boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);
        TitleBarUtils.setBack(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //绑定控件
        bindView();
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

    private void bindView() {
        ivImg = findViewById(R.id.iv_img);
        tvSelect = findViewById(R.id.tv_select);
        btnSubmit = findViewById(R.id.btn_submit);
        etName = findViewById(R.id.et_name);
        etDesc = findViewById(R.id.et_desc);
        etPrice = findViewById(R.id.et_price);
    }

    private void initData() {
        food = (Food) getIntent().getSerializableExtra("food");
        store = (Store) getIntent().getSerializableExtra("store");
        if (food != null) {
            TitleBarUtils.setTitle(this, "编辑菜品");
            TitleBarUtils.setRight(this, "删除", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BusinessResult<Void> result = FoodDB.delete(food.getId());
                    Toast.makeText(FoodEditActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.isSuccess()) {
                        finish();
                    }
                }
            });
            Glide.with(this).load(food.getImg()).into(ivImg);
            etName.setText(food.getName());
            etDesc.setText(food.getDescription());
            etPrice.setText(String.format("%.2f", food.getPrice()));
            isEdit = true;
        } else {
            TitleBarUtils.setTitle(this, "添加菜品");
            food = new Food();
            isEdit = false;
        }
    }

    private void initView() {
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片
                AlbumUtils.openAlbum(FoodEditActivity.this);
            }
        });
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片
                AlbumUtils.openAlbum(FoodEditActivity.this);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food.setStoreId(store.getId());
                food.setName(etName.getText().toString());
                food.setDescription(etDesc.getText().toString());
                food.setPrice(Float.parseFloat(etPrice.getText().toString()));
                BusinessResult<Void> result;
                if (isEdit) {
                    //编辑
                    result = FoodDB.update(food);
                } else {
                    //添加
                    result = FoodDB.insert(food);
                }
                Toast.makeText(FoodEditActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.isSuccess()) {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //如果是打开相册，获取图片路径，显示图片
            if (requestCode == AlbumUtils.OPEN_ALBUM_REQUEST_CODE) {
                String path = AlbumUtils.getImagePath(data);
                Glide.with(this).load(path).into(ivImg);
                food.setImg(path);
            }
        }
    }
}
