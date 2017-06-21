package com.android.usuario.start.Screens.Container.Search;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.print.PrintJob;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by tulio on 15/05/2017.
 */

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter {

    Profile userProfile;
    List<Project> projects;
    SearchView fragment;


    public ProjectRecyclerViewAdapter(Fragment fragment, List<Project> objects, Profile userProfile) {
        this.projects = objects;
        this.fragment = (SearchView) fragment;
        this.userProfile = userProfile;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ProjectViewHolder holder = (ProjectViewHolder) viewHolder;

        Project project  = projects.get(position);
        holder.setProject(project);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(fragment.getActivity())
                .inflate(R.layout.card_project, parent, false);

        ProjectViewHolder holder = new ProjectViewHolder(view, fragment, userProfile);

        return holder;
    }
}
