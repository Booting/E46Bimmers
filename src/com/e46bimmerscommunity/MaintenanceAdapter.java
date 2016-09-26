package com.e46bimmerscommunity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import com.e46bimmerscommunity.gridview.DynamicHeightImageView;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.Referensi;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class MaintenanceAdapter extends BaseAdapter {
 
    private ActionBarActivity activity;
    private JSONArray jsonArray;
    private static LayoutInflater inflater=null;
    private Typeface fontUbuntuL, fontUbuntuB;
    private ImageLoader mImageLoader;
    
    private final String EVENT_ID   = "MaintenanceId";
    private final String EVENT_NAME = "MaintenanceName";
    private final String USER_NAME  = "UserName";
    private final String EVENT_DESC = "MaintenanceDescription";
    private final String EVENT_IMG  = "MaintenanceImage";
    
    private ArrayList<String> arrayEventId = new ArrayList<String>(),
    		arrayEventName      = new ArrayList<String>(),
    		arrayUserName       = new ArrayList<String>(),
    	    arrayEventDesc      = new ArrayList<String>(),
    	    arrayEventImg       = new ArrayList<String>();
 
    public MaintenanceAdapter(ActionBarActivity a, JSONArray jsonArray) {
        activity 	   = a;
        inflater 	   = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.jsonArray = jsonArray;
        fontUbuntuL    = FontCache.get(activity, "Ubuntu-L");
        fontUbuntuB    = FontCache.get(activity, "Ubuntu-B");
        mImageLoader   = new ImageLoader(activity);
        
        for (int i = 0; i < this.jsonArray.length(); i++) {
        	arrayEventId.add(this.jsonArray.optJSONObject(i).optString(EVENT_ID));
        	arrayUserName.add(this.jsonArray.optJSONObject(i).optString(USER_NAME));
        	arrayEventName.add(this.jsonArray.optJSONObject(i).optString(EVENT_NAME));
        	arrayEventDesc.add(this.jsonArray.optJSONObject(i).optString(EVENT_DESC));
        	try {
        		JSONArray jsonProdImage = new JSONArray(this.jsonArray.optJSONObject(i).optString(EVENT_IMG));
        		arrayEventImg.add(jsonProdImage.toString());
            } catch (JSONException e) {
            	arrayEventImg.add("");
            }
        }
    }
 
    public int getCount() {
        return jsonArray.length();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        if (convertView==null) {
        	convertView = inflater.inflate(R.layout.list_maintenance, null);
 
        	holder = new ViewHolder();
        	holder.relItem    = (RelativeLayout)convertView.findViewById(R.id.relItem);
        	holder.imgPreview = (DynamicHeightImageView)convertView.findViewById(R.id.imgPreview);
        	holder.txtTitle   = (TextView)convertView.findViewById(R.id.txtTitle);
        	holder.txtBy      = (TextView)convertView.findViewById(R.id.txtBy);
        	holder.txtDetail  = (TextView)convertView.findViewById(R.id.txtDetail);
	        convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtTitle.setTypeface(fontUbuntuB);
        holder.txtBy.setTypeface(fontUbuntuL);
        holder.txtDetail.setTypeface(fontUbuntuL);
        
        // Setting all values in listview
        int loader = R.drawable.loaderr;
        try {
            JSONArray jsonArrayImg = new JSONArray(arrayEventImg.get(position));
	        mImageLoader.DisplayImage(Referensi.url+"/pictures/"+jsonArrayImg.get(0).toString().replace(" ", "%20"), loader, holder.imgPreview);
		} catch (JSONException e) {
	        mImageLoader.DisplayImage("", loader, holder.imgPreview);
		}
        
        holder.txtTitle.setText(arrayEventName.get(position));
        holder.txtBy.setText("By "+arrayUserName.get(position));
        holder.txtDetail.setText(arrayEventDesc.get(position));
        
        holder.relItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.startActivity(new Intent(activity, MaintenanceDetailActivity.class).
						putExtra("ItemBy", arrayUserName.get(position)).
						putExtra("ItemName", arrayEventName.get(position)).
            			putExtra("ItemDetail", arrayEventDesc.get(position)).
            			putExtra("ItemPicture", arrayEventImg.get(position)));
			}
		});
        
        return convertView;
    }
        
	public static class ViewHolder {
		DynamicHeightImageView imgPreview;
        TextView txtTitle, txtBy, txtDetail;
        RelativeLayout relItem;
    }
}