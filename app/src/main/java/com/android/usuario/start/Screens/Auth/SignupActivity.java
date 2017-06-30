package com.android.usuario.start.Screens.Auth;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.Util.Fonts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by eduar on 04/05/2017.
 */

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Profile userProfile;

    private DatabaseReference userDatabaseReference;
    private FirebaseAnalytics mFirebaseAnalytics;

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

    private int bDay;
    private int bMonth;
    private int bYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        userDatabaseReference = Database.getUsersReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String name = _name.getText().toString();
                    String email = _emailText.getText().toString().trim();
                    String adress = _adress.getText().toString();
                    String phoneNumber = _phoneNumber.getText().toString();
                    String description = _description.getText().toString();

                    userProfile = new Profile();
                    userProfile.setId(user.getUid());
                    userProfile.setName(name);
                    userProfile.setEmail(email);
                    userProfile.setAdress(adress);
                    userProfile.setPhoneNumber(phoneNumber);
                    userProfile.setDescription(description);

                    userDatabaseReference.child(user.getUid()).setValue(userProfile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendVerificationEmail(user);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            user.delete();
                            onSignupFailed();
                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

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

        bDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        bMonth = Calendar.getInstance().get(Calendar.MONTH);
        bYear = Calendar.getInstance().get(Calendar.YEAR);

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

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        _signupButton.setEnabled(false);

        progressDialog = ProgressDialog.show(this,null,"Criando Conta...",true,false);

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final String email = _emailText.getText().toString().trim();
        final String password = _passwordText.getText().toString().trim();

        // TODO: Implement your own signup logic here.
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            onSignupFailed();
                        }
                        // ...
                    }
                });
    }


    public void onSignupSuccess() {
        progressDialog.dismiss();
        new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Conta criada com sucesso!")
                .setContentText("Por Favor, verifique seu email para a validação da conta.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        _signupButton.setEnabled(true);
                        setResult(RESULT_OK, null);
                        finish();
                    }
                }).show();
    }

    public void onSignupFailed() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Falha ao criar conta.")
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
        String email = _emailText.getText().toString();
        String adress = _adress.getText().toString();
        String phoneNumber = _phoneNumber.getText().toString();
        String description = _description.getText().toString();
        String password = _passwordText.getText().toString();
        String rPassword = _rPasswordText.getText().toString();

        if(name.isEmpty()){
            _name.setError("campo vazio");
            valid = false;
        }else{
            _name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("endereço email inválido");
            valid = false;
        } else {
            _emailText.setError(null);
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

        if (password.isEmpty() || password.length() < 6 ) {
            _passwordText.setError("senha tem que ter mais do que 6 carateres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if(rPassword.isEmpty() || !rPassword.equals(password)){
            _rPasswordText.setError("senhas incompatíveis");
            valid = false;
        }else{
            _rPasswordText.setError(null);
        }

        return valid;
    }

    private void sendVerificationEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
                    FirebaseAuth.getInstance().signOut();
                    onSignupSuccess();
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do

                    //restart this activity
                    overridePendingTransition(0, 0);
                    onSignupFailed();

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
