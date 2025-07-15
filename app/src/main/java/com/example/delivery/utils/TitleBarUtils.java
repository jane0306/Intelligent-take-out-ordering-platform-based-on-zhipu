package com.example.delivery.utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.delivery.R;

public class TitleBarUtils {

    public static void setTitle(Fragment fragment, String title) {
        TextView tvTitle = fragment.getView().findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    public static void setTitle(Activity activity, String title) {
        TextView tvTitle = activity.findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    public static void setBack(Activity activity, View.OnClickListener listener) {
        View view = activity.findViewById(R.id.iv_back);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(listener);
    }

    public static void setRight(Fragment fragment, String value, View.OnClickListener onClickListener) {
        TextView tvRight = fragment.getView().findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(value);
        tvRight.setOnClickListener(onClickListener);
    }

    public static void setRight(Activity activity, String value, View.OnClickListener onClickListener) {
        TextView tvRight = activity.findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(value);
        tvRight.setOnClickListener(onClickListener);
    }
}
