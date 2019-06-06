package com.example.firebaseexample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * Created by huynhtinh1997 on 01/08/2017.
 */

public class GoogleAuthHelper {

    public static final int RC_GOOGLE_LOG_IN = 1;

    private Context mContext;
    private GoogleSignInOptions mGoogleSignInOptions;
    private GoogleApiClient mGoogleApiClient;

    /**
     * create a new GoogleAuthHelper instance and configure Google Sign-in API
     *
     * @param activity
     */
    public GoogleAuthHelper(Context activity) {
        mContext = activity;
        configureGoogleSignIn();
        configureGoogleApiClient();
    }


    private void configureGoogleSignIn() {
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    private void configureGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage((AppCompatActivity) mContext, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }


                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();
    }

    /**
     * @return intent to start Google Sign-in activity
     */
    public Intent getSignInIntent() {
        return Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
    }

    /**
     * @param data intent from onActivityResult method of the
     *             activity
     * @return signed in account
     */
    public GoogleSignInAccount getSignInAccountFromIntent(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            return result.getSignInAccount();
        }
        return null;
    }

    public AuthCredential getAuthCredential(GoogleSignInAccount account) {
        return GoogleAuthProvider.getCredential(account.getIdToken(), null);
    }

    /**
     * log out
     */
    public void logOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }
}
