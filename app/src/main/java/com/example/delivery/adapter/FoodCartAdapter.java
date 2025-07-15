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
import com.example.delivery.entity.Cart;
import com.example.delivery.entity.Food;
import com.example.delivery.sqlite.CartDB;
import com.example.delivery.utils.CurrentUserUtils;
import com.example.delivery.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.ViewHolder> {

    private final List<Cart> list = new ArrayList<>();

    @NonNull
    @Override
    public FoodCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCartAdapter.ViewHolder holder, int position) {
        Cart item = list.get(position);
        Food food = item.getFood();
        if (food == null) {
            CartDB.deleteById(item.getId());
            return;
        }

        holder.tvName.setText(food.getName());
        holder.tvDesc.setText(food.getDescription());
        //转成String并保留两位小数，并加上￥符号
        holder.tvPrice.setText("￥" + String.format("%.2f", food.getPrice()*item.getCount()));
        holder.tvCount.setText(String.valueOf(item.getCount()));
        Glide.with(holder.itemView).load(food.getImg()).into(holder.iv);
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setCount(item.getCount() + 1);
                notifyItemChanged(holder.getAdapterPosition());
                CartDB.add(item.getFood(), CurrentUserUtils.getCurrentUser().getId());
            }
        });
        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getCount() > 1) {
                    item.setCount(item.getCount() - 1);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    list.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
                CartDB.minus(item.getFood(), CurrentUserUtils.getCurrentUser().getId());
            }
        });
    }

    public void setList(List<Cart> list) {
        if (list == null) {
            this.list.clear();
            notifyDataSetChanged();
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Cart> getList() {
        return list;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName, tvDesc, tvPrice, tvCount;

        private final RoundImageView iv;

        private final ImageView ivAdd, ivMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCount = itemView.findViewById(R.id.tv_count);
            iv = itemView.findViewById(R.id.iv);
            ivAdd = itemView.findViewById(R.id.iv_add);
            ivMinus = itemView.findViewById(R.id.iv_minus);
        }
    }
}
