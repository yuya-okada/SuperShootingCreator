package com.gult.shootingcreator.network;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;

import com.gult.shootingcreator.R;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by okadakeiko on 13/10/01.
 */
public class StoreActivity extends Activity {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    public static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private List<ParseObject> todos;
    private Dialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);
    }

    private void testPost() {

    }

}