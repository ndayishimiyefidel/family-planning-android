package com.example.familyplanning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
BottomNavigationView bottomNavigation;
Fragment fragment=null;
DrawerLayout navigationDrawer;
NavigationView app_navigation;
Toolbar toolBar;
TextView logoutId;
ActionBarDrawerToggle toggle;
private FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        logoutId=findViewById(R.id.logoutId);
        //logout session

        userAuth=FirebaseAuth.getInstance();
        logoutId.setOnClickListener(view -> {
            userAuth.signOut();
            //start new activity
            Toast.makeText(DashboardActivity.this, new StringBuilder().append("You're now logged out successful."),Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, activity_login.class));

        });
        navigationDrawer=findViewById(R.id.navigationDrawer);
        app_navigation=findViewById(R.id.app_navigation);
        toolBar=findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        toggle=new ActionBarDrawerToggle(this,navigationDrawer,toolBar,R.string.menu_open,R.string.menu_close);
        navigationDrawer.addDrawerListener(toggle);
        toggle.syncState();
        app_navigation.setNavigationItemSelectedListener(this);


        //bottom navigation
        bottomNavigation=findViewById(R.id.bottomNavigation);
        //make first page to be selected
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,new HomeFragment()).commit();
        bottomNavigation.setSelectedItemId(R.id.home1);
        bottomNavigation.setOnItemSelectedListener(item -> {
          switch (item.getItemId()){
              case R.id.home1:
                  fragment=new HomeFragment();
                  break;
              case R.id.profile1:
                  fragment=new ProfileFragment();
                  break;
              case R.id.notifications1:
                  fragment=new NotificationsFragment();
                  break;
              case R.id.methodlist1:
                  fragment=new MethodFragment();
                  break;
          }
          getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout,fragment).commit();
            return true;
        });
//        int menuItemId = bottomNavigation.getMenu().getItem(2).getItemId();
//        BadgeDrawable badge = bottomNavigation.getOrCreateBadge(menuItemId);
//        badge.setNumber(6);
    }

    private void showFragment(Fragment fragment1){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.replace(R.id.fragmentLayout,fragment1,null);
        ft.addToBackStack("");
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        if(navigationDrawer.isDrawerOpen(GravityCompat.START)){
            navigationDrawer.closeDrawer(GravityCompat.START);
        }
        else{super.onBackPressed();}

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        item.setChecked(true);
        switch (id){
            case R.id.home:
                showFragment(new HomeFragment());
                break;
            case R.id.about:
                showFragment(new NotificationsFragment());
                break;
            case R.id.logout:
                showFragment(new ProfileFragment());
                break;
            case R.id.methodlist:
                showFragment(new MethodFragment());
                break;
        }
        navigationDrawer.closeDrawer(GravityCompat.START);

        return true;
    }


    //check whether user is logged in or not
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=userAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getApplicationContext(), activity_login.class));

        }
    }
}