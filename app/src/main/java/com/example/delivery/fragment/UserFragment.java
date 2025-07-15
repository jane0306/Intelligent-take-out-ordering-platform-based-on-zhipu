package com.example.delivery.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.activity.LoginActivity;
import com.example.delivery.adapter.OrderAdapter;
import com.example.delivery.entity.Order;
import com.example.delivery.sqlite.BusinessResult;
import com.example.delivery.sqlite.OrderDB;
import com.example.delivery.utils.CurrentUserUtils;
import com.example.delivery.utils.TitleBarUtils;

import java.util.List;

public class UserFragment extends Fragment {

    private TextView tvUsername;

    private RecyclerView rv;

    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        TitleBarUtils.setTitle(this, "个人中心");
        TitleBarUtils.setRight(this, "退出", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("提示");
                builder.setMessage("确认退出登录");
                builder.setPositiveButton("否", null);
                builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentActivity activity = getActivity();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                });
                builder.show();

            }
        });
        bindView();
        initData();
        initView();
    }

    private void bindView() {
        tvUsername = getView().findViewById(R.id.tv_username);
        rv = getView().findViewById(R.id.rv);
    }

    private void initData() {
        orderAdapter = new OrderAdapter();
    }

    private void initView() {
        tvUsername.setText(CurrentUserUtils.getCurrentUser().getUsername());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(orderAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusinessResult<List<Order>> result = OrderDB.queryByUserId(CurrentUserUtils.getCurrentUser().getId());
        orderAdapter.setList(result.getData());
    }
}
