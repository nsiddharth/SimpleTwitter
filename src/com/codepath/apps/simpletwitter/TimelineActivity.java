package com.codepath.apps.simpletwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;


public class TimelineActivity extends Activity {
	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		populateTimeline("0");
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this,  tweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// TODO Auto-generated method stub
				
				loadMoreTweets(page);
			}
		});
		
	}
	
	public void loadMoreTweets(int page){
		//int nextPage = page*20;
		//String since_id = String.valueOf(nextPage);
		
		Tweet last_tweet=tweets.get(tweets.size()-1);
		String last_id = String.valueOf(last_tweet.getUid());
		
		populateTimeline(last_id);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		 getMenuInflater().inflate(R.menu.menu_compose, menu);
		return super.onCreateOptionsMenu(menu);
		
		
	}

	
	

	public void onCompose(MenuItem mi){
		
		  final AlertDialog.Builder tweetDialog = new AlertDialog.Builder(this);
	        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

	        final View layout = inflater.inflate(R.layout.tweet_dialog,
	                (ViewGroup) findViewById(R.id.layout_root));
	        
	        tweetDialog.setView(layout);
	        tweetDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

	            public void onClick(DialogInterface dialog, int which) {
	            	
	            	EditText text = (EditText) layout.findViewById(R.id.etPostTweet);
	            	
	            	if(text.length() > 140)
	            	{
	            		Toast.makeText(tweetDialog.getContext(), "more than 140 characters", Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	            		client.postTweet(new JsonHttpResponseHandler(){
	            			
	            			@Override
	            			public void onSuccess(JSONObject json) {
	            				// TODO Auto-generated method stub
	            				Log.d("debug", json.toString());
	            				super.onSuccess(json);
	            				aTweets.insert(Tweet.fromJSON(json), 0);
	            			}
	            			
	            			@Override
	            			public void onFailure(Throwable arg0, String arg1) {
	            				// TODO Auto-generated method stub
	            				super.onFailure(arg0, arg1);
	            				Log.d("debug", arg0.toString());
	            			}
	            			
	            		}, text.getText().toString());
	            	}
	            	
	                dialog.dismiss();
	            }

	        });


	        tweetDialog.create();
	        tweetDialog.show();     
		
	}

	public void populateTimeline(String max_id){
		
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray json) {
				// TODO Auto-generated method stub
				super.onSuccess(json);
				aTweets.addAll(Tweet.fromJSONArray(json));
			}
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				Toast.makeText(TimelineActivity.this, "Failure ", Toast.LENGTH_SHORT);
				Log.d("debug", arg0.toString());
			}
		},  max_id, 10);
		
	}
}
