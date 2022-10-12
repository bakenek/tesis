package com.example.tesis.ui.home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;

import com.example.tesis.R;

public class homeToolbar extends AppCompatActivity {


    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);



    }

    public boolean onCreateOptionsMenu(Menu menu ){
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }


}
