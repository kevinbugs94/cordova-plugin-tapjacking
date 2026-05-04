package com.example.tapjacking;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.provider.Settings;
import android.net.Uri;
import android.os.Build;

public class TapjackingOverlayPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("startOverlay")) {
            startOverlay(callbackContext);
            return true;
        }

        if (action.equals("stopOverlay")) {
            stopOverlay(callbackContext);
            return true;
        }

        if (action.equals("checkPermission")) {
            checkPermission(callbackContext);
            return true;
        }

        return false;
    }

    private void startOverlay(CallbackContext callbackContext) {
        Intent intent = new Intent(cordova.getActivity(), OverlayService.class);
        cordova.getActivity().startService(intent);
        callbackContext.success("Overlay started");
    }

    private void stopOverlay(CallbackContext callbackContext) {
        Intent intent = new Intent(cordova.getActivity(), OverlayService.class);
        cordova.getActivity().stopService(intent);
        callbackContext.success("Overlay stopped");
    }

    private void checkPermission(CallbackContext callbackContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(cordova.getActivity())) {

                Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + cordova.getActivity().getPackageName())
                );

                cordova.getActivity().startActivity(intent);
                callbackContext.error("Permission required");
                return;
            }
        }

        callbackContext.success("Permission granted");
    }
}
