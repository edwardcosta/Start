package com.android.usuario.start.Screens.ProfileChooser.Hustler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.Screens.Container.MainActivity;

/**
 * Created by eduar on 21/06/2017.
 */

public class HustlerView extends Fragment {

    private LinearLayout content;
    private Profile userProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        userProfile = (Profile) getArguments().getSerializable("userProfile");

        LinearLayout contentContent = (LinearLayout) rootView.findViewById(R.id.fragment_profile_chooser_linearlayout);

        ImageView _img = (ImageView) rootView.findViewById(R.id.fragment_profile_chooser_img);
        TextView _profileType = (TextView) rootView.findViewById(R.id.fragment_profile_chooser_profile);
        TextView _profileDescription = (TextView) rootView.findViewById(R.id.fragment_profile_chooser_description);

        contentContent.setBackgroundResource(R.drawable.img_hustler_background);
        _img.setImageResource(R.drawable.img_hustler);
        _profileType.setText("Hustler");
        _profileDescription.setText("Gosto de empreendedorismo,n\"" +
                "comunicação, estratégia, marketing,\n" +
                "administração e coisas do tipo");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        content = (LinearLayout) getView().findViewById(R.id.content);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile.setProfileType(3);
                Database.getUsersReference().child(userProfile.getId()).setValue(userProfile);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("userProfile",userProfile);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
