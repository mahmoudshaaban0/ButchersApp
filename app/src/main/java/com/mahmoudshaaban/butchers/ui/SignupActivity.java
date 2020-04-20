package com.mahmoudshaaban.butchers.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.mahmoudshaaban.butchers.R;
import com.mahmoudshaaban.butchers.pojo.Guests;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

     DatabaseReference rootRef;
     EditText username , email , password , confirmpassword;
     Button signup;
     FirebaseAuth mAuth;
     ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupp);

        username = findViewById(R.id.username_signup);
        email = findViewById(R.id.email_signup);
        password = findViewById(R.id.password_signup);
        confirmpassword = findViewById(R.id.confirm_password_signup);
        signup = findViewById(R.id.signup_btn);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppCompatAlertDialogStyle);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });








    }

    private void createAccount() {
        final String userName = username.getText().toString();
        final String email_String = email.getText().toString();
        final String password_String = password.getText().toString();
        final String confirmPassword_String = confirmpassword.getText().toString();

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Please write your Username", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(email_String)){
            Toast.makeText(this, "Please write your Email", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(password_String)){
            Toast.makeText(this, "Please write your Password", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(confirmPassword_String)){
            Toast.makeText(this, "Please write your Password Again", Toast.LENGTH_SHORT).show();

        } else if (!password_String.equals(confirmPassword_String)){
            Toast.makeText(this, "Password Doesn't match", Toast.LENGTH_SHORT).show();

        }
        else {

            mAuth.createUserWithEmailAndPassword(email_String,password_String).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.setMessage("Please wait");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String userID = firebaseUser.getUid();

                        Guests guests = new Guests(
                                userName,email_String , password_String , confirmPassword_String , userID
                        );
                        String currentUserId = mAuth.getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference().child("Guests")
                        .child(currentUserId).setValue(guests).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });



                    } else {
                        Toast.makeText(SignupActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }

    private void validateaccount(final String userName, final String email, final String password, final String confirmPassword_String) {

        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("Clients").child(password).exists()){
                    Map<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("Username" , userName);
                    userDataMap.put("Email" , email);
                    userDataMap.put("Password" , password);
                    userDataMap.put("ConfirmPassword" , confirmPassword_String);

                    rootRef.child("Clients").child(password).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Congratulations , your account has been created ", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(SignupActivity.this, "Network error : Please try again", Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });



                }
                else{
                    Toast.makeText(SignupActivity.this, "This " + email + " Already exits", Toast.LENGTH_SHORT).show();

                    Toast.makeText(SignupActivity.this, "Please use another email", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
