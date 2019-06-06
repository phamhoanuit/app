package com.example.firebaseexample;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import java.util.Collection;

/**
 * Created by huynhtinh1997 on 14/08/2017.
 */

public class FacebookAuthHelper {

    public final static String PERMISSION_EMAIL = "email";
    public final static String PERMISSION_PUBLIC_PROFILE = "public_profile";

    /**
     * Create a new CallbackManager instance
     *
     * @return a new created CallbackManager instance
     */
    public static CallbackManager createCallbackManager() {
        return CallbackManager.Factory.create();
    }

    /**
     * @return LoginManager instance
     */
    public static LoginManager getLoginManager() {
        return LoginManager.getInstance();
    }

    /**
     * Log Facebook account in
     *
     * @param activity
     * @param permissions permissions to get user about for login
     */
    public static void logIn(Activity activity, Collection<String> permissions) {
        getLoginManager().logInWithReadPermissions(activity, permissions);
    }

    /**
     * @return current access token
     */
    public static AccessToken getCurrentAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    /**
     * Log Facebook account out
     */
    public static void logOut() {
        getLoginManager().logOut();
    }
}
