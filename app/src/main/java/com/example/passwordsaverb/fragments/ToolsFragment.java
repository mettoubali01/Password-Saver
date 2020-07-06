package com.example.passwordsaverb.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.FirebaseConstants;
import com.example.passwordsaverb.Firebase.FirebaseDatabaseReference;
import com.example.passwordsaverb.R;
import com.google.android.gms.tasks.*;
import com.google.firebase.database.*;
import java.util.*;
import dmax.dialog.SpotsDialog;
import static com.example.passwordsaverb.SlideMenu.navController;

public class ToolsFragment extends Fragment implements View.OnClickListener {
    //vars
    private FirebaseDatabase database = FirebaseDatabaseReference.DATABASE;
    private DatabaseReference userTable;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        //vars
        Button changePwdBtn = root.findViewById(R.id.change_pwd_btn);
        Button logOutBtn = root.findViewById(R.id.log_out_btn);

        changePwdBtn.setOnClickListener(this);
        logOutBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        Button btn = (Button) view;
        switch (btn.getId()){
            case R.id.change_pwd_btn:
                changeUSerPwd();
                break;
            case R.id.log_out_btn:
                logOut();
                break;
        }
    }

   //change user password method
    private void changeUSerPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password");
        builder.setMessage("Please fill all information");

        //Get the layout inflater
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View change_pwd_view = layoutInflater.inflate(R.layout.change_pwd_layout, null );

        builder.setView(change_pwd_view);

        final EditText edtCurrentPwd =  change_pwd_view.findViewById(R.id.current_pwd);
        final EditText edtNewPwd = change_pwd_view.findViewById(R.id.new_pwd);
        final EditText edtConfNewPwd = change_pwd_view.findViewById(R.id.confirm_new_pwd);

        Log.d("Forgot password", "Current pwd " + edtCurrentPwd.getText().toString() + " new pwd: " + edtNewPwd.getText().toString() + " conf new pwd:  " + edtConfNewPwd.getText().toString());
        builder.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Fore use SpotDialog
                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(ToolsFragment.this.getActivity()).build();
                waitingDialog.setTitle("Changing Password");
                waitingDialog.setMessage("Please wait.....");
                waitingDialog.show();

                //check old password
                if(!edtCurrentPwd.getText().toString().isEmpty() && !edtNewPwd.getText().toString().isEmpty() && !edtConfNewPwd.getText().toString().isEmpty()) {
                    if (Common.user.getPassword().equals(edtCurrentPwd.getText().toString())) {
                        //check if new pwd and repeat pwd
                        if (edtNewPwd.getText().toString().equals(edtConfNewPwd.getText().toString())) {
                            Map<String, Object> passwordUpdate = new HashMap<>();
                            passwordUpdate.put("password", edtConfNewPwd.getText().toString());

                            //Make update
                            //Init Firebase
                            Log.d("TAG", "onClick: " + Common.user.getPhone() + " pwd " + Common.user.toString());
                            userTable = database.getReference(FirebaseConstants.USERS_TABLE);
                            userTable.child(Common.user.getPhone())
                                    .updateChildren(passwordUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            waitingDialog.dismiss();
                                            logOut();
                                            Toast.makeText(getActivity(), "Password was update", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            waitingDialog.dismiss();
                            Toast.makeText(ToolsFragment.this.getActivity(), "New password doesn't match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        waitingDialog.dismiss();
                        Toast.makeText(ToolsFragment.this.getActivity(), "Wrong old password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    waitingDialog.dismiss();
                    Toast.makeText(ToolsFragment.this.getActivity(), "You have to add a valid data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    //logOut method
    private void logOut() {
        navController.navigate(R.id.action_nav_tools_to_nav_login);
    }
}
