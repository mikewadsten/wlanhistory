package io.wadsten.wlanhistory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HistoryFragment(), "history")
                    .commit();
        }
    }

    private void updateHistoryView() {
        NetworkStateHistory history = LogHelper.getHistory(getApplicationContext());
        HistoryFragment f = (HistoryFragment) getSupportFragmentManager().findFragmentByTag("history");

        HistoryAdapter adapter = new HistoryAdapter(this, R.layout.history_item);
        if (history != null) adapter.swapHistory(history);

        f.setListAdapter(adapter);

        f.setEmptyText("No history");
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateHistoryView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateHistoryView();
            return true;
        } else if (id == R.id.action_clear) {
            LogHelper.clearHistory(getApplicationContext());
            updateHistoryView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(NetworkState state) {
//        Toast.makeText(getApplicationContext(), state.toString(), Toast.LENGTH_SHORT).show();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new HistoryDialogFragment().setState(state).show(ft, "dialog");
    }
}
