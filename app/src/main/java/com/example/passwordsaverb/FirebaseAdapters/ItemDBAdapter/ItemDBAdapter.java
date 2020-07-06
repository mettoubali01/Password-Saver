package com.example.passwordsaverb.FirebaseAdapters.ItemDBAdapter;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.*;
import com.example.passwordsaverb.FirebaseAdapters.HomeDBAdapter.HomeDBAdapter;
import com.example.passwordsaverb.Models.Item;
import com.example.passwordsaverb.R;
import com.example.passwordsaverb.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.database.*;
import static android.content.ContentValues.TAG;
import static com.example.passwordsaverb.SlideMenu.navController;

public class ItemDBAdapter {
    //vars
    final static Bundle savedInstanceState = new Bundle();

    //get reference with category table
    public static DatabaseReference getCategoryTableReference(){
        return FirebaseDatabaseReference.getTableReference(FirebaseConstants.User_Category_Items_TABLE);
    }

    //check and save items to Firebase
    public static void saveItem(final Context context, final String categoryName){
        //get the table category reference
        final DatabaseReference userCatItemTable = getCategoryTableReference();

        if (Common.isConnectedToInternet(context)){
            userCatItemTable.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //for (DataSnapshot dt : dataSnapshot.getChildren()){
                    DataSnapshot catNameSnapshot = dataSnapshot.child(Common.user.getPhone()).child(categoryName);

                    Log.d(TAG, "User phone CREATE ITEM " + catNameSnapshot.child("phone").getKey() + " Category name: " + categoryName) ;
                    Log.d(TAG, "Category name CREATE ITEM " + catNameSnapshot.child("name").getValue() + " Coomon category " + Common.userItemCat.getName() + " : " + catNameSnapshot.exists());
                    //if the category it doesn't exists we create the user eith his item
                    if (catNameSnapshot.exists()) {
                        Log.d(TAG, "CHECKING IF THE USER EXIST I THE CATEGORY IS NULL " + catNameSnapshot.child(Common.userItemCat.getName()).getKey() + " existe => " + catNameSnapshot.exists() );
                        catNameSnapshot.child("items").getRef().push().setValue(Common.userItemCat.getItems().get(0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Added item", Toast.LENGTH_SHORT).show();
                                Common.items.clear();
                            }
                        });
                    }else {
                        catNameSnapshot.getRef().child("name").setValue(Common.userItemCat.getName());
                        catNameSnapshot.getRef().child("items").getRef().push().setValue(Common.userItemCat.getItems().get(0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Common.items.clear();
                                Toast.makeText(context, "Added item", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(context, "Check your connection, please.", Toast.LENGTH_SHORT).show();
        }
    }

    //Update an item using his key
    public static void updateItem(final int keyNumber, String categoryName, final Item item, Context context){
        Log.d(TAG, "updateItem: I'M GOING YO UPDATE INCHA ALLAH" + " CONTEXT " + context);
        if (context != null)
            Log.d(TAG, "updateItem: MY FRIEND YOU HAVE TO UPDATE");
            if (Common.isConnectedToInternet(context)){
                getCategoryTableReference().child(Common.user.getPhone()).child(categoryName).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            if (i == keyNumber)
                                dataSnapshot1.getRef().setValue(item);
                            i++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }else {
                Toast.makeText(context, "Check your connection, please.", Toast.LENGTH_SHORT).show();
            }
    }

    public static FirebaseRecyclerAdapter<Item, ItemViewHolder> loadItemCat(final String categoryName, final String p) {
        final DatabaseReference query = HomeDBAdapter.getUserCatItemsTableReference().child(Common.user.getPhone()).child(categoryName).child("items");
        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        return new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position, @NonNull final Item model) {
                holder.itemTitle.setText(model.itemTitle);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //send edit action to another layout
                        savedInstanceState.putString("action", Common.action);
                        savedInstanceState.putString("itemKey", String.valueOf(position));
                        savedInstanceState.putString("categoryName", categoryName);
                        Log.d(TAG, "item Postion " + position );
                        Log.d(TAG, "Category name desde load Item Cat " + categoryName);
                        navController.navigate(R.id.action_nav_item_to_nav_create_edit_item, savedInstanceState);
                    }
                });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_item, parent, false);
                return new ItemViewHolder(view);
            }
        };
    }

    //remove all items or nodes of category
    public static void deleteCat(final String categoryName){
        DatabaseReference query = HomeDBAdapter.getUserCatItemsTableReference().child(Common.user.getPhone()).child(categoryName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                Log.d(TAG, "Category deleted " + dataSnapshot.getRef().getKey());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //check if the item exists to delete it when we don't have any item inside
    public static void checkItemCatExists(final String categoryName){
        DatabaseReference databaseReference = HomeDBAdapter.getUserCatItemsTableReference().child(Common.user.getPhone()).child(categoryName).child("items");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "exist? " + dataSnapshot.exists());
                if(!dataSnapshot.exists()){
                    deleteCat(categoryName);
                    //i'm sure that's its always emtpy
                    Common.itemsKeys.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Asking if the user is sure about deleting the item
    public static void deleteItem(final int swipedPosition, final String categoryName){
        final DatabaseReference query = HomeDBAdapter.getUserCatItemsTableReference().child(Common.user.getPhone()).child(categoryName).child("items");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot01 : dataSnapshot.getChildren()){
                    if (i == swipedPosition) {
                       // Toast.makeText(activity, "KEY " + dataSnapshot01.getKey(), Toast.LENGTH_LONG).show();
                        dataSnapshot01.getRef().removeValue();
                        ItemDBAdapter.checkItemCatExists(categoryName);
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
