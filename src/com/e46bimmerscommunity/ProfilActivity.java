package com.e46bimmerscommunity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.Referensi;
import com.e46bimmerscommunity.utils.callURL;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ProfilActivity extends ActionBarActivity  {
    private TextView lblPictures, lblUsername, lblDescription, lblPhoneNo, lblPlatNo, lblTypeBody, lblTypeMesin, lblAlamat, lblShowMe;
    private Button btnSubmit;
    private EditText txtUsername, txtDescription, txtPhoneNo, txtPlatNo, txtTypeBody, txtTypeMesin, txtAlamat;
    private Typeface fontUbuntuL, fontUbuntuB;
    @SuppressWarnings("unused")
	private String UserId = "", UserName= "";
    private JSONArray str_login  = null;
	private ProgressDialog pDialog;
	private String url = "";
	private CheckBox checkShowMe;
    private SharedPreferences bimmersPref;
    private RequestQueue queue;
    private LinearLayout linView;
    private ProgressBar progressBar;
    private ImageView imageOne, imageTwo, imageThree, imageFour;
    private ImageLoader mImageLoader;
	private String imagepathOne=null, imagepathTwo=null, imagepathThree=null, imagepathFour=null;
	private String pathOne="", pathTwo="", pathThree="", pathFour="";
    private HttpEntity resEntity;
    private int countImage=0;
    private String oldPictures="";
    private String[] splitPic;
    private int mMaxWidth  = 480;
    private int mMaxHeight = 480;
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        setContentView(R.layout.activity_profil);
		setTitle("Profile");
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        queue 		   = Volley.newRequestQueue(this);
        bimmersPref    = getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
        fontUbuntuL    = FontCache.get(ProfilActivity.this, "Ubuntu-L");
		fontUbuntuB    = FontCache.get(ProfilActivity.this, "Ubuntu-B");
		lblPictures    = (TextView) findViewById(R.id.lblPictures);
        lblUsername    = (TextView) findViewById(R.id.lblUsername);
        lblDescription = (TextView) findViewById(R.id.lblDescription);
        lblPhoneNo     = (TextView) findViewById(R.id.lblPhoneNo); 
        lblPlatNo      = (TextView) findViewById(R.id.lblPlatNo); 
        lblTypeBody    = (TextView) findViewById(R.id.lblTypeBody); 
        lblTypeMesin   = (TextView) findViewById(R.id.lblTypeMesin); 
        lblAlamat      = (TextView) findViewById(R.id.lblAlamat);
        txtUsername    = (EditText) findViewById(R.id.txtUsername);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtPhoneNo     = (EditText) findViewById(R.id.txtPhoneNo); 
        txtPlatNo      = (EditText) findViewById(R.id.txtPlatNo); 
        txtTypeBody    = (EditText) findViewById(R.id.txtTypeBody); 
        txtTypeMesin   = (EditText) findViewById(R.id.txtTypeMesin); 
        txtAlamat      = (EditText) findViewById(R.id.txtAlamat);
        btnSubmit      = (Button) findViewById(R.id.btnSubmit);
		UserId         = bimmersPref.getString("UserId", "");
		UserName       = bimmersPref.getString("UserName", "");
		lblShowMe	   = (TextView) findViewById(R.id.lblShowMe);
		checkShowMe    = (CheckBox) findViewById(R.id.checkShowMe);
		linView        = (LinearLayout) findViewById(R.id.linView);
		progressBar    = (ProgressBar) findViewById(R.id.progressBusy);
		imageOne	   = (ImageView) findViewById(R.id.imageOne);
		imageTwo	   = (ImageView) findViewById(R.id.imageTwo);
		imageThree     = (ImageView) findViewById(R.id.imageThree);
		imageFour      = (ImageView) findViewById(R.id.imageFour);
		mImageLoader   = new ImageLoader(ProfilActivity.this);

		lblPictures.setTypeface(fontUbuntuB);
        lblUsername.setTypeface(fontUbuntuB);
        lblDescription.setTypeface(fontUbuntuB);
        lblPhoneNo.setTypeface(fontUbuntuB); 
        lblPlatNo.setTypeface(fontUbuntuB); 
        lblTypeBody.setTypeface(fontUbuntuB); 
        lblTypeMesin.setTypeface(fontUbuntuB); 
        lblAlamat.setTypeface(fontUbuntuB);
        txtUsername.setTypeface(fontUbuntuL); 
        txtDescription.setTypeface(fontUbuntuL);
        txtPhoneNo.setTypeface(fontUbuntuL); 
        txtPlatNo.setTypeface(fontUbuntuL); 
        txtTypeBody.setTypeface(fontUbuntuL); 
        txtTypeMesin.setTypeface(fontUbuntuL); 
        txtAlamat.setTypeface(fontUbuntuL);
        btnSubmit.setTypeface(fontUbuntuB);
        lblShowMe.setTypeface(fontUbuntuL);
        txtUsername.setEnabled(false);
        
        pDialog = new ProgressDialog(ProfilActivity.this);
        pDialog.setMessage("Working...");
        pDialog.setCancelable(false);
        
        btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imagepathOne!=null) {
					countImage = countImage+1;
				} if (imagepathTwo!=null) {
					countImage = countImage+1;
				} if (imagepathThree!=null) {
					countImage = countImage+1;
				} if (imagepathFour!=null) {
					countImage = countImage+1;
				}
				
				if (countImage==0) {
					new updateUserDetail().execute();
				} else {
					new UploadImage().execute();
				}
			}
		});

        String url = Referensi.url+"/getUserDetail.php?UserId="+UserId;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            	try {
                    str_login  = response.getJSONArray("info");
                    int loader = R.drawable.loaderr;
                    
                    for (int i = 0; i < str_login.length(); i++){
                        JSONObject ar = str_login.getJSONObject(i);

                        txtUsername.setText(ar.getString("UserName"));
                        txtDescription.setText(ar.getString("Description"));
                        txtPhoneNo.setText(ar.getString("Phone"));
                        txtPlatNo.setText(ar.getString("PlatNo"));
                        txtTypeBody.setText(ar.getString("TypeBody")); 
                        txtTypeMesin.setText(ar.getString("TypeMesin"));
                        txtAlamat.setText(ar.getString("Alamat"));
                        
                        oldPictures = ar.getString("Pictures");
                        splitPic    = ar.getString("Pictures").split(",");
                        
                        if (splitPic.length!=0) {
	                        for (int j=0; j<splitPic.length; j++) {
	                        	if (j==0) {
	                        		mImageLoader.DisplayImage(Referensi.url+"/pictures/"+splitPic[j], loader, imageOne);
	                        	} else if (j==1) {
	                        		mImageLoader.DisplayImage(Referensi.url+"/pictures/"+splitPic[j], loader, imageTwo);
	                        	} else if (j==2) {
	                        		mImageLoader.DisplayImage(Referensi.url+"/pictures/"+splitPic[j], loader, imageThree);
	                        	} else {
	                        		mImageLoader.DisplayImage(Referensi.url+"/pictures/"+splitPic[j], loader, imageFour);
	                        	}
	                        }
                        }
                        
                        if (ar.getString("isShow").equalsIgnoreCase("1")) {
                        	checkShowMe.setChecked(true);
                        } else {
                        	checkShowMe.setChecked(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            	
            	progressBar.setVisibility(View.GONE);
            	linView.setVisibility(View.VISIBLE);
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
           	 	new updateUserDetail().execute();
            } else {
           	 	countImage=0;
            }
        }
    }
    
    private class updateUserDetail extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            if (splitPic.length!=0) {
	        	if (imagepathOne!=null) {
		        	oldPictures = oldPictures.replace(splitPic[0], pathOne);
	        	} if (imagepathTwo!=null) {
	        		try {
	        			oldPictures = oldPictures.replace(splitPic[1], pathTwo);
	        		} catch (Exception e) {
						oldPictures = oldPictures+","+pathTwo;
					}
	        	} if (imagepathThree!=null) {
	        		try {
	        			oldPictures = oldPictures.replace(splitPic[2], pathThree);
	        		} catch (Exception e) {
	        			oldPictures = oldPictures+","+pathThree;
					}
	        	} if (imagepathFour!=null) {
	        		try {
	        			oldPictures = oldPictures.replace(splitPic[3], pathFour);
	        		} catch (Exception e) {
	        			oldPictures = oldPictures+","+pathFour;
					}
	        	}
        	} else {
        		if (countImage==1) {
	        		oldPictures = pathOne;
	        	} else if (countImage==2) {
	        		oldPictures = pathOne+","+pathTwo;
	        	} else if (countImage==3) {
	        		oldPictures = pathOne+","+pathTwo+","+pathThree;
	        	} else {
	        		oldPictures = pathOne+","+pathTwo+","+pathThree+","+pathFour;
	        	}
        	}
        }
        @Override
        protected String doInBackground(String... params) {
        	try {
        		String _Description = URLEncoder.encode(txtDescription.getText().toString(), "utf-8");
				String _PhoneNo     = URLEncoder.encode(txtPhoneNo.getText().toString(), "utf-8");
				String _PlatNo      = URLEncoder.encode(txtPlatNo.getText().toString(), "utf-8");
				String _TypeBody    = URLEncoder.encode(txtTypeBody.getText().toString(), "utf-8");
	        	String _TypeMesin   = URLEncoder.encode(txtTypeMesin.getText().toString(), "utf-8");
	        	String _Alamat      = URLEncoder.encode(txtAlamat.getText().toString(), "utf-8");
	        	String _isShow;
	        	boolean isChecked = checkShowMe.isChecked();
	        	if (isChecked) {
	        		_isShow = "1";
	        	} else {
	        		_isShow = "0";
	        	}
	        	
	        	url = Referensi.url+"/service.php?ct=ADDNEWUSERDETAIL" +
	        			"&Description="+_Description+
	        			"&Pictures="+oldPictures+
	        			"&Phone="+_PhoneNo+
	        			"&PlatNo="+_PlatNo+
	        			"&TypeBody="+_TypeBody+
	        			"&TypeMesin="+_TypeMesin+
	        			"&Alamat="+_Alamat+
	        			"&isShow="+_isShow+
	        			"&UserId="+UserId;
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
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfilActivity.this);
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Update data succesfully!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	setResult(Activity.RESULT_OK);
                    	dialog.cancel();
                    }
                });
                if (!isFinishing()) { alertDialog.show(); }
            } else {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfilActivity.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Update data error! Please try again or close apps and open again.");
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
    		} else {
    			Toast.makeText(ProfilActivity.this, "Maaf, gambar yang bisa diupload hanya dari Gallery saja!", Toast.LENGTH_LONG).show();
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
        	Toast.makeText(ProfilActivity.this, "Error", Toast.LENGTH_LONG).show();
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
             File file1 = null, file2, file3, file4;
             FileBody bin1 = null, bin2, bin3, bin4;
             
             MultipartEntity reqEntity = new MultipartEntity();
             if (countImage==1) {
            	 post = new HttpPost(Referensi.url+"/UploadOne.php");
            	 if (imagepathOne!=null) {
            		 file1     = new File(imagepathOne);
                	 pathOne   = file1.getName();
            	 } else if (imagepathTwo!=null) {
            		 file1     = new File(imagepathTwo);
                	 pathTwo   = file1.getName();
            	 } else if (imagepathThree!=null) {
            		 file1     = new File(imagepathThree);
                	 pathThree = file1.getName();
            	 } else {
            		 file1     = new File(imagepathFour);
                	 pathFour  = file1.getName();
            	 }

            	 bin1 = new FileBody(file1);
            	 reqEntity.addPart("uploadedfile1", bin1);
             } else if (countImage==2) {
            	 post = new HttpPost(Referensi.url+"/UploadTwo.php");
            	 
            	 if (imagepathOne!=null) {
            		 file1     = new File(imagepathOne);
                	 pathOne   = file1.getName();
            	 } else if (imagepathTwo!=null) {
            		 file1     = new File(imagepathTwo);
                	 pathTwo   = file1.getName();
            	 } else if (imagepathThree!=null) {
            		 file1     = new File(imagepathThree);
                	 pathThree = file1.getName();
            	 } else {
            		 file1     = new File(imagepathFour);
                	 pathFour  = file1.getName();
            	 }
            	 
            	 if (imagepathTwo!=null) {
            		 file2     = new File(imagepathTwo);
                	 pathTwo   = file2.getName();
            	 } else if (imagepathThree!=null) {
            		 file2     = new File(imagepathThree);
                	 pathThree = file2.getName();
            	 } else if (imagepathFour!=null) {
            		 file2     = new File(imagepathFour);
                	 pathFour  = file2.getName();
            	 } else {
            		 file2     = new File(imagepathOne);
                	 pathOne   = file2.getName();
            	 }
            	 
            	 bin1 = new FileBody(file1);
            	 bin2 = new FileBody(file2);
            	 reqEntity.addPart("uploadedfile1", bin1);
            	 reqEntity.addPart("uploadedfile2", bin2);
             } else if (countImage==3) {
            	 post = new HttpPost(Referensi.url+"/UploadThree.php");
            	 
            	 if (imagepathOne!=null) {
            		 file1	   = new File(imagepathOne);
                	 pathOne   = file1.getName();
            	 } else if (imagepathTwo!=null) {
            		 file1 	   = new File(imagepathTwo);
                	 pathTwo   = file1.getName();
            	 } else if (imagepathThree!=null) {
            		 file1 	   = new File(imagepathThree);
                	 pathThree = file1.getName();
            	 } else {
            		 file1 	   = new File(imagepathFour);
                	 pathFour  = file1.getName();
            	 }
            	 
            	 if (imagepathTwo!=null) {
            		 file2     = new File(imagepathTwo);
                	 pathTwo   = file2.getName();
            	 } else if (imagepathThree!=null) {
            		 file2     = new File(imagepathThree);
                	 pathThree = file2.getName();
            	 } else if (imagepathFour!=null) {
            		 file2     = new File(imagepathFour);
                	 pathFour  = file2.getName();
            	 } else {
            		 file2     = new File(imagepathOne);
                	 pathOne   = file2.getName();
            	 }
            	 
            	 if (imagepathThree!=null) {
            		 file3 	   = new File(imagepathThree);
                	 pathThree = file3.getName();
            	 } else if (imagepathFour!=null) {
            		 file3     = new File(imagepathFour);
                	 pathFour  = file3.getName();
            	 } else if (imagepathOne!=null) {
            		 file3 	   = new File(imagepathOne);
                	 pathOne   = file3.getName();
            	 } else {
            		 file3 	   = new File(imagepathTwo);
                	 pathTwo   = file3.getName();
            	 }
            	 
            	 bin1 = new FileBody(file1);
            	 bin2 = new FileBody(file2);
            	 bin3 = new FileBody(file3);
            	 reqEntity.addPart("uploadedfile1", bin1);
            	 reqEntity.addPart("uploadedfile2", bin2);
            	 reqEntity.addPart("uploadedfile3", bin3);
             } else if (countImage==4) {
            	 post = new HttpPost(Referensi.url+"/UploadFour.php");
            	 
            	 if (imagepathOne!=null) {
            		 file1 	   = new File(imagepathOne);
                	 pathOne   = file1.getName();
            	 } else if (imagepathTwo!=null) {
            		 file1 	   = new File(imagepathTwo);
                	 pathTwo   = file1.getName();
            	 } else if (imagepathThree!=null) {
            		 file1 	   = new File(imagepathThree);
                	 pathThree = file1.getName();
            	 } else {
            		 file1 	   = new File(imagepathFour);
                	 pathFour  = file1.getName();
            	 }
            	 
            	 if (imagepathTwo!=null) {
            		 file2 	   = new File(imagepathTwo);
                	 pathTwo   = file2.getName();
            	 } else if (imagepathThree!=null) {
            		 file2 	   = new File(imagepathThree);
                	 pathThree = file2.getName();
            	 } else if (imagepathFour!=null) {
            		 file2 	   = new File(imagepathFour);
                	 pathFour  = file2.getName();
            	 } else {
            		 file2 = new File(imagepathOne);
                	 pathOne   = file2.getName();
            	 }
            	 
            	 if (imagepathThree!=null) {
            		 file3     = new File(imagepathThree);
                	 pathThree = file3.getName();
            	 } else if (imagepathFour!=null) {
            		 file3     = new File(imagepathFour);
                	 pathFour  = file3.getName();
            	 } else if (imagepathOne!=null) {
            		 file3     = new File(imagepathOne);
                	 pathOne   = file3.getName();
            	 } else {
            		 file3 	   = new File(imagepathTwo);
                	 pathTwo   = file3.getName();
            	 }
            	 
            	 if (imagepathFour!=null) {
            		 file4     = new File(imagepathFour);
                	 pathFour  = file4.getName();
            	 } else if (imagepathOne!=null) {
            		 file4     = new File(imagepathOne);
                	 pathOne   = file4.getName();
            	 } else if (imagepathTwo!=null) {
            		 file4     = new File(imagepathTwo);
                	 pathTwo   = file4.getName();
            	 } else {
            		 file4     = new File(imagepathThree);
                	 pathThree = file4.getName();
            	 }
            	 
            	 bin1 = new FileBody(file1);
            	 bin2 = new FileBody(file2);
            	 bin3 = new FileBody(file3);
            	 bin4 = new FileBody(file4);
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
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
	
}