package com.example.firebaseexample.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.firebaseexample.FacebookAuthHelper;
import com.example.firebaseexample.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        initGoogleSignInService();
    }

    @OnClick(R.id.fb_database)
    public void goToFirebaseDatabase(){
        startActivity(new Intent(MainActivity.this, FirebaseDatabaseActivity.class));
    }

    @OnClick(R.id.fb_cloud_message)
    public void goToFirebaseCloudMessage(){
        startActivity(new Intent(MainActivity.this, FirebaseCloudMessageActivity.class));
    }

    @OnClick(R.id.fb_remote_config)
    public void goToFBRemoteConfig(){
        startActivity(new Intent(MainActivity.this, FBRemoteConfigActivity.class));

    }

    @OnClick(R.id.fb_storage)
    public void goToFirebaseStorage(){
        startActivity(new Intent(MainActivity.this, FirebaseStorageActivity.class));
    }

    @OnClick(R.id.google_map_btn)
    public void goToGoogleMapScreen(){
        startActivity(new Intent(MainActivity.this, GoogleMapActivity.class));
    }

    private void initGoogleSignInService(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                facebookSignOut();
                goToLoginActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToLoginActivity(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}
