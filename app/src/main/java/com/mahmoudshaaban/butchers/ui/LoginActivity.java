package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Clients;
import com.mahmoudshaaban.butchers.pojo.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    DatabaseReference rootRef;
    private String data = "Clients";
    Clients userData;
    EditText password , email ;
    CheckBox checkBox;
    Button signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        checkBox = findViewById(R.id.checkbox);
        signin = findViewById(R.id.login_button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });



    }

    private void loginUser() {
        final String email_String = email.getText().toString();
        final String password_String = password.getText().toString();


        if (TextUtils.isEmpty(email_String)) {
            Toast.makeText(this, "Please write your Email Address", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password_String)) {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();

        }

        else {

            AllowAccessToAccount(email_String, password_String);
        }


    }

    private void AllowAccessToAccount(final String email, final String password) {

        if (checkBox.isChecked()){

            Paper.book().write(Prevalent.userEmailAddress, email);
            Paper.book().write(Prevalent.userPassword, password);

        }

        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(data).child(password).exists()) {

                    userData = dataSnapshot.child("Clients").child(password).getValue(Clients.class);
                    if (userData.getEmail().equals(email)) {
                        if (userData.getPassword().equals(password)) {
                            if (data.equals("Clients")) {
                                Toast.makeText(LoginActivity.this, "Logged in successfully ... ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);

                            }
                        }

                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "This account doesn't exist", Toast.LENGTH_SHORT).show();

                }


                }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
