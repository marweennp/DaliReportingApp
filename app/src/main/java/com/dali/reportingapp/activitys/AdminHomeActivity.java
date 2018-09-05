package com.dali.reportingapp.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dali.reportingapp.R;

public class AdminHomeActivity extends AppCompatActivity {

    private String user_id;
    private String user_email;
    private String user_first_name;
    private String user_last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //Retrieve the Intent Bundle Extras values
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (!(extras == null)) {
                user_id = extras.getString("user_id");
                user_email = extras.getString("user_email");
                user_first_name = extras.getString("user_first_name");
                user_last_name = extras.getString("user_last_name");
            }
        }
        Toast.makeText(AdminHomeActivity.this, "\n" +user_id+"\n" +user_email+"\n" +user_first_name+" " +user_last_name, Toast.LENGTH_LONG).show();



    }
}
