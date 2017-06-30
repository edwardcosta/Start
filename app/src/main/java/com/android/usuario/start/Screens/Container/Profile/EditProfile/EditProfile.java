package com.android.usuario.start.Screens.Container.Profile.EditProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.Screens.ProfileChooser.ScreenSlidePagerActivity;
import com.android.usuario.start.Util.Fonts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by eduar on 30/06/2017.
 */

public class EditProfile extends AppCompatActivity {

    private Profile userProfile;

    private DatabaseReference userDatabaseReference;

    private TextView _logo;
    private TextInputLayout _inputName;
    private EditText _name;
    private TextInputLayout _inputEmail;
    private EditText _emailText;
    private TextInputLayout _inputAdress;
    private EditText _adress;
    private TextInputLayout _inputPhoneNumber;
    private EditText _phoneNumber;
    private TextInputLayout _inputDescription;
    private EditText _description;
    private TextInputLayout _inputPassword;
    private EditText _passwordText;
    private TextInputLayout _inputRPassword;
    private EditText _rPasswordText;
    private TextView _signupButton;
    private TextView _loginLink;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        userProfile = (Profile) getIntent().getExtras().getSerializable("userProfile");

        userDatabaseReference = Database.getUsersReference();

        _logo = (TextView) findViewById(R.id.activity_signup_logo);
        _inputName = (TextInputLayout) findViewById(R.id.activity_signup_textinput_name);
        _name = (EditText) findViewById(R.id.activity_signup_input_name);
        _inputEmail = (TextInputLayout) findViewById(R.id.activity_signup_textinput_email);
        _emailText = (EditText) findViewById(R.id.activity_signup_input_email);
        _inputAdress = (TextInputLayout) findViewById(R.id.activity_signup_textinput_adress);
        _adress = (EditText) findViewById(R.id.activity_signup_input_adress);
        _inputPhoneNumber = (TextInputLayout) findViewById(R.id.activity_signup_textinput_phonenumber);
        _phoneNumber = (EditText) findViewById(R.id.activity_signup_input_phonenumber);
        _inputDescription = (TextInputLayout) findViewById(R.id.activity_signup_textinput_description);
        _description = (EditText) findViewById(R.id.activity_signup_input_description);
        _inputPassword = (TextInputLayout) findViewById(R.id.activity_signup_textinput_password);
        _passwordText = (EditText) findViewById(R.id.activity_signup_input_password);
        _inputRPassword = (TextInputLayout) findViewById(R.id.activity_signup_textinput_retype_password);
        _rPasswordText = (EditText) findViewById(R.id.activity_signup_input_retype_password);
        _signupButton = (TextView) findViewById(R.id.activity_signup_btn_signup);
        _loginLink = (TextView) findViewById(R.id.activity_signup_link_login);

        _inputPassword.setVisibility(View.GONE);
        _inputRPassword.setVisibility(View.GONE);
        _loginLink.setVisibility(View.GONE);

        //Setting fonts
        Fonts fonts = new Fonts(this);
        _logo.setTypeface(fonts.BEBAS_NEUE_BOLD);
        _inputName.setTypeface(fonts.OPEN_SANS_REGULAR);
        _name.setTypeface(fonts.OPEN_SANS_REGULAR);
        _inputEmail.setTypeface(fonts.OPEN_SANS_REGULAR);
        _emailText.setTypeface(fonts.OPEN_SANS_REGULAR);
        _inputAdress.setTypeface(fonts.OPEN_SANS_REGULAR);
        _adress.setTypeface(fonts.OPEN_SANS_REGULAR);
        _inputPhoneNumber.setTypeface(fonts.OPEN_SANS_REGULAR);
        _phoneNumber.setTypeface(fonts.OPEN_SANS_REGULAR);
        _inputDescription.setTypeface(fonts.OPEN_SANS_REGULAR);
        _description.setTypeface(fonts.OPEN_SANS_REGULAR);
        _inputPassword.setTypeface(fonts.OPEN_SANS_REGULAR);
        _passwordText.setTypeface(fonts.OPEN_SANS_REGULAR);
        _inputRPassword.setTypeface(fonts.OPEN_SANS_REGULAR);
        _signupButton.setTypeface(fonts.OPEN_SANS_REGULAR);
        _loginLink.setTypeface(fonts.OPEN_SANS_REGULAR);

        _name.setText(userProfile.getName());
        _emailText.setText(userProfile.getEmail());
        _emailText.setEnabled(false);
        _adress.setText(userProfile.getAdress());
        _phoneNumber.setText(userProfile.getPhoneNumber());
        _description.setText(userProfile.getDescription());

        //TODO create edit profile logic
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {

        _signupButton.setEnabled(false);

        progressDialog = ProgressDialog.show(this,null,"Criando Conta...",true,false);

        if (!validate()) {
            onSignupFailed();
            return;
        }

        String name = _name.getText().toString();
        String adress = _adress.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();
        String description = _description.getText().toString();

        userProfile.setName(name);
        userProfile.setAdress(adress);
        userProfile.setPhoneNumber(phoneNumber);
        userProfile.setDescription(description);

        userDatabaseReference.child(userProfile.getId()).setValue(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onSignupSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onSignupFailed();
            }
        });
    }

    public void onSignupSuccess() {
        progressDialog.dismiss();
        new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Tudo Certo!")
                .setContentText("Perfil atualizado com sucesso.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        _signupButton.setEnabled(true);
                        Intent intent = new Intent(EditProfile.this, ScreenSlidePagerActivity.class);
                        intent.putExtra("userProfile",userProfile);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    public void onSignupFailed() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Falha ao atualizar conta.")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            _signupButton.setEnabled(true);
                        }
                    }).show();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = _name.getText().toString();
        String adress = _adress.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();
        String description = _description.getText().toString();

        if(name.isEmpty()){
            _name.setError("campo vazio");
            valid = false;
        }else{
            _name.setError(null);
        }

        if(adress.isEmpty()){
            _adress.setError("campo vazio");
            valid = false;
        }else{
            _adress.setError(null);
        }

        if(phoneNumber.isEmpty()){
            _phoneNumber.setError("campo vazio");
            valid = false;
        }else{
            _phoneNumber.setError(null);
        }

        if(description.isEmpty()){
            _description.setError("campo vazio");
            valid = false;
        }else{
            _description.setError(null);
        }

        return valid;
    }
}
