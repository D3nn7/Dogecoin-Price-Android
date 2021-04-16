package com.danny.cryptkurs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class OfflineActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline);

		RelativeLayout ReloadBTN = (RelativeLayout)findViewById(R.id.reloadBTN);

		ReloadBTN.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent retryOnline = new Intent(v.getContext(), MainActivity.class);
				startActivity(retryOnline);
			}
		});
	}
}