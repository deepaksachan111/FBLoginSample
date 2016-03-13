package fbproflie.my.fbloginsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {
        private TextView mTextview;
    private CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    ImageView imageView;

    Bitmap fb_img;

    Bundle set_bundle = new Bundle();

    FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
         /*   AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displaywelcomeMessage(profile);*/

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            response.getError();

                            try {
                                if (android.os.Build.VERSION.SDK_INT > 9) {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                    String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    URL fb_url = new URL(profilePicUrl);//small | noraml | large
                                    HttpsURLConnection conn1 = (HttpsURLConnection) fb_url.openConnection();
                                    HttpsURLConnection.setFollowRedirects(true);
                                    conn1.setInstanceFollowRedirects(true);
                                     fb_img = BitmapFactory.decodeStream(conn1.getInputStream());
                                    imageView.setImageBitmap(fb_img);

                                   // Bundle args = new Bundle();
                                    set_bundle.putParcelable("bitmap", fb_img);

                                    Fragment toFragment = new WellFragment();
                                    toFragment.setArguments(set_bundle);
                                    getFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment, toFragment)
                                            .addToBackStack(null).commit();

                                }
                            }catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,picture");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager =CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {

            }
        };

      profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                displaywelcomeMessage(profile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        return  v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton button = (LoginButton)view.findViewById(R.id.login_button);
        imageView = (ImageView)view.findViewById(R.id.image_view);

         mTextview =(TextView)view.findViewById(R.id.text);
        button.setReadPermissions("public_profile", "email","user_friends");
        button.setFragment(this);
        button.registerCallback(callbackManager,facebookCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

      private void displaywelcomeMessage( Profile profile){
          if(profile!= null){
              mTextview.setText("Welcome :"+ profile.getName());
              set_bundle.putString("tt",profile.getName());
          }
      }
    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displaywelcomeMessage(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }
}
