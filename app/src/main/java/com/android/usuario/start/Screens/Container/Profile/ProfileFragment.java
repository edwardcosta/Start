package com.android.usuario.start.Screens.Container.Profile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Auth.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by tulio on 05/05/2017.
 */

public class ProfileFragment extends Fragment {

    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_profile,container,false);

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
