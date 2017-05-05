package com.android.usuario.start.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.usuario.start.View.User.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by eduar on 04/05/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user == null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
