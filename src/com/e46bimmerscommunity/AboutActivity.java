package com.e46bimmerscommunity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;

public class AboutActivity extends ActionBarActivity  {
    private WebView txtDetail;
    private String mimeType = "text/html";
    private String encoding = "utf-8";
    private String htmlText = "";
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_about);
		setTitle("About");
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);

        txtDetail  = (WebView) findViewById(R.id.txtDetail);

        htmlText = "<html><body style='text-align:justify'><b><u>About E46 Bimmers</u></b><br/>" +
                "Sejumlah konseptor bekerjasama menyatukan ide dengan tujuan berkomunikasi, berkoordinasi dan menciptakan hubungan keluarga antar sesama melalui komunitas E46 Bimmers, suatu komunitas keluarga yang harmonis melalui kebersamaan dan kegiatan-kegiatan tertentu.<br/>" +
                "<br/>" +
                "Mari bersama E46Bimmers dalam unity dan kesatuan hati untuk menjalankan kreatifitas yang bertanggung jawab di daerah2 masing-masing untuk akhirnya bertemu di satu titik menyatukan visi dan misi E46Bimmers Community.<br/><br/>" +
                "<b><u>Visi :</u></b><br/>" +
                "Bimmers membentuk keluarga yang harmonis antar sesama pencinta BMW E46 melalui kebersamaan dan kegiatan-kegiatan tertentu <br/>" +
                "<b><u>Misi :</u></b><br/>" +
                "Bimmers sharing segala permasalah perawatan BMW E46 dan memberikan manfaat dan value added lainnya kepada member.<br/>" +
                "<br/>" +
                "E46Bimmers berupaya selalu hadir menjadi komunitas keluarga BMW terbaik yang bersama-sama membangun Indonesia dengan club atau komunitas BMW lainnya. <br/><br/>"+
                "<b>Facebook :</b> <a href='https://www.facebook.com/pages/E46-Bimmers-Community/418239778348522?fref=ts'>E46 Bimmers Community</a><br/>"+
                "<b>Website :</b> <a href='http://e46bimmers.id'>www.e46bimmers.id</a><br/><br/>"+
                "Saran dan masukan email ke <a href=''>admin@e46bimmers.id</a></body></Html>";
        //txtDetail.setTypeface(fontHabibi);
        txtDetail.loadData(htmlText, mimeType, encoding);
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