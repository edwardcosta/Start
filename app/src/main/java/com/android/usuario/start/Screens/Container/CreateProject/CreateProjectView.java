package com.android.usuario.start.Screens.Container.CreateProject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    private Calendar startDate;
    private EditText projectName;
    private EditText description;
    private TextView dateInit;
    private int year, month, day;

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

        projectName = (EditText) view.findViewById(R.id.projectName);
        description = (EditText) view.findViewById(R.id.description);

        dateInit = (TextView) view.findViewById(R.id.dateInit);
        dateInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To show current date in the datepicker
                startDate = Calendar.getInstance();
                year = startDate.get(Calendar.YEAR);
                month = startDate.get(Calendar.MONTH);
                day = startDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //Change date on TextView
                        dateInit.setText(selectedday + "\\" + (selectedmonth + 1) + "\\" + selectedyear);
                    }
                },year, month, day);
                mDatePicker.setTitle("Data de início");
                mDatePicker.show();
            }
        });

        Button create = (Button) view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProject = new Project(projectName.getText().toString(), description.getText().toString(), "Wilson", null, 0);
                mProjectsDatabaseReference.push().setValue(mProject);
            }
        });

    }
}
