package com.e46bimmerscommunity;

import org.json.JSONArray;
import org.json.JSONException;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.LoopViewPager;
import com.e46bimmerscommunity.utils.Referensi;
import com.viewpagerindicator.CirclePageIndicator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class MaintenanceDetailActivity extends ActionBarActivity {
	private Typeface fontUbuntuL, fontUbuntuB;
    private TextView txtItemName, txtBy;
    private WebView txtDetail;
    private ImageLoader mImageLoader;
    private LoopViewPager imagePager;
    private CirclePageIndicator cIndicator;
    private ImagePagerAdapter productImageAdapter;
    @SuppressWarnings("unused")
	private int imagePosition = 0;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.activity_maintenance_detail);
		setTitle("Maintenance Info Detail");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

		mImageLoader = new ImageLoader(MaintenanceDetailActivity.this);
        fontUbuntuL  = FontCache.get(MaintenanceDetailActivity.this, "Ubuntu-L");
		fontUbuntuB  = FontCache.get(MaintenanceDetailActivity.this, "Ubuntu-B");
		imagePager   = (LoopViewPager) findViewById(R.id.pagerItemImages);
        cIndicator   = (CirclePageIndicator) findViewById(R.id.indicator);
		txtItemName  = (TextView) findViewById(R.id.txtItemName);
		txtBy		 = (TextView) findViewById(R.id.txtBy);
		txtDetail    = (WebView) findViewById(R.id.txtDetail);
		
		txtItemName.setTypeface(fontUbuntuB);
		txtBy.setTypeface(fontUbuntuL);
		
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(getIntent().getExtras().getString("ItemPicture"));
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

        txtBy.setText("By "+getIntent().getExtras().getString("ItemBy"));
        txtItemName.setText(getIntent().getExtras().getString("ItemName"));
        txtDetail.loadData(getIntent().getExtras().getString("ItemDetail"), "text/html", "utf-8");
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
            ImageView imageView = new ImageView(MaintenanceDetailActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);        
            int loader = R.drawable.loaderr;
            mImageLoader.DisplayImage(Referensi.url+"/pictures/"+jsonProductImages.optString(position).replace(" ", "%20"), loader, imageView);
            ((ViewPager) container).addView(imageView, 0);
            
            imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(MaintenanceDetailActivity.this, PhotoViewer.class);
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
