package com.danny.cryptkurs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		ImageView WebsiteBTN =(ImageView)findViewById(R.id.website);
		ImageView GithubBTN =(ImageView)findViewById(R.id.github);
		ImageView InstagramBTN =(ImageView)findViewById(R.id.instagram);
		ImageView PaypalBTN =(ImageView)findViewById(R.id.paypal);

		TextView LinkFontAwesome =(TextView) findViewById(R.id.fontawesome);
		TextView LinkVolley =(TextView) findViewById(R.id.volley);
		TextView LinkBinance =(TextView) findViewById(R.id.binance);
		TextView LinkDogecoin =(TextView) findViewById(R.id.dogecoin);

		WebsiteBTN.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://danny.schapeit.com";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		GithubBTN.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://github.com/D3nn7";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		InstagramBTN.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://www.instagram.com/d3nn7.me/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		PaypalBTN.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://paypal.me/dannyschapeit";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		LinkFontAwesome.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://fontawesome.com/license";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		LinkVolley.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://github.com/google/volley/blob/master/LICENSE";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		LinkBinance.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://www.binance.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		LinkDogecoin.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				String url = "https://dogecoin.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		TextView versionName = (TextView) findViewById(R.id.appVersion);
		versionName.setText(BuildConfig.VERSION_NAME);
	}
}
