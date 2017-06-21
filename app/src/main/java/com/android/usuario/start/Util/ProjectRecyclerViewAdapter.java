package com.android.usuario.start.Util;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.R;

import java.util.List;

/**
 * Created by tulio on 15/05/2017.
 */

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter {

    private Profile userProfile;
    private List<Project> projects;
    private Fragment fragment;


    public ProjectRecyclerViewAdapter(Fragment fragment, List<Project> objects, Profile userProfile) {
        this.projects = objects;
        this.fragment = fragment;
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
