package com.danny.cryptkurs.updater;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.danny.cryptkurs.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;

public class Updater
{
	public JSONObject checkForUpdates(String appVersion, Context context){
		String updateUrl = "https://dogecoin.schapeit.com/update.json";

		//make request
		StringRequest request = new StringRequest(updateUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String string) {
				JSONObject object = null;
				try
				{
					object = new JSONObject(string);
					String name = object.getString("name");
					String version = object.getString("version");
					String date = object.getString("date");
					String updateUrl = object.getString("update-url");
					ArrayList<JSONObject> versionChanges = new ArrayList<JSONObject>();
					StringBuilder changes = new StringBuilder();

					JSONArray changesArray = object.getJSONArray("changes");

					for (int i = 0; i < changesArray.length(); i++) {
						String change = changesArray.getString(i);
						changes.append("- ").append(change).append("\n");
					}

					if (!version.equals(appVersion)) {
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("Update");
						alertDialog.setMessage("there is an update available for the app. To continue using the app, you need to update it:\n" +
								"\n" +
								name + "\n" +
								"Version: " + version + "\n" +
								"Update date: " + date + "\n" +
								"\n" +
								"Changelog: " + "\n" +
								changes);
						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "UPDATE NOW",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										updateApp(context, updateUrl);
									}
								});
						alertDialog.setCancelable(false);
						alertDialog.show();
					} else {
						Log.e(String.valueOf(context), "[Updater]: no app-update available");
					}
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e(String.valueOf(context), "can't look for updates...");
			}
		});

		RequestQueue rQueue = Volley.newRequestQueue(context);
		rQueue.add(request);

		return null;
	}

	private void updateApp(Context context, String updateUrl) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(updateUrl));
		context.startActivity(i);
	}
}
