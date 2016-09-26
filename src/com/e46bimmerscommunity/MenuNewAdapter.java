package com.e46bimmerscommunity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.e46bimmerscommunity.utils.Referensi;

public class MenuNewAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private Context _context;
    private Typeface fontUbuntuB;
    private Activity activity;
	private boolean isAMember;
    private SharedPreferences bimmersPref;
    
    public MenuNewAdapter(Context context, boolean mIsAMember) {
        this.mLayoutInflater = LayoutInflater.from(context);
        _context             = context;
		fontUbuntuB 		 = FontCache.get(context, "Ubuntu-B");
		activity			 = (Activity) _context;
		isAMember			 = mIsAMember;
        bimmersPref 		 = activity.getSharedPreferences(Referensi.PREF_NAME, Activity.MODE_PRIVATE);
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.menu_list_cell, parent, false);
            vh = new ViewHolder();

            vh.imgMenu = (ImageView) convertView.findViewById(R.id.imgMenu);
            vh.txtMenu = (TextView) convertView.findViewById(R.id.txtMenu);
            vh.relItem = (RelativeLayout) convertView.findViewById(R.id.relItem);
            
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.txtMenu.setTypeface(fontUbuntuB);

        if (position==0) {
        	vh.imgMenu.setImageResource(R.drawable.menu_one);
        	vh.txtMenu.setText("PROFILE MEMBER");
        } else if (position==1) {
        	vh.imgMenu.setImageResource(R.drawable.menu_four);
        	vh.txtMenu.setText("BMW PARTS & SERVICE");
        } else if (position==2) {
        	vh.imgMenu.setImageResource(R.drawable.menu_five);
        	vh.txtMenu.setText("FIND FRIEND LOCATION");
        } else if (position==3) {
        	vh.imgMenu.setImageResource(R.drawable.menu_three);
        	vh.txtMenu.setText("MAINTENANCE INFO");
        } else if (position==4) {
        	vh.imgMenu.setImageResource(R.drawable.menu_two);
        	vh.txtMenu.setText("BULLETIN BOARD / EVENT");
        } else if (position==5) {
        	vh.imgMenu.setImageResource(R.drawable.menu_six);
        	vh.txtMenu.setText("BUY & SELL FORUM");
        } else if (position==6) {
        	vh.imgMenu.setImageResource(R.drawable.menu_seven);
        	vh.txtMenu.setText("ABOUT");
        } else if (position==7) {
        	vh.imgMenu.setImageResource(R.drawable.menu_eight);
        	vh.txtMenu.setText("EXIT");
        }
        
        vh.relItem.setOnClickListener(new OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (position==0) {
					activity.startActivityForResult(new Intent(activity, ProfilActivity.class), 2);
				} else if (position==1) {
					if (isAMember) {
						activity.startActivity(new Intent(activity, MainActivity.class));
					} else {
						showNotification();
					}
				} else if (position==2) {
					if (isAMember) {
						activity.startActivity(new Intent(activity, FindFriendLocationActivity.class));
					} else {
						showNotification();
					}
				} else if (position==3) {
					if (isAMember) {
						activity.startActivity(new Intent(activity, MaintenanceActivity.class));
					} else {
						showNotification();
					}
				} else if (position==4) {
					if (isAMember) {
						activity.startActivity(new Intent(activity, EventActivity.class));
					} else {
						showNotification();
					}
				} else if (position==5) {
					if (isAMember) {
						activity.startActivity(new Intent(activity, JualBeliSyaratActivity.class));
					} else {
						showNotification();
					}
				} else if (position==6) {
					activity.startActivity(new Intent(activity, AboutActivity.class));
				} else {
					SharedPreferences.Editor editor = bimmersPref.edit();
					editor.putString("UserId", "");
					editor.putString("UserName", "");
					editor.putString("Email", "");
					editor.commit();
					
					Intent intent = new Intent(activity, LoginActivity.class);
					activity.startActivity(intent);
					activity.finish();
				}
			}
		});
        
        return convertView;
    }
    
    public void showNotification() {
    	AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Sorry");
        alertDialog.setMessage("Please fill your data in profile first. Thanks!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        if (!activity.isFinishing()) { alertDialog.show(); }
    }

    static class ViewHolder {
    	ImageView imgMenu;
        TextView txtMenu;
        RelativeLayout relItem;
    }

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}