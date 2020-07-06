package com.example.passwordsaverb.FirebaseAdapters.HomeDBAdapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.*;
import com.example.passwordsaverb.FirebaseAdapters.CategoryDBAdapter.CategoryDBAdapter;
import com.example.passwordsaverb.Interface.Callback;
import com.example.passwordsaverb.Models.UserItemCat;
import com.example.passwordsaverb.R;
import com.example.passwordsaverb.ViewHolder.UserCategoriesViewHolder;
import com.firebase.ui.database.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;
import static com.example.passwordsaverb.SlideMenu.navController;

public class HomeDBAdapter {

    //get reference of user_category_items table
    public static DatabaseReference getUserCatItemsTableReference(){
        return FirebaseDatabaseReference.getTableReference(FirebaseConstants.User_Category_Items_TABLE);
    }

    //load categories with item counter of the user
    public static FirebaseRecyclerAdapter loadUserCat(final Context context , final String p, final RecyclerView recyclerView, final TextView msg){

        HomeDBAdapter.getUserCatItemsTableReference().child(Common.user.getPhone()).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    msg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Log.d(TAG, "************ ESTA BUITT**********");
                } else if (dataSnapshot.getValue() != null && !dataSnapshot.getValue().toString().contains("items")) {
                    msg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Log.d(TAG, " *********ESTA BUIT O NO ******************* " + dataSnapshot.getValue().toString().contains("items"));
                }else{
                    msg.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "* ********onDataChange: NO ESTA BUIT****************************");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<UserItemCat> options = new FirebaseRecyclerOptions.Builder<UserItemCat>()
                .setQuery(HomeDBAdapter.getUserCatItemsTableReference().child(Common.user.getPhone()), UserItemCat.class)
                .build();

        return new FirebaseRecyclerAdapter<UserItemCat, UserCategoriesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserCategoriesViewHolder holder, int position, @NonNull final UserItemCat model) {
                Log.d("CATUSER", "onBindViewHolder: " + model.getName());
                holder.categoryName.setText(model.getName());

                CategoryDBAdapter.getIconByName(new Callback() {
                    @Override
                    public void onSuccess(Object s) {
                        holder.categoryIcon.setImageResource(context.getResources().getIdentifier((String)s,"drawable",context.getPackageName() ));

                    }
                }, model.getName());

                CategoryDBAdapter.getItemsCounterOfCat(new Callback() {
                    @Override
                    public void onSuccess(Object s) {
                        holder.itemsCounter.setText(String.valueOf(s));
                    }
                }, model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("HOme ", "Voy a entrar" );
                        if (!model.getName().equals("null")) {
                            Log.d("HOme dins condicional ", "onClick: " + model.getName());
                            Bundle savedInstanceState = new Bundle();
                            savedInstanceState.putString("p", p);
                            savedInstanceState.putString("categoryName", model.getName());
                            Log.d("Home ", " Category name " + model.getName());
                            navController.navigate(R.id.action_nav_home_to_nav_item, savedInstanceState);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public UserCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_categories_item, parent,false);

                return new UserCategoriesViewHolder(view);
            }
        };
    }
}