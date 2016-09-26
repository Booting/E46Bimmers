package com.e46bimmerscommunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import com.e46bimmerscommunity.gridview.DynamicHeightImageView;
import com.e46bimmerscommunity.utils.ImageLoader;
import com.e46bimmerscommunity.utils.Referensi;
import java.util.ArrayList;

public class JualBeliAdapter extends BaseAdapter {

    private final String USERID 	  = "UserId";
    private final String USERNAME 	  = "UserName";
    private final String CATEGORINAME = "CategoriName";
    private final String ITEMNAME     = "ItemName";
    private final String DESKRIPSI    = "Deskripsi";
    private final String HARGA 		  = "Harga";
    private final String STOCK 		  = "Stock";
    private final String LOKASI 	  = "Lokasi";
    private final String CONTACT 	  = "Contact";
    private final String PICTURE 	  = "Picture";

    private ArrayList<String> arrayUserId = new ArrayList<String>(),
            arrayUserName     = new ArrayList<String>(),
            arrayCategoriName = new ArrayList<String>(),
            arrayItemName     = new ArrayList<String>(),
            arrayDeskripsi    = new ArrayList<String>(),
            arrayHarga 		  = new ArrayList<String>(),
            arrayStock        = new ArrayList<String>(),
            arrayLokasi       = new ArrayList<String>(),
            arrayContact      = new ArrayList<String>(),
            arrayPicture      = new ArrayList<String>();

    private final LayoutInflater mLayoutInflater;
    private final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private ImageLoader mImageLoader;
    public JSONArray jsonItemList;
    private Context _context;
    private ArrayList<Double> arrayImageHeight = new ArrayList<Double>();
    private Typeface fontUbuntuL, fontUbuntuB;
    private Activity activity;
    
    public JualBeliAdapter(Context context, JSONArray jsonItemList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.jsonItemList    = jsonItemList;
        mImageLoader         = new ImageLoader(context);
        _context             = context;
        fontUbuntuL 		 = FontCache.get(context, "Ubuntu-L");
		fontUbuntuB 		 = FontCache.get(context, "Ubuntu-B");
		activity			 = (Activity) _context;
        
        for (int i = 0; i < this.jsonItemList.length(); i++) {
        	arrayUserId.add(this.jsonItemList.optJSONObject(i).optString(USERID));
        	arrayUserName.add(this.jsonItemList.optJSONObject(i).optString(USERNAME));
        	arrayCategoriName.add(this.jsonItemList.optJSONObject(i).optString(CATEGORINAME));
        	arrayItemName.add(this.jsonItemList.optJSONObject(i).optString(ITEMNAME));
        	arrayDeskripsi.add(this.jsonItemList.optJSONObject(i).optString(DESKRIPSI));
        	arrayHarga.add(this.jsonItemList.optJSONObject(i).optString(HARGA));
        	arrayStock.add(this.jsonItemList.optJSONObject(i).optString(STOCK));
        	arrayLokasi.add(this.jsonItemList.optJSONObject(i).optString(LOKASI));
        	arrayContact.add(this.jsonItemList.optJSONObject(i).optString(CONTACT));
        	try {
        		JSONArray jsonProdImage = new JSONArray(this.jsonItemList.optJSONObject(i).optString(PICTURE));
        		arrayPicture.add(imageURLwithRatio(jsonProdImage.toString()));
            } catch (JSONException e) {
            	arrayPicture.add("");
            }
        }
    }
    
