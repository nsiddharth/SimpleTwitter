package com.codepath.apps.simpletwitter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 Tweet tweet = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       View v; // view lookup cache stored in tag
	       if (convertView == null) {
	         
	          LayoutInflater inflater = LayoutInflater.from(getContext());
	          v = inflater.inflate(R.layout.tweet_item, parent, false);
	          
	       } else {
	           v = convertView;
	       }
	       // Populate the data into the template view using the data object
	     
	       ImageView ivUserImage = (ImageView) v.findViewById(R.id.ivProfileImage);
	       
	       TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
	       TextView tvName = (TextView) v.findViewById(R.id.tvUserName);
	       TextView tvRelativeTS = (TextView) v.findViewById(R.id.tvRelativeTS);
	       ivUserImage.setImageResource(android.R.color.transparent);
	       
	       ImageLoader imageLoader = ImageLoader.getInstance();
	       imageLoader.displayImage(tweet.getUser().getProfileImageURL(), ivUserImage);
	       
	       tvBody.setText(tweet.getBody());
	       tvName.setText(tweet.getUser().getName());
	       tvRelativeTS.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
	       
	       // Return the completed view to render on screen
	       return v;
	   
		
		
		//return super.getView(position, convertView, parent);
	}

	
	
}
