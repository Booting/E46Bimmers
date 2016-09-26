package com.e46bimmerscommunity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.e46bimmerscommunity.gridview.StaggeredGridView;
import com.e46bimmerscommunity.utils.GPSTracker;
import com.e46bimmerscommunity.utils.JSONParser;
import com.e46bimmerscommunity.utils.Referensi;
import com.e46bimmerscommunity.utils.callURL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

public class MenuNewActivity extends ActionBarActivity {
	private StaggeredGridView itemList;
	private MenuNewAdapter menuNewAdapter;
	private String url="", url1="", url2="";
	double Latitude, Longitude;
	private long timeInMilliseconds;
    private JSONArray str_login = null;
    private boolean isMember=false, isBlacklist = false;
	private String UserId="", UserName="", Email="";
    private SharedPreferences bimmersPref;
    private GPSTracker gps;
    private ProgressBar progressBar;
    
	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_new);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bimmersPref = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		itemList    = (StaggeredGridView) findViewById(R.id.itemList);
		progressBar = (ProgressBar) findViewById(R.id.progressBusy);
		UserId      = bimmersPref.getString("UserId", "");
		UserName    = bimmersPref.getString("UserName", "");
		Email       = bimmersPref.getString("Email", "");
		
		Calendar c = Calendar.getInstance(); 
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Date mDate;
		try {
			mDate = df.parse(formattedDate);
			timeInMilliseconds = mDate.getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		initLocationManager();
    }
	
	/**
     * Initialize the location manager.
     */
    private void initLocationManager() {
    	// create class object
        gps = new GPSTracker(MenuNewActivity.this);

        // check if GPS enabled     
        if (gps.canGetLocation()) {
        	Latitude  = gps.getLatitude();
            Longitude = gps.getLongitude();
            new cekDataUserIfAvailable().execute();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
	
	private class cekDataUserIfAvailable extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {
        	url = Referensi.url+"/getUser.php?UserId="+UserId;
        	String hasil = callURL.call(url);
			return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("false")) {
            	new addNewUser().execute();
            } else {
            	new updateUserLocation().execute();
            }
        }
    }
	
	private class updateUserLocation extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
        	url1 = Referensi.url+"/service.php?ct=UPDATELOCATION&Email="+Email+
        			"&Latitude="+Latitude+
        			"&Longitude="+Longitude+
        			"&LastUpdate="+timeInMilliseconds+
        			"&UserId="+UserId;
        	String hasil = callURL.call(url1);
        	checkIfAMember();
        	return hasil;
        }
        @SuppressLint("NewApi")
		@Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            
            menuNewAdapter = new MenuNewAdapter(MenuNewActivity.this, isMember);
    		itemList.setAdapter(menuNewAdapter);
    		
            if (isBlacklist) {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuNewActivity.this);
                alertDialog.setTitle("Ooopppsssss");
                alertDialog.setMessage("Sorry, you are in blacklist!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	dialog.cancel();
                        finish();
                    }
                });
                alertDialog.setCancelable(false);
                if (!isFinishing()) { alertDialog.show(); }
            }
        }
    }
	
	private class addNewUser extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
			String Name = null;
			try {
				Name = URLEncoder.encode(UserName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	url2 = Referensi.url+"/service.php?ct=ADDNEWUSER&UserId="+UserId+"&UserName="+Name;
        	String hasil = callURL.call(url2);
        	return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new updateUserLocation().execute();
        }
    }
	
	@SuppressLint("NewApi")
	private void checkIfAMember() {
		JSONParser jParser = new JSONParser();
        String link_url = Referensi.url+"/getUserDetail.php?UserId="+UserId;
        JSONObject json = jParser.AmbilJson(link_url);

        try {
            str_login = json.getJSONArray("info");

            for (int i = 0; i < str_login.length(); i++){
                JSONObject ar = str_login.getJSONObject(0);

                if (ar.getString("PlatNo").equalsIgnoreCase("-")&&ar.getString("TypeBody").equalsIgnoreCase("-")) {
                	isMember=false;
                } else {
                	isMember=true;
                }
                if (ar.getString("isBlacklist").equalsIgnoreCase("1")) {
                	isBlacklist = true;
                } else {
                	isBlacklist = false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuNewActivity.this);
            alertDialog.setTitle("GPS settings is change");
            alertDialog.setMessage("GPS is change. Close and open apps again?");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    finish();
                }
            });
            if (!isFinishing()) { alertDialog.show(); }
        } else {
        	if (resultCode == RESULT_OK) {
        		initLocationManager(); 
        	}
        }
    }
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			finish();
			return true;
		}
		return super.onKeyUp( keyCode, event );
	}
	
}
