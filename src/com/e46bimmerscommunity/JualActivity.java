package com.e46bimmerscommunity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.e46bimmerscommunity.model.Kategori;
import com.e46bimmerscommunity.utils.Referensi;
import com.e46bimmerscommunity.utils.ServiceHandler;
import com.e46bimmerscommunity.utils.callURL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressWarnings("deprecation")
public class JualActivity extends ActionBarActivity implements OnItemSelectedListener {
	private Typeface fontUbuntuL, fontUbuntuB;
    private TextView lblNamaBarang, lblKategori, lblDeskripsi, lblHarga, lblRupiah, lblStock, lblNoTelp, lblLokasi,
    	txtCancel, txtSubmit, lblUpload;
    private EditText txtNamaBarang, txtDeskripsi, txtHarga, txtStock, txtNoTelp, txtLokasi; 
    private Spinner txtKategori;
    private ArrayList<Kategori> categoriList;
	private int categoriId;
    private String UserId = "";
    private ProgressDialog pDialog;
	private String url = "";
	private ImageView imageOne, imageTwo, imageThree, imageFour;
	private String imagepathOne=null, imagepathTwo=null, imagepathThree=null, imagepathFour=null;
    private JSONArray jsonArray = new JSONArray();
    @SuppressWarnings("unused")
	private long totalSize = 0;
    private HttpEntity resEntity;
    private int countImage=0;
    private int mMaxWidth  = 480;
    private int mMaxHeight = 480;
    
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activityslideup, R.anim.no_anim);
		setContentView(R.layout.activity_jual);
		setTitle("SELL");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        UserId        = getIntent().getExtras().getString("UserId");
		fontUbuntuL   = FontCache.get(JualActivity.this, "Ubuntu-L");
		fontUbuntuB   = FontCache.get(JualActivity.this, "Ubuntu-B");
		lblNamaBarang = (TextView) findViewById(R.id.lblNamaBarang);
		lblKategori	  = (TextView) findViewById(R.id.lblKategori); 
		lblDeskripsi  = (TextView) findViewById(R.id.lblDeskripsi); 
		lblHarga	  = (TextView) findViewById(R.id.lblHarga); 
		lblRupiah	  = (TextView) findViewById(R.id.lblRupiah); 
		lblStock	  = (TextView) findViewById(R.id.lblStock); 
		lblNoTelp	  = (TextView) findViewById(R.id.lblNoTelp); 
		lblLokasi	  = (TextView) findViewById(R.id.lblLokasi);
		txtNamaBarang = (EditText) findViewById(R.id.txtNamaBarang); 
		txtDeskripsi  = (EditText) findViewById(R.id.txtDeskripsi); 
		txtHarga	  = (EditText) findViewById(R.id.txtHarga); 
		txtStock	  = (EditText) findViewById(R.id.txtStock); 
		txtNoTelp	  = (EditText) findViewById(R.id.txtNoTelp); 
		txtLokasi     = (EditText) findViewById(R.id.txtLokasi);
		txtCancel     = (TextView) findViewById(R.id.txtCancel);
		txtSubmit     = (TextView) findViewById(R.id.txtSubmit);
		txtKategori	  = (Spinner) findViewById(R.id.txtKategori);
		lblUpload     = (TextView) findViewById(R.id.lblUpload);
		imageOne      = (ImageView) findViewById(R.id.imageOne);
		imageTwo      = (ImageView) findViewById(R.id.imageTwo);
		imageThree    = (ImageView) findViewById(R.id.imageThree);
		imageFour     = (ImageView) findViewById(R.id.imageFour);
		
		txtKategori.setOnItemSelectedListener(this);
		lblNamaBarang.setTypeface(fontUbuntuB); 
		lblKategori.setTypeface(fontUbuntuB); 
		lblDeskripsi.setTypeface(fontUbuntuB); 
		lblHarga.setTypeface(fontUbuntuB); 
		lblRupiah.setTypeface(fontUbuntuL); 
		lblStock.setTypeface(fontUbuntuB); 
		lblNoTelp.setTypeface(fontUbuntuB); 
		lblLokasi.setTypeface(fontUbuntuB);
		txtNamaBarang.setTypeface(fontUbuntuL); 
		txtDeskripsi.setTypeface(fontUbuntuL); 
		txtHarga.setTypeface(fontUbuntuL); 
		txtStock.setTypeface(fontUbuntuL); 
		txtNoTelp.setTypeface(fontUbuntuL); 
		txtLokasi.setTypeface(fontUbuntuL); 
		txtCancel.setTypeface(fontUbuntuB); 
		txtSubmit.setTypeface(fontUbuntuB);
		lblUpload.setTypeface(fontUbuntuB);
		
		pDialog = new ProgressDialog(JualActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
		
		getCategoriSpinner();
    }
	
	public void getCategoriSpinner() {
		ServiceHandler jsonParser  = new ServiceHandler();
        String json  = jsonParser.makeServiceCall(Referensi.url+"/getCategori.php", ServiceHandler.GET);
        categoriList  = new ArrayList<Kategori>();
        
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray categories = jsonObj.getJSONArray("categories");                        

                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject catObj = (JSONObject) categories.get(i);
                        Kategori cat = new Kategori(catObj.getInt("CategoriId"), catObj.getString("CategoriName"), catObj.getString("Latitude"), catObj.getString("Longitude"));
                        categoriList.add(cat);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("JSON Data", "Didn't receive any data from server!");
        }
        populateSpinnerCategori();
	}
	
	/**
     * Adding spinner data
     * */
    private void populateSpinnerCategori() {
        List<String> lables  = new ArrayList<String>();
        categoriId = 0;
 
        for (int i = 0; i < categoriList.size(); i++) {
            lables.add(categoriList.get(i).getName());
        }
        
        txtKategori.setAdapter(null);
        ArrayAdapter<String> spinnerAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtKategori.setAdapter(spinnerAdapter);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	parent.getItemAtPosition(position);
        switch (parent.getId()) {         
            case R.id.txtKategori:
            	categoriId = categoriList.get(position).getId();
            	break;        
        }
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {   }
	
	public void btnCancelClick(View v) {
		finish();
	}
	
	public void btnSubmitClick(View v) {
		if (imagepathOne==null && imagepathTwo==null && imagepathThree==null && imagepathFour==null) {
			Toast.makeText(getBaseContext(), "Gambar tidak boleh kosong!", Toast.LENGTH_SHORT).show();
		} else if (txtNamaBarang.getText().toString().equalsIgnoreCase("")) {
			txtNamaBarang.setError("Nama Barang tidak boleh kosong!");
		} else if (txtDeskripsi.getText().toString().equalsIgnoreCase("")) {
			txtDeskripsi.setError("Deskripsi tidak boleh kosong!");
		} else if (txtHarga.getText().toString().equalsIgnoreCase("")) {
			txtHarga.setError("Harga tidak boleh kosong!");
		} else if (txtStock.getText().toString().equalsIgnoreCase("")) {
			txtStock.setError("Stock tidak boleh kosong!");
		} else if (txtNoTelp.getText().toString().equalsIgnoreCase("")) {
			txtNoTelp.setError("No. Telp tidak boleh kosong!");
		} else if (txtLokasi.getText().toString().equalsIgnoreCase("")) {
			txtLokasi.setError("Lokasi tidak boleh kosong!");
		} else if (txtDeskripsi.getText().toString().contains("\"")) {
			txtDeskripsi.setError("Maaf, deskripsi Anda tidak boleh menggunakan karakter \", silahkan ganti dengan inch atau yang lainnya!");
		} else {
			if (imagepathOne!=null) {
				countImage = countImage+1;
			} if (imagepathTwo!=null) {
				countImage = countImage+1;
			} if (imagepathThree!=null) {
				countImage = countImage+1;
			} if (imagepathFour!=null) {
				countImage = countImage+1;
			}
			new UploadImage().execute();
		}
	}
	
	private class UploadImage extends AsyncTask<HttpEntity, Void, HttpEntity> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected HttpEntity doInBackground(HttpEntity... params) {
        	return doFileUpload();
        }
        @Override
        protected void onPostExecute(HttpEntity result) {
            super.onPostExecute(result);
            if (result != null) {
           	 	countImage=0;
                new addNewItem().execute();
            } else {
           	 	countImage=0;
            }
        }
    }
	
	private class addNewItem extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
        	String ItemName  = null;
			String Deskripsi = null;
			String Lokasi    = null;
			String Contact   = null;
        	String newHarga  = null;
			try {
				ItemName  = URLEncoder.encode(txtNamaBarang.getText().toString(), "utf-8");
				Deskripsi = URLEncoder.encode(txtDeskripsi.getText().toString(), "utf-8");
				Lokasi    = URLEncoder.encode(txtLokasi.getText().toString(), "utf-8");
				Contact   = URLEncoder.encode(txtNoTelp.getText().toString(), "utf-8");
				newHarga   = URLEncoder.encode("Rp " + txtHarga.getText().toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	url = Referensi.url+"/service.php?ct=ADDNEWITEM&SellerId="+UserId+
        			"&CategoriId="+categoriId+
        			"&ItemName="+ItemName+
        			"&Deskripsi="+Deskripsi+
        			"&Stock="+txtStock.getText().toString()+
        			"&Harga="+newHarga+
        			"&Lokasi="+Lokasi+
        			"&Contact="+Contact+
        			"&Picture="+jsonArray.toString();
        	String hasil = callURL.call(url);
        	return hasil;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("true")) {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(JualActivity.this);
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Add new item succesfully!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	dialog.cancel();
                    	setResult(RESULT_OK);
                    	finish();
                    }
                });
                if (!isFinishing()) { alertDialog.show(); }
            } else {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(JualActivity.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Add new item error! Please try again or close apps and open again.");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	dialog.cancel();
                    }
                });
                if (!isFinishing()) { alertDialog.show(); }
            }
        }
    }
	
	public void imageOneClick(View view) {
		Intent intent = new Intent(); 
		intent.setType("image/*"); 
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), 1); 
    }
	
	public void imageTwoClick(View view) {
		Intent intent = new Intent(); 
		intent.setType("image/*"); 
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), 2); 
    }
	
	public void imageThreeClick(View view) {
		Intent intent = new Intent(); 
		intent.setType("image/*"); 
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), 3); 
    }
	
	public void imageFourClick(View view) {
		Intent intent = new Intent(); 
		intent.setType("image/*"); 
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(Intent.createChooser(intent, "Complete action using"), 4); 
    }
	
	// When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Activity.RESULT_OK) {
    		if (data.getData()!=null) {
    			try {
    				String path = "" + data.getData();
                    if (path.startsWith("content://")) {
                    	doFileParseFromGoogle(path, requestCode, data.getData());
                    } else {
                    	Uri selectedImageUri = data.getData();
    		            BitmapFactory.Options options = new BitmapFactory.Options();
    		            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    		            
    		            if (requestCode==1) {
	    		            imagepathOne = getPath(selectedImageUri);
	    		            Bitmap bitmap=BitmapFactory.decodeFile(imagepathOne, options);
	    		            imageOne.setImageBitmap(bitmap);
    		            } else if (requestCode==2) {
    		            	imagepathTwo = getPath(selectedImageUri);
	    		            Bitmap bitmap=BitmapFactory.decodeFile(imagepathTwo, options);
	    		            imageTwo.setImageBitmap(bitmap);
    		            } else if (requestCode==3) {
    		            	imagepathThree = getPath(selectedImageUri);
	    		            Bitmap bitmap=BitmapFactory.decodeFile(imagepathThree, options);
	    		            imageThree.setImageBitmap(bitmap);
    		            } else if (requestCode==4) {
    		            	imagepathFour = getPath(selectedImageUri);
	    		            Bitmap bitmap=BitmapFactory.decodeFile(imagepathFour, options);
	    		            imageFour.setImageBitmap(bitmap);
    		            }
                    }
    			} catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
    
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    private void doFileParseFromGoogle(String file, int requestCode, Uri selectedImageUri) throws FileNotFoundException {
        Bitmap bitmap = null;
        InputStream is = null;
        is = getContentResolver().openInputStream(Uri.parse(file));
        bitmap = BitmapFactory.decodeStream(is);

        //Resize image
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, mMaxWidth, mMaxHeight), Matrix.ScaleToFit.CENTER);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

        if (bitmap == null) {
        	Toast.makeText(JualActivity.this, "Error", Toast.LENGTH_LONG).show();
        } else {
        	if (requestCode==1) {
		        imagepathOne = getPath(selectedImageUri);
        		imageOne.setImageBitmap(bitmap);
        	} else if (requestCode==2) {
		        imagepathTwo = getPath(selectedImageUri);
        		imageTwo.setImageBitmap(bitmap);
        	} else if (requestCode==3) {
		        imagepathThree = getPath(selectedImageUri);
        		imageThree.setImageBitmap(bitmap);
        	} else if (requestCode==4) {
		        imagepathFour = getPath(selectedImageUri);
        		imageFour.setImageBitmap(bitmap);
        	}
        }
    }
    
	private HttpEntity doFileUpload() {
    	try {
             HttpClient client = new DefaultHttpClient();
             HttpPost post = null;
             File file1, file2, file3, file4;
             FileBody bin1, bin2, bin3, bin4;
             
             MultipartEntity reqEntity = new MultipartEntity();
             if (countImage==1) {
            	 post  = new HttpPost(Referensi.url+"/UploadOne.php");
            	 file1 = new File(imagepathOne);
            	 bin1  = new FileBody(file1);
            	 jsonArray.put(file1.getName());
            	 reqEntity.addPart("uploadedfile1", bin1);
             } else if (countImage==2) {
            	 post  = new HttpPost(Referensi.url+"/UploadTwo.php");
            	 file1 = new File(imagepathOne);
            	 file2 = new File(imagepathTwo);
            	 bin1  = new FileBody(file1);
            	 bin2  = new FileBody(file2);
            	 jsonArray.put(file1.getName());
            	 jsonArray.put(file2.getName());
            	 reqEntity.addPart("uploadedfile1", bin1);
            	 reqEntity.addPart("uploadedfile2", bin2);
             } else if (countImage==3) {
            	 post  = new HttpPost(Referensi.url+"/UploadThree.php");
            	 file1 = new File(imagepathOne);
            	 file2 = new File(imagepathTwo);
            	 file3 = new File(imagepathThree);
            	 bin1  = new FileBody(file1);
            	 bin2  = new FileBody(file2);
            	 bin3  = new FileBody(file3);
            	 jsonArray.put(file1.getName());
            	 jsonArray.put(file2.getName());
            	 jsonArray.put(file3.getName());
            	 reqEntity.addPart("uploadedfile1", bin1);
            	 reqEntity.addPart("uploadedfile2", bin2);
            	 reqEntity.addPart("uploadedfile3", bin3);
             } else if (countImage==4) {
            	 post  = new HttpPost(Referensi.url+"/UploadFour.php");
            	 file1 = new File(imagepathOne);
            	 file2 = new File(imagepathTwo);
            	 file3 = new File(imagepathThree);
            	 file4 = new File(imagepathFour);
            	 bin1  = new FileBody(file1);
            	 bin2  = new FileBody(file2);
            	 bin3  = new FileBody(file3);
            	 bin4  = new FileBody(file4);
            	 jsonArray.put(file1.getName());
            	 jsonArray.put(file2.getName());
            	 jsonArray.put(file3.getName());
            	 jsonArray.put(file4.getName());
            	 reqEntity.addPart("uploadedfile1", bin1);
            	 reqEntity.addPart("uploadedfile2", bin2);
            	 reqEntity.addPart("uploadedfile3", bin3);
            	 reqEntity.addPart("uploadedfile4", bin4);
             }
             
             reqEntity.addPart("user", new StringBody("User"));
             post.setEntity(reqEntity);
             
             HttpResponse response = client.execute(post);
             resEntity = response.getEntity();
             @SuppressWarnings("unused")
			final String response_str = EntityUtils.toString(resEntity);
        } catch (Exception ex) {
        	countImage=0;
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    	return resEntity;
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
        overridePendingTransition(R.anim.no_anim, R.anim.activityslidedown);
    }
}
