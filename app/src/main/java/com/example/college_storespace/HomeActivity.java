package com.example.college_storespace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class HomeActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Toolbar  toolbar =(Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        nav = (NavigationView)findViewById( R.id.navmenu );
        drawerLayout  = (DrawerLayout)findViewById( R.id.drawer );
        toggle = new ActionBarDrawerToggle( this,drawerLayout,toolbar,R.string.open,R.string.close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
        nav.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_video:
                        Toast.makeText( getApplicationContext(),"Video Panel is Open",Toast.LENGTH_LONG ).show();
                        drawerLayout.closeDrawer( GravityCompat.START );
                        break;

                    case R.id.menu_audio:
                        Toast.makeText( getApplicationContext(),"Audio Panel is Open",Toast.LENGTH_LONG ).show();
                        drawerLayout.closeDrawer( GravityCompat.START );
                        break;
                    case R.id.menu_pdf:
                        Toast.makeText( getApplicationContext(),"PDF Panel is Open",Toast.LENGTH_LONG ).show();
                        drawerLayout.closeDrawer( GravityCompat.START );
                        break;

                }
                return true;
            }
        } );
        
    }
}