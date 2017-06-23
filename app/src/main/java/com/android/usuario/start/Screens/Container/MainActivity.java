package com.android.usuario.start.Screens.Container;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.Screens.Auth.SignupActivity;
import com.android.usuario.start.Screens.Container.CreateProject.CreateProjectView;
import com.android.usuario.start.Screens.Container.MyProjects.MyProjectsView;
import com.android.usuario.start.Screens.Container.Profile.ProfileFragment;
import com.android.usuario.start.Screens.Container.Search.SearchView;
import com.android.usuario.start.Screens.Container.Favorite.FavoriteView;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Profile userProfile;

    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    private EditText _searchBar;

    private TextView _filterButton;
    private LinearLayout _filterLayout;
    private TextView _initDate;
    private Spinner _difficulty_spinner;
    private RelativeLayout.LayoutParams layoutParams;

    private int lastSelected;
    private int iDay;
    private int iMonth;
    private int iYear;

    private boolean filterOpen = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (lastSelected == item.getItemId()) {
                return true;
            }
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content);

            if (fragment != null) {
                fragmentTransaction.remove(fragment);
            }

            lastSelected = item.getItemId();

            Bundle bundle = new Bundle();
            bundle.putSerializable("userProfile",userProfile);

            switch (item.getItemId()) {
                case R.id.navigation_search:
                    fragment = new SearchView();
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_star:
                    fragment = new FavoriteView();
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_create:
                    fragment = new CreateProjectView();
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_my_projects:
                    fragment = new MyProjectsView();
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Fresco.initialize(this);

        userProfile = (Profile) getIntent().getExtras().getSerializable("userProfile");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        _searchBar = (EditText) findViewById(R.id.activity_main_searchBar);
        _searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (_searchBar.getRight() - _searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        performSearch();
                        return true;
                    }
                }
                return false;
            }
        });
        _searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        _filterButton = (TextView) findViewById(R.id.activity_main_btn_filter_open);
        _filterLayout = (LinearLayout) findViewById(R.id.activity_main_filter);
        _initDate = (TextView) findViewById(R.id.activity_main_filter_initDate);

        final int toolbarHeight = toolbar.getHeight();

        _filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!filterOpen){
                    filterOpen = true;
                    _filterLayout.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    layoutParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.BELOW,R.id.appbar);
                    _filterLayout.setLayoutParams(layoutParams);
                }else{
                    filterOpen = false;
                    _filterLayout.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, toolbarHeight));
                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,toolbarHeight);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    _filterLayout.setLayoutParams(layoutParams);
                }
            }
        });

        iDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        iMonth = Calendar.getInstance().get(Calendar.MONTH);
        iYear = Calendar.getInstance().get(Calendar.YEAR);

        _initDate.setText(iDay + "\\" + (iMonth + 1) + "\\" + iYear);

        _initDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //Change date on TextView
                        _initDate.setText(selectedday + "\\" + (selectedmonth + 1) + "\\" + selectedyear);
                    }
                }, iYear, iMonth, iDay);
                mDatePicker.setTitle("Nascimento");
                mDatePicker.show();
            }
        });


        _difficulty_spinner = (Spinner) findViewById(R.id.activity_main_filter_difficulty_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _difficulty_spinner.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        navigation.setSelectedItemId(R.id.navigation_search);

    }

    private void performSearch(){
        Toast.makeText(this,"Pesquisa",Toast.LENGTH_SHORT).show();
    }
}