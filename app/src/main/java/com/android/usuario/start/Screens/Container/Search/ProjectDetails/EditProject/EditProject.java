package com.android.usuario.start.Screens.Container.Search.ProjectDetails.EditProject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.R;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.RequestManager.Storage;
import com.android.usuario.start.Screens.Container.Search.SearchView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by eduar on 30/06/2017.
 */

public class EditProject extends AppCompatActivity {

    private Profile userProfile;
    private Project project;

    private static final int PHOTO_PICKER_1 = 1;
    private static final int PHOTO_PICKER_2 = 2;
    private static final int PHOTO_PICKER_3 = 3;
    private static final int PHOTO_PICKER_4 = 4;
    private static final int PHOTO_PICKER_5 = 5;
    private static final int PHOTO_PICKER_6 = 6;

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mProjectsDatabaseReference;

    private int tries2;
    private int tries3;

    private SweetAlertDialog creatingProject;

    private int pYear;
    private int pDay;
    private int pMonth;
    private Calendar today = Calendar.getInstance();
    private ArrayList<Uri> projectPhotos = new ArrayList<>();

    private Uri image1 = null;
    private Uri image2 = null;
    private Uri image3 = null;
    private Uri image4 = null;
    private Uri image5 = null;
    private Uri image6 = null;

    private EditText _hashtags;
    private EditText _projectName;
    private EditText _description;
    private EditText _duration;
    private TextView _dateInit;
    private EditText _nHackersInput;
    private EditText _nHippiesInput;
    private EditText _nHustlersInput;
    private Spinner _difficulty_spinner;
    private ImageView _image1;
    private ImageView _image2;
    private ImageView _image3;
    private ImageView _image4;
    private ImageView _image5;
    private ImageView _image6;
    private ImageView _clearImage1;
    private ImageView _clearImage2;
    private ImageView _clearImage3;
    private ImageView _clearImage4;
    private ImageView _clearImage5;
    private ImageView _clearImage6;

