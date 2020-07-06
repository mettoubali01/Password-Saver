package com.example.passwordsaverb.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.FirebaseAdapters.CategoryDBAdapter.CategoryDBAdapter;
import com.example.passwordsaverb.FirebaseAdapters.ItemDBAdapter.ItemDBAdapter;
import com.example.passwordsaverb.Interface.Callback;
import com.example.passwordsaverb.Models.Item;
import com.example.passwordsaverb.Models.UserItemCat;
import com.example.passwordsaverb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;

import static android.content.ContentValues.TAG;
import static com.example.passwordsaverb.SlideMenu.navController;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEditItemFragment extends Fragment{
    //vars
    private EditText edtTitleItem, edtUsernameItem, edtPwdItem, edtNoteItem, edtUrlItem;
    private Button btnCreateItem;
    public String categoryName = null, action = null, itemKey= null, p = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryName = getArguments().getString("categoryName");
        Log.d(TAG, "Category name desde " + categoryName);
        action = getArguments().getString("action");
        itemKey = getArguments().getString("itemKey");
        if (getArguments() != null) {
            p = getArguments().getString("p");
        }
        Log.d(TAG, "final de onCreate de CreateEditItemFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_edit_item, container, false);

        edtTitleItem = rootView.findViewById(R.id.item_title_create);
        edtUsernameItem = rootView.findViewById(R.id.item_username_create);
        edtPwdItem = rootView.findViewById(R.id.item_pwd_create);
        edtNoteItem = rootView.findViewById(R.id.item_notes_create);
        edtUrlItem = rootView.findViewById(R.id.item_url_create);
        btnCreateItem = rootView.findViewById(R.id.create_edit_item_btn);

        Common.item = new Item(edtTitleItem.getText().toString(),
                edtUsernameItem.getText().toString(),
                edtPwdItem.getText().toString(),
                edtNoteItem.getText().toString(),
                edtUrlItem.getText().toString());

        try {
            Log.d(TAG, "onCreateView: Checking Attribute A " + p + " " + action);

            if (action.equals("edit")){
                setHasOptionsMenu(true);

                ((AppCompatActivity)getContext()).getSupportActionBar().setTitle(action + " item");
                btnCreateItem.setText("EDIT ITEM");
                fillInputs(itemKey, categoryName);
                activeSaveButton(getActivity(), action, btnCreateItem);
            }else if(action == "create"){
                setHasOptionsMenu(false);

                activeSaveButton(getActivity(), "create", btnCreateItem);
            }
        }catch (NullPointerException e){
            action = "create";
        }
        return rootView;
    }

    //Active save item button according to action argument
    public void activeSaveButton(final Activity activity, final String action, Button btnCreateItem){
        btnCreateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.item = new Item(edtTitleItem.getText().toString(),
                        edtUsernameItem.getText().toString(),
                        edtPwdItem.getText().toString(),
                        edtNoteItem.getText().toString(),
                        edtUrlItem.getText().toString());
                Log.d(TAG, "EDIT Item " + categoryName + " : KEY : " + itemKey + " action " + action);
                if (action.equals("edit")) {

                    Log.d(TAG, "Update Item " + Common.item + " ACTION:  " + action);
                    try {
                        Log.d(TAG, " \n Position inside edit part " + itemKey);
                        ItemDBAdapter.updateItem(Integer.parseInt(itemKey), categoryName, Common.item , activity );
                    }catch (NumberFormatException e){
                        Log.d(TAG, "NumberFormatException: editing item " + e.toString());
                    }
                    Toast.makeText(activity, "Item updated", Toast.LENGTH_LONG).show();

                    Bundle savedInstanceState = new Bundle();
                    savedInstanceState.putString("categoryName", categoryName);
                    savedInstanceState.putString("p","n");
                    Log.d(TAG, "Category mamesdssfd " + categoryName );
                    navController.navigate(R.id.action_nav_create_edit_item_to_nav_home, savedInstanceState);
                } else if (action.equals("create")){
                    Log.d(TAG, "We are trying to create an item");
                    Toast.makeText(activity, "Item created", Toast.LENGTH_SHORT).show();
                    Common.userItemCat = new UserItemCat(categoryName, null);
                    Common.userItemCat.addItems(Common.item);
                    ItemDBAdapter.saveItem(activity, categoryName);
                    Log.d(TAG, "We just finish creating the item " + Common.item);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.remove_item) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("REMOVE ITEM")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getActivity(), "You removed me!!, yes", Toast.LENGTH_SHORT).show();
                            ItemDBAdapter.deleteItem(Integer.parseInt(itemKey), categoryName);
                            Bundle savedInstanceState = new Bundle();
                            savedInstanceState.putString("categoryName", categoryName);
                            savedInstanceState.putString("p","n");
                            Log.d(TAG, "Category mamesdssfd " + categoryName );
                            navController.navigate(R.id.action_nav_create_edit_item_to_nav_home, savedInstanceState);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // show the AlertDialog
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void fillInputs(final String key, String categoryName ){
        Log.d(TAG, "fillInputs: " + key + " " + categoryName );
        DatabaseReference databaseReference = ItemDBAdapter.getCategoryTableReference().child(Common.user.getPhone()).child(categoryName).child("items");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(i == Integer.parseInt(key)) {
                        Log.d(TAG, "onDataChange FillInputs : " + dataSnapshot1.getValue());
                        Log.d(TAG, "onDataChange: Is not null " + dataSnapshot.getKey());
                        Item item = dataSnapshot1.getValue(Item.class);
                        Log.d(TAG, "Item edit item " + item);
                        edtTitleItem.setText(item.getItemTitle());
                        edtUsernameItem.setText(item.getItemUsername());
                        edtPwdItem.setText(item.getItemPwd());
                        edtNoteItem.setText(item.getItemNote());
                        edtUrlItem.setText(item.getItemUrl());
                        break;
                    }

                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
}