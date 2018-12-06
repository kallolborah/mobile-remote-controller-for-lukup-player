package com.player.widget;

import java.util.ArrayList;
import java.util.HashMap;

import com.player.R;
import com.player.util.CommonUtil;
import com.player.util.Constant;
import com.player.util.ScreenStyles;
import com.player.util.Utils;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScrollList extends BaseAdapter{
	
	Activity mActivity;
	String tag;
	String caller;
	ArrayList<HashMap<String, String>> data;
	private ViewGroup mParent = null;
	
	public ScrollList(Activity activity,ArrayList<HashMap<String, String>> list,String Tag,String Caller){
		if(Constant.DEBUG)  Log.d("ScrollList" ,"ScrollList().Caller: "+Caller+", Tag: "+Tag+" Listdata" + list.size());
		mActivity = activity;
		data = list;
		tag = Tag;
		caller = Caller;
		if(Constant.DEBUG)  Log.d("ScrollList" ,"ScrollList().Caller: "+caller+", Tag: "+tag+" Listdata" + data.size());
	}

	public void addData(ArrayList<HashMap<String,String>>list){
		Log.i("Scrollist", data.size()+"");
		data.addAll(list);
		Log.i("Scrollist after adding",""+ data.size());
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		if(mParent != null){
			if(mParent.getChildAt(position) != null){
				return mParent.getChildAt(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) throws InflateException  {
		try{
			RelativeLayout mainselection;
			View v = null;
			if (convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	            v = inflater.inflate(R.layout.list, parent, false);
	            mainselection=(RelativeLayout)v.findViewById(R.id.Layout);
		    }else{
	        	v=convertView;
	        }
			
			v.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.list_selector));
			
			RelativeLayout image = (RelativeLayout) v.findViewById(R.id.overlaylist_image);
			TextView text = (TextView)v.findViewById(R.id.text);
			TextView title = (TextView)v.findViewById(R.id.overlaylist_title);
			TextView desc = (TextView)v.findViewById(R.id.desc);
			HashMap<String, String> fields = new HashMap<String, String>();
			fields = data.get(position);
			
			if(fields.get(ScreenStyles.LIST_KEY_THUMB_URL) != null){
				String imgUrl = fields.get(ScreenStyles.LIST_KEY_THUMB_URL);
				if(imgUrl != null && !imgUrl.equalsIgnoreCase("")){
					image.setVisibility(View.GONE);
				}else{
					image.setVisibility(View.VISIBLE);
					text.setText("Subscribed");
					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					layoutParams.leftMargin = 10;
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
					title.setGravity(Gravity.CENTER_VERTICAL);
					title.setLayoutParams(layoutParams);
					layoutParams.alignWithParent = true;
				}
			}
			
			if(fields.containsKey(ScreenStyles.LIST_KEY_TITLE)){
				String listTitle = fields.get(ScreenStyles.LIST_KEY_TITLE);
				if(listTitle != null && !listTitle.equalsIgnoreCase("")){
					title.setText(listTitle);
					if(listTitle != null){
						if(listTitle.length() >30){
							String subTitle = listTitle.substring(0, 30);
							subTitle = subTitle+"..." ;
							title.setSingleLine(true);
							title.setText(subTitle);
						}else{
							title.setSingleLine(true);
							title.setText(listTitle);
						}
					}
				}
			}
			
			if(tag.equalsIgnoreCase("event") || tag.equalsIgnoreCase("Reminders") || tag.equalsIgnoreCase("Records")){
				desc.setVisibility(View.VISIBLE);
			}else{
				desc.setVisibility(View.GONE);
			}
			
			if(tag.equalsIgnoreCase("screen")|| tag.equalsIgnoreCase("record")) {
				image.setVisibility(View.GONE);
			}
			
			String Category = "";
			if(fields.containsKey("category")){
				Category = fields.get("category");
			}
			
			String startTime = "";
			if(fields.containsKey("starttime")){
				startTime = fields.get("starttime");
			}
			
			String Desc = "";
			if(fields.containsKey("desc")){
				Desc = fields.get("desc");
			}
			
			
			if(fields.containsKey("servicetype")){
				String serviceType = fields.get("servicetype");
				if(serviceType != null && !serviceType.equalsIgnoreCase("")){
					if(serviceType.equalsIgnoreCase("live")) {
						if(startTime != null && !startTime.equalsIgnoreCase("")){
							desc.setText(startTime);
						}else{
							desc.setVisibility(View.GONE);
						}
						
					}else{
						
						if(Category != null && !Category.equalsIgnoreCase("")){
							desc.setText(Category);
						}else{
							desc.setVisibility(View.GONE);
						}
						String value = isSubscribeItem(fields, tag, caller);
						if(!value.equalsIgnoreCase("unSubscribed")){
							image.setVisibility(View.VISIBLE);
							text.setText(value);
						}else{
							image.setVisibility(View.GONE);
						}
					}
				}else{
					desc.setVisibility(View.GONE);
				}
			}
			
			if(caller.equalsIgnoreCase("Notification")){
				if(Desc != null && !Desc.equalsIgnoreCase("")){
					desc.setText(Desc);
					desc.setVisibility(View.VISIBLE);
				}
				
			}
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	private String isSubscribeItem(HashMap<String, String> fields,String tag,String caller){
		String subscribe = "";
		String price = "";
		String event = "";
		String channelPrice = "";
		String pricingModel = "";
		String serviceType = "";
		
		if(fields.containsKey("servicetype")){
			serviceType = fields.get("servicetype");
		}if(fields.containsKey("subscribe")){
			subscribe = fields.get("subscribe");
		}if(fields.containsKey("price")){
			price = fields.get("price");
		}if(fields.containsKey("channelprice")){
			channelPrice = fields.get("channelprice");
		}if(fields.containsKey("pricingmodel")){
			pricingModel = fields.get("pricingmodel");
		}if(fields.containsKey("event")){
			event = fields.get("event");
		}
		if(Constant.DEBUG)  Log.d("ScrollList" ,"ScrollList().subscribe: "+subscribe+", price: "+price+", channelPrice: "+channelPrice);
		if(caller.equalsIgnoreCase("Guide")){
			if(tag.equalsIgnoreCase("event") || tag.equalsIgnoreCase("service")){
				if(Utils.checkNullAndEmpty(subscribe) && Utils.checkNullAndEmpty(price)){
					if(subscribe.equalsIgnoreCase("true")){
						return "Subscribed";
					}else if (MoneyConverter(price) == 0) {
						return "Free";
					}
				}
			}
		}else if(caller.equalsIgnoreCase("Plan")){
			if(tag.equalsIgnoreCase("Favourites")){
				if(pricingModel.equalsIgnoreCase("PPV")){
					if(Utils.checkNullAndEmpty(subscribe) && Utils.checkNullAndEmpty(price)){
						if(subscribe.equalsIgnoreCase("true") && MoneyConverter(price) != 0){
							return "Subscribed";
						}else if (MoneyConverter(price) == 0) {
							return "Free";
						}
					}
				}else{
					if(Utils.checkNullAndEmpty(subscribe) && Utils.checkNullAndEmpty(price)){
						if(subscribe.equalsIgnoreCase("true") && MoneyConverter(price) != 0){
							return "Subscribed";
						}else if (MoneyConverter(price) == 0) {
							return "Free";
						}
					}
				}
			}
		}else if(caller.equalsIgnoreCase("PlayList") || caller.equalsIgnoreCase("Search")){
			if(tag.equalsIgnoreCase("event")){
				if(pricingModel.equalsIgnoreCase("PPV")){
					if(Utils.checkNullAndEmpty(subscribe) && Utils.checkNullAndEmpty(price)){
						if(subscribe.equalsIgnoreCase("true") && MoneyConverter(price) != 0){
							return "Subscribed";
						}else if (MoneyConverter(price) == 0) {
							return "Free";
						}
					}
				}else if(pricingModel.equalsIgnoreCase("PPC")){
					if(Utils.checkNullAndEmpty(subscribe) && Utils.checkNullAndEmpty(channelPrice)){
						if(subscribe.equalsIgnoreCase("true") && MoneyConverter(channelPrice) != 0){
							return "Subscribed";
						}else if (MoneyConverter(channelPrice) == 0) {
							return "Free";
						}
					}
				}
			}
		}else {
			return "unSubscribed";
		}
		return "unSubscribed";
	}
	
	private int MoneyConverter(String val){
		int cost = 0;
		if(!val.equalsIgnoreCase("")){
			cost = (int) Double.parseDouble(val);
		}
		if(Constant.DEBUG)  Log.d("ScrollList" ,"ScrollList().cost: "+cost);
		return cost;
	}
}