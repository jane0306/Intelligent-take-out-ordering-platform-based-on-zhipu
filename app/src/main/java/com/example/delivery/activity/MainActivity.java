package com.example.delivery.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.delivery.R;
import com.example.delivery.fragment.CartFragment;
import com.example.delivery.fragment.HomeFragment;
import com.example.delivery.fragment.UserFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.delivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragmentList;

    private ViewPager2 pager;

    private NavigationBarView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 关联主页面布局

        // 仅保留 Fragment 初始化逻辑
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new CartFragment());
        fragmentList.add(new UserFragment());
        bindView();   // 调用 Fragment 绑定方法
        initView();   // 调用 Fragment 初始化方法
    }

    private void bindView() {
        pager = findViewById(R.id.pager);
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void initView() {

        pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });
        pager.setOffscreenPageLimit(fragmentList.size());
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }
        });
        pager.setUserInputEnabled(false);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                pager.setCurrentItem(item.getOrder(), false);
                return true;
            }
        });
    }
}