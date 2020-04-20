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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Prevalent;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    RecyclerView mrecycler, posts_recycler;
    FloatingActionButton post_icon;
    private DatabaseReference productref, Rootref;
    RecyclerView.LayoutManager layoutManager;
    FragmentManager fragmentManager;
    private static final String TAG = "HomeActivity";
    FirebaseAuth mAuth;
    public FirebaseUser currentUser, userdata;
    CircleImageView profileimageview;
    TextView userStatuesTextView , userNameTextView;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productref = FirebaseDatabase.getInstance().getReference().child("Posts");
        Rootref = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        userdata = mAuth.getCurrentUser();





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
                        break;
                    case R.id.edit:
                        fragment = new MessagesFragment();
                        break;

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
        userNameTextView = headerview.findViewById(R.id.username_text);
        profileimageview = headerview.findViewById(R.id.user_profile_pic);
        userStatuesTextView = headerview.findViewById(R.id.statues);




        // bottom nav

        // toggle ( menu bottom )
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar2, R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            VeriftyUserExistence();

        }
    }



    private void VeriftyUserExistence() {

        String currentUserID = mAuth.getCurrentUser().getUid();
        Rootref.child("Guests").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))){
                    String userImage = dataSnapshot.child("image").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userStatues = dataSnapshot.child("Statues").getValue().toString();

                    Picasso.get().load(userImage).into(profileimageview);
                    userStatuesTextView.setText(userStatues);
                    userNameTextView.setText(userName);



                } else {
                    String name = dataSnapshot.child("name").getValue().toString();
                    userNameTextView.setText(name);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            mAuth.signOut();
            Intent logout = new Intent(HomeActivity.this, MainActivity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logout);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void statues(String stus){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Guests").child(userdata.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("checkonline" , stus);
        reference.updateChildren(hashMap);


    }

}



