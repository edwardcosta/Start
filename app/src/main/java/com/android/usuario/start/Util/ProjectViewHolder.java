package com.android.usuario.start.Util;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.R;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.Screens.Container.Search.ProjectDetails.ProjectDetailsFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Random;

public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Profile userProfile;

    private TextView _projectName;
    private TextView _hashtags;
    private TextView _duration;
    private TextView _date;
    private TextView _difficulty;
    private TextView _hustler;
    private TextView _hacker;
    TextView _hippie;
    private ProgressBar _hustlerProgresbar;
    private ProgressBar _hackerProgresbar;
    private ProgressBar _hippieProgresbar;
    private ImageView _favoriteProject;
    private SimpleDraweeView _imagCard;

    private Project project;
    private Fragment fragment;

    public ProjectViewHolder(View convertView, Fragment fragment, Profile userProfile) {
        super(convertView);
        this.fragment = fragment;
        this.userProfile = userProfile;

        _favoriteProject = (ImageView) convertView.findViewById(R.id.card_project_favorit_heart);
        _imagCard = (SimpleDraweeView) convertView.findViewById(R.id.card_project_img);

        _projectName = (TextView) convertView.findViewById(R.id.card_project_title);
        _hashtags = (TextView) convertView.findViewById(R.id.card_project_hashtag);
        _duration = (TextView) convertView.findViewById(R.id.card_project_duration);
        _date = (TextView) convertView.findViewById(R.id.card_project_date);
        _difficulty = (TextView) convertView.findViewById(R.id.card_project_difficulty);
        _hustler = (TextView) convertView.findViewById(R.id.card_project_hustler);
        _hacker = (TextView) convertView.findViewById(R.id.card_project_hacker);
        _hippie = (TextView) convertView.findViewById(R.id.card_project_hippie);
        _hustlerProgresbar = (ProgressBar) convertView.findViewById(R.id.card_project_hustler_progressbar);
        _hackerProgresbar = (ProgressBar) convertView.findViewById(R.id.card_project_hacker_progressbar);
        _hippieProgresbar = (ProgressBar) convertView.findViewById(R.id.card_project_hippie_progressbar);

        _hackerProgresbar.setIndeterminate(false);
        _hippieProgresbar.setIndeterminate(false);
        _hustlerProgresbar.setIndeterminate(false);

        /* makes all the cell be clickable */
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("project", project);

        ProjectDetailsFragment detailsFragment = new ProjectDetailsFragment();
        detailsFragment.setArguments(bundle);

        fragment.getFragmentManager().beginTransaction()
                .replace(R.id.content, detailsFragment, "details")
                .addToBackStack(null)
                .commit();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;

        if(project != null) {
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
                _hippieProgresbar.setProgress(100);
            } else {
                _hippieProgresbar.setMax(project.getMaxHippies());
                _hippieProgresbar.setProgress(project.getnHippies());
            }
            _projectName.setText(project.getName());

            String hashtags = "";
            for(String hs : project.getHashtags()){
                hashtags = hashtags.concat(hs + " ");
            }
            _hashtags.setText(hashtags);
            _duration.setText(project.getDuration() + " dias");
            _date.setText(project.getStartDay() + "/" + (project.getStartMonth()));
            _difficulty.setText(String.valueOf(project.getDifficulty()));
            _hustler.setText(project.getnHustlers() + "/" + project.getMaxHustlers());
            _hacker.setText(project.getnHackers() + "/" + project.getMaxHackers());
            _hippie.setText(project.getnHippies() + "/" + project.getMaxHippies());

            if (project.getImages().isEmpty() && project.getImages().get(0).isEmpty()) {

                Random random = new Random();
                int i1 = (random.nextInt(5));

                switch (i1) {
                    case 0:
                        _imagCard.setImageResource(R.drawable.img_card_project_placeholder_1);
                        break;
                    case 1:
                        _imagCard.setImageResource(R.drawable.img_card_project_placeholder_2);
                        break;
                    case 2:
                        _imagCard.setImageResource(R.drawable.img_card_project_placeholder_3);
                        break;
                    case 3:
                        _imagCard.setImageResource(R.drawable.img_card_project_placeholder_4);
                        break;
                    case 4:
                        _imagCard.setImageResource(R.drawable.img_card_project_placeholder_5);
                        break;
                    case 5:
                        _imagCard.setImageResource(R.drawable.img_card_project_placeholder_6);
                        break;
                }
            } else {
                _imagCard.setImageURI(project.getImages().get(0));
            }
        }

        if(userProfile.getFavoritesProjects().contains(project.getId())){
            _favoriteProject.setImageResource(R.drawable.ic_favorite_full_card);
        }else{
            _favoriteProject.setImageResource(R.drawable.ic_favorite_card);
        }

        _favoriteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userProfile.getFavoritesProjects().contains(project.getId())){
                    userProfile.removeFavoriteProject(project.getId());
                    _favoriteProject.setImageResource(R.drawable.ic_favorite_card);
                    Database.getUsersReference().child(userProfile.getId()).setValue(userProfile);
                }else{
                    userProfile.addFavoriteProject(project.getId());
                    _favoriteProject.setImageResource(R.drawable.ic_favorite_full_card);
                    Database.getUsersReference().child(userProfile.getId()).setValue(userProfile);
                }
            }
        });
    }

}
