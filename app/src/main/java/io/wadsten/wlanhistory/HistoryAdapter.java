package io.wadsten.wlanhistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mike on 9/7/15.
 */
public class HistoryAdapter extends ArrayAdapter<NetworkState> {
    private final int resourceId;
    private final Context ctx;
    private NetworkStateHistory nshistory;

    public HistoryAdapter(Context context, int resource) {
        super(context, resource);
        this.ctx = context;
        this.resourceId = resource;

        this.nshistory = new NetworkStateHistory();
    }

    public void swapHistory(NetworkStateHistory history) {
        this.nshistory = history;
        this.notifyDataSetChanged();
    }

    @Override
    public NetworkState getItem(int position) {
        return nshistory.history.get(position);
    }

    @Override
    public int getPosition(NetworkState item) {
        return nshistory.indexOf(item);
    }

    @Override
    public int getCount() {
        return nshistory.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(resourceId, null);
        }

        assert view != null;

        TextView state = (TextView) view.findViewById(R.id.state);
        TextView timestamp = (TextView) view.findViewById(R.id.timestamp);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        NetworkState item = getItem(position);

        if (item == null) {
            state.setText("null");
            timestamp.setText("");
            icon.setImageResource(android.R.drawable.presence_offline);
            return view;
        }

        state.setText(String.format("%s/%s", item.state, item.detailedState));

        switch (item.state) {
            case DISCONNECTED:
                icon.setImageResource(android.R.drawable.presence_offline);
                break;
            case DISCONNECTING:
                icon.setImageResource(android.R.drawable.presence_invisible);
                break;
            case CONNECTED:
                icon.setImageResource(android.R.drawable.presence_online);

                state.setText(String.format("%s (%s)", item.ssid, item.bssid));
                break;
            case CONNECTING:
                state.setText(String.format("%s (%s)", item.ssid, item.detailedState));
            default:
                icon.setImageResource(android.R.drawable.presence_away);
                break;
        }

        timestamp.setText(item.timestamp);

        return view;
    }
}
