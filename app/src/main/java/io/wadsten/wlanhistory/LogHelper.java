package io.wadsten.wlanhistory;

import android.content.Context;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okio.BufferedSink;
import okio.Okio;


public class LogHelper {
    private static final String TAG = "LogHelper";
    private static Lock lock = new ReentrantLock();

    private static File getCacheFile(Context context) {
        return new File(context.getCacheDir().getPath() + "/cache.json");
    }

    private static JsonAdapter<NetworkStateHistory> makeAdapter() {
        Moshi moshi = new Moshi.Builder().build();

        return moshi.adapter(NetworkStateHistory.class);
    }

    public static NetworkStateHistory getHistory(Context ctx) {
        if (lock.tryLock()) {
            File cache = getCacheFile(ctx);

            if (!cache.exists()) {
                lock.unlock();
                return null;
            }

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<NetworkStateHistory> adapter = moshi.adapter(NetworkStateHistory.class);

            try {
                return adapter.fromJson(Okio.buffer(Okio.source(cache)));
            } catch (IOException ioe) {
                Log.e(TAG, "IOException in getHistory: " + ioe.getMessage());
                return null;
            } catch (JsonDataException j) {
                j.printStackTrace();
                return null;
            } finally {
                lock.unlock();
            }
        } else {
            // Couldn't lock the file.
            return null;
        }
    }

    public static void writeHistory(Context ctx, NetworkStateHistory history) {
        lock.lock();

        File cache = getCacheFile(ctx);
        if (!cache.exists()) {
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonAdapter<NetworkStateHistory> adapter = makeAdapter();

        try {
            BufferedSink sink = Okio.buffer(Okio.sink(cache));

            adapter.toJson(sink, history);
            sink.flush();
            sink.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void clearHistory(Context context) {
        lock.lock();

        getCacheFile(context).delete();

        lock.unlock();
    }
}
