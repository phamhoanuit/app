package com.example.firebaseexample.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.firebaseexample.FacebookAuthHelper;
import com.example.firebaseexample.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    @BindView(R.id.btn_login_google)
    Button btnGoogleSignIn;
    SignInButton btnLoginGoogle;

    @BindView(R.id.btn_login_facebook)
    Button btnFacebookSignIn;
    LoginButton btnLoginFacebook;
    private CallbackManager mFbCallbackManager;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_GG_SIGN_IN = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initial();

//        getHashKeyFacebook();
    }

    private void getHashKeyFacebook() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void initial() {
        mAuth = FirebaseAuth.getInstance();
        initGoogleSignIn();
        initFacebookSignIn();
    }

    /**
     * Login use Facebook
     */

    /**
     * initialize Google Authentication, Facebook Sign-in button
     */

    private void initFacebookSignIn() {
        mFbCallbackManager = CallbackManager.Factory.create();
        btnLoginFacebook = findViewById(R.id.login_facebook);
        btnLoginFacebook.setOnClickListener(this);
        btnLoginFacebook.setReadPermissions("email", "public_profile");
        btnLoginFacebook.registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess:  " + loginResult);
                handleFacebookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: " + error.toString());
            }
        });
    }

    /**
     * Handle the result from Facebook sign in
     **/
    private void handleFacebookLogin(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * credential facebook account on firebase
     *
     * @param token
     */
    private void handleFacebookAccessToken(AccessToken token) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        facebookSignInCredential(credential);
    }


    /**
     * sign out facebook account
     */
    private void facebookSignOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        if (FacebookAuthHelper.getCurrentAccessToken() != null) {
            FacebookAuthHelper.logOut();
        }
    }


    /**
     * Receive the result from Google Sign-in or SMS Sign-in and handle it
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GG_SIGN_IN:
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handlerSignInResult(result);
                    break;

                default:
                    mFbCallbackManager.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_facebook:
                btnLoginFacebook.performClick();
                break;

            case R.id.btn_login_google:
                googleSignIn();
                break;
        }
    }
    /**
     * facebook sign in with credential
     *
     * @param credential
     */
    private void facebookSignInCredential(final AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, "onComplete: user: "
                                    + "\n email: " + user.getEmail()
                                    + "\n display name: " + user.getDisplayName()
                                    + "\n phone: " + user.getPhoneNumber()
                                    + "\n provide id: " + user.getProviderId()
                                    + "\n id: " + user.getUid()

                            );
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            facebookSignOut();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, R.string.welcome_activity_toast_facebook_sign_in_fail, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * initialize Google Authentication
     */
    private void initGoogleSignIn() {
        btnLoginGoogle = findViewById(R.id.login_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        btnLoginGoogle.setSize(SignInButton.SIZE_STANDARD);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_GG_SIGN_IN);
    }

    /**
     * handle google sign in
     *
     * @param result
     */
    private void handlerSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Log.d(TAG, "handlerSignInResult: " + result.getStatus().toString());
        }
    }

    /**
     * credential google account on firebase
     *
     * @param acc
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        if (acc == null) {
            Toast.makeText(this, R.string.msg_error_connect_server, Toast.LENGTH_SHORT).show();
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            String email = user.getEmail();
//                            String firstName = user.getDisplayName();
//                            String phone = user.getPhoneNumber();
//                            Log.d(TAG, "onComplete: google auth: "
//                                    + "\n email: " + email
//                                    + "\n firstName: " + firstName
//                                    + "\n phone: " + phone
//                            );
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
