package com.example.drawsomething;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {
    private int tableId;
    private int place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.tableId = getIntent().getIntExtra("tableId",0);
        this.place = getIntent().getIntExtra("place",0);
    }

    public void leaveTable(View view) {
        String url =  MessageFormat.format("http://10.62.16.240:8080/DrawSomethingAPI/LeaveTable?id_table={0}&user={1}",tableId,place);
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                finish();
            }
        });

    }
}