    /* no image = 0
     * has image to send = 1
     * image sent = 2
     */
    private List<Integer> isImageSent = Arrays.asList(0,0,0,0,0,0);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_project);

        userProfile = (Profile) getIntent().getExtras().get("userProfile");
        project = (Project) getIntent().getExtras().get("project");

        mUsersDatabaseReference = Database.getUsersReference();
        mProjectsDatabaseReference = Database.getProjectsReference();

        pDay = today.get(Calendar.DAY_OF_MONTH);
        pMonth = today.get(Calendar.MONTH);
        pYear = today.get(Calendar.YEAR);

        _hashtags = (EditText) findViewById(R.id.fragment_create_project_hashtags);
        _projectName = (EditText) findViewById(R.id.projectName);
        _description = (EditText) findViewById(R.id.description);
        _duration = (EditText) findViewById(R.id.duration);
        _dateInit = (TextView) findViewById(R.id.dateInit);
        _nHackersInput = (EditText) findViewById(R.id.nHackers);
        _nHippiesInput = (EditText) findViewById(R.id.nHippies);
        _nHustlersInput = (EditText) findViewById(R.id.nHustlers);

        String hashtags = "";
        for(String hs : project.getHashtags()){
            hashtags = hashtags.concat(hs + " ");
        }
        _hashtags.setText(hashtags);
        _projectName.setText(project.getName());
        _description.setText(project.getDescription());
        _duration.setText(project.getDuration());
        _dateInit.setText(project.getStartDay() + "/" + (project.getStartMonth()) + "/" + project.getStartYear());
        _nHackersInput.setText(project.getnHackers());
        _nHippiesInput.setText(project.getnHippies());
        _nHustlersInput.setText(project.getnHustlers());

        _image1 = (ImageView) findViewById(R.id.project_image_1);
        _image2 = (ImageView) findViewById(R.id.project_image_2);
        _image3 = (ImageView) findViewById(R.id.project_image_3);
        _image4 = (ImageView) findViewById(R.id.project_image_4);
        _image5 = (ImageView) findViewById(R.id.project_image_5);
        _image6 = (ImageView) findViewById(R.id.project_image_6);

        for(int i = 0; i< project.getImages().size(); i++){
            String s = project.getImages().get(i);
            if(!s.isEmpty()){
                Picasso.with(EditProject.this).load(s);
            }
        }

        _clearImage1 = (ImageView) findViewById(R.id.project_image_clear_1);
        _clearImage2 = (ImageView) findViewById(R.id.project_image_clear_2);
        _clearImage3 = (ImageView) findViewById(R.id.project_image_clear_3);
        _clearImage4 = (ImageView) findViewById(R.id.project_image_clear_4);
        _clearImage5 = (ImageView) findViewById(R.id.project_image_clear_5);
        _clearImage6 = (ImageView) findViewById(R.id.project_image_clear_6);
        _clearImage1.setVisibility(View.INVISIBLE);
        _clearImage2.setVisibility(View.INVISIBLE);
        _clearImage3.setVisibility(View.INVISIBLE);
        _clearImage4.setVisibility(View.INVISIBLE);
        _clearImage5.setVisibility(View.INVISIBLE);
        _clearImage6.setVisibility(View.INVISIBLE);

        createImageListeners();

        _dateInit.setText(pDay + "\\" + (pMonth + 1) + "\\" +  pYear);

        _dateInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(EditProject.this, new DatePickerDialog.OnDateSetListener() {
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

        Button create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProject();
            }
        });

        _difficulty_spinner = (Spinner) findViewById(R.id.difficulty_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditProject.this, R.array.difficulty_array, android.R.layout.simple_spinner_item);
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
        creatingProject = new SweetAlertDialog(EditProject.this,SweetAlertDialog.PROGRESS_TYPE);
        creatingProject.setTitleText("Criando Projeto");
        creatingProject.setCancelable(false);
        creatingProject.show();

        List<String> hs = new ArrayList<>();
        String hashtagsAux = _hashtags.getText().toString().trim();
        String[] hashtags = hashtagsAux.split(" ");
        Collections.addAll(hs,hashtags);

        project.setName(_projectName.getText().toString());
        project.setDescription(_description.getText().toString());
        project.setStartDay(pDay);
        project.setStartMonth(pMonth);
        project.setStartYear(pYear);
        if (_duration.getText().toString().equals("")) {
            project.setDuration(0);
        } else {
            project.setDuration(Integer.parseInt(_duration.getText().toString()));
        }
        if (_nHackersInput.getText().toString().equals("")) {
            project.setMaxHackers(0);
        } else {
            project.setMaxHackers(Integer.parseInt(_nHackersInput.getText().toString()));
        }
        if (_nHippiesInput.getText().toString().equals("")) {
            project.setMaxHippies(0);
        } else {
            project.setMaxHippies(Integer.parseInt(_nHippiesInput.getText().toString()));
        }
        if (_nHustlersInput.getText().toString().equals("")) {
            project.setMaxHustlers(0);
        } else {
            project.setMaxHustlers(Integer.parseInt(_nHustlersInput.getText().toString()));
        }
        project.setDifficulty(_difficulty_spinner.getSelectedItemPosition());
        project.setHashtags(hs);
        //Send to Firebase
        sendImagesToFirebase();

        //TODO create edit project logic
    }

    private void sendImagesToFirebase(){
        StorageReference saveImage1 = Storage.getProjectImagesReference(project.getId()).child("image1.jpg");
        StorageReference saveImage2 = Storage.getProjectImagesReference(project.getId()).child("image2.jpg");
        StorageReference saveImage3 = Storage.getProjectImagesReference(project.getId()).child("image3.jpg");
        StorageReference saveImage4 = Storage.getProjectImagesReference(project.getId()).child("image4.jpg");
        StorageReference saveImage5 = Storage.getProjectImagesReference(project.getId()).child("image5.jpg");
        StorageReference saveImage6 = Storage.getProjectImagesReference(project.getId()).child("image6.jpg");

        boolean hasImageToSend = false;

        if(isImageSent.get(0) != 0){
            sendImagesToFirebase(saveImage1,_image1,0);
            hasImageToSend = true;
        }
        if(isImageSent.get(1) != 0){
            sendImagesToFirebase(saveImage2,_image2,1);
            hasImageToSend = true;
        }
        if(isImageSent.get(2) != 0){
            sendImagesToFirebase(saveImage3,_image3,2);
            hasImageToSend = true;
        }
        if(isImageSent.get(3) != 0){
            sendImagesToFirebase(saveImage4,_image4,3);
            hasImageToSend = true;
        }
        if(isImageSent.get(4) != 0){
            sendImagesToFirebase(saveImage5,_image5,4);
            hasImageToSend = true;
        }
        if(isImageSent.get(5) != 0){
            sendImagesToFirebase(saveImage6,_image6,5);
            hasImageToSend = true;
        }
        if(!hasImageToSend){
            sendToFirebase();
        }
    }

    private void sendImagesToFirebase(final StorageReference storageReference, final ImageView imageView, final int imgNumber){

        Storage.uploadFromImageView(imageView, storageReference, new Storage.UploadImageCallBack() {
            @Override
            public void uploadImageCallBack(Uri uri) {
                if(uri == null){
                    isImageSent.set(imgNumber,1);
                    tries3++;
                    if(tries3 < 5) {
                        sendImagesToFirebase(storageReference, imageView, imgNumber);
                    }else{
                        creatingProject.dismiss();
                        new SweetAlertDialog(EditProject.this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro")
                                .setContentText("Falha ao conectar com o servidor. " +
                                        "Tente novamente mais tarde.")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();

                                    }
                                }).show();
                    }
                }else{
                    isImageSent.set(imgNumber,2);
                    project.getImages().set(imgNumber,uri.toString());
                }
                verifyImages();
            }
        });
    }

    private void verifyImages(){
        if(!isImageSent.contains(1)){
            sendToFirebase();
        }
    }

    private void sendToFirebase(){
        mProjectsDatabaseReference.child(project.getId()).setValue(project).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mUsersDatabaseReference.child(userProfile.getId()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mUsersDatabaseReference.child(userProfile.getId()).setValue(userProfile);
                        creatingProject.dismiss();
                        new SweetAlertDialog(EditProject.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Tudo Certo")
                                .setContentText("Projeto Criado com Sucesso")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        finish();
                                    }
                                })
                                .show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                tries2++;
                creatingProject.dismiss();
                if(tries2 < 5){
                    new SweetAlertDialog(EditProject.this,SweetAlertDialog.ERROR_TYPE)
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
                            finish();
                        }
                    }).show();
                }else{
                    new SweetAlertDialog(EditProject.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Erro")
                            .setContentText("Aparentemente você tentou criar varias cezes o projeto."+
                                    "Por favor, tente novamente mais tarde.")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }
            }
        });
    }

    private void createImageListeners() {
        _image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image1 != null) {
                    projectPhotos.remove(image1);
                    image1 = null;
                }
                isImageSent.set(0,1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando:"), PHOTO_PICKER_1);
            }
        });
        _image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image2 != null) {
                    projectPhotos.remove(image2);
                    image2 = null;
                }
                isImageSent.set(1,1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando:"), PHOTO_PICKER_2);
            }
        });
        _image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image3 != null) {
                    projectPhotos.remove(image3);
                    image3 = null;
                }
                isImageSent.set(2,1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando:"), PHOTO_PICKER_3);
            }
        });
        _image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image4 != null) {
                    projectPhotos.remove(image4);
                    image4 = null;
                }
                isImageSent.set(3,1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando:"), PHOTO_PICKER_4);
            }
        });
        _image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image5 != null) {
                    projectPhotos.remove(image5);
                    image5 = null;
                }
                isImageSent.set(4,1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando:"), PHOTO_PICKER_5);
            }
        });
        _image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (image6 != null) {
                    projectPhotos.remove(image6);
                    image6 = null;
                }
                isImageSent.set(5,1);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a ação usando:"), PHOTO_PICKER_6);
            }
        });
        _clearImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSent.set(0,0);
                _image1.setImageResource(R.drawable.img_empty_image);
                _clearImage1.setVisibility(View.INVISIBLE);
                projectPhotos.remove(image1);
                image1 = null;
            }
        });
        _clearImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSent.set(1,0);
                _image2.setImageResource(R.drawable.img_empty_image);
                _clearImage2.setVisibility(View.INVISIBLE);
                projectPhotos.remove(image2);
                image2 = null;
            }
        });
        _clearImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSent.set(2,0);
                _image3.setImageResource(R.drawable.img_empty_image);
                _clearImage3.setVisibility(View.INVISIBLE);
                projectPhotos.remove(image3);
                image3 = null;
            }
        });
        _clearImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSent.set(3,0);
                _image4.setImageResource(R.drawable.img_empty_image);
                _clearImage4.setVisibility(View.INVISIBLE);
                projectPhotos.remove(image4);
                image4 = null;
            }
        });
        _clearImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSent.set(4,0);
                _image5.setImageResource(R.drawable.img_empty_image);
                _clearImage5.setVisibility(View.INVISIBLE);
                projectPhotos.remove(image5);
                image5 = null;
            }
        });
        _clearImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSent.set(5,0);
                _image6.setImageResource(R.drawable.img_empty_image);
                _clearImage6.setVisibility(View.INVISIBLE);
                projectPhotos.remove(image6);
                image6 = null;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_PICKER_1:
                    image1 = data.getData();
                    projectPhotos.add(image1);
                    _image1.setImageURI(image1);
                    _clearImage1.setVisibility(View.VISIBLE);
                    Log.d("Photos", projectPhotos.toString());
                    break;
                case PHOTO_PICKER_2:
                    image2 = data.getData();
                    projectPhotos.add(image2);
                    _image2.setImageURI(image2);
                    _clearImage2.setVisibility(View.VISIBLE);
                    Log.d("Photos", projectPhotos.toString());
                    break;
                case PHOTO_PICKER_3:
                    image3 = data.getData();
                    projectPhotos.add(image3);
                    _image3.setImageURI(image3);
                    _clearImage3.setVisibility(View.VISIBLE);
                    Log.d("Photos", projectPhotos.toString());
                    break;
                case PHOTO_PICKER_4:
                    image4 = data.getData();
                    projectPhotos.add(image4);
                    _image4.setImageURI(image4);
                    _clearImage4.setVisibility(View.VISIBLE);
                    Log.d("Photos", projectPhotos.toString());
                    break;
                case PHOTO_PICKER_5:
                    image5 = data.getData();
                    projectPhotos.add(image5);
                    _image5.setImageURI(image5);
                    _clearImage5.setVisibility(View.VISIBLE);
                    Log.d("Photos", projectPhotos.toString());
                    break;
                case PHOTO_PICKER_6:
                    image6 = data.getData();
                    projectPhotos.add(image6);
                    _image6.setImageURI(image6);
                    _clearImage6.setVisibility(View.VISIBLE);
                    Log.d("Photos", projectPhotos.toString());
                    break;
                default:
                    break;
            }
        }
    }
}
