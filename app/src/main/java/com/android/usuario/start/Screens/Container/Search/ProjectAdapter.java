package com.android.usuario.start.Screens.Container.Search;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by tulio on 15/05/2017.
 */

public class ProjectAdapter extends ArrayAdapter<Project> {
    public ProjectAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_project, parent, false);
        }

        final ImageView _favoriteProject = (ImageView) convertView.findViewById(R.id.card_project_favorit_heart);

        TextView _projectName = (TextView) convertView.findViewById(R.id.card_project_title);
        TextView _duration = (TextView) convertView.findViewById(R.id.card_project_duration);
        TextView _date = (TextView) convertView.findViewById(R.id.card_project_date);
        TextView _difficulty = (TextView) convertView.findViewById(R.id.card_project_difficulty);
        TextView _hustler = (TextView) convertView.findViewById(R.id.card_project_hustler);
        TextView _hacker = (TextView) convertView.findViewById(R.id.card_project_hacker);
        TextView _hippie = (TextView) convertView.findViewById(R.id.card_project_hippie);

        ProgressBar _hustlerProgresbar = (ProgressBar) convertView.findViewById(R.id.card_project_hustler_progressbar);
        ProgressBar _hackerProgresbar = (ProgressBar) convertView.findViewById(R.id.card_project_hacker_progressbar);
        ProgressBar _hippieProgresbar = (ProgressBar) convertView.findViewById(R.id.card_project_hippie_progressbar);

        _hackerProgresbar.setIndeterminate(false);
        _hippieProgresbar.setIndeterminate(false);
        _hustlerProgresbar.setIndeterminate(false);

        Project project = getItem(position);

        if(project != null){
            if(project.getMaxHustlers() == 0){
                _hustlerProgresbar.setProgress(100);
            }else{
                _hustlerProgresbar.setMax(project.getMaxHustlers());
                _hustlerProgresbar.setProgress(project.getnHustlers());
            }

            if(project.getMaxHackers() == 0){
                _hackerProgresbar.setProgress(100);
            }else{
                _hackerProgresbar.setMax(project.getMaxHackers());
                _hackerProgresbar.setProgress(project.getnHackers());
            }
            if(project.getMaxHippies() == 0){
                _hippieProgresbar.setProgress(100);
            } else{
                _hippieProgresbar.setMax(project.getMaxHippies());
                _hippieProgresbar.setProgress(project.getnHippies());
            }
            _projectName.setText(project.getName());
            _duration.setText(project.getDuration() + " dias");
            _date.setText(project.getStartDay() + "/" + (project.getStartMonth() + 1) + "/" + project.getStartYear());
            _difficulty.setText(String.valueOf(project.getDifficulty()));
            _hustler.setText(project.getnHustlers() + "/" + project.getMaxHustlers());
            _hacker.setText(project.getnHackers() + "/" + project.getMaxHackers());
            _hippie.setText(project.getnHippies() + "/" + project.getMaxHippies());

            _favoriteProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _favoriteProject.setImageResource(R.drawable.ic_favorite_full_card);
                }
            });
        }

        return convertView;
    }
}
