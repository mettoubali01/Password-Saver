package com.example.passwordsaverb.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.FirebaseConstants;
import com.example.passwordsaverb.Models.Category;
import com.example.passwordsaverb.R;
import com.example.passwordsaverb.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.*;
import com.google.firebase.database.*;
import static com.example.passwordsaverb.SlideMenu.navController;

public class CategoryFragment extends Fragment {
    private static final String TAG = "Category Fragment";
    //vars
    private FirebaseDatabase database;
    private DatabaseReference categoryDBReference;
    private RecyclerView recycler_category_fragment;
    private FirebaseRecyclerAdapter<Category, CategoryViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Category> options;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        categoryDBReference = database.getReference(FirebaseConstants.CATEGORY_TABLE);

        //Load Categories Home
        recycler_category_fragment = (RecyclerView) root.findViewById(R.id.category_recyclerview);
        recycler_category_fragment.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_category_fragment.setLayoutManager(layoutManager);

        //Checking connection to load the categories
        if (Common.isConnectedToInternet(getContext())) {
            loadCategories();
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

    //Load categories on recyclerview of home activity
    private void loadCategories() {
        options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(categoryDBReference, Category.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull final Category category) {
                holder.txtMenuName.setText(category.getName());
                holder.imageView.setImageResource(getResources().getIdentifier(category.getIcon(),"drawable",getActivity().getPackageName()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "checking id the " + category.getName());
                        if (!category.getName().equals("null")) {
                            Bundle savedInstanceState = new Bundle();
                            savedInstanceState.putString("categoryName", category.getName());
                            savedInstanceState.putString("action", Common.create);
                            Log.d(TAG, "I'm going to create item of this current action " + Common.create + " of this cat Id " + category.getName());
                            navController.navigate(R.id.action_nav_category_to_nav_create_edit_item, savedInstanceState);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent,false);
                return new CategoryViewHolder(view);
            }
        };
        recycler_category_fragment.setAdapter(firebaseRecyclerAdapter);
    }
}
