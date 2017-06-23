package com.android.usuario.start.Screens.Container.Profile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * Created by tulio on 05/05/2017.
 */

public class ProfileFragment extends Fragment {

    private Profile userProfile;

    private View parentView;

    private TextView _edit;
    private ImageView _profileImgType;
    private TextView _profileTextType;
    private TextView _name;
    private TextView _description;
    private ListView _projects;
    private TextView _email;
    private TextView _phoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfile = (Profile) getArguments().getSerializable("userProfile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_profile,container,false);

        _edit = (TextView) parentView.findViewById(R.id.fragment_profile_edit);
        _profileImgType = (ImageView) parentView.findViewById(R.id.fragment_profile_user_image);
        _profileTextType = (TextView) parentView.findViewById(R.id.fragment_profile_user_profile);
        _name = (TextView) parentView.findViewById(R.id.fragment_profile_user_name);
        _description = (TextView) parentView.findViewById(R.id.fragment_profile_description);
        _projects = (ListView) parentView.findViewById(R.id.fragment_profile_projects);
        _email = (TextView) parentView.findViewById(R.id.fragment_profile_user_email);
        _phoneNumber = (TextView) parentView.findViewById(R.id.fragment_profile_user_phone);

        switch (userProfile.getProfileType()){
            case 0:
                _profileTextType.setText("");
                _profileImgType.setImageResource(R.drawable.img_profile_placeholder);
                break;
            case 1:
                _profileTextType.setText("Hacker");
                _profileImgType.setImageResource(R.drawable.img_profile_placeholder);
                break;
            case 2:
                _profileTextType.setText("Hipster");
                _profileImgType.setImageResource(R.drawable.img_profile_placeholder);
                break;
            case 3:
                _profileTextType.setText("Hustler");
                _profileImgType.setImageResource(R.drawable.img_profile_placeholder);
                break;
        }

        _name.setText(userProfile.getName());
        _description.setText(userProfile.getDescription());

        _email.setText(userProfile.getEmail());
        _phoneNumber.setText(userProfile.getPhoneNumber());


        Button logout = (Button) parentView.findViewById(R.id.signup_btn_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        return parentView;
    }
}
