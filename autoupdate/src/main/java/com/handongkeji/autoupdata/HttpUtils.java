package com.handongkeji.autoupdata;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class HttpUtils {

    public Executor executor = Executors.newCachedThreadPool();
    public Handler handler = new Handler(Looper.getMainLooper());

    public Response get(String path){
        BufferedReader br = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;
            final StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null){
                sb.append(str);
            }
            return new Response(sb.toString(),Response.SUCCESS);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            return new Response(e.toString(),Response.ERROR);
        } catch (final IOException e) {
            e.printStackTrace();
            return new Response(e.toString(),Response.ERROR);
        }
    }

    public void asyncGet(final String path, final Callback callback){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Response response = get(path);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete(response);
                    }
                });
            }
        });
    }

    public interface Callback{
        void onComplete(Response response);
    }
}
