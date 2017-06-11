package com.android.usuario.start.Screens.Container.CreateProject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.android.usuario.start.Screens.Container.Search.SearchView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by eduar on 15/05/2017.
 */

public class CreateProjectView extends Fragment {

    private View view;

    private DatabaseReference mProjectsDatabaseReference;

    private int tries;
    private int tries2;

    private SweetAlertDialog creatingProject;

    private Project mProject = new Project();
    private int pYear;
    private int pDay;
    private int pMonth;
    private Calendar today = Calendar.getInstance();
    private int dificuldade;

    private EditText _hashtags;
    private EditText _projectName;
    private EditText _description;
    private EditText _duration;
    private TextView _dateInit;
    private EditText _nHackersInput;
    private EditText _nHippiesInput;
    private EditText _nHustlersInput;
    private Spinner _difficulty_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        verifyID();
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

        _hashtags = (EditText) view.findViewById(R.id.fragment_create_project_hashtags);
        _projectName = (EditText) view.findViewById(R.id.projectName);
        _description = (EditText) view.findViewById(R.id.description);
        _duration = (EditText) view.findViewById(R.id.duration);
        _dateInit = (TextView) view.findViewById(R.id.dateInit);
        _nHackersInput = (EditText) view.findViewById(R.id.nHackers);
        _nHippiesInput = (EditText) view.findViewById(R.id.nHippies);
        _nHustlersInput = (EditText) view.findViewById(R.id.nHustlers);

        _dateInit.setText(pDay + "\\" + (pMonth + 1) + "\\" +  pYear);

        _dateInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //Change date on TextView
                        setDate(selectedday, selectedmonth, selectedyear);
                        _dateInit.setText(selectedday + "\\" + (selectedmonth + 1) + "\\" + selectedyear);
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

        _difficulty_spinner = (Spinner) view.findViewById(R.id.difficulty_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.difficulty_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _difficulty_spinner.setAdapter(adapter);

    }

    private void setDate (int day, int month, int year) {
        pDay = day;
        pMonth = month;
        pYear = year;
    }

    private void createProject () {
        creatingProject = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE);
        creatingProject.setTitleText("Criando Projeto");
        creatingProject.setCancelable(false);
        creatingProject.show();

        List<String> hs = new ArrayList<>();
        String hashtagsAux = _hashtags.getText().toString().trim();
        String[] hashtags = hashtagsAux.split(" ");
        Collections.addAll(hs,hashtags);

        mProject.setName(_projectName.getText().toString());
        //TODO get User name
        mProject.setDescription(_description.getText().toString());
        mProject.setStartDay(pDay);
        mProject.setStartMonth(pMonth);
        mProject.setStartYear(pYear);
        if (_duration.getText().toString().equals("")) {
            mProject.setDuration(0);
        } else {
            mProject.setDuration(Integer.parseInt(_duration.getText().toString()));
        }
        if (_nHackersInput.getText().toString().equals("")) {
            mProject.setMaxHackers(0);
        } else {
            mProject.setMaxHackers(Integer.parseInt(_nHackersInput.getText().toString()));
        }
        if (_nHippiesInput.getText().toString().equals("")) {
            mProject.setMaxHippies(0);
        } else {
            mProject.setMaxHippies(Integer.parseInt(_nHippiesInput.getText().toString()));
        }
        if (_nHustlersInput.getText().toString().equals("")) {
            mProject.setMaxHustlers(0);
        } else {
            mProject.setMaxHustlers(Integer.parseInt(_nHustlersInput.getText().toString()));
        }
        mProject.setDifficulty(_difficulty_spinner.getSelectedItemPosition());
        mProject.setHashtags(hs);
        //TODO difficulty (int) to string
        //Send to Firebase
        sendToFirebase();
    }

    private void sendToFirebase(){
        mProjectsDatabaseReference.child(mProject.getId()).setValue(mProject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                creatingProject.dismiss();
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Tudo Certo")
                        .setContentText("Projeto Criado com Sucesso")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.content, new SearchView())
                                        .commit();
                            }
                        })
                        .show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tries2++;
                if(tries2 < 5){
                    new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Falha ao tentar criar o projeto!")
                            .setContentText("Gostaria de tentar novamente?")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    sendToFirebase();

                                }
                            }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    creatingProject.dismiss();
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.content, new SearchView())
                                            .commit();
                                }
                    }).show();
                }else{
                    new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro")
                            .setContentText("Aparentemente você tentou criar varias cezes o projeto."+
                                    "Por favor, tente novamente mais tarde.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    creatingProject.dismiss();

                                }
                            }).show();
                }
            }
        });
    }

    private void verifyID(){
        final String id = UUID.randomUUID().toString();
        DatabaseReference projects = Database.getProjectsReference();
        projects.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Project p = dataSnapshot.getValue(Project.class);
                tries = 0;
                if(p == null){
                    mProject.setId(id);
                }else {
                    verifyID();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tries++;
                if(tries < 5){
                    verifyID();
                }else{
                    if(creatingProject != null && creatingProject.isShowing()){
                        creatingProject.dismiss();
                    }
                    new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Falha ao tentar criar projeto!")
                            .setContentText("Por algum motivo não conseguimos conectar com nossos servidores," +
                                    " e por, esse motivo, não conseguiremos criar seu projeto. :(\n" +
                                    " Deseja continuar e que tentemos novamente?")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    tries = 0;
                                    verifyID();
                                }
                            }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.content, new SearchView())
                                            .commit();
                                    }
                    }).show();
                }
            }
        });
    }
}
