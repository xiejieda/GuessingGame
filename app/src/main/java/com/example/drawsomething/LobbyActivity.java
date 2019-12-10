package com.example.drawsomething;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.drawsomething.bean.DrawSomethingTable;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LobbyActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        setupHall();
    }

    private void setupHall(){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://10.62.16.240:8080/DrawSomethingAPI/Table";
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Error in get table",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String context = response.body().string();
                Log.i(TAG, "onResponse: "+context);
                Gson gson = new Gson();
                final DrawSomethingTable[] tables = gson.fromJson(context,DrawSomethingTable[].class);

                runOnUiThread(new Runnable() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void run() {
                        LinearLayout linearLayout = findViewById(R.id.hall);
                        LinearLayout oneLine = null;
                        for (int i=0;i<tables.length;i++){
                            oneLine = new LinearLayout(getApplicationContext());
                            oneLine.setOrientation(LinearLayoutCompat.HORIZONTAL);
                            linearLayout.addView(oneLine);
                            View view = getLayoutInflater().inflate(R.layout.table, null);
//                            findViewById(R.id.)
                            oneLine.addView(view);
                        }
                    }
                });

            }
        });
    }
}
