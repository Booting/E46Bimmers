package com.e46bimmerscommunity;

import java.util.ArrayList;
import org.json.JSONArray;
import com.e46bimmerscommunity.utils.CircleImageView;
import com.e46bimmerscommunity.utils.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
    private static LayoutInflater mInflater = null;
    private Context context;
	private Typeface fontUbuntuL;
    private ImageLoader imgLoader;
    private JSONArray jsonArray;
    
    private final String COMMENTID    = "CommentId";
    private final String USERID 	  = "UserId";
    private final String USERNAME 	  = "UserName";
    private final String WHOCOMMENTID = "WhoCommentId";
    private final String COMMENTTEXT  = "CommentText";
    
    private ArrayList<String> arrayCommentId = new ArrayList<String>(),
    		arrayUserId       = new ArrayList<String>(),
    		arrayUserName     = new ArrayList<String>(),
    		arrayWhoCommentId = new ArrayList<String>(),
    	    arrayCommentText  = new ArrayList<String>();
    
    public CommentAdapter(Context mContext, JSONArray mJsonArray) {
        context     = mContext;
        mInflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fontUbuntuL = FontCache.get(context, "Ubuntu-L");
        imgLoader   = new ImageLoader(mContext);
        jsonArray   = mJsonArray;
        
        for (int i=0; i<this.jsonArray.length(); i++) {
        	arrayCommentId.add(this.jsonArray.optJSONObject(i).optString(COMMENTID));
        	arrayUserId.add(this.jsonArray.optJSONObject(i).optString(USERID));
        	arrayUserName.add(this.jsonArray.optJSONObject(i).optString(USERNAME));
        	arrayWhoCommentId.add(this.jsonArray.optJSONObject(i).optString(WHOCOMMENTID));
        	arrayCommentText.add(this.jsonArray.optJSONObject(i).optString(COMMENTTEXT));
        }
    }

    public static class ViewHolder {
        public TextView txtUser, txtComment;
        public CircleImageView profileImage;
    }

    @Override
    public int getCount() {
    	return jsonArray.length();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({ "InflateParams" })
	@Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_comment, null);

            holder 		      	= new ViewHolder();
            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.profileImage);
            holder.txtUser     	= (TextView) convertView.findViewById(R.id.txtUser);
            holder.txtComment   = (TextView) convertView.findViewById(R.id.txtComment);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        int loader = R.drawable.loaderr;
        holder.profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);   
        imgLoader.DisplayImage("https://graph.facebook.com/"+arrayWhoCommentId.get(position)+"/picture?type=normal", loader, holder.profileImage);
	    
        holder.txtUser.setTypeface(fontUbuntuL);
        holder.txtComment.setTypeface(fontUbuntuL);
        holder.txtUser.setText(arrayUserName.get(position));
        holder.txtComment.setText(arrayCommentText.get(position));

        return convertView;
    }
    
}
