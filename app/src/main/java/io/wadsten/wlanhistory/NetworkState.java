package io.wadsten.wlanhistory;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

import java.sql.Timestamp;
import java.util.Date;

public class NetworkState {
    public final String ssid;
    public final String bssid;
    public final String state;
    public final int rssi;

    public final String timestamp;

    public NetworkState(NetworkInfo network, WifiInfo wifi) {
        state = network.getDetailedState().toString();

        ssid = wifi.getSSID();
        bssid = wifi.getBSSID();
        rssi = wifi.getRssi();

        timestamp = new Timestamp(new Date().getTime()).toString();
    }

    @Override
    public String toString() {
        return String.format("NetworkState<ssid=%s, bssid=%s, state=%s, rssi=%d, time=%s>",
                this.ssid, this.bssid, this.state, this.rssi, this.timestamp);
    }
}
