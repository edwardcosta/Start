package com.android.usuario.start.Screens.Container.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Container.MyProjects.ProjectDetails.ProjectDetailsFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProjectsDatabaseReference;
    private ChildEventListener mChildEventListener;

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

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProjectsDatabaseReference = Database.getProjectsReference();


        // Initialize message ListView and its adapter
        List<Project> projects = new ArrayList<>();
        mProjectAdapter = new ProjectAdapter(getActivity(), R.layout.project_card, projects);
        mProjectListView.setAdapter(mProjectAdapter);

        //Populando com projetos ficticios
        //databasePopulate();

        mProjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Project project = (Project) adapterView.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("project", project);

                ProjectDetailsFragment detailsFragment = new ProjectDetailsFragment();
                detailsFragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.content, detailsFragment, "details")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        mProjectAdapter.clear();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Project project = dataSnapshot.getValue(Project.class);
                    mProjectAdapter.add(project);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mProjectsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mProjectsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void databasePopulate() {
        for (int i = 0; i<10; i++) {
            mProject = new Project("TP" + i, "Disciplina optativa", "Wilson");
            mProjectsDatabaseReference.push().setValue(mProject);
        }
    }
}
