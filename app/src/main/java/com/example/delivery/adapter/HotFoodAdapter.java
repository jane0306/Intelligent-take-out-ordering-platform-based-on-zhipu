package com.example.delivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delivery.R;
import com.example.delivery.entity.Food;
import com.example.delivery.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class HotFoodAdapter extends RecyclerView.Adapter<HotFoodAdapter.ViewHolder> {

    private final List<Food> list = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public HotFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotFoodAdapter.ViewHolder holder, int position) {
        Food item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("￥%s", item.getPrice()));
        Glide.with(holder.itemView).load(item.getImg()).into(holder.ivFood);
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onAdd(item);
                }
            }
        });
    }

    public void setList(List<Food> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onAdd(Food item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName, tvPrice;

        private final RoundImageView ivFood;

        private final ImageView ivAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivFood = itemView.findViewById(R.id.iv_food);
            ivAdd = itemView.findViewById(R.id.iv_add);
        }
    }
}
