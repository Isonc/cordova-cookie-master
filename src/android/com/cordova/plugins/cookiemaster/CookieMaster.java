package com.cordova.plugins.cookiemaster;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import java.net.HttpCookie;

//import android.webkit.CookieManager;
import org.xwalk.core.XWalkCookieManager;

public class CookieMaster extends CordovaPlugin {

    private final String TAG = "CookieMasterPlugin";
    public static final String ACTION_GET_COOKIE_VALUE = "getCookieValue";
    public static final String ACTION_SET_COOKIE_VALUE = "setCookieValue";
    public static final String ACTION_CLEAR_COOKIES = "clearCookies";

    private XWalkCookieManager mCookieManager = null;
    public CookieMaster() {
        mCookieManager = new XWalkCookieManager();
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (ACTION_GET_COOKIE_VALUE.equals(action)) {
            final String url = args.getString(0);
            final String cookieName = args.getString(1);

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        String[] cookies = mCookieManager.getCookie(url).split("; ");
                        String cookieValue = "";

                        for (int i = 0; i < cookies.length; i++) {
                            if (cookies[i].contains(cookieName + "=")) {
                                String[] cValues = cookies[i].split(cookieName + "=");
                                cookieValue = cValues[1];
                                break;
                            }
                        }

                        JSONObject json = null;
                        if (cookieValue != "") {
                            json = new JSONObject("{cookieValue:\"" + cookieValue + "\"}");
                        }
                        if (json != null) {
                            PluginResult res = new PluginResult(PluginResult.Status.OK, json);
                            callbackContext.sendPluginResult(res);
                        } else {
                            callbackContext.error("Cookie not found!");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                        callbackContext.error(e.getMessage());
                    }
                }
            });
            return true;

        } else if (ACTION_SET_COOKIE_VALUE.equals(action)) {
            final String url = args.getString(0);
            final String cookieName = args.getString(1);
            final String cookieValue = args.getString(2);

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        callbackContext.error("Invalid function!");
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e.getMessage());
                        callbackContext.error(e.getMessage());
                    }
                }
            });
            return true;
        }

        else if (ACTION_CLEAR_COOKIES.equals(action)) {
            mCookieManager.removeAllCookie();
            callbackContext.success();
            return true;
        }

        callbackContext.error("Invalid action");
        return false;

    }
}
