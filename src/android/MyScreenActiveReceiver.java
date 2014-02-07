package de.vogella.android.receiver.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyScreenActiveReceiver extends BroadcastReceiver {

  private static final String LOG_TAG = "MyScreenActiveReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle extras = intent.getExtras();
    if (extras != null) {
      Log.d(LOG_TAG, "There are extras");
    } else {
      Log.d(LOG_TAG, "No extras");
    }
  }
} 