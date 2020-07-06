package com.example.passwordsaverb.ViewHolder;

import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.passwordsaverb.Interface.ItemClickListener;
import com.example.passwordsaverb.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //vars
    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName = (TextView)itemView.findViewById(R.id.category_name);
        imageView = (ImageView)itemView.findViewById(R.id.category_icon);

        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
