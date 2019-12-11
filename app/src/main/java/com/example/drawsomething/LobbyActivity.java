package com.example.drawsomething;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.drawsomething.bean.GuessingGameTable;
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
            public void onResponse(@NotNull final Call call, @NotNull Response response) throws IOException {
                String context = response.body().string();
                Log.i(TAG, "onResponse: "+context);
                Gson gson = new Gson();
                final GuessingGameTable[] tables = gson.fromJson(context, GuessingGameTable[].class);

                runOnUiThread(new Runnable() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void run() {
                        LinearLayout linearLayout = findViewById(R.id.hall);
                        LinearLayout oneLine = null;
                        for (int i=0;i<tables.length;i++){
                            GuessingGameTable guessingGameTable = tables[i];
                            oneLine = new LinearLayout(getApplicationContext());
                            oneLine.setOrientation(LinearLayoutCompat.HORIZONTAL);
                            linearLayout.addView(oneLine);
                            View view = getLayoutInflater().inflate(R.layout.table, null);
                            Button user_1 = (Button) view.findViewById(R.id.user_1);
                            Button user_2 = (Button) view.findViewById(R.id.user_2);
                            Button user_3 = (Button) view.findViewById(R.id.user_3);
                            Button user_4 = (Button) view.findViewById(R.id.user_4);
                            if (guessingGameTable.getUser_1() > 0){
                                user_1.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.people));
                            }else{
                                user_1.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.down));
                                user_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_2() > 0){
                                user_2.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.people));
                            }else{
                                user_2.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.down));
                                user_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_3() > 0){
                                user_3.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.people));
                            }else{
                                user_3.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.down));
                                user_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_4() > 0){
                                user_4.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.people));
                            }else{
                                user_4.setBackground(ActivityCompat.getDrawable(getApplicationContext(),R.mipmap.down));
                                user_4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            oneLine.addView(view);
                        }
                    }
                });

            }
        });
    }
}
