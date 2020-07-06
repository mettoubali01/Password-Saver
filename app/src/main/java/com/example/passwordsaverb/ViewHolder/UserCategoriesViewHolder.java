package com.example.passwordsaverb.ViewHolder;

import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.passwordsaverb.Interface.ItemClickListener;
import com.example.passwordsaverb.R;

public class UserCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    //vars
    public ImageView categoryIcon;
    public TextView categoryName, itemsCounter;
    private ItemClickListener itemClickListener;

    public UserCategoriesViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryIcon = itemView.findViewById(R.id.image_user_cat_item);
        categoryName = itemView.findViewById(R.id.category_name_user_cat_item);
        itemsCounter = itemView.findViewById(R.id.counter_user_cat_item);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
