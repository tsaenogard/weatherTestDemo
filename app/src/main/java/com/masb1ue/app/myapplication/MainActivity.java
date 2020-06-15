package com.masb1ue.app.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FileDownloader _fileDownloader;
    private String URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001";
    private String _authorizationKey = "CWB-F084759E-00C8-4CC7-8CFF-464F2A6D25F1";
    String TAG = "testDemo";
    private List _list;
    private RecyclerView _recycleView;
    private LinearLayoutManager _linearLayoutManager;
    private MyRecycleViewAdapter _adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fileDownloader = new FileDownloader(this);
        _fileDownloader.setCallback(_callback);
        String urlString = URL + "?"
                +"Authorization=" + _authorizationKey + "&"
                + "format=JSON" + "&"
                + "locationName=" + "臺北市";
        _fileDownloader.checkjsonFile(urlString);

        _list = new ArrayList();
        _recycleView = findViewById(R.id.mainRV);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSharePreference();
    }

    public void checkSharePreference() {
        SharedPreferences sp = getSharedPreferences("com.masb1ue.app.myapplication", MODE_PRIVATE);
        if (sp.getBoolean("isFirst", false))
            Toast.makeText(this, "歡迎回來", Toast.LENGTH_SHORT).show();
        else
            sp.edit().putBoolean("isFirst", true).commit();
    }



    public FileDownloader.FileCallback _callback = new FileDownloader.FileCallback() {
        @Override
        public void onStart() {
        }

        @Override
        public void onDownload(int percent, String name) {
        }

        @Override
        public void onParse(Bundle bundle) {
            if (bundle != null)
                Log.d(TAG, bundle.toString());
            int num = bundle.getInt("num");
            for (int i = 0; i < num; i++) {
                String temp =
                        bundle.getString("start" + i) + "\n" +
                                bundle.getString("end" + i) + "\n" +
                                bundle.getString("para" + i) +
                                bundle.getString("unit" + i);
                _list.add(temp);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //创建布局管理器，垂直设置LinearLayoutManager.VERTICAL，水平设置LinearLayoutManager.HORIZONTAL
                    _linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    //创建适配器，将数据传递给适配器
                    _adapter = new MyRecycleViewAdapter(_list, MainActivity.this);
                    //设置布局管理器
                    _recycleView.setLayoutManager(_linearLayoutManager);
                    //设置适配器adapter
                    _recycleView.setAdapter(_adapter);
                }
            });

        }

        @Override
        public void onCheck(Boolean md5check, String name) {
        }

        @Override
        public void onError(String error, String name) {
        }

        @Override
        public void onDownloadFinish(String name) {
        }
    };
}
