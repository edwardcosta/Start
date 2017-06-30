package com.android.usuario.start.Screens.Container.Search.ProjectDetails;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.Util.Fonts;
import com.android.usuario.start.Util.Singleton;
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
    private TextView _duration;
    private TextView _date;
    private TextView _difficulty;
    private TextView _hustler;
    private TextView _hacker;
    private TextView _hipster;
    private ProgressBar _hustlerProgresbar;
    private ProgressBar _hackerProgresbar;
    private ProgressBar _hipsterProgresbar;
    private TextView _description;

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
        _difficulty = (TextView) view.findViewById(R.id.proj_details_proj_difficulty);
        _date = (TextView) view.findViewById(R.id.proj_details_proj_date);
        _duration = (TextView) view.findViewById(R.id.proj_details_proj_hustler_duration);
        _hustler = (TextView) view.findViewById(R.id.proj_details_proj_hustler);
        _hacker = (TextView) view.findViewById(R.id.proj_details_proj_hacker);
        _hipster = (TextView) view.findViewById(R.id.proj_details_proj_hipster);
        _hustlerProgresbar = (ProgressBar) view.findViewById(R.id.proj_details_proj_hustler_progressbar);
        _hackerProgresbar = (ProgressBar) view.findViewById(R.id.proj_details_proj_hacker_progressbar);
        _hipsterProgresbar = (ProgressBar) view.findViewById(R.id.proj_details_proj_hipster_progressbar);
        _description = (TextView) view.findViewById(R.id.proj_details_description);

        _hackerProgresbar.setIndeterminate(false);
        _hipsterProgresbar.setIndeterminate(false);
        _hustlerProgresbar.setIndeterminate(false);

        _projectTitle.setText(project.getName());

        String hashtags = "";
        for(String hs : project.getHashtags()){
            hashtags = hashtags.concat(hs + " ");
        }
        _hashtags.setText(hashtags);

        if (project.getMaxHustlers() == 0) {
            _hustlerProgresbar.setProgress(100);
        } else {
            _hustlerProgresbar.setMax(project.getMaxHustlers());
            _hustlerProgresbar.setProgress(project.getnHustlers());
        }

        if (project.getMaxHackers() == 0) {
            _hackerProgresbar.setProgress(100);
        } else {
            _hackerProgresbar.setMax(project.getMaxHackers());
            _hackerProgresbar.setProgress(project.getnHackers());
        }
        if (project.getMaxHippies() == 0) {
            _hipsterProgresbar.setProgress(100);
        } else {
            _hipsterProgresbar.setMax(project.getMaxHippies());
            _hipsterProgresbar.setProgress(project.getnHippies());
        }

        _date.setText(project.getStartDay() + "/" + (project.getStartMonth()));
        _difficulty.setText(Singleton.getStringProjectDifficulty(project.getDifficulty()));
        _hustler.setText(project.getnHustlers() + "/" + project.getMaxHustlers());
        _hacker.setText(project.getnHackers() + "/" + project.getMaxHackers());
        _hipster.setText(project.getnHippies() + "/" + project.getMaxHippies());
        _description.setText(project.getDescription());

        Button projectIn = (Button) view.findViewById(R.id.proj_details_button);
        if(!project.getAuthor().equals(userProfile.getId())) {
            projectIn.setOnClickListener(signInOnClickListener);
        }else {
            projectIn.setVisibility(View.GONE);
        }

        int numberParticipants = project.getMaxHackers() + project.getMaxHippies() + project.getMaxHustlers();

        //Setting fonts
        Fonts fonts = new Fonts(getContext());
        _projectTitle.setTypeface(fonts.OPEN_SANS_BOLD);
        _hashtags.setTypeface(fonts.OPEN_SANS_SEMIBOLD);
        projectIn.setTypeface(fonts.OPEN_SANS_SEMIBOLD);
        _date.setTypeface(fonts.OPEN_SANS_REGULAR);
        _difficulty.setTypeface(fonts.OPEN_SANS_REGULAR);
        _duration.setTypeface(fonts.OPEN_SANS_REGULAR);
        _hacker.setTypeface(fonts.OPEN_SANS_ITALIC);
        _hipster.setTypeface(fonts.OPEN_SANS_ITALIC);
        _hustler.setTypeface(fonts.OPEN_SANS_ITALIC);

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
