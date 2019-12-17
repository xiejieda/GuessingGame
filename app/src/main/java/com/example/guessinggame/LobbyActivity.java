package com.example.guessinggame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guessinggame.bean.GuessingGameTable;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LobbyActivity extends AppCompatActivity {
    private final static String TAG=LobbyActivity.class.getSimpleName();
    private int userId;
    private String ip="192.168.1.102";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        this.userId = getIntent().getIntExtra("user_id",0);
        setupHall();
    }

    private void setupHall(){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/Table",ip);
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
                String content = response.body().string();
                Log.i(TAG, "onResponse: " + content);
                Gson gson = new Gson();
                final GuessingGameTable[] tables = gson.fromJson(content, GuessingGameTable[].class);

                runOnUiThread(new Runnable() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void run() {
                        LinearLayout linearLayout = findViewById(R.id.hall);
                        linearLayout.removeAllViews();
                        for (int i = 0; i < tables.length; i++) {
                            final GuessingGameTable guessingGameTable = tables[i];
                            LinearLayout oneLine = new LinearLayout(getApplicationContext());
                            oneLine.setOrientation(LinearLayoutCompat.HORIZONTAL);
                            linearLayout.addView(oneLine);
                            View view = getLayoutInflater().inflate(R.layout.table, null);

                            Button user_1 = (Button) view.findViewById(R.id.user_1);
                            Button user_2 = (Button) view.findViewById(R.id.user_2);
                            Button user_3 = (Button) view.findViewById(R.id.user_3);
                            Button user_4 = (Button) view.findViewById(R.id.user_4);
                            if (guessingGameTable.getUser_1()==0&&guessingGameTable.getUser_2()==0&&guessingGameTable.getUser_3()==0&&guessingGameTable.getUser_4()==0){
                                gameLastCheck(i+1);
                            }
                            if (guessingGameTable.getUser_1() > 0) {
                                user_1.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_1.setEnabled(false);
                            } else {
                                user_1.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_1.setEnabled(true);
                                user_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(1, guessingGameTable);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_2() > 0) {
                                user_2.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_2.setEnabled(false);
                            } else {
                                user_2.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_2.setEnabled(true);
                                user_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(2, guessingGameTable);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_3() > 0) {
                                user_3.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_3.setEnabled(false);
                            } else {
                                user_3.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_3.setEnabled(true);
                                user_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(3, guessingGameTable);
                                    }
                                });
                            }
                            if (guessingGameTable.getUser_4() > 0) {
                                user_4.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.people));
                                user_4.setEnabled(false);
                            } else {
                                user_4.setBackground(ActivityCompat.getDrawable(getApplicationContext(), R.mipmap.down));
                                user_4.setEnabled(true);
                                user_4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        joinTable(4, guessingGameTable);
                                    }
                                });
                            }

                            if (guessingGameTable.getLast_check()==1){
                                ((TextView) view.findViewById(R.id.gamestatus)).setText("本桌已开始");
                                user_1.setEnabled(false);
                                user_2.setEnabled(false);
                                user_3.setEnabled(false);
                                user_4.setEnabled(false);
                            }else{
                                ((TextView) view.findViewById(R.id.gamestatus)).setText("");
                                user_1.setEnabled(true);
                                user_2.setEnabled(true);
                                user_3.setEnabled(true);
                                user_4.setEnabled(true);
                            }

                            int table_id = i+1;
                            ((TextView) view.findViewById(R.id.table_id)).setText("_"+table_id+"_");
                            oneLine.addView(view);
                        }
                    }
                });

            }
        });
    }

    private void gameLastCheck(int tableId){
        OkHttpClient okHttpClient = new OkHttpClient();
        String gameurl = MessageFormat.format("http://{0}:8080/GuessingGameAPI/GameStop?table_id={1}",ip,tableId);
        Request gamerequest = new Request.Builder().url(gameurl).build();
        okHttpClient.newCall(gamerequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content = response.body().string();
            }
        });
    }

    private void joinTable(int place,GuessingGameTable guessingGameTable){
        final Intent intent = new Intent(getApplicationContext(),GameActivity.class);
        intent.putExtra("tableId",guessingGameTable.getId());
        intent.putExtra("place",place);
        intent.putExtra("userId",userId);
        String url =  MessageFormat.format("http://{0}:8080/GuessingGameAPI/JoinTable?id_table={1}&user={2}&id={3}",ip,guessingGameTable.getId(),place,userId);
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                Log.i(TAG, "onResponse: "+content);
                startActivity(intent);
            }
        });
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run(){
            setupHall();
            handler.postDelayed(this,1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable,1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void logout(View view) {
        logout();
    }
    private void logout() {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/Logout?id={1}",ip,userId);
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Failed on Logout",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                LobbyActivity.this.finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        logout();
    }
    
}
