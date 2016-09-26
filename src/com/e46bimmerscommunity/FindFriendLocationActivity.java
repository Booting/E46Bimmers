package com.e46bimmerscommunity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e46bimmerscommunity.model.Kategori;
import com.e46bimmerscommunity.utils.GPSTracker;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.Referensi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FindFriendLocationActivity extends ActionBarActivity implements OnMarkerClickListener, OnItemSelectedListener {
	
	private GoogleMap mMap;
    private Marker marker;
    private Double newLatitude, newLongitude;
    private JSONArray str_login  = null;
    private HashMap<Marker, EventInfo> eventMarkerMap;
	private SupportMapFragment fragment;
    private RequestQueue queue;
    private ProgressBar progressBar;
    private GPSTracker gps;
    private LinearLayout linDesc, linShowMyCar, linWhatsApp;
    private TextView txtShowMyCar, txtWhatsApp, txtTitle, txtDetail;
    private Typeface fontUbuntuL, fontUbuntuB;
    private ImageView imgProfile, imgSearch;
    private ImageLoader mImageLoader;
    private ArrayList<Kategori> wilayahList, userList;
	private String mLatitudee, mLongitudee, mLatitudeUser, mLongitudeUser;
	private Spinner spinWilayah, spinUser;
	private RelativeLayout relMaps;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.activity_find_friend_location);
		setTitle("Find Friend Location");
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mImageLoader = new ImageLoader(this);
		queue    	 = Volley.newRequestQueue(this);
        fragment 	 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        mMap 	     = fragment.getMap();
		progressBar  = (ProgressBar) findViewById(R.id.progressBusy);
		linDesc		 = (LinearLayout) findViewById(R.id.linDesc);
		linShowMyCar = (LinearLayout) findViewById(R.id.linShowMyCar);
		linWhatsApp  = (LinearLayout) findViewById(R.id.linWhatsApp);
		txtShowMyCar = (TextView) findViewById(R.id.txtShowMyCar);
		txtWhatsApp  = (TextView) findViewById(R.id.txtWhatsApp);
		txtTitle     = (TextView) findViewById(R.id.txtTitle);
		txtDetail	 = (TextView) findViewById(R.id.txtDetail);
        fontUbuntuL  = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB  = FontCache.get(this, "Ubuntu-B");
        imgProfile   = (ImageView) findViewById(R.id.imgProfile);
		spinWilayah	 = (Spinner) findViewById(R.id.spinWilayah);
		imgSearch	 = (ImageView) findViewById(R.id.imgSearch);
		spinUser	 = (Spinner) findViewById(R.id.spinUser);
		relMaps      = (RelativeLayout) findViewById(R.id.relMaps);

		spinWilayah.setOnItemSelectedListener(this);
		spinUser.setOnItemSelectedListener(this);
        mMap.setOnMarkerClickListener(this);
		txtTitle.setTypeface(fontUbuntuB);
        txtDetail.setTypeface(fontUbuntuL);
        txtShowMyCar.setTypeface(fontUbuntuB);
        txtWhatsApp.setTypeface(fontUbuntuB);
        
        imgSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (wilayahList!=null) {
					if (mLatitudee.equalsIgnoreCase("")) {
		            	LatLng fromPoss = new LatLng(newLatitude, newLongitude);
		            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPoss, 10));
		            } else {
		            	LatLng searchPos = new LatLng(Double.parseDouble(mLatitudee), Double.parseDouble(mLongitudee));
		            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPos, 10));
		            }
				}
			}
		});
        
        initLocationManager();
	}
	
	@Override
    public boolean onMarkerClick(final Marker marker) {
    	try {
    		final Handler handler = new Handler();
            final long startTime  = SystemClock.uptimeMillis();
            final long duration   = 2000;
            
            Projection proj = mMap.getProjection();
            final LatLng markerLatLng = marker.getPosition();
            Point startPoint = proj.toScreenLocation(markerLatLng);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - startTime;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * markerLatLng.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * markerLatLng.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));
                    if (t < 1.0) {
                        handler.postDelayed(this, 16);
                    }
                }
            });
    		
	    	linDesc.setVisibility(View.VISIBLE);
	    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13));
	    	
	    	final String[] splitMoreDetail = marker.getTitle().split("\\|");
	    	final String phoneNumber = splitMoreDetail[1];
	    	final String userId      = splitMoreDetail[2];
	    	final String Description = splitMoreDetail[3];
	    	final String Pictures    = splitMoreDetail[4];
	    	
	    	if (marker.getTitle()!=null) {
		    	txtTitle.setText(splitMoreDetail[0]);
		    	txtDetail.setText(Html.fromHtml(marker.getSnippet()));
		    	int loader = R.drawable.loaderr;
		        mImageLoader.DisplayImage("https://graph.facebook.com/"+userId+"/picture?type=normal", loader, imgProfile);
	    	} else {
	    		linDesc.setVisibility(View.GONE);
	    	}
	    	
	    	linShowMyCar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String[] splitSnippet = marker.getSnippet().split("<br>");
					startActivity(new Intent(FindFriendLocationActivity.this, ShowMyCarActivity.class).
							putExtra("UserId", userId).
							putExtra("Name", splitMoreDetail[0]).
							putExtra("Description", Description).
							putExtra("Pictures", Pictures).
	            			putExtra("PlatNo", splitSnippet[0].replace("Plat No : ", "")));
				}
			});
	    	
	    	linWhatsApp.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (phoneNumber.equalsIgnoreCase("")) {
						Toast.makeText(getBaseContext(), "No. Telp is Required!", Toast.LENGTH_SHORT).show();
					} else {
						try {
							Uri uri = Uri.parse("smsto:"+ "+62"+phoneNumber.substring(1));
		                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
		                    i.setPackage("com.whatsapp");
		                    i.putExtra("sms_body", "The text goes here");
		                    i.putExtra("chat",true);
		                    startActivity(i);
						} catch (ActivityNotFoundException e) {
		                    Toast.makeText(getApplicationContext(), "no whatsapp!", Toast.LENGTH_SHORT).show();
		                }
					}
				}
			});
    	} catch (Exception e) {
			linDesc.setVisibility(View.GONE);
		}
    	return true;
    }
	
	/**
     * Initialize the location manager.
     */
    private void initLocationManager() {
    	// create class object
        gps = new GPSTracker(FindFriendLocationActivity.this);

        // check if GPS enabled     
        if (gps.canGetLocation()) {
            newLatitude  = gps.getLatitude();
            newLongitude = gps.getLongitude();
            getAllData();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    
    public void getAllData() {
        String url = Referensi.url+"/getAllUser.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
                    str_login = response.getJSONArray("info");

                    Kategori cat0 = new Kategori("0", "All User", "", "");
                    userList = new ArrayList<Kategori>();
                    userList.add(cat0);
                    
                    for (int i = 0; i < str_login.length(); i++) {
                        JSONObject ar = str_login.getJSONObject(i);

                        String Longitude   = ar.getString("Longitude");
                        String Latitude    = ar.getString("Latitude");
                        String PlatNo	   = ar.getString("PlatNo");
                        String UserName    = ar.getString("UserName");
                        String LastUpdate  = ar.getString("LastUpdate");
                        String UserId      = ar.getString("UserId");
                        String PhoneNo     = ar.getString("Phone");
                        
                        String Description; 
                        if (ar.getString("Description").equalsIgnoreCase("")) {
                        	Description = "# no founder just member";
                        } else {
                        	Description = ar.getString("Description");
                        }
                        
                        String Pictures;
                        if (ar.getString("Pictures").equalsIgnoreCase("")) {
                        	Pictures = "https://scontent-sin1-1.xx.fbcdn.net/hphotos-xal1/v/t1.0-9/11742672_421217024717464_6411350321001256104_n.jpg?oh=e1b265dcbf09263f7636855b9e2d544c&oe=56ED3243";
                        } else {
                        	Pictures = ar.getString("Pictures");
                        }

                        Kategori cat = new Kategori(UserId, UserName, Latitude, Longitude);
                        userList.add(cat);
                        
                        initializeMap(Double.parseDouble(Latitude), Double.parseDouble(Longitude), UserName, PlatNo, LastUpdate, UserId, PhoneNo, Description, Pictures);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            	
            	getWilayahSpinner();
            	progressBar.setVisibility(View.GONE);
            	relMaps.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
    }
    
    public void getWilayahSpinner() {
		String url = Referensi.url+"/getWilayah.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
            		JSONArray categories = response.getJSONArray("categories");                        

                    Kategori cat0 = new Kategori(0, "All Wilayah", "", "");
                    wilayahList   = new ArrayList<Kategori>();
                    wilayahList.add(cat0);
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Kategori cat = new Kategori(catObj.getInt("WilayahId"), catObj.getString("WilayahName"), catObj.getString("Latitude"), catObj.getString("Longitude"));
                        wilayahList.add(cat);
                    }
                    
                    populateSpinnerWilayah();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsObjRequest);
	}
    
    /**
     * Adding spinner data
     * */
    private void populateSpinnerWilayah() {
        List<String> lables  = new ArrayList<String>();
        mLatitudee  = "";
        mLongitudee = "";
 
        for (int i = 0; i < wilayahList.size(); i++) {
            lables.add(wilayahList.get(i).getName());
        }
        
        // Creating adapter for spinner
        spinWilayah.setAdapter(null);
        ArrayAdapter<String> spinnerAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinWilayah.setAdapter(spinnerAdapter);
    }
    
    /**
     * Adding spinner data
     * */
    private void populateSpinnerUser() {
        List<String> lables  = new ArrayList<String>();
        mLatitudeUser  = "";
        mLongitudeUser = "";
 
        for (int i = 0; i < userList.size(); i++) {
            lables.add(userList.get(i).getName());
        }
        
        // Creating adapter for spinner
        spinUser.setAdapter(null);
        ArrayAdapter<String> spinnerAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinUser.setAdapter(spinnerAdapter);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	parent.getItemAtPosition(position);

        switch (parent.getId()) {         
            case R.id.spinWilayah:
            	int idnya   = wilayahList.get(position).getId();
            	mLatitudee  = wilayahList.get(position).getLatitude();
            	mLongitudee = wilayahList.get(position).getLongitude();
            	
            	if (idnya!=0) {
	            	try {
		            	Kategori cat0 = new Kategori("0", "All User", "", "");
		                userList = new ArrayList<Kategori>();
		                userList.add(cat0);
		                
		                for (int i = 0; i < str_login.length(); i++) {
		                    JSONObject ar = str_login.getJSONObject(i);
		
		                    String Longitude = ar.getString("Longitude");
		                    String Latitude  = ar.getString("Latitude");
	                        String UserName  = ar.getString("UserName");
	                        String UserId    = ar.getString("UserId");
		                    
	                        Location locationA = new Location("point A");
	                        locationA.setLatitude(Double.parseDouble(mLatitudee));
	                        locationA.setLongitude(Double.parseDouble(mLongitudee));
	                        
	                        Location locationB = new Location("point B");
	                        locationB.setLatitude(Double.parseDouble(Latitude));
	                        locationB.setLongitude(Double.parseDouble(Longitude));
	
	            			double distance = locationA.distanceTo(locationB);
	            			if (distance <= 30000) {
			                    Kategori cat = new Kategori(UserId, UserName, Latitude, Longitude);
		                        userList.add(cat);
	            			}
		                }
		                
		                populateSpinnerUser();
	            	} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	LatLng searchPos = new LatLng(Double.parseDouble(mLatitudee), Double.parseDouble(mLongitudee));
	            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPos, 10));
            	} else {
            		populateSpinnerUser();
            		
            		LatLng fromPoss = new LatLng(newLatitude, newLongitude);
	            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPoss, 10));
            	}
                
            	break;
            case R.id.spinUser:
            	String idnyaa  = userList.get(position).getUserId();
            	mLatitudeUser  = userList.get(position).getLatitude();
            	mLongitudeUser = userList.get(position).getLongitude();

        		linDesc.setVisibility(View.GONE);
            	if (!idnyaa.equalsIgnoreCase("0")) {
	            	LatLng searchPos = new LatLng(Double.parseDouble(mLatitudeUser), Double.parseDouble(mLongitudeUser));
	            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPos, 13));
            	} else {
            		if (!mLatitudee.equalsIgnoreCase("")) {
	            		LatLng searchPos = new LatLng(Double.parseDouble(mLatitudee), Double.parseDouble(mLongitudee));
		            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPos, 10));
            		} else {
            			LatLng fromPoss = new LatLng(newLatitude, newLongitude);
    	            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPoss, 10));
            		}
            	}
            	
            	break;
        }
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {   }

    @SuppressLint("NewApi")
	private void initializeMap(double lat1, double lng1, String UserName, String PlatNo, String LastUpdate, String UserId, String PhoneNo, String Description, String Pictures) {

        if (mMap!=null) {
            final LatLng fromPosition = new LatLng(newLatitude, newLongitude);
			@SuppressWarnings("unused")
			final LatLng toPosition   = new LatLng(lat1, lng1);

            Location locationA = new Location("point A");
            locationA.setLatitude(newLatitude);
            locationA.setLongitude(newLongitude);

            Location locationB = new Location("point B");
            locationB.setLatitude(lat1);
            locationB.setLongitude(lng1);

            @SuppressWarnings("unused")
			double distance = locationA.distanceTo(locationB);
          
            // Move the camera instantly to hamburg with a zoom of 15.
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 10));
            
            long lastUpdate     = Long.parseLong(LastUpdate);
            long remainingDays  = Referensi.getRemainingDays(lastUpdate);
            Date dateLastUpdate = new Date(lastUpdate);
            
            if (remainingDays == 0) {
            	Date dtLastUpdate1 = null;
                try {
                	dtLastUpdate1 = Referensi.getSimpleDateFormatHours().parse(Referensi.getSimpleDateFormatHours().format(dateLastUpdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long eventMillis1   = dtLastUpdate1.getTime();
                long diffMillis1    = Referensi.getCurrentMillis() - eventMillis1;
                long remainingHours = TimeUnit.MILLISECONDS.toHours(diffMillis1);

                if (remainingHours == 0) {
                	long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis1);
                	if (remainingMinutes <= 10) {
                		marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat1, lng1))
                            .title(UserName+"|"+PhoneNo+"|"+UserId+"|"+Description+"|"+Pictures)
                            .snippet("Plat No : "+PlatNo+"<br>"+"Last Update : "+remainingMinutes+" minutes")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.male_on)));
                	} else {
                		marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat1, lng1))
                            .title(UserName+"|"+PhoneNo+"|"+UserId+"|"+Description+"|"+Pictures)
                            .snippet("Plat No : "+PlatNo+"<br>"+"Last Update : "+remainingMinutes+" minutes")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.male_idle)));
                	}
                } else {
                	marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat1, lng1))
                        .title(UserName+"|"+PhoneNo+"|"+UserId+"|"+Description+"|"+Pictures)
                        .snippet("Plat No : "+PlatNo+"<br>"+"Last Update : "+remainingHours+" hours")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.male_off)));
                }
            } else {
            	marker = mMap.addMarker(new MarkerOptions()
	                .position(new LatLng(lat1, lng1))
	                .title(UserName+"|"+PhoneNo+"|"+UserId+"|"+Description+"|"+Pictures)
	                .snippet("Plat No : "+PlatNo+"<br>"+"Last Update : "+remainingDays+" days")
	                .icon(BitmapDescriptorFactory.fromResource(R.drawable.male_off)));
            }
            
            eventMarkerMap = new HashMap<Marker, EventInfo>();
            eventMarkerMap.put(marker, null);
        } else {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getMap();
            Toast.makeText(getApplicationContext(), "MAP NULL", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyUp( keyCode, event );
	}
	
	@Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
