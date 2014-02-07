/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package org.apache.cordova.systemevents;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class Listener extends CordovaPlugin {

    private static final String LOG_TAG = "Listener";

    private CallbackContext connectionCallbackContext;
    private boolean registered = false;

    ConnectivityManager sockMan;
    BroadcastReceiver receiver;
    private String lastStatus = "";

    /**
     * Constructor.
     */
    public Listener() {
        this.receiver = null;
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        //this.sockMan = (ConnectivityManager) cordova.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //this.connectionCallbackContext = null;

        // We need to listen to connectivity events to update navigator.connection
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        if (this.receiver == null) {
            this.receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // (The null check is for the ARM Emulator, please use Intel Emulator for better results)
                    String action = intent.getAction();
                    if(action.equals(Intent.ACTION_USER_PRESENT))                        
                        //updateConnectionInfo(sockMan.getActiveNetworkInfo());
                         Log.d(LOG_TAG, "Intent received: " + Intent.ACTION_USER_PRESENT);
                        execute
                        callbackContext.success(message);
                }
            };
            cordova.getActivity().registerReceiver(this.receiver, intentFilter);
            this.registered = true;
        }

    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if (action.equals("userPresent")) {
            this.connectionCallbackContext = callbackContext;
            NetworkInfo info = sockMan.getActiveNetworkInfo();
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "userPresent");
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        return false;
    }

    public void onDestroy() {
    }


    /**
     * Create a new plugin result and send it back to JavaScript
     *
     * @param connection the network info to set as navigator.connection
     */
    private void sendUpdate(String type) {
        if (connectionCallbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, type);
            result.setKeepCallback(true);
            connectionCallbackContext.sendPluginResult(result);
        }
        webView.postMessage("networkconnection", type);
    }
}
