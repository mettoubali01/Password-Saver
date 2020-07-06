package com.example.passwordsaverb.ViewHolder;

import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.passwordsaverb.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    //vars
    public TextView itemTitle;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        itemTitle = itemView.findViewById(R.id.item_title);
    }
}
