package com.android.usuario.start.Screens.Container.Search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.R;
import com.android.usuario.start.Util.ProjectRecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduar on 15/05/2017.
 */

public class SearchView extends Fragment {

    private Profile userProfile;

    private View parentView;
    private EditText searchView;

    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public ProjectRecyclerViewAdapter mProjectAdapter;
    public List<Project> projects = new ArrayList<>();

    // Firebase instance variables
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
        parentView = inflater.inflate(R.layout.fragment_search, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.fragment_search_swipetorefresh);
        mRecyclerView = (RecyclerView) parentView.findViewById(R.id.projectListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // Initialize Firebase components
        mProjectsDatabaseReference = Database.getProjectsReference();
        attachDatabaseReadListener();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        searchView = (EditText) parentView.findViewById(R.id.activity_main_searchBar);

        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshContent();
    }

    public void refreshContent(){
        // Initialize message ListView and its adapter
        mProjectAdapter = new ProjectRecyclerViewAdapter(this, projects, userProfile);
        mRecyclerView.setAdapter(mProjectAdapter);
        swipeRefreshLayout.setRefreshing(false);
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
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Project project = dataSnapshot.getValue(Project.class);
                    projects.remove(project);
                    mProjectAdapter.notifyDataSetChanged();
                }
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mProjectsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    public void getAllProjectsFromFirebase() {
        ValueEventListener projectListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projects.clear();
                for (DataSnapshot projectSnapshot: dataSnapshot.getChildren()) {
                    Project project = projectSnapshot.getValue(Project.class);
                    projects.add(project);
                }
                mProjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("SearchFragment", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mProjectsDatabaseReference.addListenerForSingleValueEvent(projectListener);
    }
}
