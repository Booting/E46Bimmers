package com.e46bimmerscommunity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e46bimmerscommunity.utils.Referensi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class EventActivity extends ActionBarActivity {
    private JSONArray str_login  = null;
    private ListView list;
    private LazyAdapter lazyAdapter;
    private EventAdapter eventAdapter;
    private ScrollView linEventDetail;
    private TextView txtDate, txtTitle, txtDateTime, txtAlamat, txtDeskripsi;
    private Typeface fontHabibi, fontOldStandardBold;
    private GoogleMap mMap;
    private RequestQueue queue;
    private ProgressBar progressBar;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_event);
		setTitle("Bulletin Board / Event");
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
        
        fontOldStandardBold = FontCache.get(this, "OldStandard-Bold");
        fontHabibi    	    = FontCache.get(this, "OldStandard-Regular");
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    
        queue 		   = Volley.newRequestQueue(this);
        list 		   = (ListView) findViewById(R.id.list);
        linEventDetail = (ScrollView) findViewById(R.id.linEventDetail);
        txtDate		   = (TextView) findViewById(R.id.txtDate);
        txtTitle	   = (TextView) findViewById(R.id.txtTitle);
        txtDateTime	   = (TextView) findViewById(R.id.txtDateTime);
        txtAlamat	   = (TextView) findViewById(R.id.txtAlamat);
        txtDeskripsi   = (TextView) findViewById(R.id.txtDeskripsi);
		progressBar    = (ProgressBar) findViewById(R.id.progressBusy);

        txtTitle.setTypeface(fontOldStandardBold);
        txtDateTime.setTypeface(fontHabibi);
        txtAlamat.setTypeface(fontHabibi);
        txtDeskripsi.setTypeface(fontHabibi);
        txtDate.setTypeface(fontOldStandardBold);
        
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	linEventDetail.setVisibility(View.VISIBLE);
            	list.setVisibility(View.GONE);
            	
                txtDate.setText(((TextView) view.findViewById(R.id.txtDate)).getText().toString());
                txtTitle.setText(((TextView) view.findViewById(R.id.txtTitle)).getText().toString());
                txtDateTime.setText(((TextView) view.findViewById(R.id.txtDateTime)).getText().toString());
                txtAlamat.setText(((TextView) view.findViewById(R.id.txtAlamat)).getText().toString());
                txtDeskripsi.setText("Keterangan: \n"+((TextView) view.findViewById(R.id.txtDeskripsi)).getText().toString());
                
                SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                mMap = fragment.getMap();
                
                double lat = Double.parseDouble(((TextView) view.findViewById(R.id.txtLatitude)).getText().toString());
                double lng = Double.parseDouble(((TextView) view.findViewById(R.id.txtLongitude)).getText().toString());
                initializeMap(lat, lng);
            }
        });
        
        getAllData();
    }
    
    public void getAllData() {
        String url = Referensi.url+"/getEvent.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
                    str_login   = response.getJSONArray("info");
                    lazyAdapter = new LazyAdapter(EventActivity.this, str_login);
                    list.setAdapter(lazyAdapter);
                    //eventAdapter = new EventAdapter(EventActivity.this, str_login);
                    //list.setAdapter(eventAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            	
            	progressBar.setVisibility(View.GONE);
            	list.setVisibility(View.VISIBLE);
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
    
    private void initializeMap(double lat1, double lng1) {
		if (mMap!=null) {
			final LatLng toPosition = new LatLng(lat1, lng1);
		
			// Move the camera instantly to hamburg with a zoom of 15.
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 14));
			
			// Add our position
			mMap.addMarker(new MarkerOptions().position(toPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));
	
		} else {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.container)).getMap();
            Toast.makeText(getApplicationContext(), "MAP NULL", Toast.LENGTH_LONG).show();
            initializeMap(lat1, lng1);
        }
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	if (list.getVisibility()!=View.GONE) {
				finish();
			} else {
				linEventDetail.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
			}
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event ){
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
			if (list.getVisibility()!=View.GONE) {
				finish();
			} else {
				linEventDetail.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
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