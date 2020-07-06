package com.example.passwordsaverb.Common;

import android.content.Context;
import android.net.*;
import com.example.passwordsaverb.Models.Item;
import com.example.passwordsaverb.Models.*;
import java.util.*;

public final class Common {
    public static User user;
    public static UserItemCat userItemCat;
    public static Item item = new Item();
    public static ArrayList items = new ArrayList();
    public static Set itemsKeys = new LinkedHashSet();
    public static String action = "edit";
    public static String create = "create";

    public static boolean isConnectedToInternet(Context context){
       ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

       if (connectivityManager != null){
           NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
           if (info != null){
               for (int i = 0; i < info.length; i++) {
                   if (info[i].getState() == NetworkInfo.State.CONNECTED)
                       return true;
               }
           }
       }
       return false;
   }
}