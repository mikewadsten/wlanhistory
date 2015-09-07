package io.wadsten.wlanhistory;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


public class HistoryDialogFragment extends DialogFragment {
    private static final String ARG_STATE = "state";

    private NetworkState state;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param state the network state to show
     * @return A new instance of fragment HistoryDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryDialogFragment newInstance(NetworkState state) {
        HistoryDialogFragment fragment = new HistoryDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STATE, state);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            state = getArguments().getParcelable(ARG_STATE);
        }
    }

    public HistoryDialogFragment setState(NetworkState state) {
        this.state = state;

        return this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (state == null) return;
        outState.putParcelable(ARG_STATE, state);
    }

    private void setItemText(View view, int id, String content) {
        ((TextView) view.findViewById(id)).setText(content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (state == null && savedInstanceState == null) {
            return null;
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_STATE)) {
            state = savedInstanceState.getParcelable(ARG_STATE);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_dialog, null);

        setItemText(v, R.id.ssid, state.ssid);
        setItemText(v, R.id.bssid, state.bssid);
        setItemText(v, R.id.state, state.state.toString());
        setItemText(v, R.id.rssi, String.format("%d dBm", state.rssi));
        setItemText(v, R.id.detailedState, state.detailedState.toString());

        setItemText(v, R.id.reason, state.reason);
        setItemText(v, R.id.available, String.format("%s", state.available));
        setItemText(v, R.id.linkSpeed, String.format("%s Mbps", state.linkSpeed));
        setItemText(v, R.id.supplicantState, state.supplicantState.toString());

        setItemText(v, R.id.timestamp, state.timestamp);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss also on click.
                getDialog().dismiss();
            }
        });

        getDialog().setCancelable(true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }
}
