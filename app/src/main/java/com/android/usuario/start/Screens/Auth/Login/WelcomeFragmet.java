package com.android.usuario.start.Screens.Auth.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.usuario.start.R;

/**
 * Created by eduar on 08/06/2017.
 */

public class WelcomeFragmet extends Fragment {

    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_welcome_screen,container,false);

        return parentView;
    }
}