package com.android.usuario.start.Screens.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.usuario.start.R;
import com.android.usuario.start.Util.Singleton;

/**
 * Created by eduar on 26/06/2017.
 */

public class Profile extends AppCompatActivity{

    private com.android.usuario.start.DataObject.Profile userProfile;

    private TextView _edit;
    private ImageView _profileImgType;
    private TextView _profileTextType;
    private TextView _name;
    private TextView _description;
    private LinearLayout _projects;
    private TextView _email;
    private TextView _phoneNumber;
    private Button _logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userProfile = (com.android.usuario.start.DataObject.Profile) getIntent().getExtras().getSerializable("userProfile");

        _edit = (TextView) findViewById(R.id.fragment_profile_edit);
        _profileImgType = (ImageView) findViewById(R.id.fragment_profile_user_image);
        _profileTextType = (TextView) findViewById(R.id.fragment_profile_user_profile);
        _name = (TextView) findViewById(R.id.fragment_profile_user_name);
        _description = (TextView) findViewById(R.id.fragment_profile_description);
        _projects = (LinearLayout) findViewById(R.id.fragment_profile_projects);
        _email = (TextView) findViewById(R.id.fragment_profile_user_email);
        _phoneNumber = (TextView) findViewById(R.id.fragment_profile_user_phone);
        _logout = (Button) findViewById(R.id.signup_btn_logout);
        _profileTextType.setText(Singleton.getStringProfileType(userProfile.getProfileType()));
        _profileImgType.setImageResource(Singleton.getImageProfileType(userProfile.getProfileType()));


        _edit.setVisibility(View.GONE);

        _name.setText(userProfile.getName());
        _description.setText(userProfile.getDescription());

        _email.setText(userProfile.getEmail());
        _phoneNumber.setText(userProfile.getPhoneNumber());

        _logout.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
