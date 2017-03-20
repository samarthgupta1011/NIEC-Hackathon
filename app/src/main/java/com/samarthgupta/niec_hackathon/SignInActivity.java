package com.samarthgupta.niec_hackathon;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private TextView tv1;
    private Button btnLogin;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();

            if (auth.getCurrentUser() != null) {
                // TO USE
                //auth.signOut();


                //////////////////////////////////
               // startActivity(new Intent(SignInActivity.this, Home.class));
                finish();
            }

            // set the view now
            setContentView(R.layout.activity_sign_in);

            //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);

            inputEmail = (EditText) findViewById(R.id.et_email_signin);
            inputPassword = (EditText) findViewById(R.id.et_password_signin);
            progressBar = (ProgressBar) findViewById(R.id.login_progress);
            btnLogin = (Button) findViewById(R.id.bt_signin);
            tv1=(TextView)findViewById(R.id.tv_signUp);


            auth = FirebaseAuth.getInstance();
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = inputEmail.getText().toString().trim();
                    final String password = inputPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));

                                        } else {
                                            Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        //TO USE - LOGIN COMPLETE
                                        //////////////////////////////////
                                      //  Intent intent = new Intent(SignInActivity.this, Home.class);
                                     //   startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            });


            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SignInActivity.this,RegisterActivity.class));
                }
            });


        //GOOGLE SIGN IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

         mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.bt_google_signin);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_google_signin:
                signIn();
                break; }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "onConnectionFailed:" + connectionResult);


    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("TAG", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            ///////////////////////
            Log.i("TAG",acct.getDisplayName());
            Log.i("TAG",acct.getEmail());
            //SEND DATA TO FIREBASE
         //   mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
           updateUI(false);
        }
    }

  private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.bt_google_signin).setVisibility(View.GONE);
        } else {
            findViewById(R.id.bt_google_signin).setVisibility(View.VISIBLE);
        }
    }



}
