package com.ramo.air.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.ramo.air.listener.HttpCallbackListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author ramo
 * @time 2015.12.04
 */
public class NetUtils {

    /**
     * 仅仅获取服务器数据 不携带请求参数
     *
     * @param url
     * @param listener
     * @return
     */
    public static void getIOFromUrl(final String url,
                                    final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url);
                    HttpResponse response = client.execute(get);

                    if (response.getStatusLine().getStatusCode() == 200) {
                        if (listener != null)
                            listener.onSucc(EntityUtils.toString(response
                                    .getEntity()));

                    }
                } catch (Exception e) {
                    Log.e("net conn", "请求失败");
                    if (listener != null)
                        listener.onError(e);
                }
            }
        }).start();
    }

    /**
     * 向服务器发送请求 带参数
     *
     * @param url
     * @param name
     * @param val
     * @param listener
     * @return
     */
    public static void sendRequestToUrl(final String url, final String[] name,
                                        final String[] val, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);

                    // 通过NameValuePair集合保存待提交的数据
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    for (int i = 0; i < name.length; i++)
                        params.add(new BasicNameValuePair(name[i], val[i]));

                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
                            params, "utf-8");
                    post.setEntity(entity);

                    HttpResponse response = client.execute(post);
                    // 如果返回状态码是200 则表示请求成功
                    if (response.getStatusLine().getStatusCode() == 200) {
                        if (listener != null)
                            listener.onSucc(EntityUtils.toString(
                                    response.getEntity(), "utf-8"));
                        // 把数据放在回调函数里
                    }

                } catch (Exception e) {
                    if (listener != null)
                        listener.onError(e);
                }
            }
        }).start();
    }

    public static void getHttpUrlConnection(final String url,
                                            final HttpCallbackListener listener) {

        try {
            new Thread(new Runnable() {
                private URL mUrl = new URL(url);
                private HttpURLConnection connection = null;

                @Override
                public void run() {
                    try {
                        connection = (HttpURLConnection) mUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);

                        InputStream in = connection.getInputStream();
                        BufferedReader read = new BufferedReader(
                                new InputStreamReader(in));

                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = read.readLine()) != null) {
                            response.append(line);
                        }

                        if (listener != null) {
                            listener.onSucc(response.toString());
                        }
                    } catch (IOException e) {
                        listener.onError(e);
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (MalformedURLException e) {
            listener.onError(e);
            e.printStackTrace();
        }
    }

    public static void getHttpUrlConnectionJuHe(final String url,
                                                final Map<String, Object> map, final HttpCallbackListener listener) {
        if (map != null) {


            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(url);
            stringBuffer.append("?");
            for (String key : map.keySet()) {
                stringBuffer.append(key);
                stringBuffer.append("=");
                stringBuffer.append(map.get(key));
                stringBuffer.append("&");
            }
            String urlStr = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
            L.e("request url  :" + urlStr);
            getHttpUrlConnection(urlStr, listener);
        }else{
            getHttpUrlConnection(url, listener);
        }
    }

    //获取当前网络状态
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }
}
