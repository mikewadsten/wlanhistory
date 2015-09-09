package io.wadsten.wlanhistory;

import java.util.ArrayList;
import java.util.List;

public class NetworkStateHistory {
    // Space-saving measure.
    private static final int MAX_HISTORY = 300;

    public final List<NetworkState> history;

    public NetworkStateHistory() {
        this.history = new ArrayList<>();
    }

    public NetworkStateHistory(List<NetworkState> list) {
        this();

        this.history.addAll(list);
    }

    public void add(NetworkState state) {
        history.add(0, state);
        
        if (history.size() <= MAX_HISTORY) {
            return;
        }
        
        // Trim history down to the "first" MAX_HISTORY items (i.e. the most recent items).
        history = new ArrayList<NetworkState>(history.subList(0, MAX_HISTORY));
    }

    public int size() {
        return history.size();
    }

    public int indexOf(NetworkState item) {
        return history.indexOf(item);
    }
}
