package com.android.usuario.start.Screens.Container.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.R;
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

    private Profile userProfile;

    private RecyclerView mRecyclerView;
    private ProjectRecyclerViewAdapter mProjectAdapter;
    private Project mProject;
    private View view;
    List<Project> projects;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mProjectsDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userProfile = (Profile) getArguments().getSerializable("userProfile");
    }

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.projectListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mProjectsDatabaseReference = Database.getProjectsReference();

        // Initialize message ListView and its adapter
        projects = new ArrayList<>();
        mProjectAdapter = new ProjectRecyclerViewAdapter(this, projects,userProfile);
        mRecyclerView.setAdapter(mProjectAdapter);

        //Populando com projetos ficticios
        //databasePopulate();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Project project = dataSnapshot.getValue(Project.class);
                    projects.add(project);
                    mProjectAdapter.notifyDataSetChanged();
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
            mProject = new Project();
            mProjectsDatabaseReference.push().setValue(mProject);
        }
    }
}
