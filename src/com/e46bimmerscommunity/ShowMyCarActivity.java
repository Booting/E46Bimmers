package com.e46bimmerscommunity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.LoopViewPager;
import com.e46bimmerscommunity.utils.NestedListView;
import com.e46bimmerscommunity.utils.Referensi;
import com.e46bimmerscommunity.utils.callURL;
import com.viewpagerindicator.CirclePageIndicator;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ShowMyCarActivity extends ActionBarActivity {
	private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtPlatNo, txtName, txtDescription, txtLike, txtCountLike, txtComment,txtCountComment, txtIsLike;
    private ImageView imgProfile;
    private ImageLoader mImageLoader;
    private LoopViewPager imagePager;
    private CirclePageIndicator cIndicator;
    private ImagePagerAdapter productImageAdapter;
    @SuppressWarnings("unused")
	private int imagePosition = 0;
    private EditText txtWriteComment;
    private Button btnPost;
    private NestedListView lsvList;
    private CommentAdapter commentAdapter;
    private ProgressBar progressBar;
    private RequestQueue queue;
    private JSONArray str_login  = null;
    private LinearLayout linList;
	private ProgressDialog pDialog;
	private String url="", UserId, WhoCommentId, LikeId;
    private SharedPreferences bimmersPref;
    private ImageSwitcher imgLike;
    private RelativeLayout relLike;
    private boolean isAvailable=false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.activity_show_my_car);
		setTitle("Show My Car");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bimmersPref     = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
		mImageLoader    = new ImageLoader(ShowMyCarActivity.this);
        fontUbuntuL     = FontCache.get(ShowMyCarActivity.this, "Ubuntu-L");
		fontUbuntuB     = FontCache.get(ShowMyCarActivity.this, "Ubuntu-B");
		imagePager      = (LoopViewPager) findViewById(R.id.pagerItemImages);
        cIndicator      = (CirclePageIndicator) findViewById(R.id.indicator);
        txtPlatNo       = (TextView) findViewById(R.id.txtPlatNo);
        txtName		    = (TextView) findViewById(R.id.txtName);
        txtDescription  = (TextView) findViewById(R.id.txtDescription);
        txtLike		    = (TextView) findViewById(R.id.txtLike);
        txtCountLike    = (TextView) findViewById(R.id.txtCountLike);
        txtComment      = (TextView) findViewById(R.id.txtComment);
        txtCountComment = (TextView) findViewById(R.id.txtCountComment);
		imgProfile      = (ImageView) findViewById(R.id.imgProfile);
		txtWriteComment = (EditText) findViewById(R.id.txtWriteComment);
		btnPost			= (Button) findViewById(R.id.btnPost);
		lsvList			= (NestedListView) findViewById(R.id.lsvList);
		progressBar  	= (ProgressBar) findViewById(R.id.progressBusy);
		queue 			= Volley.newRequestQueue(this);
		linList			= (LinearLayout) findViewById(R.id.linList);
		UserId			= getIntent().getExtras().getString("UserId");
		WhoCommentId    = bimmersPref.getString("UserId", "");
		imgLike         = (ImageSwitcher) findViewById(R.id.imgLike);
		relLike			= (RelativeLayout) findViewById(R.id.relLike);
		txtIsLike		= (TextView) findViewById(R.id.txtIsLike);
		
		txtName.setTypeface(fontUbuntuB);
		txtPlatNo.setTypeface(fontUbuntuL);
		txtDescription.setTypeface(fontUbuntuL);
		txtLike.setTypeface(fontUbuntuL);
		txtCountLike.setTypeface(fontUbuntuL);
		txtComment.setTypeface(fontUbuntuL);
		txtCountComment.setTypeface(fontUbuntuL);
		txtWriteComment.setTypeface(fontUbuntuL);
		btnPost.setTypeface(fontUbuntuB);
		
		imgLike.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressLint("InlinedApi")
			@SuppressWarnings("deprecation")
			@Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
                myView.setImageResource(R.drawable.thumb_up_outline);
                return myView;
            }
        });
		
		relLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String diLike;
				if (txtIsLike.getText().toString().equalsIgnoreCase("1")) {
					diLike = "0";
				} else {
					diLike = "1";
				}
				
				if (isAvailable) {
					new postLike("UPDATELIKE", diLike).execute();
				} else {
					new postLike("ADDNEWLIKE", diLike).execute();
				}
			}
		});
		
		JSONArray jsonArray = null;
		try {
			if (getIntent().getExtras().getString("Pictures").equalsIgnoreCase("")) {
				jsonArray = new JSONArray("[\"https://scontent-sin1-1.xx.fbcdn.net/hphotos-xal1/v/t1.0-9/11742672_421217024717464_6411350321001256104_n.jpg?oh=e1b265dcbf09263f7636855b9e2d544c&oe=56ED3243\"]");
			} else {
				String[] splitPic = getIntent().getExtras().getString("Pictures").split(",");
				jsonArray = new JSONArray();
				for (int i=0; i<splitPic.length; i++) {
					jsonArray.put(splitPic[i]);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		productImageAdapter = new ImagePagerAdapter(jsonArray);
        imagePager.setAdapter(productImageAdapter);
        cIndicator.setViewPager(imagePager);
        cIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                imagePosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        txtName.setText(getIntent().getExtras().getString("Name"));
        txtPlatNo.setText(getIntent().getExtras().getString("PlatNo"));
        txtDescription.setText(getIntent().getExtras().getString("Description"));
        int loader = R.drawable.loaderr;
        mImageLoader.DisplayImage("https://graph.facebook.com/"+getIntent().getExtras().getString("UserId")+"/picture?type=large", loader, imgProfile);
    
        btnPost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (txtWriteComment.getText().toString().equalsIgnoreCase("")) {
					txtWriteComment.setError("Comment is required!");
				} else if (txtWriteComment.getText().toString().contains("\"")) {
					txtWriteComment.setError("Maaf, deskripsi Anda tidak boleh menggunakan karakter \", silahkan ganti dengan inch atau yang lainnya!");
				} else {
					new postComment().execute();
				}
			}
		});
        
        getUserLike(getIntent().getExtras().getString("UserId"));
	}
	
	public void getUserLike(final String UserId) {
        String url = Referensi.url+"/getUserLike.php?UserId="+UserId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
            		int count = 0;
                    str_login = response.getJSONArray("info");
                    for (int i=0; i<str_login.length(); i++) {
                    	// CEK DI LIKE ATAU TIDAK
                    	if (str_login.optJSONObject(i).optString("WhoLikeId").equalsIgnoreCase(WhoCommentId)) {
                    		LikeId      = str_login.optJSONObject(i).optString("LikeId");
                    		isAvailable = true;
                    		if (str_login.optJSONObject(i).optString("LikeStatus").equalsIgnoreCase("1")) {
                    			imgLike.setImageResource(R.drawable.thumb_up);
                    			txtIsLike.setText("1");
                    		} else {
                    			imgLike.setImageResource(R.drawable.thumb_up_outline);
                    			txtIsLike.setText("0");
                    		}
                    	} else {
                    		txtIsLike.setText("0");
                    	}
                    	
                    	// GET COUNT
                    	if (str_login.optJSONObject(i).optString("LikeStatus").equalsIgnoreCase("1")) {
                    		count = count + 1;
                    	}
                    }
                    txtCountLike.setText(""+count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            	getUserComment(UserId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	getUserComment(UserId);
            }
        });
        queue.add(jsObjRequest);
    }
	
	public void getUserComment(String UserId) {
        String url = Referensi.url+"/getUserComment.php?UserId="+UserId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
                    str_login      = response.getJSONArray("info");
                    commentAdapter = new CommentAdapter(ShowMyCarActivity.this, str_login);
                    lsvList.setAdapter(commentAdapter);
                    txtCountComment.setText(""+str_login.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
                linList.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        queue.add(jsObjRequest);
    }
	
	private class postComment extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowMyCarActivity.this);
            pDialog.setMessage("Working...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
        	try {
				String mComment = URLEncoder.encode(txtWriteComment.getText().toString(), "utf-8");
	        	url = Referensi.url+"/service.php?ct=ADDNEWCOMMENT" +
	        			"&UserId="+UserId+
	        			"&WhoCommentId="+WhoCommentId+
	        			"&CommentText="+mComment;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	String hasil = callURL.call(url);
        	return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        	pDialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
            	txtWriteComment.setText("");
            	Toast.makeText(getApplicationContext(), "Post comment succesfully!", Toast.LENGTH_SHORT).show();
            	getUserComment(UserId);
            } else {
            	Toast.makeText(getApplicationContext(), "Post comment failed! Try Again!", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	private class postLike extends AsyncTask<String, Void, String> {
		String ct, diLike;
		public postLike(String mCt, String mDiLike) {
			ct     = mCt;
			diLike = mDiLike;
		}
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowMyCarActivity.this);
            pDialog.setMessage("Working...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
        	if (ct.equalsIgnoreCase("ADDNEWLIKE")) {
	    		url = Referensi.url+"/service.php?ct="+ ct +
	        			"&UserId="+UserId+
	        			"&WhoLikeId="+WhoCommentId+
	        			"&LikeStatus="+diLike;
    		} else {
    			url = Referensi.url+"/service.php?ct="+ ct +
	        			"&LikeStatus="+diLike+
	        			"&LikeId="+LikeId;
    		}
        	
        	String hasil = callURL.call(url);
        	return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        	pDialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
            	if (diLike.equalsIgnoreCase("1")) {
            		Toast.makeText(getApplicationContext(), "Like succesfully!", Toast.LENGTH_SHORT).show();
            	} else {
            		Toast.makeText(getApplicationContext(), "Unlike succesfully!", Toast.LENGTH_SHORT).show();
            	}
            	getUserLike(UserId);
            } else {
            	if (diLike.equalsIgnoreCase("1")) {
            		Toast.makeText(getApplicationContext(), "Like failed! Try Again!", Toast.LENGTH_SHORT).show();
                    imgLike.setImageResource(R.drawable.thumb_up_outline);
            	} else {
            		Toast.makeText(getApplicationContext(), "Unlike failed! Try Again!", Toast.LENGTH_SHORT).show();
                    imgLike.setImageResource(R.drawable.thumb_up);
            	}
            }
        }
    }
	
	private class ImagePagerAdapter extends PagerAdapter {
        private JSONArray jsonProductImages;
        
        public ImagePagerAdapter(JSONArray jsonProductImages) {
            this.jsonProductImages = jsonProductImages;
        }
        @Override
        public int getCount() {
            return jsonProductImages.length();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(ShowMyCarActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);        
            int loader = R.drawable.loaderr;
            
            if (jsonProductImages.optString(position).contains("https")) {
            	mImageLoader.DisplayImage(jsonProductImages.optString(position).replace(" ", "%20"), loader, imageView);
            } else {
            	mImageLoader.DisplayImage(Referensi.url+"/pictures/"+jsonProductImages.optString(position).replace(" ", "%20"), loader, imageView);
            }
            
            ((ViewPager) container).addView(imageView, 0);
            
            imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(ShowMyCarActivity.this, PhotoViewer.class);
		            i.putExtra("image_list", jsonProductImages.toString());
		            i.putExtra("position", position);
		            startActivity(i);
				}
			});
            
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
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
		if( keyCode == KeyEvent.KEYCODE_BACK ) {
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
