package com.android.usuario.start.Screens.Auth.Login;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.usuario.start.R;
import com.android.usuario.start.Util.Fonts;

/**
 * Created by eduar on 08/06/2017.
 */

public class WelcomeFragmet extends Fragment {

    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_welcome_screen,container,false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView _logo = (TextView) parentView.findViewById(R.id.fragment_welcome_screen_logo);

        //Setting fonts
        Fonts fonts = new Fonts(getContext());
        _logo.setTypeface(fonts.BEBAS_NEUE_BOLD);

        return parentView;
    }
}
