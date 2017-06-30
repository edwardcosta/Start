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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Auth.LoginActivity;
import com.android.usuario.start.Screens.Container.Profile.EditProfile.EditProfile;
import com.android.usuario.start.Util.Fonts;
import com.android.usuario.start.Util.Singleton;
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
    private LinearLayout _projects;
    private TextView _projectsTitle;
    private TextView _contactTitle;
    private TextView _email;
    private TextView _phoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfile = (Profile) getArguments().getSerializable("userProfile");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_profile,container,false);

        _edit = (TextView) parentView.findViewById(R.id.fragment_profile_edit);
        _profileImgType = (ImageView) parentView.findViewById(R.id.fragment_profile_user_image);
        _profileTextType = (TextView) parentView.findViewById(R.id.fragment_profile_user_profile);
        _name = (TextView) parentView.findViewById(R.id.fragment_profile_user_name);
        _description = (TextView) parentView.findViewById(R.id.fragment_profile_description);
        _projects = (LinearLayout) parentView.findViewById(R.id.fragment_profile_projects);
        _projectsTitle = (TextView) parentView.findViewById(R.id.fragment_profile_projects_title);
        _contactTitle = (TextView) parentView.findViewById(R.id.fragment_contact_title);
        _email = (TextView) parentView.findViewById(R.id.fragment_profile_user_email);
        _phoneNumber = (TextView) parentView.findViewById(R.id.fragment_profile_user_phone);
        _profileTextType.setText(Singleton.getStringProfileType(userProfile.getProfileType()));
        _profileImgType.setImageResource(Singleton.getImageProfileType(userProfile.getProfileType()));

        _name.setText(userProfile.getName());
        _description.setText(userProfile.getDescription());

        _email.setText(userProfile.getEmail());
        _phoneNumber.setText(userProfile.getPhoneNumber());


        Button logout = (Button) parentView.findViewById(R.id.fragment_profile_signup_btn_logout);

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

        _edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("userProfile",userProfile);
                getActivity().startActivity(intent);
            }
        });

        //Setting fonts
        Fonts fonts = new Fonts(getContext());
        _name.setTypeface(fonts.OPEN_SANS_BOLD);
        _description.setTypeface(fonts.OPEN_SANS_REGULAR);
        _profileTextType.setTypeface(fonts.OPEN_SANS_REGULAR);
        _email.setTypeface(fonts.OPEN_SANS_REGULAR);
        _phoneNumber.setTypeface(fonts.OPEN_SANS_REGULAR);
        _contactTitle.setTypeface(fonts.OPEN_SANS_SEMIBOLD);
        _projectsTitle.setTypeface(fonts.OPEN_SANS_SEMIBOLD);
        _edit.setTypeface(fonts.OPEN_SANS_SEMIBOLD);
        logout.setTypeface(fonts.OPEN_SANS_SEMIBOLD);

        return parentView;
    }
}
