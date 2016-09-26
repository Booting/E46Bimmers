package com.e46bimmerscommunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.e46bimmerscommunity.utils.Referensi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends ActionBarActivity implements OnItemSelectedListener, OnMarkerClickListener {
	
	private GoogleMap mMap, mMapTwo;
    @SuppressWarnings("unused")
	private Marker marker, markerTwo;
    private Double newLatitude, newLongitude;
    private JSONArray str_login  = null;
    private HashMap<Marker, EventInfo> eventMarkerMap;
	private GoogleMapV2Direction md;
	private SupportMapFragment fragment, fragmentTwo;
    private Spinner spinPlace, spinWilayah;
	private ArrayList<Kategori> wilayahList, placeList;
	private int wilayahId, placeId;
	private Button btnSearch;
	private TextView txtTitle, txtDetail;
    private LinearLayout linDesc, linRute, linCall;
    private Typeface fontUbuntuL, fontUbuntuB;
    private LatLng fromPosition, toPosition;
    private TextView txtRute, txtCall;
    private RequestQueue queue;
    private ProgressBar progressBar;
    private GPSTracker gps;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.activity_main);
		setTitle("BMW Parts & Service");
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

		queue 		= Volley.newRequestQueue(this);
		fragment 	= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        mMap 		= fragment.getMap();
		spinPlace	= (Spinner) findViewById(R.id.spinPlace);
		spinWilayah	= (Spinner) findViewById(R.id.spinWilayah);
		btnSearch 	= (Button) findViewById(R.id.btnSearch);
        txtTitle	= (TextView) findViewById(R.id.txtTitle);
        txtDetail   = (TextView) findViewById(R.id.txtDetail);
        linDesc		= (LinearLayout) findViewById(R.id.linDesc);
        fontUbuntuL = FontCache.get(this, "Ubuntu-L");
        fontUbuntuB = FontCache.get(this, "Ubuntu-B");
        linRute		= (LinearLayout) findViewById(R.id.linRute);
        linCall		= (LinearLayout) findViewById(R.id.linCall);
        txtRute		= (TextView) findViewById(R.id.txtRute);
        txtCall		= (TextView) findViewById(R.id.txtCall);
		progressBar = (ProgressBar) findViewById(R.id.progressBusy);
        
        mMap.setOnMarkerClickListener(this);
		spinWilayah.setOnItemSelectedListener(this);
		spinPlace.setOnItemSelectedListener(this);
		txtTitle.setTypeface(fontUbuntuB);
        txtDetail.setTypeface(fontUbuntuL);
        txtRute.setTypeface(fontUbuntuB);
        txtCall.setTypeface(fontUbuntuB);
		
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (fragment.getView().getVisibility()==View.GONE) {
					fragment.getView().setVisibility(View.VISIBLE);
					fragmentTwo.getView().setVisibility(View.GONE);
					mMapTwo.clear();
				}
				mMap.clear();
            	getAllData(""+wilayahId, ""+placeId);
			}
		});
		
		getWilayahSpinner();
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
	    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11));
	    	
	    	final String[] splitMoreDetail = marker.getTitle().split("\\|");
	    	final String phoneNumber = splitMoreDetail[2];
	    	final String SpesialisId = splitMoreDetail[1];
	    	
	    	if (marker.getTitle()!=null) {
		    	txtTitle.setText(splitMoreDetail[0]);
		    	txtDetail.setText(Html.fromHtml(marker.getSnippet()));
	    	} else {
	    		linDesc.setVisibility(View.GONE);
	    	}
	    	
	    	linRute.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					openMap(fromPosition, marker.getPosition(), SpesialisId, marker.getTitle());
				}
			});
	    	
	    	linCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (phoneNumber.equalsIgnoreCase("")) {
						Toast.makeText(getBaseContext(), "No. Telp is Required!", Toast.LENGTH_SHORT).show();
					} else {
						String uri = "tel:" + phoneNumber;
		                Intent intent = new Intent(Intent.ACTION_CALL);
		                intent.setData(Uri.parse(uri));
		                startActivity(intent);
					}
				}
			});
    	} catch (Exception e) {
			linDesc.setVisibility(View.GONE);
		}
    	return true;
    }
	
	public void getWilayahSpinner() {
		String url = Referensi.url+"/getWilayah.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
            		JSONArray categories = response.getJSONArray("categories");                        

                    Kategori cat0 = new Kategori(0, "All", "", "");
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
	
	public void getPlaceSpinner(int wilayahId) {
		String url = Referensi.url+"/getPlace.php?BengkelWilayah="+wilayahId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
            		JSONArray categories = response.getJSONArray("categories");                        

                    Kategori cat0 = new Kategori(0, "All", "", "");
                    placeList     = new ArrayList<Kategori>();
                    placeList.add(cat0);
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Kategori cat = new Kategori(catObj.getInt("BengkelId"), catObj.getString("BengkelName"), catObj.getString("Latitude"), catObj.getString("Longitude"));
                        placeList.add(cat);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                populateSpinnerPlace();
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
	
    /**
     * Adding spinner data
     * */
    private void populateSpinnerWilayah() {
        List<String> lables  = new ArrayList<String>();
        wilayahId = 0;
 
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
    private void populateSpinnerPlace() {
        List<String> lables  = new ArrayList<String>();
        placeId = 0;
 
        for (int i = 0; i < placeList.size(); i++) {
            lables.add(placeList.get(i).getName());
        }
        
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinPlace.setAdapter(spinnerAdapter);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	parent.getItemAtPosition(position);

        switch (parent.getId()) {         
            case R.id.spinWilayah:
            	wilayahId = wilayahList.get(position).getId();
            	getPlaceSpinner(wilayahId);
            	break;        
            case R.id.spinPlace:
            	placeId = placeList.get(position).getId();
            	break;
        }
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {   }

    /**
     * Initialize the location manager.
     */
    private void initLocationManager() {
    	// create class object
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled     
        if (gps.canGetLocation()) {
            newLatitude  = gps.getLatitude();
            newLongitude = gps.getLongitude();
            getAllData("", "");
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public void getAllData(final String wilayah, final String id) {
        String url = Referensi.url+"/getLBS.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
                    str_login = response.getJSONArray("info");

                    for(int i = 0; i < str_login.length(); i++){
                        JSONObject ar = str_login.getJSONObject(i);

                        String BengkelLongitude 	= ar.getString("BengkelLongitude");
                        String BengkelLatitude 	    = ar.getString("BengkelLatitude");
                        String BengkelNoTelp3 	    = ar.getString("BengkelNoTelp3");
                        String BengkelNoTelp2 	    = ar.getString("BengkelNoTelp2");
                        String BengkelNoTelp1 	    = ar.getString("BengkelNoTelp1");
                        String BengkelAlamat        = ar.getString("BengkelAlamat");
                        String BengkelContactPerson = ar.getString("BengkelContactPerson");
                        String BengkelName 	        = ar.getString("BengkelName");
                        String SpesialisId    	    = ar.getString("SpesialisId");
                        String WilayahId    	    = ar.getString("WilayahId");
                        String BengkelId  	 	    = ar.getString("BengkelId");

                        if (!(wilayah.equalsIgnoreCase("")||wilayah.equalsIgnoreCase("0"))&&
                        		(id.equalsIgnoreCase("")||id.equalsIgnoreCase("0"))) {
                        	if (WilayahId.equalsIgnoreCase(wilayah)) {
                        		initializeMap(Double.parseDouble(BengkelLatitude), Double.parseDouble(BengkelLongitude),
                                        SpesialisId, BengkelName, BengkelContactPerson, BengkelAlamat,
                                        BengkelNoTelp1, BengkelNoTelp2, BengkelNoTelp3, WilayahId, "");
                        	}
                        } else if (!(id.equalsIgnoreCase("")||id.equalsIgnoreCase("0"))) {
                        	if (BengkelId.equalsIgnoreCase(id)) {
                        		initializeMap(Double.parseDouble(BengkelLatitude), Double.parseDouble(BengkelLongitude),
                                        SpesialisId, BengkelName, BengkelContactPerson, BengkelAlamat,
                                        BengkelNoTelp1, BengkelNoTelp2, BengkelNoTelp3, "", BengkelId);
                        	}
                        } else {
                        	initializeMap(Double.parseDouble(BengkelLatitude), Double.parseDouble(BengkelLongitude),
                                    SpesialisId, BengkelName, BengkelContactPerson, BengkelAlamat,
                                    BengkelNoTelp1, BengkelNoTelp2, BengkelNoTelp3, "", "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            	
            	progressBar.setVisibility(View.GONE);
            	fragment.getView().setVisibility(View.VISIBLE);
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

    private void initializeMap(double lat1, double lng1, final String SpesialisId, String BengkelName,
                               String BengkelContactPerson, String BengkelAlamat,
                               String BengkelNoTelp1, String BengkelNoTelp2, String BengkelNoTelp3,
                               String WilayahId, String BengkelId) {

        if (mMap!=null) {
            fromPosition = new LatLng(newLatitude, newLongitude);
			toPosition   = new LatLng(lat1, lng1);

            Location locationA = new Location("point A");
            locationA.setLatitude(newLatitude);
            locationA.setLongitude(newLongitude);

            Location locationB = new Location("point B");
            locationB.setLatitude(lat1);
            locationB.setLongitude(lng1);

            double distance = locationA.distanceTo(locationB);
            if (BengkelContactPerson.equalsIgnoreCase("")) {
                BengkelContactPerson="-";
            }
            if (BengkelNoTelp1.equalsIgnoreCase("")) {
                BengkelNoTelp1="-";
            }
            if (BengkelNoTelp2.equalsIgnoreCase("")) {
                BengkelNoTelp2="-";
            }
            if (BengkelNoTelp3.equalsIgnoreCase("")) {
                BengkelNoTelp3="-";
            }

            String detail  = "Contact Person: "+BengkelContactPerson+"<br>"+
                    "Alamat: "+BengkelAlamat+"<br>"+"NoTelp1: "+BengkelNoTelp1+", "+"NoTelp2: "+BengkelNoTelp2+", "+
                    "NoTelp3: "+BengkelNoTelp3+"<br>"+"Jarak: " + (int)distance/1000 + " KM";
            String moreDetail   = BengkelName+"|"+SpesialisId+"|"+BengkelNoTelp1;
            EventInfo eventInfo = new EventInfo(new LatLng(lat1, lng1), moreDetail, detail);

            // Move the camera instantly to hamburg with a zoom of 15.
            if (!(WilayahId.equalsIgnoreCase("")&&BengkelId.equalsIgnoreCase(""))) {
            	if (!BengkelId.equalsIgnoreCase("")) {
            		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 15));
            	} else {
            		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 11));
            	}
            } else {
            	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 11));
            }

            
            if (!(WilayahId.equalsIgnoreCase("")&&BengkelId.equalsIgnoreCase(""))) {
            	if (SpesialisId.equalsIgnoreCase("1")) {
            		marker = mMap.addMarker(new MarkerOptions()
	                    .position(eventInfo.getLatLong())
	                    .title(eventInfo.getName())
	                    .snippet(eventInfo.getType())
	                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.authorized)));
            	} else if (SpesialisId.equalsIgnoreCase("2")) {
            		marker = mMap.addMarker(new MarkerOptions()
                    	.position(eventInfo.getLatLong())
                    	.title(eventInfo.getName())
                    	.snippet(eventInfo.getType())
                    	.icon(BitmapDescriptorFactory.fromResource(R.drawable.supermarket)));
            	} else if (SpesialisId.equalsIgnoreCase("3")) {
            		marker = mMap.addMarker(new MarkerOptions()
                        .position(eventInfo.getLatLong())
                        .title(eventInfo.getName())
                        .snippet(eventInfo.getType())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.repair)));
                } else if (SpesialisId.equalsIgnoreCase("4")) {
                    marker = mMap.addMarker(new MarkerOptions()
                        .position(eventInfo.getLatLong())
                        .title(eventInfo.getName())
                        .snippet(eventInfo.getType())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.footprint)));
                } else {
                	marker = mMap.addMarker(new MarkerOptions()
		                .position(eventInfo.getLatLong())
		                .title(eventInfo.getName())
		                .snippet(eventInfo.getType())
		                .icon(BitmapDescriptorFactory.fromResource(R.drawable.speedhump)));
                }

                eventMarkerMap = new HashMap<Marker, EventInfo>();
                eventMarkerMap.put(marker, eventInfo);
            } else {
	            if (distance < 60000) {
	            	if (SpesialisId.equalsIgnoreCase("1")) {
	            		marker = mMap.addMarker(new MarkerOptions()
		                    .position(eventInfo.getLatLong())
		                    .title(eventInfo.getName())
		                    .snippet(eventInfo.getType())
		                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.authorized)));
	            	} else if (SpesialisId.equalsIgnoreCase("2")) {
	            		marker = mMap.addMarker(new MarkerOptions()
	                    	.position(eventInfo.getLatLong())
	                    	.title(eventInfo.getName())
	                    	.snippet(eventInfo.getType())
	                    	.icon(BitmapDescriptorFactory.fromResource(R.drawable.supermarket)));
	            	} else if (SpesialisId.equalsIgnoreCase("3")) {
	            		marker = mMap.addMarker(new MarkerOptions()
	                        .position(eventInfo.getLatLong())
	                        .title(eventInfo.getName())
	                        .snippet(eventInfo.getType())
	                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.repair)));
	                } else if (SpesialisId.equalsIgnoreCase("4")) {
	                    marker = mMap.addMarker(new MarkerOptions()
	                        .position(eventInfo.getLatLong())
	                        .title(eventInfo.getName())
	                        .snippet(eventInfo.getType())
	                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.footprint)));
	                } else {
	                	marker = mMap.addMarker(new MarkerOptions()
			                .position(eventInfo.getLatLong())
			                .title(eventInfo.getName())
			                .snippet(eventInfo.getType())
			                .icon(BitmapDescriptorFactory.fromResource(R.drawable.speedhump)));
	                }
	
	                // Add our position
	                mMap.addMarker(new MarkerOptions().position(fromPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
	
	                eventMarkerMap = new HashMap<Marker, EventInfo>();
	                eventMarkerMap.put(marker, eventInfo);
	            }
            }
        } else {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getMap();
            Toast.makeText(getApplicationContext(), "MAP NULL", Toast.LENGTH_LONG).show();
        }
    }

    public void openMap(LatLng fromPosition, LatLng toPosition, String SpesialisId, String name) {
		try {
			md = new GoogleMapV2Direction();  
			
			// Getting reference to SupportMapFragment
			fragmentTwo = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
			mMapTwo 	= fragmentTwo.getMap();
			
			fragment.getView().setVisibility(View.GONE);
			fragmentTwo.getView().setVisibility(View.VISIBLE);
	
			if (mMapTwo!=null) {
				String _Lat  = String.valueOf(toPosition.latitude);
				String _Long = String.valueOf(toPosition.longitude);
				
				if ((_Lat.equals("") || (_Long.equals("")))) {
					Toast.makeText(getBaseContext(), "Lokasi Tujuan tidak mempunyai Latitude dan Longitude", Toast.LENGTH_LONG).show();
				} else {
					md = new GoogleMapV2Direction();
					
					// Getting reference to SupportMapFragment
					mMapTwo.animateCamera(CameraUpdateFactory.newLatLngZoom(fromPosition, 13));
					mMapTwo.addMarker(new MarkerOptions().position(fromPosition).title("Your Position")
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
					
					if (SpesialisId.equalsIgnoreCase("3")||SpesialisId.equalsIgnoreCase("5")) {
						markerTwo = mMapTwo.addMarker(new MarkerOptions()
	                            .position(toPosition)
	                            .title(name)
	                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.repair)));
	                } else {
	                	markerTwo = mMapTwo.addMarker(new MarkerOptions()
	                            .position(toPosition)
	                            .title(name)
	                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.supermarket)));
	                }
					getDirectionMap(fromPosition, toPosition);
				}
			} else {
				Toast.makeText(getBaseContext(), "No Map", Toast.LENGTH_SHORT).show();
			}
		} catch (NullPointerException e) {
			Toast.makeText(MainActivity.this, "Can't get gps location, make sure your gps is enable",Toast.LENGTH_LONG).show();
		}
	}
	
    private void getDirectionMap(LatLng from, LatLng to) {
		LatLng fromto[] = { from, to };
		new LongOperation().execute(fromto);
	}

	private class LongOperation extends AsyncTask<LatLng, Void, Document> {
		@Override
		protected Document doInBackground(LatLng... params) {
			Document doc = md.getDocument(params[0], params[1], GoogleMapV2Direction.MODE_DRIVING);
			return doc;
		}
		@Override
		protected void onPostExecute(Document result) {
			setResult(result);
		}
		@Override
		protected void onPreExecute() { }
		@Override
		protected void onProgressUpdate(Void... values) { }
	}

	public void setResult(Document doc) {
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}

		mMapTwo.addPolyline(rectLine);
	}
    
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	if (fragment.getView().getVisibility()!=View.GONE) {
				finish();
			} else {
				fragment.getView().setVisibility(View.VISIBLE);
				fragmentTwo.getView().setVisibility(View.GONE);
				mMapTwo.clear();
			}
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if (fragment.getView().getVisibility()!=View.GONE) {
				finish();
			} else {
				fragment.getView().setVisibility(View.VISIBLE);
				fragmentTwo.getView().setVisibility(View.GONE);
				mMapTwo.clear();
			}
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
