package com.example.passwordsaverb.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//holds the reference fo our database.
public class FirebaseDatabaseReference {

    private FirebaseDatabaseReference(){}

    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

     public static DatabaseReference getTableReference(String table){
         return DATABASE.getReference(table);
     }
}
