package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        checkBox = findViewById(R.id.checkbox);
        signin = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

       /* mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }


            }
        };*/





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

            final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);

            progressDialog.setMessage("Please wait ");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email_String,password_String).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        progressDialog.dismiss();


                    }else {
                        Toast.makeText(LoginActivity.this, "Email or password isn't correct", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });






        }


    }


}
