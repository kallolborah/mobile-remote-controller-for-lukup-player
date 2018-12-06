package com.player.widget;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.player.R;
import com.player.util.Constant;

public class ConnectionSetupAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<String> mlist;
	private ArrayList<String> name;
	private static LayoutInflater inflater=null;
	private Typeface font = null;
	private ArrayList<HashMap<String, String>> wifimap;
	private String type;
	
	public ConnectionSetupAdapter(Context context , ArrayList<String>address , ArrayList<String> name,String type) {
		this.mContext = context;
		this.mlist = address;
		this.name = name;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
		font = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf"); 
		if(Constant.DEBUG)Log.d("ListViewAdapter","ConnectionSetupAdapter()Setadapter");
	}
	
	public  ConnectionSetupAdapter(Context context , ArrayList<HashMap<String, String>> map, String type) {
		mContext = context;
		wifimap = map ;
		this.type = type;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		if(type.equalsIgnoreCase("BT")){
			return mlist.size();
		}else {
			if(wifimap != null)
				return wifimap.size();
			else return 0;
		}
		
	}

	
	
	@Override
	public String getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void addDevice(String device , String name){
		if(!mlist.contains(device)){
			mlist.add(device);
			this.name.add(name);
			notifyDataSetChanged();
		}
	}
	
	public void addwifiDevice(ArrayList<HashMap<String, String>>map){
		if(map != null){
			wifimap = map;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder = null;
		if(convertView==null){
			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.simplelist, null);
			ImageView list_image = (ImageView)vi.findViewById(R.id.list_image);
			list_image.setVisibility(View.GONE);
			holder.title = (TextView)vi.findViewById(R.id.title); // title
			vi.setTag(holder);
			vi.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_selector));
			
		}else{
			holder = (ViewHolder)vi.getTag();
		}
		if(type.equalsIgnoreCase("BT")){
			if(holder.title != null){
				holder.title.setTag("title");
			}

			holder.title.setTypeface(font);
			holder.title.setSingleLine(true);
			holder.title.setTextColor(Color.parseColor("#FFFFFF"));
			holder.title.setText(name.get(position)+"\n"+ getItem(position));
		}else if(type.equalsIgnoreCase("wifi")){
			if(holder.title != null){
				holder.title.setTag("title");
			}
			holder.title.setTypeface(font);
			holder.title.setSingleLine(true);
			holder.title.setTextColor(Color.parseColor("#FFFFFF"));
			holder.title.setText(wifimap.get(position).get("address"));
		}
	
		return vi;
	}

	static class ViewHolder {
		TextView title ;
	
	}


}
