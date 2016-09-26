package com.e46bimmerscommunity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.e46bimmerscommunity.utils.JSONParser;
import com.e46bimmerscommunity.utils.Referensi;
import com.e46bimmerscommunity.utils.callURL;
import com.e46bimmerscommunity.utils.initLocation;
import com.facebook.Session;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends ActionBarActivity {
	
	private Button btnProfileMember, btnInfoBengkel, btnFindFriendLocation, btnInfoPerawatan,
				btnBuletinBoard, btnJualBeli, btnAbout, btnExit;
	private String url = "", url1 = "", url2 = "";
	private ProgressDialog pDialog;
	private String UserId = "", UserName = "";
	double Latitude, Longitude;
	private long timeInMilliseconds;
    private JSONArray str_login  = null;
    private boolean isAMember = false, isBlacklist = false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		btnProfileMember	  = (Button) findViewById(R.id.btnProfileMember);
		btnInfoBengkel		  = (Button) findViewById(R.id.btnInfoBengkel);
		btnFindFriendLocation = (Button) findViewById(R.id.btnFindFriendLocation);
		btnInfoPerawatan	  = (Button) findViewById(R.id.btnInfoPerawatan);
		btnBuletinBoard		  = (Button) findViewById(R.id.btnBuletinBoard); 
		btnJualBeli			  = (Button) findViewById(R.id.btnJualBeli);
		btnAbout			  = (Button) findViewById(R.id.btnAbout); 
		btnExit				  = (Button) findViewById(R.id.btnExit);
		
		UserId   			  = getIntent().getExtras().getString("UserId");
		UserName			  = getIntent().getExtras().getString("UserName");
		
		btnProfileMember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), ProfilActivity.class).
						putExtra("UserId", UserId).
                		putExtra("UserName", UserName));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
			}
		});
		
		btnInfoBengkel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isAMember) {
					startActivity(new Intent(getApplicationContext(), MainActivity.class).
							putExtra("UserId", UserId).
	                		putExtra("UserName", UserName));
					overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
					finish();
				}
			}
		});
		
		btnFindFriendLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isAMember) {
					startActivity(new Intent(getApplicationContext(), FindFriendLocationActivity.class).
							putExtra("UserId", UserId).
	                		putExtra("UserName", UserName));
					overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
					finish();
				}
			}
		});
		
		btnInfoPerawatan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isAMember) {
					startActivity(new Intent(getApplicationContext(), MaintenanceActivity.class).
							putExtra("UserId", UserId).
	                		putExtra("UserName", UserName));
					overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
					finish();
				}
			}
		});
		
		btnBuletinBoard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isAMember) {
					startActivity(new Intent(getApplicationContext(), EventActivity.class).
							putExtra("UserId", UserId).
	                		putExtra("UserName", UserName));
					overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
					finish();
				}
			}
		});
		
		btnJualBeli.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), JualBeliSyaratActivity.class).
						putExtra("UserId", UserId).
                		putExtra("UserName", UserName));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
			}
		});
		
		btnAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), AboutActivity.class).
						putExtra("UserId", UserId).
                		putExtra("UserName", UserName));
				overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left );
				finish();
			}
		});
		
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Session session = Session.getActiveSession();
	            try {
	                if (!session.isClosed()) {
	                    session.closeAndClearTokenInformation();
	                }
	            } catch (Exception e) { }
	            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
	            startActivity(intent);
	            finish();
			}
		});
		
		pDialog = new ProgressDialog(MenuActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
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
		
		try {
			Location location = initLocation.initLocationManager(MenuActivity.this);
			Latitude  = location.getLatitude();
			Longitude = location.getLongitude();
			if (Latitude!=0||Longitude!=0) {
				new cekDataUserIfAvailable().execute();
			} else {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
	            alertDialog.setTitle("Apps cann't find your location");
	            alertDialog.setMessage("So apps cann't send your position to server?");
	            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog,int which) {
	                	dialog.cancel();
	                }
	            });
	            if (!isFinishing()) { alertDialog.show(); }
			}
		} catch (Exception e) {
			// TODO: handle exception
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
            alertDialog.setTitle("Apps cann't find your location");
            alertDialog.setMessage("So apps cann't send your position to server?");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                	dialog.cancel();
                }
            });
            if (!isFinishing()) { alertDialog.show(); }
		}
	}
	
	private class cekDataUserIfAvailable extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isFinishing()) { pDialog.show(); }
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
        	url1 = Referensi.url+"/service.php?ct=UPDATELOCATION&Email=" +
        			"&Latitude="+Latitude+
        			"&Longitude="+Longitude+
        			"&LastUpdate="+timeInMilliseconds+
        			"&UserId="+UserId;
        	String hasil = callURL.call(url1);
        	checkIfAMember();
        	return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (isBlacklist) {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
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
        	url2 = Referensi.url+"/service.php?ct=ADDNEWUSER&UserId="+UserId+"&UserName="+UserName;
        	String hasil = callURL.call(url2);
        	return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new updateUserLocation().execute();
        }
    }
	
	private void checkIfAMember() {
		JSONParser jParser = new JSONParser();
        String link_url = Referensi.url+"/getUserDetail.php?UserId="+UserId;
        JSONObject json = jParser.AmbilJson(link_url);

        try {
            str_login = json.getJSONArray("info");

            for (int i = 0; i < str_login.length(); i++){
                JSONObject ar = str_login.getJSONObject(0);

                if (ar.getString("PlatNo").equalsIgnoreCase("")&&ar.getString("TypeBody").equalsIgnoreCase("")) {
                	isAMember = false;
                } else {
                	isAMember = true;
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
        	AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
            alertDialog.setTitle("GPS settings is change");
            alertDialog.setMessage("GPS is change. Close and open apps again?");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    finish();
                }
            });
            if (!isFinishing()) { alertDialog.show(); }
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
