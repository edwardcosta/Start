package com.android.usuario.start.Screens.Container.CreateProject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.usuario.start.R;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.RequestManager.Database;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

/**
 * Created by eduar on 15/05/2017.
 */

public class CreateProjectView extends Fragment {

    private View view;

    private DatabaseReference mProjectsDatabaseReference;

    private Project mProject;
    private int pYear;
    private int pDay;
    private int pMonth;
    private Calendar today = Calendar.getInstance();
    private int dificuldade;

    private EditText projectName;
    private EditText description;
    private EditText duration;
    private TextView dateInit;
    private EditText nHackersInput;
    private EditText nHippiesInput;
    private EditText nHustlersInput;
    private Spinner difficulty_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_project, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();

        mProjectsDatabaseReference = Database.getProjectsReference();

        pDay = today.get(Calendar.DAY_OF_MONTH);
        pMonth = today.get(Calendar.MONTH);
        pYear = today.get(Calendar.YEAR);

        projectName = (EditText) view.findViewById(R.id.projectName);
        description = (EditText) view.findViewById(R.id.description);
        duration = (EditText) view.findViewById(R.id.duration);
        dateInit = (TextView) view.findViewById(R.id.dateInit);
        nHackersInput = (EditText) view.findViewById(R.id.nHackers);
        nHippiesInput = (EditText) view.findViewById(R.id.nHippies);
        nHustlersInput = (EditText) view.findViewById(R.id.nHustlers);

        dateInit.setText(pDay + "\\" + (pMonth + 1) + "\\" +  pYear);

        dateInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //Change date on TextView
                        setDate(selectedday, selectedmonth, selectedyear);
                        dateInit.setText(selectedday + "\\" + (selectedmonth + 1) + "\\" + selectedyear);
                    }
                }, pYear, pMonth, pDay);
                mDatePicker.setTitle("Início");
                mDatePicker.show();
            }
        });

        Button create = (Button) view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProject();
            }
        });

        difficulty_spinner = (Spinner) view.findViewById(R.id.difficulty_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.difficulty_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        difficulty_spinner.setAdapter(adapter);

    }

    private void setDate (int day, int month, int year) {
        pDay = day;
        pMonth = month;
        pYear = year;
    }

    private void createProject () {
        mProject = new Project();
        mProject.setName(projectName.getText().toString());
        //TODO get User name
        mProject.setDescription(description.getText().toString());
        mProject.setStartDay(pDay);
        mProject.setStartMonth(pMonth);
        mProject.setStartYear(pYear);
        mProject.setDuration(Integer.parseInt(duration.getText().toString()));
        mProject.setMaxHackers(Integer.parseInt(nHackersInput.getText().toString()));
        mProject.setMaxHippies(Integer.parseInt(nHippiesInput.getText().toString()));
        mProject.setMaxHustlers(Integer.parseInt(nHustlersInput.getText().toString()));
        mProject.setDifficulty(difficulty_spinner.getSelectedItemPosition());
        //TODO difficulty (int) to string
        //TODO get hashtags
        //Send to Firebase
        mProjectsDatabaseReference.push().setValue(mProject);
    }
}
