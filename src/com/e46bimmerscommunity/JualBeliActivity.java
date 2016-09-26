package com.e46bimmerscommunity;

import org.json.JSONArray;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.e46bimmerscommunity.gridview.StaggeredGridView;
import com.e46bimmerscommunity.utils.Referensi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class JualBeliActivity extends ActionBarActivity {
	private StaggeredGridView itemList;
	private JualBeliAdapter jBeliAdapter;
    private String UserId = "";
    private ProgressBar progressBar;
    private ImageView btnSell;
    private RequestQueue queue;
    private SharedPreferences bimmersPref;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.activity_jual_beli);
		setTitle("BUY and SELL Forum");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

		queue 		= Volley.newRequestQueue(this);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bimmersPref = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		UserId      = bimmersPref.getString("UserId", "");
		itemList    = (StaggeredGridView) findViewById(R.id.itemList);
		btnSell		= (ImageView) findViewById(R.id.btnSell);
		
		getAllData();
		
		btnSell.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(getApplicationContext(), JualActivity.class).putExtra("UserId", UserId), 1);
			}
		});
    }
	
	@SuppressLint("NewApi")
	public void getAllData() {
		progressBar.setVisibility(View.VISIBLE);
		itemList.setVisibility(View.GONE);
		
        String url = Referensi.url+"/getAllItems.php";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            	progressBar.setVisibility(View.GONE);
            	itemList.setVisibility(View.VISIBLE);
        		
            	jBeliAdapter = new JualBeliAdapter(JualBeliActivity.this, response);
            	itemList.setAdapter(jBeliAdapter);
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
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
            	getAllData();
            }
        }
	}
	
	@Override
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
