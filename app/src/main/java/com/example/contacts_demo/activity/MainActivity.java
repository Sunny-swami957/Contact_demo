package com.example.contacts_demo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.example.contacts_demo.R;
import com.example.contacts_demo.database.DatabaseHandler;
import com.example.contacts_demo.entity.ContactList;


public class MainActivity extends AppCompatActivity {

    private NavHostFragment finalHost;
    private DatabaseHandler db;
    private Bundle savedInst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInst=savedInstanceState;
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(getApplicationContext());
        finalHost= NavHostFragment.create(R.navigation.nav_graph);
        ContactList.getInstance().sync(db,this,true);
    }
    public void setup()
    {
        if(savedInst==null&&getSupportFragmentManager()!=null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_container, finalHost)
                    .setPrimaryNavigationFragment(finalHost) // equivalent to app:defaultNavHost="true"
                    .commit();
        }
    }

}
