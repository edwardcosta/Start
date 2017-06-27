package com.android.usuario.start.Screens.Container.Search.ProjectDetails;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.RequestManager.Database;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProjectDetailsFragment extends Fragment {

    private Project project;
    private Profile userProfile;

    private CarouselView carouselView;

    private TextView _projectTitle;
    private TextView _hashtags;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        project = (Project) getArguments().getSerializable("project");
        userProfile = (Profile) getArguments().get("userProfile");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_details, container, false);

        carouselView = (CarouselView) view.findViewById(R.id.proj_details_image_view);
        carouselView.setPageCount(3);
        carouselView.setImageListener(imageListener);
        _projectTitle = (TextView) view.findViewById(R.id.proj_details_proj_title);
        _hashtags = (TextView) view.findViewById(R.id.proj_details_proj_hashtag);
        /*TextView projectPeriod = (TextView) view.findViewById(R.id.proj_details_period_duration);
        TextView projectStartDate = (TextView) view.findViewById(R.id.proj_details_start_date);
        TextView projectParticipantsNumber = (TextView) view.findViewById(R.id.proj_details_participants);
        TextView projectDescription = (TextView) view.findViewById(R.id.proj_details_description);*/

        Button projectIn = (Button) view.findViewById(R.id.proj_details_button);
        if(!project.getAuthor().equals(userProfile.getId())) {
            projectIn.setOnClickListener(signInOnClickListener);
        }else {
            projectIn.setVisibility(View.GONE);
        }

        int numberParticipants = project.getMaxHackers() + project.getMaxHippies() + project.getMaxHustlers();

        _projectTitle.setText(project.getName());
        /*projectPeriod.setText(String.valueOf(project.getDuration())+ " dias");
        projectStartDate.setText(project.getStartDay() + "/" + project.getStartMonth());
        projectParticipantsNumber.setText(String.valueOf(numberParticipants));
        projectDescription.setText(project.getDescription());*/

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        }
    };

    /* ON Clicks */
    View.OnClickListener signInOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final SweetAlertDialog progress = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            progress.setTitleText("Solicitando Participação").show();
            if(!project.getWantToParticipate().contains(userProfile.getId())) {
                project.addWantToParticipateOsProject(userProfile.getId());
                Database.getProjectsReference().child(project.getId()).setValue(project)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progress.dismiss();
                                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Solicitação enviada com sucesso!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                getFragmentManager().popBackStack();
                                            }
                                        }).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.dismiss();
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro ao fazer solicitação")
                                .setContentText("Por favor, tente mais tarde")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
            }else{
                progress.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Atenção!")
                        .setContentText("Você já fez a solicitação de partipação no projeto," +
                                " por favor agauarde a confirmação do administrador.")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }).show();
            }
        }
    };
}
