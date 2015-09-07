package io.wadsten.wlanhistory;

import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.Date;

public class NetworkState implements Parcelable {
    public String ssid;
    public String bssid;
    public NetworkInfo.State state;
    public int rssi;

    public NetworkInfo.DetailedState detailedState;
    public String reason;
    public boolean available;
    public int linkSpeed;
    public SupplicantState supplicantState;

    public String timestamp;

    public NetworkState(NetworkInfo network, WifiInfo wifi) {
        // What we want to normally look at.
        ssid = wifi.getSSID();
        bssid = wifi.getBSSID();
        state = network.getState();
        rssi = wifi.getRssi();

        // More detailed stuff.
        detailedState = network.getDetailedState();
        reason = network.getReason();
        available = network.isAvailable();
        linkSpeed = wifi.getLinkSpeed();
        supplicantState = wifi.getSupplicantState();

        timestamp = new Timestamp(new Date().getTime()).toString();
    }

    protected NetworkState(String ssid, String bssid, NetworkInfo.State state,
                        int rssi, NetworkInfo.DetailedState detailedState,
                        String reason, boolean available, int linkSpeed,
                        SupplicantState supplicantState, String timestamp) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.state = state;
        this.rssi = rssi;
        this.detailedState = detailedState;
        this.reason = reason;
        this.available = available;
        this.linkSpeed = linkSpeed;
        this.supplicantState = supplicantState;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("NetworkState<state=%s, bssid=%s, ssid=%s, rssi=%d, time=%s>",
                this.state, this.bssid, this.ssid, this.rssi, this.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null) {
            return false;
        } else if (!(o instanceof NetworkState)) {
            return false;
        }

        NetworkState other = (NetworkState) o;

        return (this.timestamp.equals(other.timestamp)) &&
                (this.state.equals(other.state)) &&
                (this.bssid.equals(other.bssid));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ssid);
        dest.writeString(bssid);
        dest.writeSerializable(state);
        dest.writeInt(rssi);

        dest.writeSerializable(detailedState);
        dest.writeString(reason);
        dest.writeByte((byte) (available ? 0x1 : 0x0));
        dest.writeInt(linkSpeed);
        dest.writeSerializable(supplicantState);

        dest.writeString(timestamp);
    }

    public static final Parcelable.Creator<NetworkState> CREATOR = new Creator<NetworkState>() {
        @Override
        public NetworkState createFromParcel(Parcel source) {
            String ssid = source.readString();
            String bssid = source.readString();
            NetworkInfo.State state = (NetworkInfo.State) source.readSerializable();
            int rssi = source.readInt();

            NetworkInfo.DetailedState dstate = (NetworkInfo.DetailedState) source.readSerializable();
            String reason = source.readString();
            boolean available = (source.readByte() != 0);
            int linkSpeed = source.readInt();
            SupplicantState sstate = (SupplicantState) source.readSerializable();

            String timestamp = source.readString();

            return new NetworkState(ssid, bssid, state, rssi, dstate, reason, available,
                                    linkSpeed, sstate, timestamp);
        }
    
        @Override
        public NetworkState[] newArray(int size) {
            return null;
        }
    };
}
