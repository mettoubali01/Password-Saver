package com.example.passwordsaverb.fragments;

import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.FirebaseAdapters.HomeDBAdapter.HomeDBAdapter;
import com.example.passwordsaverb.Models.UserItemCat;
import com.example.passwordsaverb.R;
import com.example.passwordsaverb.ViewHolder.UserCategoriesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment{
    //vars
    private RecyclerView recycler_home;
    private TextView message;
    private FirebaseRecyclerAdapter<UserItemCat, UserCategoriesViewHolder> firebaseRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Load home categories
        recycler_home = root.findViewById(R.id.user_cats_recyclerView);
        message = root.findViewById(R.id.empty_view);
        recycler_home.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_home.setLayoutManager(layoutManager);
        //Checking connection to load the categories
        if (Common.isConnectedToInternet(getContext())) {
            Bundle args = getArguments();
            String value = "";
            if(args == null) {
                firebaseRecyclerAdapter = HomeDBAdapter.loadUserCat(getActivity(), null, recycler_home, message);
            }else {
                value = args.getString("p");
                firebaseRecyclerAdapter = HomeDBAdapter.loadUserCat(getActivity(), value, recycler_home, message);
            }
            recycler_home.setAdapter(firebaseRecyclerAdapter);

        }else {
            Toast.makeText(getActivity(), "Check your connection !!", Toast.LENGTH_SHORT).show();
            return root;
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Common.isConnectedToInternet(getContext())){
            firebaseRecyclerAdapter.startListening();
        }else {
            return;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
