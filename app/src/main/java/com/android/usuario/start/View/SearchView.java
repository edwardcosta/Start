package com.android.usuario.start.View;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.usuario.start.R;
import com.android.usuario.start.View.Project.ProjectAdapter;
import com.android.usuario.start.View.Project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 15/05/2017.
 */

public class SearchView extends Fragment {

    private ListView mProjectListView;
    private ProjectAdapter mProjectAdapter;
    private Project mProject;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();

        mProjectListView = (ListView) view.findViewById(R.id.projectListView);


        // Initialize message ListView and its adapter
        List<Project> projects = new ArrayList<>();
        mProjectAdapter = new ProjectAdapter(getActivity(), R.layout.project_card, projects);
        mProjectListView.setAdapter(mProjectAdapter);

        //Populando com projetos ficticios
        mProject = new Project("TP1", "Disciplina optativa", "Wilson");
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
        mProjectAdapter.add(mProject);
    }
}
