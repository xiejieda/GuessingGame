package com.example.guessinggame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {
    private int tableId;
    private int place;
    private int userId;
    private Button user1button;
    private Button user2button;
    private Button user3button;
    private Button user4button;
    private int lastcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.tableId = getIntent().getIntExtra("tableId",0);
        this.place = getIntent().getIntExtra("place",0);
        this.userId = getIntent().getIntExtra("userId",0);
        user1button = (Button) findViewById(R.id.user1button);
        user2button = (Button) findViewById(R.id.user2button);
        user3button = (Button) findViewById(R.id.user3button);
        user4button = (Button) findViewById(R.id.user4button);
        selfbutton(place);
        setupgame();
    }

    public void leaveTable(View view) {
        AlertDialog.Builder buider= new AlertDialog.Builder(this);
        buider.setIcon(android.R.drawable.ic_dialog_info);
        buider.setTitle("提示");
        buider.setMessage("确定退出？");
        buider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url =  MessageFormat.format("http://10.62.16.247:8080/GuessingGameAPI/LeaveTable?id_table={0}&user={1}&id={2}",tableId,place,userId);
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
        });
        buider.create().show();

    }

    private void setupgame(){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://10.62.16.247:8080/GuessingGameAPI/TableStatus?table_id={0}",tableId);
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Failed on setup",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content = response.body().string();
                Gson gson = new Gson();
                final Map<String,Object> result = gson.fromJson(content, Map.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> user_1_info = (Map<String, Object>) result.get("user_1_info");
                        Map<String, Object> user_2_info = (Map<String, Object>) result.get("user_2_info");
                        Map<String, Object> user_3_info = (Map<String, Object>) result.get("user_3_info");
                        Map<String, Object> user_4_info = (Map<String, Object>) result.get("user_4_info");
                        if (user_1_info!=null){
                            ((TextView) findViewById(R.id.user1)).setText(user_1_info.get("username").toString());
                            if ((int)(double)user_1_info.get("game_status")==1){
                                user1button.setText("取消准备");
                            }else {
                                user1button.setText("准备");
                            }
                        }else {
                            ((TextView) findViewById(R.id.user1)).setText("暂无玩家加入");
                            user1button.setText("准备");
                        }
                        if (user_2_info!=null){
                            ((TextView) findViewById(R.id.user2)).setText(user_2_info.get("username").toString());
                            if ((int)(double)user_2_info.get("game_status")==1){
                                user2button.setText("取消准备");
                            }else {
                                user2button.setText("准备");
                            }
                        }else {
                            ((TextView) findViewById(R.id.user2)).setText("暂无玩家加入");
                            user2button.setText("准备");
                        }
                        if (user_3_info!=null){
                            ((TextView) findViewById(R.id.user3)).setText(user_3_info.get("username").toString());
                            if ((int)(double)user_3_info.get("game_status")==1){
                                user3button.setText("取消准备");
                            }else {
                                user3button.setText("准备");
                            }
                        }else {
                            ((TextView) findViewById(R.id.user3)).setText("暂无玩家加入");
                            user3button.setText("准备");
                        }
                        if (user_4_info!=null){
                            ((TextView) findViewById(R.id.user4)).setText(user_4_info.get("username").toString());
                            if ((int)(double)user_4_info.get("game_status")==1){
                                user4button.setText("取消准备");
                            }else {
                                user4button.setText("准备");
                            }
                        }else {
                            ((TextView) findViewById(R.id.user4)).setText("暂无玩家加入");
                            user4button.setText("准备");
                        }
                        lastcheck=0;
                        int x=0;
                        int y=0;
                        if (user_1_info!=null){
                            x++;
                        }
                        if (user_2_info!=null){
                            x++;
                        }
                        if (user_3_info!=null){
                            x++;
                        }
                        if (user_4_info!=null){
                            x++;
                        }
                        if (user_1_info!=null&&(int)(double)user_1_info.get("game_status")==1){
                            y++;
                        }
                        if (user_2_info!=null&&(int)(double)user_2_info.get("game_status")==1){
                            y++;
                        }
                        if (user_3_info!=null&&(int)(double)user_3_info.get("game_status")==1){
                            y++;
                        }
                        if (user_4_info!=null&&(int)(double)user_4_info.get("game_status")==1){
                            y++;
                        }
                        System.out.println(x);
                        if (x==y&&x!=0){
                            lastcheck=1;
                        }else{
                            lastcheck=0;
                        }
                    }
                });
            }
        });

        if (lastcheck==1){
            String gameurl = MessageFormat.format("http://10.62.16.247:8080/GuessingGameAPI/GameStart?table_id={0}",tableId);
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
        }else {
            String gameurl = MessageFormat.format("http://10.62.16.247:8080/GuessingGameAPI/GameStop?table_id={0}",tableId);
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
    }

    private void selfbutton(int place){

        if (place==1){
            user2button.setEnabled(false);
            user3button.setEnabled(false);
            user4button.setEnabled(false);
        }else if (place==2){
            user1button.setEnabled(false);
            user3button.setEnabled(false);
            user4button.setEnabled(false);
        }else if (place==3){
            user1button.setEnabled(false);
            user2button.setEnabled(false);
            user4button.setEnabled(false);
        }else{
            user1button.setEnabled(false);
            user2button.setEnabled(false);
            user3button.setEnabled(false);
        }
    }

    private void changeStatus(int i){
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://10.62.16.247:8080/GuessingGameAPI/UserStatus?id={0}&status={1}",userId,i);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Failed on change status",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content = response.body().string();
            }
        });
    }

    public void user1status(View view) {
        if ("准备".equals(user1button.getText().toString())){
            changeStatus(1);
        }else{
            changeStatus(0);
        }
    }

    public void user2status(View view) {
        if ("准备".equals(user2button.getText().toString())){
            changeStatus(1);
        }else{
            changeStatus(0);
        }
    }

    public void user3status(View view) {
        if ("准备".equals(user3button.getText().toString())){
            changeStatus(1);
        }else{
            changeStatus(0);
        }
    }

    public void user4status(View view) {
        if ("准备".equals(user4button.getText().toString())){
            changeStatus(1);
        }else{
            changeStatus(0);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run(){
            setupgame();
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
}