    private String imageURLwithRatio(String imageName) throws NumberFormatException {
        arrayImageHeight.add(1.0);
        return imageName;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void refreshData() {
        for (int i = arrayUserId.size(); i < this.jsonItemList.length(); i++) {
        	arrayUserId.add(this.jsonItemList.optJSONObject(i).optString(USERID));
        	arrayUserName.add(this.jsonItemList.optJSONObject(i).optString(USERNAME));
        	arrayCategoriName.add(this.jsonItemList.optJSONObject(i).optString(CATEGORINAME));
        	arrayItemName.add(this.jsonItemList.optJSONObject(i).optString(ITEMNAME));
        	arrayDeskripsi.add(this.jsonItemList.optJSONObject(i).optString(DESKRIPSI));
        	arrayHarga.add(this.jsonItemList.optJSONObject(i).optString(HARGA));
        	arrayStock.add(this.jsonItemList.optJSONObject(i).optString(STOCK));
        	arrayLokasi.add(this.jsonItemList.optJSONObject(i).optString(LOKASI));
        	arrayContact.add(this.jsonItemList.optJSONObject(i).optString(CONTACT));
        	try {
                JSONArray jsonProdImage = new JSONArray(this.jsonItemList.optJSONObject(i).optString(PICTURE));
                arrayPicture.add(imageURLwithRatio(jsonProdImage.optString(i)));
            } catch (JSONException e) {
            	arrayPicture.add("");
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jsonItemList.length();
    }

    @Override
    public Object getItem(int position) {
        return this.jsonItemList.optString(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_list_cell, parent, false);
            vh = new ViewHolder();

            vh.relItem		= (LinearLayout) convertView.findViewById(R.id.relItem);
            vh.imgItem      = (DynamicHeightImageView) convertView.findViewById(R.id.imgItem);
            vh.txtItemName  = (TextView) convertView.findViewById(R.id.txtItemName);
            vh.txtItemPrice = (TextView) convertView.findViewById(R.id.txtItemPrice);
            vh.txtSeller    = (TextView) convertView.findViewById(R.id.txtSeller);
            vh.txtStock     = (TextView) convertView.findViewById(R.id.txtStock);
            vh.txtDeskripsi = (TextView) convertView.findViewById(R.id.txtDeskripsi);
            vh.txtLocation  = (TextView) convertView.findViewById(R.id.txtLocation);
            vh.txtKategori  = (TextView) convertView.findViewById(R.id.txtKategori);
            vh.txtContact	= (TextView) convertView.findViewById(R.id.txtContact);

            vh.txtItemName.setTypeface(fontUbuntuB);
            vh.txtItemPrice.setTypeface(fontUbuntuL);
            vh.txtSeller.setTypeface(fontUbuntuL);
            vh.txtStock.setTypeface(fontUbuntuL);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        vh.imgItem.setHeightRatio(positionHeight);
        
        JSONArray jArray = null;
        try {
            int loader = R.drawable.loaderr;
			jArray = new JSONArray(arrayPicture.get(position));
	        mImageLoader.DisplayImage(Referensi.url+"/pictures/"+jArray.get(0).toString().replace(" ", "%20"), loader, vh.imgItem);
		} catch (JSONException e) { e.printStackTrace(); }
        
        vh.txtItemPrice.setText("Rp "+Referensi.currencyFormater(Double.parseDouble(arrayHarga.get(position).replace("Rp ", ""))).replace(",", "."));
        vh.txtItemName.setText(arrayItemName.get(position));
        vh.txtSeller.setText(arrayUserName.get(position));
        vh.txtStock.setText(arrayStock.get(position));
        vh.txtDeskripsi.setText(arrayDeskripsi.get(position));
        vh.txtLocation.setText(arrayLokasi.get(position));
        vh.txtKategori.setText(arrayCategoriName.get(position));
        vh.txtContact.setText(arrayContact.get(position));
        
        vh.relItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				activity.startActivity(new Intent(activity, JualBeliDetailActivity.class).
						putExtra("ItemPicture", arrayPicture.get(position)).
						putExtra("ItemName", vh.txtItemName.getText().toString()).
                		putExtra("ItemPrice", vh.txtItemPrice.getText().toString()).
		        		putExtra("ItemSeller", vh.txtSeller.getText().toString()).
		        		putExtra("ItemStock", vh.txtStock.getText().toString()).
		        		putExtra("ItemLocation", vh.txtLocation.getText().toString()).
		        		putExtra("ItemKategori", vh.txtKategori.getText().toString()).
		        		putExtra("ItemContact", vh.txtContact.getText().toString()).
						putExtra("ItemDeskripsi", vh.txtDeskripsi.getText().toString()));
			}
		});
        
        return convertView;
    }

    static class ViewHolder {
    	LinearLayout relItem;
        DynamicHeightImageView imgItem;
        TextView txtItemName;
        TextView txtItemPrice;
        TextView txtSeller;
        TextView txtStock;
        TextView txtDeskripsi;
        TextView txtLocation;
        TextView txtKategori;
        TextView txtContact;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getImageHeightRatio(position);
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getImageHeightRatio(int position) {
        double ratio = (arrayImageHeight.get(position) / 300.0) / 2.0;
        if (ratio > 1.5)
            ratio = 1.1;
        if (ratio < 1) {
            ratio = 0.8;
        }
        return ratio;
    }

}