package com.masb1ue.app.myapplication;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloader {
    private OkHttpClient _okHttpClient;
    private Context _context;
    private String TAG = "FILE_DOWNLOADER";
    private FileCallback _Ota_callback;
//    private boolean isContinous = false;

    private Boolean forceOtaUpdate = false;
    final private String CHECK_JSON_URL = "http://ipadvcloud.voicesymbol.com/Airoha/VOZA.json";

    public interface FileCallback {

        void onStart();
        void onDownload(int percent, String name);
        //parseJSON
        void onParse(Bundle bundle);
        //checkMD5
        void onCheck(Boolean md5check, String name);
        void onError(String error, String name);
        //finisheverydownload
        void onDownloadFinish(String name);
    }


    public FileDownloader(Context context) {
        this._context = context;
        initOkHttpClient();
    }


    private void initOkHttpClient() {
        File sdcache = _context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        _okHttpClient = builder.build();

    }

    public void setCallback(FileCallback fileCallback) {
        this._Ota_callback = fileCallback;
    }

    /**
     * 檢查json
     */
    public void checkjsonFile(final String urlJson) {
        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url(urlJson).build();
                    Response response = _okHttpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Log.i(TAG, "getjsonFile Fail---");
                        return;
                    }
                    String jsonData = response.body().string();
                    Bundle bundle = parseJson(jsonData);
                    _Ota_callback.onParse(bundle);
                }catch (IOException e) {
                    _Ota_callback.onError(e.toString(),"checkjsonFile");
                    e.printStackTrace();
                }
            }
        };
        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    /**
     * 解析json
     */
    public Bundle parseJson(String jsonData) {
        try {
            JSONObject data = new JSONObject(jsonData);
            if (!data.getString("success").equals("true")){
                Log.d(TAG, "get file failed");
                return null;
            }
            JSONObject records = data.getJSONObject("records");
            String description = records.getString("datasetDescription");
            JSONArray location = records.getJSONArray("location");
            for (int i = 0; i < location.length(); i++) {
                if (location.getJSONObject(i).getString("locationName").equals("臺北市")) {
                    JSONArray weatherElement = location.getJSONObject(i).getJSONArray("weatherElement");
                    for (int j = 0; j < weatherElement.length(); j++) {
                        if (weatherElement.getJSONObject(j).getString("elementName").equals("MinT")) {
                            JSONArray time = weatherElement.getJSONObject(j).getJSONArray("time");
                            Bundle b = new Bundle();
                            b.putInt("num", time.length());
                            for (int k = 0; k < time.length(); k++) {
                                b.putString("start" + k,time.getJSONObject(k).getString("startTime"));
                                b.putString("end" + k,time.getJSONObject(k).getString("endTime"));
                                b.putString("para" + k,time.getJSONObject(k).getJSONObject("parameter").getString("parameterName"));
                                b.putString("unit" + k,time.getJSONObject(k).getJSONObject("parameter").getString("parameterUnit"));
                            }
                            return b;
                        }else {
                            Log.d(TAG, "there is no MinT");
                        }
                    }
                }else {
                    Log.d(TAG, "city is not taipei");
                }

            }
            return null;
        }catch(JSONException e) {
            _Ota_callback.onError(e.toString(),"parseJson");
            e.printStackTrace();
        }
        return null;
    }


}