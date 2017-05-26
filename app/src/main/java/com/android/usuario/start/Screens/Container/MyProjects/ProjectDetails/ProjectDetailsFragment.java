package com.android.usuario.start.Screens.Container.MyProjects.ProjectDetails;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.usuario.start.R;

import org.w3c.dom.Text;

public class ProjectDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_details, container, false);

        VideoView videoView = (VideoView) view.findViewById(R.id.proj_details_video_view);
        TextView projectTitle = (TextView) view.findViewById(R.id.proj_details_proj_title);


        return view;
    }
}
