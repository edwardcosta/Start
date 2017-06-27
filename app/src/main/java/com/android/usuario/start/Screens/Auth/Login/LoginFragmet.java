package com.android.usuario.start.Screens.Auth.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.usuario.start.DataObject.Profile;
import com.android.usuario.start.R;
import com.android.usuario.start.RequestManager.Database;
import com.android.usuario.start.Screens.Auth.LoginActivity;
import com.android.usuario.start.Screens.Auth.SignupActivity;
import com.android.usuario.start.Screens.Container.MainActivity;
import com.android.usuario.start.Screens.ProfileChooser.ScreenSlidePagerActivity;
import com.android.usuario.start.Screens.Splash.SplashActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by eduar on 08/06/2017.
 */

public class LoginFragmet extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference userDatabaseReference;

    //private LoginButton _loginFacebookButton;
    //private SignInButton _loginGoogleButton;
    private TextView _logo;
    private TextInputLayout _inputEmail;
    private EditText _emailText;
    private TextInputLayout _inputPassword;
    private EditText _passwordText;
    private TextView _loginButton;
    private ProgressDialog progressDialog;
    private TextView _passwordForgot;
    private TextView _signupLink;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;


    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_user_login,container,false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,savedInstanceState);
        userDatabaseReference = Database.getUsersReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    checkIfEmailVerified(user);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        _logo = (TextView) parentView.findViewById(R.id.activity_login_logo);
        //_loginFacebookButton = (LoginButton) findViewById(R.id.activity_login_facebok_login_button);
        //_loginGoogleButton = (SignInButton) findViewById(R.id.activity_login_google_login_button);
        _inputEmail = (TextInputLayout) parentView.findViewById(R.id.fragment_login_textinput_email);
        _emailText = (EditText) parentView.findViewById(R.id.activity_login_input_email);
        _inputPassword = (TextInputLayout) parentView.findViewById(R.id.fragment_login_textinput_password);
        _passwordText = (EditText) parentView.findViewById(R.id.activity_login_input_password);
        _loginButton = (TextView) parentView.findViewById(R.id.activity_login_btn_login);
        _passwordForgot = (TextView) parentView.findViewById(R.id.activity_login_btn_password_forgot);
        _signupLink = (TextView) parentView.findViewById(R.id.activity_login_link_signup);


        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/BebasNeue-Bold.ttf");
        _logo.setTypeface(type);
        type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Semibold.ttf");
        _inputEmail.setTypeface(type);
        _emailText.setTypeface(type);
        _inputPassword.setTypeface(type);
        _passwordText.setTypeface(type);
        _signupLink.setTypeface(type);
        type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        _passwordForgot.setTypeface(type);
        _loginButton.setTypeface(type);

        mCallbackManager = CallbackManager.Factory.create();
        //_loginFacebookButton.setReadPermissions("email", "public_profile");
        //_loginFacebookButton.setTextSize(20f);
        /*_loginFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

        _loginGoogleButton.setSize(1);
        setGooglePlusButtonText(_loginGoogleButton);
        _loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });*/

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _passwordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail()) {
                    String email = _emailText.getText().toString();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Email enviado!")
                                        .setContentText("Verifique seu email para redefinir sua senha")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        }).show();
                            }else{
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Erro!")
                                        .setContentText("Email não encontrado")
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
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        return parentView;
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            onLoginFailed();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            onLoginFailed();
                        }
                    }
                });
    }

    protected void setGooglePlusButtonText(SignInButton signInButton) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(20f);
                //tv.setTypeface(null, Typeface.NORMAL);
                return;
            }
        }
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = ProgressDialog.show(getContext(),null,"Fazendo login...",true,false);

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                progressDialog.dismiss();

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    onLoginFailed();
                }

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        } else if (requestCode == RC_SIGN_IN) { // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                onLoginFailed();
            }
        }
    }

    public void onLoginSuccess(FirebaseUser user) {
        Database.getUsersReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile userProfile = dataSnapshot.getValue(Profile.class);
                if(userProfile != null) {
                    if (userProfile.getProfileType() == 0) {
                        Intent intent = new Intent(getContext(), ScreenSlidePagerActivity.class);
                        intent.putExtra("userProfile", userProfile);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra("userProfile", userProfile);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onLoginFailed();
            }
        });
    }

    public void onLoginFailed() {
        Toast.makeText(getContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("email inválido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 ) {
            _passwordText.setError("senha tem que ter mais do que 4 caracteres");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private boolean validateEmail(){
        String email = _emailText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("email inválido");
            return false;
        } else {
            _emailText.setError(null);
            return true;
        }
    }

    private void checkIfEmailVerified(FirebaseUser user) {

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
            onLoginSuccess(user);
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getContext(),"Email não verificado! Verifique seu email",Toast.LENGTH_LONG).show();
            onLoginFailed();
            //restart this activity

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
