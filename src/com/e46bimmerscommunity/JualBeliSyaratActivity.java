package com.e46bimmerscommunity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class JualBeliSyaratActivity extends ActionBarActivity {
	private WebView txtDetail;
    private String mimeType = "text/html";
    private String encoding = "utf-8";
    private String htmlText = "";
    private Typeface fontUbuntuB;
    private TextView txtNext;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		setContentView(R.layout.activity_jual_beli_syarat);
		setTitle("Syarat & Ketentuan BUY and SELL Forum");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		fontUbuntuB = FontCache.get(JualBeliSyaratActivity.this, "Ubuntu-B");
		txtDetail   = (WebView) findViewById(R.id.txtDetail);
		txtNext     = (TextView) findViewById(R.id.txtNext);
        
        htmlText = "<html><body style='text-align:justify'><b><u>SYARAT & KETENTUAN BUY AND SELL FORUM</u></b><br/>" +
                "Setiap pengguna menerima, memahami, menyetujui mematuhi semua isi dalam Syarat & Ketentuan Layanan di bawah ini. Syarat dan Ketentuan mengikat para pengguna app E46Bimmers.<br/>" +
                "<br/>" +
                "E46Bimmers Community tidak bertanggung jawab atas isi informasi, gambar dan keterangan lainnya yang terdapat atau diterbitkan dalam aplikasi ini. E46Bimmers Community tidak bertanggung jawab dan tidak dapat diminta pertanggungjawaban atas kerugian langsung maupun tidak langsung apapun sebagai akibat dari keuntungan berkaitan dengan penggunaan atau kinerja dari informasi dan/atau gambar yang disediakan apikasi ini. Admin berhak memperbaharui dan/atau menghapus dan/atau mengubah isi iklan dan/atau mengubah tampilan dan isinya tanpa melakukan pemberitahuanterlebih dahulu kepada pengguna dan demi memberi pelayanan yang lebih baik kepada setiap pengguna.<br/><br/>" +
                "Untuk informasi materi yang sudah terjual atau sudah tidak tersedia lagi maka pengguna dapat mengirimkan email ke admin@E46bimmers.id atau materi informasi jual beli di akan kadaluwarsa (hilang otomatis) setelah jangka waktu 3 bulan. Apabila belum terjual atau masih tersedia, pengguna dipersilahkan memperbaharui materi tersebut, admin tidak menanggung kewajiban untuk memperbaharui materi.<br/>" +
                "<br/>" +
                "Pengguna wajib tunduk dan patuh pada peraturan perundangan-undangan yang berlaku serta hukum yang berlaku di Republik Indonesia. Segala tindakan yang bertentangan, melanggar, tidak sesuai termasuk tidak terbatas pada ketidakpatuhan pada peraturan perundang-undangan menjadi tanggung jawab pengguna sepenuhnya. E46 Bimmers Community berhak mengungkapkan identitas pengguna kepada pihak ketiga dan/atau pihak yang berwenang berkaitan tentang materi yang ditempatkan pengguna ke aplikasi atau jika materi merupakan pelanggaran terhadap Hak Kekayaan Intelektual atau hak pribadi pihak ketiga, sesuai peraturan yang berlaku di Indonesia.<br/>" +
                "<br/>" +
                "Syarat & Ketentuan Layanan dapat diubah dan/atau diperbaharui sewaktu-waktu oleh admin tanpa pemberitahuan terlebih dahulu. Apabila pengguna memiliki hal lain yang ingin disampaikan silakan menghubungi email <a href=''>admin@e46bimmers.id</a></body></Html>";
        txtDetail.loadData(htmlText, mimeType, encoding);
        txtNext.setTypeface(fontUbuntuB);
    }
	
	public void btnNextClick(View v) {
		startActivity(new Intent(getApplicationContext(), JualBeliActivity.class));
		finish();
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
