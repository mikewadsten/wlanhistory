package io.wadsten.wlanhistory;

import java.util.ArrayList;
import java.util.List;

public class NetworkStateHistory {
    public final List<NetworkState> history;

    public NetworkStateHistory() {
        this.history = new ArrayList<>();
    }

    public NetworkStateHistory(List<NetworkState> list) {
        this();

        this.history.addAll(list);
    }

    public void add(NetworkState state) {
        history.add(state);
    }
}
