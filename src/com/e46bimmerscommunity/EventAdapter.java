package com.e46bimmerscommunity;

import java.util.ArrayList;
import org.json.JSONArray;

import com.e46bimmerscommunity.gridview.DynamicHeightImageView;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.Referensi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class EventAdapter extends BaseAdapter {
 
    private ActionBarActivity activity;
    private JSONArray jsonArray;
    private static LayoutInflater inflater=null;
    private Typeface fontUbuntuB;
    private ImageLoader mImageLoader;
    
    private final String EVENT_ID        = "EventId";
    private final String EVENT_NAME      = "EventName";
    private final String EVENT_DATE      = "EventDate";
    private final String EVENT_TIME      = "EventTime";
    private final String EVENT_ADDRESS   = "EventAddress";
    private final String EVENT_DESC      = "EventDesc";
    private final String EVENT_LATITUDE  = "EventLatitude";
    private final String EVENT_LONGITUDE = "EventLongitude";
    
    private ArrayList<String> arrayEventId = new ArrayList<String>(),
    		arrayEventName      = new ArrayList<String>(),
    		arrayEventDate      = new ArrayList<String>(),
    	    arrayEventTime      = new ArrayList<String>(),
    	    arrayEventAddress   = new ArrayList<String>(),
    	    arrayEventDesc      = new ArrayList<String>(),
    	    arrayEventLatitude  = new ArrayList<String>(),
    	    arrayEventLongitude = new ArrayList<String>();
 
    public EventAdapter(ActionBarActivity a, JSONArray jsonArray) {
        activity 	   = a;
        inflater 	   = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fontUbuntuB    = FontCache.get(activity, "Ubuntu-B");
        this.jsonArray = jsonArray;
        mImageLoader   = new ImageLoader(activity);
        
        for (int i = 0; i < this.jsonArray.length(); i++) {
        	arrayEventId.add(this.jsonArray.optJSONObject(i).optString(EVENT_ID));
        	arrayEventName.add(this.jsonArray.optJSONObject(i).optString(EVENT_NAME));
        	arrayEventDate.add(this.jsonArray.optJSONObject(i).optString(EVENT_DATE));
        	arrayEventTime.add(this.jsonArray.optJSONObject(i).optString(EVENT_TIME));
        	arrayEventAddress.add(this.jsonArray.optJSONObject(i).optString(EVENT_ADDRESS));
        	arrayEventDesc.add(this.jsonArray.optJSONObject(i).optString(EVENT_DESC));
        	arrayEventLatitude.add(this.jsonArray.optJSONObject(i).optString(EVENT_LATITUDE));
        	arrayEventLongitude.add(this.jsonArray.optJSONObject(i).optString(EVENT_LONGITUDE));
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
	public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        if (convertView==null) {
        	convertView = inflater.inflate(R.layout.list_event_new, null);
 
        	holder = new ViewHolder();
        	holder.txtDate  = (TextView) convertView.findViewById(R.id.txtDate);
        	holder.txtMenu  = (TextView) convertView.findViewById(R.id.txtMenu);
        	//holder.imgMaps  = (DynamicHeightImageView) convertView.findViewById(R.id.imgMaps);
        	
	        convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtDate.setTypeface(fontUbuntuB);
        holder.txtMenu.setTypeface(fontUbuntuB);
        
        // Setting all values in listview
        holder.txtDate.setText(arrayEventDate.get(position));
        holder.txtMenu.setText(arrayEventName.get(position));
        
        int loader = R.drawable.loaderr;
        mImageLoader.DisplayImage("http://maps.google.com/maps/api/staticmap?center="+arrayEventLatitude.get(position)+","+arrayEventLongitude.get(position)+"&zoom=15&size=200x500&sensor=false", loader, holder.imgMaps);
        
        if (position!=0) {
        	if (arrayEventDate.get(position).equalsIgnoreCase(arrayEventDate.get(position-1))) {
        		holder.txtDate.setVisibility(View.GONE);
        	} else {
        		holder.txtDate.setVisibility(View.VISIBLE);
        	}
        }
        
        return convertView;
    }
        
	public static class ViewHolder {
        TextView txtDate, txtMenu;
        DynamicHeightImageView imgMaps;
    }
}