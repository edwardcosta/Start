package com.android.usuario.start.Screens.Container.CreateProject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.DataObject.Project;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.RequestManager.Storage;
import com.android.usuario.start.Screens.Container.Search.SearchView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by eduar on 15/05/2017.
 */

public class CreateProjectView extends Fragment {

    private static final int PHOTO_PICKER_1 = 1;
    private static final int PHOTO_PICKER_2 = 2;
    private static final int PHOTO_PICKER_3 = 3;
    private static final int PHOTO_PICKER_4 = 4;
    private static final int PHOTO_PICKER_5 = 5;
    private static final int PHOTO_PICKER_6 = 6;

    private View view;

    private Profile userProfile;

    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mProjectsDatabaseReference;
    private StorageReference mProjectPhotosStorageReference;

    private int tries;
    private int tries2;
    private int tries3;

    private SweetAlertDialog creatingProject;

    private Project mProject = new Project();
    private int pYear;
    private int pDay;
    private int pMonth;
    private Calendar today = Calendar.getInstance();
    private int dificuldade;
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

        userProfile = (Profile) getArguments().getSerializable("userProfile");

        mUsersDatabaseReference = Database.getUsersReference();
        mProjectsDatabaseReference = Database.getProjectsReference();
        //mProjectPhotosStorageReference = Storage.getProjectImagesReference();

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
        _image1 = (ImageView) view.findViewById(R.id.project_image_1);
        _image2 = (ImageView) view.findViewById(R.id.project_image_2);
        _image3 = (ImageView) view.findViewById(R.id.project_image_3);
        _image4 = (ImageView) view.findViewById(R.id.project_image_4);
        _image5 = (ImageView) view.findViewById(R.id.project_image_5);
        _image6 = (ImageView) view.findViewById(R.id.project_image_6);
        _clearImage1 = (ImageView) view.findViewById(R.id.project_image_clear_1);
        _clearImage2 = (ImageView) view.findViewById(R.id.project_image_clear_2);
        _clearImage3 = (ImageView) view.findViewById(R.id.project_image_clear_3);
        _clearImage4 = (ImageView) view.findViewById(R.id.project_image_clear_4);
        _clearImage5 = (ImageView) view.findViewById(R.id.project_image_clear_5);
        _clearImage6 = (ImageView) view.findViewById(R.id.project_image_clear_6);
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

        mProject.setAuthor(userProfile.getId());
        userProfile.addMyProject(mProject.getId());

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
        sendImagesToFirebase();
    }

    private void sendImagesToFirebase(){
        StorageReference saveImage1 = Storage.getProjectImagesReference(mProject.getId()).child("image1.jpg");
        StorageReference saveImage2 = Storage.getProjectImagesReference(mProject.getId()).child("image2.jpg");
        StorageReference saveImage3 = Storage.getProjectImagesReference(mProject.getId()).child("image3.jpg");
        StorageReference saveImage4 = Storage.getProjectImagesReference(mProject.getId()).child("image4.jpg");
        StorageReference saveImage5 = Storage.getProjectImagesReference(mProject.getId()).child("image5.jpg");
        StorageReference saveImage6 = Storage.getProjectImagesReference(mProject.getId()).child("image6.jpg");

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
                        new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
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
                    mProject.getImages().set(imgNumber,uri.toString());
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
        mProjectsDatabaseReference.child(mProject.getId()).setValue(mProject).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mUsersDatabaseReference.child(userProfile.getId()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mUsersDatabaseReference.child(userProfile.getId()).setValue(userProfile);
                        creatingProject.dismiss();
                        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Tudo Certo")
                                .setContentText("Projeto Criado com Sucesso")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                        Fragment fragment = new SearchView();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("userProfile",userProfile);
                                        fragment.setArguments(bundle);
                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.content, fragment)
                                                .commit();
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
                                    Fragment fragment = new SearchView();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("userProfile",userProfile);
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.content, fragment)
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
                                    Fragment fragment = new SearchView();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("userProfile",userProfile);
                                    fragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.content, fragment)
                                            .commit();
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
