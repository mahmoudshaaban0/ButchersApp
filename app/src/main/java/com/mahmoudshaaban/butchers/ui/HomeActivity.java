package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Prevalent;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    RecyclerView mrecycler, posts_recycler;
    FloatingActionButton post_icon;
    private DatabaseReference productref;
    RecyclerView.LayoutManager layoutManager;
    FragmentManager fragmentManager;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productref = FirebaseDatabase.getInstance().getReference().child("Posts");


        final ChipNavigationBar chipNavigationBar = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            chipNavigationBar.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment)
                    .commit();
        }
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {

                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.discover:
                        fragment = new FindFriendsFragment();

                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                            .commit();
                } else {
                    Log.d(TAG, "onItemSelected: error in create");
                }
            }
        });


        // toolbar
        Toolbar toolbar2 = findViewById(R.id.toolbarDrawer);

        setSupportActionBar(toolbar2);
        toolbar2.setTitle("Butchers");
        toolbar2.setTitleTextColor(getResources().getColor(android.R.color.white));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //nav drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        TextView userNameTextView = headerview.findViewById(R.id.username_text);
        CircleImageView profileimageview = headerview.findViewById(R.id.user_profile_pic);
        TextView userStatuesTextView = headerview.findViewById(R.id.statues);


        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).into(profileimageview);
        userNameTextView.setText(Prevalent.currentOnlineUser.getUsername());
        userStatuesTextView.setText(Prevalent.currentOnlineUser.getStatues());

        // bottom nav


        // toggle ( menu bottom )
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar2, R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();




    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_profile) {
            Intent go = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(go);
        }
        if (id == R.id.nav_discover) {
            Paper.book().destroy();
            Intent logout = new Intent(HomeActivity.this, MainActivity.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}



