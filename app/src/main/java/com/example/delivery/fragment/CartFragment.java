package com.example.delivery.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.adapter.FoodCartAdapter;
import com.example.delivery.entity.Cart;
import com.example.delivery.entity.Food;
import com.example.delivery.entity.Order;
import com.example.delivery.sqlite.CartDB;
import com.example.delivery.sqlite.OrderDB;
import com.example.delivery.utils.CurrentUserUtils;
import com.example.delivery.utils.TitleBarUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;

    private FoodCartAdapter foodCartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        TitleBarUtils.setTitle(this, "购物车");
        TitleBarUtils.setRight(this, "结算", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Cart> list = foodCartAdapter.getList();
                if (list == null || list.size() == 0) {
                    Toast.makeText(getContext(), "购物车为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                float total = 0;
                for (Cart cart : list) {
                    Food food = cart.getFood();
                    total += cart.getCount() * food.getPrice();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("当前需支付" + total + "元");
                builder.setPositiveButton("否", null);
                float finalTotal = total;
                builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Integer userId = CurrentUserUtils.getCurrentUser().getId();
                        CartDB.deleteByUserId(userId);
                        foodCartAdapter.clear();
                        Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
                        // 生成订单
                        Order order = new Order();
                        order.setUserId(userId);
                        order.setTotalPrice(finalTotal);
                        String payTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        order.setPayTime(payTime);
                        OrderDB.add(userId, order);
                    }
                });
                builder.show();
            }
        });
        bindView();
        initView();
    }

    private void bindView() {
        foodCartAdapter = new FoodCartAdapter();
    }

    private void initView() {
        recyclerView = getView().findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(foodCartAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        foodCartAdapter.setList(CartDB.queryByUserId(CurrentUserUtils.getCurrentUser().getId()).getData());
    }
}
