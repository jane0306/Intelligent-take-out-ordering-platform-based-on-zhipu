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
import com.example.delivery.entity.Store;
import com.example.delivery.sqlite.BusinessResult;
import com.example.delivery.sqlite.StoreDB;
import com.example.delivery.utils.AlbumUtils;
import com.example.delivery.utils.TitleBarUtils;

public class StoreEditActivity extends AppCompatActivity {

    private ImageView ivImg;

    private TextView tvSelect;

    private EditText etName,etDesc;

    private Button btnSubmit;

    private Store store;

    private boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_edit);
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
    }

    private void initData() {
        store = (Store) getIntent().getSerializableExtra("store");
        if (store != null) {
            TitleBarUtils.setTitle(this, "编辑店铺");
            TitleBarUtils.setRight(this, "删除", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BusinessResult<Void> result = StoreDB.delete(store.getId());
                    Toast.makeText(StoreEditActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if (result.isSuccess()) {
                        Intent intent = new Intent();
                        intent.putExtra("isDelete", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
            Glide.with(this).load(store.getImg()).into(ivImg);
            etName.setText(store.getName());
            etDesc.setText(store.getDescription());
            isEdit = true;
        } else {
            TitleBarUtils.setTitle(this, "添加店铺");
            store = new Store();
            isEdit = false;
        }
    }

    private void initView() {
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片
                AlbumUtils.openAlbum(StoreEditActivity.this);
            }
        });
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择图片
                AlbumUtils.openAlbum(StoreEditActivity.this);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store.setName(etName.getText().toString());
                store.setDescription(etDesc.getText().toString());
                BusinessResult<Store> result;
                if (isEdit) {
                    //编辑
                    result = StoreDB.update(store);
                } else {
                    //添加
                    result = StoreDB.insert(store);
                }
                Toast.makeText(StoreEditActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                if (result.isSuccess()) {
                    Intent intent = new Intent();
                    intent.putExtra("store", store);
                    intent.putExtra("isDelete", false);
                    setResult(RESULT_OK, intent);
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
                store.setImg(path);
            }
        }
    }
}
