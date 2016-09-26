package com.e46bimmerscommunity;

import java.util.ArrayList;
import org.json.JSONArray;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class LazyAdapter extends BaseAdapter {
 
    private ActionBarActivity activity;
    private JSONArray jsonArray;
    private static LayoutInflater inflater=null;
    private Typeface fontHabibi, fontOldStandardBold;
    
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
 
    public LazyAdapter(ActionBarActivity a, JSONArray jsonArray) {
        activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontOldStandardBold = FontCache.get(a, "OldStandard-Bold");
        this.jsonArray = jsonArray;
        fontHabibi     = FontCache.get(a, "OldStandard-Regular");
        
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
 
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
        if (convertView==null) {
        	convertView = inflater.inflate(R.layout.list_event, null);
 
        	holder = new ViewHolder();
        	holder.txtId 		= (TextView)convertView.findViewById(R.id.txtId);
        	holder.txtTitle 	= (TextView)convertView.findViewById(R.id.txtTitle);
        	holder.txtDateTime  = (TextView)convertView.findViewById(R.id.txtDateTime);
        	holder.txtAlamat 	= (TextView)convertView.findViewById(R.id.txtAlamat);
        	holder.txtDeskripsi = (TextView)convertView.findViewById(R.id.txtDeskripsi);
        	holder.txtDate		= (TextView)convertView.findViewById(R.id.txtDate);
        	holder.txtLatitude	= (TextView)convertView.findViewById(R.id.txtLatitude);
        	holder.txtLongitude	= (TextView)convertView.findViewById(R.id.txtLongitude);
	        convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        holder.txtId.setTypeface(fontHabibi);
        holder.txtTitle.setTypeface(fontOldStandardBold);
        holder.txtDateTime.setTypeface(fontHabibi);
        holder.txtAlamat.setTypeface(fontHabibi);
        holder.txtDeskripsi.setTypeface(fontHabibi);
        holder.txtDate.setTypeface(fontOldStandardBold);
        
        // Setting all values in listview
        holder.txtId.setText(arrayEventId.get(position));
        holder.txtTitle.setText(arrayEventName.get(position));
        holder.txtDateTime.setText("Jam: "+arrayEventTime.get(position));
        holder.txtAlamat.setText("Lokasi: "+arrayEventAddress.get(position));
        holder.txtDeskripsi.setText(arrayEventDesc.get(position));
        holder.txtDate.setText(arrayEventDate.get(position));
        holder.txtLatitude.setText(arrayEventLatitude.get(position));
        holder.txtLongitude.setText(arrayEventLongitude.get(position));
        
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
		TextView txtId;
        TextView txtTitle;
        TextView txtDateTime;
        TextView txtAlamat;
        TextView txtDeskripsi;
        TextView txtDate;
        TextView txtLatitude;
        TextView txtLongitude;
    }
}