package com.example.passwordsaverb.FirebaseAdapters.CategoryDBAdapter;

import android.util.Log;

import androidx.annotation.NonNull;;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.*;
import com.example.passwordsaverb.Interface.Callback;
import com.google.firebase.database.*;

import static android.content.ContentValues.TAG;
import static com.example.passwordsaverb.FirebaseAdapters.HomeDBAdapter.HomeDBAdapter.getUserCatItemsTableReference;

public class CategoryDBAdapter{

    //get reference of category table
    public static DatabaseReference getCatTableReference(){
        return FirebaseDatabaseReference.getTableReference(FirebaseConstants.CATEGORY_TABLE);
    }

    //get category name icon by his name
    public static void getIconByName(final Callback callback, final String categoryName){
        final String[] result = {""};

        getCatTableReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    result[0] = String.valueOf(dataSnapshot1.child("icon").getValue());
                    if (result[0].contains(transform(categoryName))) {
                        callback.onSuccess(String.valueOf(dataSnapshot1.child("icon").getValue()));
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get total items of particular category
    public static void getItemsCounterOfCat(final Callback callback, String categoryName){
        Log.d(TAG, "getItemsCounterOfCat: "  + categoryName);
        getUserCatItemsTableReference().child(Common.user.getPhone()).child(categoryName).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: getItemCounterOfCat " + dataSnapshot.getChildrenCount());
                callback.onSuccess(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get total items of particular category
    public static void getItemsKeysByCatName(final Callback callback, final String categoryName){
        Log.d(TAG, "\ngetItemsKeysByCatName: Inicio del metodo " + categoryName + "\n");
        getUserCatItemsTableReference().child(Common.user.getPhone()).child(categoryName).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren() ){
                    Log.d(TAG, "Category name " + categoryName + " item key " + dataSnapshot1.getKey());
                    Common.itemsKeys.add(dataSnapshot1.getKey());
                }
                Log.d(TAG, "All Keys inside the getKeys method " + Common.itemsKeys.toString()  + " CAt " + categoryName);
                Log.d(TAG, "Separador \n");
                callback.onSuccess(Common.itemsKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //for clean categories name to facilitate the search of his icon
    public static String transform(String cad){
        return cad.replace(" ", "_").replace("-","_").toLowerCase();
    }
}
