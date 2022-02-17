package com.danny.cryptkurs.updater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;

public class Updater
{
	public void checkForUpdates(String appVersion, Context context){
		String updateUrl = "https://github.com/D3nn7/Dogecoin-Price-Android/blob/main/update.json";

		//make request
		StringRequest request = new StringRequest(updateUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String string) {
				JSONObject object;
				try
				{
					object = new JSONObject(string);
					String name = object.getString("name");
					String version = object.getString("version");
					String date = object.getString("date");
					String updateUrl = object.getString("update-url");
					String playstoreUrl = object.getString("playstore-url");
					boolean isCriticalUpdate = object.getBoolean("critical-update");
					StringBuilder changes = new StringBuilder();

					String previewMessage;

					JSONArray changesArray = object.getJSONArray("changes");

					for (int i = 0; i < changesArray.length(); i++) {
						String change = changesArray.getString(i);
						changes.append("- ").append(change).append("\n");
					}

					if (!version.equals(appVersion)) {
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("Update");
						if (isCriticalUpdate) {
							alertDialog.setCancelable(false);
							previewMessage = "there is an critical update available for the app.";
						} else {
							previewMessage = "there is an update available for the app.";
						}
						alertDialog.setMessage(previewMessage + "\n" +
								"\n" +
								name + "\n" +
								"Version: " + version + "\n" +
								"Update date: " + date + "\n" +
								"\n" +
								"Changelog: " + "\n" +
								changes);
						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "APK update",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										updateApp(context, updateUrl);
									}
								});
						alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "PLAYSTORE",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										updateApp(context, playstoreUrl);
									}
								});
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
	}

	private void updateApp(Context context, String updateUrl) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(updateUrl));
		context.startActivity(i);
	}
}
