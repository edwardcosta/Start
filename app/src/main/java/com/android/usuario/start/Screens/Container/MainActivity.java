package com.android.usuario.start.Screens.Container;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Container.CreateProject.CreateProjectView;
import com.android.usuario.start.Screens.Container.Profile.ProfileFragment;
import com.android.usuario.start.Screens.Container.Search.SearchView;
import com.android.usuario.start.Screens.Container.Star.FavoriteView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private int lastSelected;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if(lastSelected == item.getItemId()){
                return true;
            }
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content);

            if(fragment != null){
                fragmentTransaction.remove(fragment);
            }

            lastSelected = item.getItemId();

            switch (item.getItemId()) {
                case R.id.navigation_search:
                    fragment = new SearchView();
                    fragmentTransaction.add(R.id.content,fragment).commit();
                    return true;
                case R.id.navigation_star:
                    fragment = new FavoriteView();
                    fragmentTransaction.add(R.id.content,fragment).commit();
                    return true;
                case R.id.navigation_create:
                    fragment = new CreateProjectView();
                    fragmentTransaction.add(R.id.content,fragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new CreateProjectView();
                    fragmentTransaction.add(R.id.content,fragment).commit();
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    fragmentTransaction.add(R.id.content,fragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton projectCreate = (FloatingActionButton) findViewById(R.id.projectCreate);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        navigation.setSelectedItemId(R.id.navigation_search);

        projectCreate.bringToFront();
    }

}
