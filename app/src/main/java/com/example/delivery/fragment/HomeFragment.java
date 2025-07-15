package com.example.delivery.fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.delivery.activity.AIChatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.activity.FoodActivity;
import com.example.delivery.adapter.HotFoodAdapter;
import com.example.delivery.adapter.StoreAdapter;
import com.example.delivery.entity.Cart;
import com.example.delivery.entity.Food;
import com.example.delivery.entity.Store;
import com.example.delivery.sqlite.BusinessResult;
import com.example.delivery.sqlite.CartDB;
import com.example.delivery.sqlite.FoodDB;
import com.example.delivery.sqlite.StoreDB;
import com.example.delivery.utils.CurrentUserUtils;

import java.text.SimpleDateFormat;

public class HomeFragment extends Fragment {

    private RecyclerView rvStore, rvHotFood;

    private EditText etSearch;

    private TextView tvDate;

    private StoreAdapter storeAdapter;

    private HotFoodAdapter hotFoodAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // 找到按钮并设置点击事件（根据布局中实际控件类型选择 AppCompatButton 或 FloatingActionButton
//        FloatingActionButton fabAIChat = rootView.findViewById(R.id.fab_ai_chat);
//        fabAIChat.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), AIChatActivity.class);
//            startActivity(intent);
//        });
        ExtendedFloatingActionButton fabAIChat = rootView.findViewById(R.id.fab_ai_chat);
        fabAIChat.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AIChatActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView();
        initData();
        initView();
    }

    private void bindView() {
        rvHotFood = getView().findViewById(R.id.rv_hot_food);
        rvStore = getView().findViewById(R.id.rv_store);
        etSearch = getView().findViewById(R.id.et_search);
        tvDate = getView().findViewById(R.id.tv_date);
    }

    private void initData() {
        storeAdapter = new StoreAdapter();
        storeAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onClick(Store item) {
                Intent intent = new Intent(getContext(), FoodActivity.class);
                intent.putExtra("store", item);
                startActivity(intent);
            }
        });
        hotFoodAdapter = new HotFoodAdapter();
        hotFoodAdapter.setOnItemClickListener(new HotFoodAdapter.OnItemClickListener() {
            @Override
            public void onAdd(Food item) {
                BusinessResult<Cart> result = CartDB.add(item, CurrentUserUtils.getCurrentUser().getId());
                Toast.makeText(getContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        rvHotFood.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvHotFood.setAdapter(hotFoodAdapter);
        rvStore.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStore.setAdapter(storeAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                storeAdapter.setList(StoreDB.selectByName(s.toString()).getData());
            }
        });
        // 设置日期 格式为 月 日
        tvDate.setText(getDate());
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
        return sdf.format(System.currentTimeMillis());
    }

    @Override
    public void onResume() {
        super.onResume();
        hotFoodAdapter.setList(FoodDB.selectRandom().getData());
        storeAdapter.setList(StoreDB.selectAll().getData());
    }
}
