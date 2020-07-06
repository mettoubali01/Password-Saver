package com.example.passwordsaverb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.FirebaseConstants;
import com.example.passwordsaverb.Firebase.FirebaseDatabaseReference;
import com.google.firebase.database.*;
import com.example.passwordsaverb.Models.User;

public class Register extends AppCompatActivity {
    private static final String TAG = "Register activity";
    private EditText registerPhoneNumber, registerName, registerPassword, registerConfirmedPassword, registerHint;
    private Button registerBtn;
    DatabaseReference userTable;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.register_layout);

        registerPhoneNumber = findViewById(R.id.phone_number_register);
        registerName = findViewById(R.id.name_register);
        registerPassword = findViewById(R.id.password_register);
        registerConfirmedPassword = findViewById(R.id.password_register_confirm);
        registerHint = findViewById(R.id.hint_register);
        registerBtn = findViewById(R.id.create_account);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Init Firebase
        userTable = FirebaseDatabaseReference.getTableReference(FirebaseConstants.USERS_TABLE);

        //activate the register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(Register.this.getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Check if already user
                            if (!checkEmptyInput(registerPhoneNumber, registerName, registerPassword, registerConfirmedPassword, registerHint)){
                                if (!dataSnapshot.child(registerPhoneNumber.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    if (registerPassword.getText().toString().equals(registerConfirmedPassword.getText().toString()))  {
                                        User user = new User(registerName.getText().toString(), registerPassword.getText().toString(), registerHint.getText().toString());
                                        userTable.child(registerPhoneNumber.getText().toString()).setValue(user);
                                        Toast.makeText(Register.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "The passwords are not equals. Check them. Please", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(Register.this, "Phone number already register", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                mDialog.dismiss();
                                Toast.makeText(Register.this, "You have to add all your data to register", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(Register.this, "Check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    //method to control if the inputs are not empties
    public boolean checkEmptyInput(EditText registerPhoneNumber, EditText registerName, EditText registerPassword, EditText registerConfirmedPassword, EditText registerHint) {
        boolean estat = false;

        if (registerPhoneNumber.getText().toString().isEmpty()
            || registerName.getText().toString().isEmpty()
            || registerPassword.getText().toString().isEmpty()
            || registerConfirmedPassword.getText().toString().isEmpty()
            || registerHint.getText().toString().isEmpty()){

            estat = true;
        }
        Log.d(TAG, "inside check inputs methods " + estat);
        return estat;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
