package com.example.passwordsaverb.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.FirebaseAdapters.HomeDBAdapter.HomeDBAdapter;
import com.example.passwordsaverb.FirebaseAdapters.ItemDBAdapter.ItemDBAdapter;
import com.example.passwordsaverb.Models.Category;
import com.example.passwordsaverb.Models.Item;
import com.example.passwordsaverb.R;
import com.example.passwordsaverb.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {
    //vars
    private RecyclerView recycler_item_fragment;
    private FirebaseRecyclerAdapter<Item, ItemViewHolder> firebaseRecyclerAdapter;
    private String categoryName = null, p =null;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        categoryName = getArguments().getString("categoryName");
        Log.d(TAG, "Category name Desde Item Fragment " + categoryName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_item, container, false);

        //Load home categories
        recycler_item_fragment = root.findViewById(R.id.item_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_item_fragment.setLayoutManager(layoutManager);

        //Checking connection to load the categories
        if (Common.isConnectedToInternet(getContext())) {

            String p = getArguments().getString("p");
            Log.d(TAG, "Item fragments EEE" + categoryName);
            //setUpItemTouchHelper();
            firebaseRecyclerAdapter = ItemDBAdapter.loadItemCat(categoryName, p);
            recycler_item_fragment.setAdapter(firebaseRecyclerAdapter);

        }else {
            Toast.makeText(getActivity(), "Check your connection !!", Toast.LENGTH_SHORT).show();
            return root;
        }
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle(categoryName + " items");
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public void setUpItemTouchHelper(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int swipedPosition = viewHolder.getAdapterPosition();
                firebaseRecyclerAdapter.getSnapshots().getSnapshot(swipedPosition).getRef().setValue(null);
                        //.
                //Toast.makeText(getContext(), String.valueOf(swipedPosition) + " : " +  categoryName + " + " + Common.itemsKeys.size() + " : " + Common.itemsKeys , Toast.LENGTH_SHORT).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recycler_item_fragment);
    }
}