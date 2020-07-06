package com.example.passwordsaverb.fragments;

import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.text.*;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.passwordsaverb.Common.Common;
import com.example.passwordsaverb.Firebase.*;
import com.example.passwordsaverb.Models.User;
import com.example.passwordsaverb.R;
import com.example.passwordsaverb.Register;
import com.example.passwordsaverb.SlideMenu;
import com.google.firebase.database.*;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    //var
    private Button loginButton;
    private TextView registerLink, forgotPwdLink;
    private EditText phoneNumber, password;
    DatabaseReference userTable;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = rootView.findViewById(R.id.login);
        registerLink = rootView.findViewById(R.id.sign_up_link);
        forgotPwdLink = rootView.findViewById(R.id.forgot_pwd_link);
        loginButton = rootView.findViewById(R.id.login);
        phoneNumber = rootView.findViewById(R.id.phone_number_login);
        password = rootView.findViewById(R.id.password_login);

        // for login button
        login();

        //spannable for sign up
        spannableMethod(registerLink, 17, "Not a Member yet? Sing Up here.", Register.class);
        //spannable for update password
        spannableMethod(forgotPwdLink, 0, "Forgot Password?", null);

        return rootView;
    }

    // login method
    public void login(){
        //Check if the user object is empty
        Log.d(TAG, "login: Common user " + Common.user);
        if (Common.user != null) {
            Log.d(TAG, "login: Common.user is not null");
            //that means user is already logged in
            //so close this activity
            //getActivity().finish();
        }

        //Reset the errors
        phoneNumber.setError(null);
        password.setError(null);

        //Init Firebase
        userTable = FirebaseDatabaseReference.getTableReference(FirebaseConstants.USERS_TABLE);

        //give an action to login button
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getActivity())) {
                    final ProgressDialog mDialog = new ProgressDialog(getActivity());
                    mDialog.setMessage("Please waiting...");
                    mDialog.show();

                    userTable.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Check if the user is not exist id DB
                            if (dataSnapshot.child(phoneNumber.getText().toString()).exists() && !phoneNumber.getText().toString().equals("")) {
                                //Get User information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(phoneNumber.getText().toString()).getValue(User.class);
                                //Log.d("Siiii",  user.getPassword() + " pwd input " + password.getText().toString());
                                if (user.getPassword().equals(password.getText().toString())) {
                                    Toast.makeText(getActivity(), "Sign in Successfully", Toast.LENGTH_LONG).show();
                                    Intent signIn = new Intent(getActivity(), SlideMenu.class);
                                    //Store current user
                                    Common.user = user;
                                    Common.user.setPhone(phoneNumber.getText().toString());
                                    startActivity(signIn);
                                    getActivity().finish();

                                    userTable.removeEventListener(this);
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_LONG).show();
                                }
                            } else if (phoneNumber.getText().toString().equals("") && !password.getText().toString().isEmpty()) {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "Phone Number input is empty", Toast.LENGTH_SHORT).show();
                            } else if(phoneNumber.getText().toString().equals("") && phoneNumber.getText().toString().equals("")){
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "Your have to add your information", Toast.LENGTH_SHORT).show();
                            }else if(password.getText().toString().equals("") && !phoneNumber.getText().toString().isEmpty()){
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "Password input is empty.", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(getActivity(), "User not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    //spannable Sign Up method
    private void spannableMethod(TextView textViewLink, int index, String spannableString, final Class class_){
        SpannableString spannableAction = new SpannableString(spannableString);
        ClickableSpan clickableSpanSignUp = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent registerActivity;
                if (class_ == Register.class) {
                    registerActivity = new Intent(getActivity(), class_);
                    startActivity(registerActivity);
                }else {
                    showForgotPwdDialog();
                }
            }
        };

        spannableAction.setSpan(clickableSpanSignUp, index, spannableAction.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewLink.setText(spannableAction);
        textViewLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Alert Dialog to recover the password
    private void showForgotPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your hint");

        //Get the layout inflater
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View forgot_view = layoutInflater.inflate(R.layout.forgot_pwd_layout, null );

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final EditText edtPhone = forgot_view.findViewById(R.id.phone_for_pwd_recover);
        final EditText edtHint = forgot_view.findViewById(R.id.hint_for_pwd_recover);
        //Init Firebase
        userTable = FirebaseDatabaseReference.getTableReference(FirebaseConstants.USERS_TABLE);
        Log.d("Forgot password", "showForgotPwdDialog: " + edtPhone.getText().toString());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Check if user available
                userTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists() && !edtPhone.getText().toString().isEmpty()) {
                            if (user.getHint().equals(edtHint.getText().toString()))
                                Toast.makeText(getActivity(), "Your Password : " + user.getPassword(), Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getActivity(), "Wrong hint code!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "You have to add a valid data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
}
