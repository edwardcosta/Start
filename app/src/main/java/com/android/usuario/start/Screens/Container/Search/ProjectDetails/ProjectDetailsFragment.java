package com.android.usuario.start.Screens.Container.Search.ProjectDetails;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.usuario.start.R;
import com.android.usuario.start.DataObject.Project;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProjectDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_details, container, false);

        VideoView videoView = (VideoView) view.findViewById(R.id.proj_details_video_view);
        TextView projectTitle = (TextView) view.findViewById(R.id.proj_details_proj_title);
        TextView projectPeriod = (TextView) view.findViewById(R.id.proj_details_period_duration);
        TextView projectStartDate = (TextView) view.findViewById(R.id.proj_details_start_date);
        TextView projectParticipantsNumber = (TextView) view.findViewById(R.id.proj_details_participants);
        TextView projectDescription = (TextView) view.findViewById(R.id.proj_details_description);

        Button projectIn = (Button) view.findViewById(R.id.proj_details_button);
        projectIn.setOnClickListener(signInOnClickListener);

        Bundle bundle = getArguments();
        Project project = (Project) bundle.getSerializable("project");

        int numberParticipants = project.getMaxHackers() + project.getMaxHippies() + project.getMaxHustlers();

        projectTitle.setText(project.getName());
        projectPeriod.setText(String.valueOf(project.getDuration())+ " dias");
        projectStartDate.setText(project.getStartDay() + "/" + project.getStartMonth());
        projectParticipantsNumber.setText(String.valueOf(numberParticipants));
        projectDescription.setText(project.getDescription());

        return view;
    }

    View.OnClickListener signInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Good job!")
                    .setContentText("You clicked the button!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            getFragmentManager().popBackStack();
                        }
                    })
                    .show();
        }
    };
}
