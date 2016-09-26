package com.e46bimmerscommunity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.e46bimmerscommunity.utils.Referensi;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    private TextView txtText1, txtText2, txtText3, lblSignWithFacebook;
    private Typeface fontOldStandard;
    private ProgressBar progressBar;
    private RelativeLayout loginBtn;
	@SuppressWarnings("unused")
	private String facebookAccessToken;
    private SharedPreferences bimmersPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        bimmersPref 		= getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontOldStandard     = FontCache.get(this, "OldStandard-Regular");
        txtText1            = (TextView) findViewById(R.id.txtText1);
        txtText2            = (TextView) findViewById(R.id.txtText2);
        txtText3            = (TextView) findViewById(R.id.txtText3);
        loginBtn            = (RelativeLayout) findViewById(R.id.btnLogin);
        progressBar         = (ProgressBar) findViewById(R.id.progressBar);
        lblSignWithFacebook = (TextView) findViewById(R.id.lblSignWithFacebook);

        txtText1.setTypeface(fontOldStandard);
        txtText2.setTypeface(fontOldStandard);
        txtText3.setTypeface(fontOldStandard);
        lblSignWithFacebook.setTypeface(fontOldStandard);
        
        if (!bimmersPref.getString("UserId", "").equals("")) {
        	startActivity(new Intent(getApplicationContext(), MenuNewActivity.class));
            overridePendingTransition( R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom );
            finish();
        }
        
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.e46bimmerscommunity", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				facebookAuthorize();
			}
		});
    }

    public void facebookAuthorize() {
        Session session = Session.getActiveSession();
        if (session == null) {
            session = new Session(LoginActivity.this);
            Session.setActiveSession(session);
        }

        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends")));
        } else {
            Session.openActiveSession(LoginActivity.this, true, callback);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            facebookAccessToken = session.getAccessToken().toString();
            if (session.getState() == SessionState.OPENED) {
                facebookGetUser();
            }
            if (session.getState() == SessionState.CLOSED_LOGIN_FAILED) {
                if (Session.getActiveSession() != null) {
                    Session.getActiveSession().closeAndClearTokenInformation();
                }
            }
        }

    };
    
    private void facebookGetUser() {
    	progressBar.setVisibility(View.VISIBLE);
    	loginBtn.setVisibility(View.GONE);
        Session session = Session.getActiveSession();
        
        //REQUEST
        com.facebook.Request requestUserInfo = com.facebook.Request.newMeRequest(session, new com.facebook.Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, com.facebook.Response response) {
                JSONObject facebookUserInfo = user.getInnerJSONObject();
                try {
                	SharedPreferences.Editor editor = bimmersPref.edit();
					editor.putString("UserId", user.getId());
					editor.putString("UserName", facebookUserInfo.getString("name"));
					editor.putString("Email", facebookUserInfo.getString("email"));
					editor.commit();
					
                	startActivity(new Intent(getApplicationContext(), MenuNewActivity.class));
                    overridePendingTransition( R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom );
                    finish();
                	
				} catch (JSONException e) {
					// TODO Auto-generated cat ch block
					e.printStackTrace();
					if (Session.getActiveSession() != null)
                        Session.getActiveSession().closeAndClearTokenInformation();
				}
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email, name");
        requestUserInfo.setParameters(parameters);
        requestUserInfo.executeAsync();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(LoginActivity.this, requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }
}