package io.wadsten.wlanhistory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WlanStateReceiver extends BroadcastReceiver {
    private static final String TAG = "WlanStateReceiver";

    public WlanStateReceiver() {
    }

    /**
     * Get the current Wi-Fi connection info, because there's some indications online that we
     * don't get everything in the intent?
     * @param context the context to use
     * @return the Wi-Fi manager's connection info
     */
    private WifiInfo getConnectionInfo(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        return manager.getConnectionInfo();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            return;
        }

        NetworkInfo network = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        WifiInfo info = getConnectionInfo(context);

        // Build our internal representation of the current state.
        NetworkState state = new NetworkState(network, info);
        Log.d(TAG, state.toString());

        NetworkStateHistory history = LogHelper.getHistory(context);
        if (history == null) {
            // File doesn't exist yet.
            history = new NetworkStateHistory();
        }

        history.add(state);

        LogHelper.writeHistory(context, history);
    }
}
