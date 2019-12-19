package com.example.guessinggame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guessinggame.bean.GuessingGameRibble;
import com.example.guessinggame.bean.MyBean;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameActivity extends AppCompatActivity {
    private int tableId;
    private int place;
    private int userId;
    private String username;
    private Button user1button;
    private Button user2button;
    private Button user3button;
    private Button user4button;
    private TextView time;
    private int lastcheck = 0;
    private String tip;
    private String answer = "";
    private String ribble;
    boolean stopThread = false;
    private RecyclerView rv;
    private int length = 0;
    private int grade = 0;
    private static final int GAME_TIME = 60;
    private ArrayList<Double> current;
    private ArrayList<Double> previous;
    private int gradestatus0 = 0;
    private int gradestatus1 = 0;
    private String ip = "192.168.43.30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.tableId = getIntent().getIntExtra("tableId", 0);
        this.place = getIntent().getIntExtra("place", 0);
        this.userId = getIntent().getIntExtra("userId", 0);
        this.username = getIntent().getStringExtra("username");
        user1button = (Button) findViewById(R.id.user1button);
        user2button = (Button) findViewById(R.id.user2button);
        user3button = (Button) findViewById(R.id.user3button);
        user4button = (Button) findViewById(R.id.user4button);
        time = (TextView) findViewById(R.id.time);
        rv = ((RecyclerView) findViewById(R.id.rv));
        current = new ArrayList<>();
        previous = new ArrayList<>();
        selfbutton(place);
        setupgame();
    }

    public void leaveTable(View view) {
        exit();
    }

    private void exit() {
        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setIcon(android.R.drawable.ic_dialog_info);
        buider.setTitle("提示");
        buider.setMessage("确定退出？");
        buider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/LeaveTable?id_table={1}&user={2}&id={3}", ip, tableId, place, userId);
                OkHttpClient okHttpClient = new OkHttpClient();
                final Request request = new Request.Builder().url(url).build();
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

    private void setupgame() {
        for (int j = 0; j < current.size(); j++) {
            previous.set(j, current.get(j));
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/TableStatus?table_id={1}", ip, tableId);
        final Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Failed on setup", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String content = response.body().string();
                Gson gson = new Gson();
                final Map<String, Object> result = gson.fromJson(content, Map.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> user_1_info = (Map<String, Object>) result.get("user_1_info");
                        Map<String, Object> user_2_info = (Map<String, Object>) result.get("user_2_info");
                        Map<String, Object> user_3_info = (Map<String, Object>) result.get("user_3_info");
                        Map<String, Object> user_4_info = (Map<String, Object>) result.get("user_4_info");
                        current = new ArrayList<>();
                        if (user_1_info != null) {
                            ((TextView) findViewById(R.id.user1)).setText(user_1_info.get("username").toString());
                            ((TextView) findViewById(R.id.user1_grade)).setText(user_1_info.get("game_grade").toString());
                            current.add(Double.valueOf(((TextView) findViewById(R.id.user1_grade)).getText().toString()));
                            if ((int) (double) user_1_info.get("game_status") == 1) {
                                user1button.setText("取消准备");
                            } else {
                                user1button.setText("准备");
                            }
                        } else {
                            ((TextView) findViewById(R.id.user1)).setText("暂无玩家加入");
                            user1button.setText("准备");
                        }
                        if (user_2_info != null) {
                            ((TextView) findViewById(R.id.user2)).setText(user_2_info.get("username").toString());
                            ((TextView) findViewById(R.id.user2_grade)).setText(user_2_info.get("game_grade").toString());
                            current.add(Double.valueOf(((TextView) findViewById(R.id.user2_grade)).getText().toString()));
                            if ((int) (double) user_2_info.get("game_status") == 1) {
                                user2button.setText("取消准备");
                            } else {
                                user2button.setText("准备");
                            }
                        } else {
                            ((TextView) findViewById(R.id.user2)).setText("暂无玩家加入");
                            user2button.setText("准备");
                        }
                        if (user_3_info != null) {
                            ((TextView) findViewById(R.id.user3)).setText(user_3_info.get("username").toString());
                            ((TextView) findViewById(R.id.user3_grade)).setText(user_3_info.get("game_grade").toString());
                            current.add(Double.valueOf(((TextView) findViewById(R.id.user3_grade)).getText().toString()));
                            if ((int) (double) user_3_info.get("game_status") == 1) {
                                user3button.setText("取消准备");
                            } else {
                                user3button.setText("准备");
                            }
                        } else {
                            ((TextView) findViewById(R.id.user3)).setText("暂无玩家加入");
                            user3button.setText("准备");
                        }
                        if (user_4_info != null) {
                            ((TextView) findViewById(R.id.user4)).setText(user_4_info.get("username").toString());
                            ((TextView) findViewById(R.id.user4_grade)).setText(user_4_info.get("game_grade").toString());
                            current.add(Double.valueOf(((TextView) findViewById(R.id.user4_grade)).getText().toString()));
                            if ((int) (double) user_4_info.get("game_status") == 1) {
                                user4button.setText("取消准备");
                            } else {
                                user4button.setText("准备");
                            }
                        } else {
                            ((TextView) findViewById(R.id.user4)).setText("暂无玩家加入");
                            user4button.setText("准备");
                        }
                        gradestatus1 = current.size();
                        for (int n = 0; n < current.size(); n++) {
                            if (previous.size() == n) {
                                previous.add(current.get(n));
                            }
                        }

                        for (int n = 0; n < current.size(); n++) {
                            if (!current.get(n).equals(previous.get(n))) {
                                gradestatus0++;
                            }
                        }
                        int x = 0;
                        int y = 0;
                        if (user_1_info != null) {
                            x++;
                        }
                        if (user_2_info != null) {
                            x++;
                        }
                        if (user_3_info != null) {
                            x++;
                        }
                        if (user_4_info != null) {
                            x++;
                        }
                        if (user_1_info != null && (int) (double) user_1_info.get("game_status") == 1) {
                            y++;
                        }
                        if (user_2_info != null && (int) (double) user_2_info.get("game_status") == 1) {
                            y++;
                        }
                        if (user_3_info != null && (int) (double) user_3_info.get("game_status") == 1) {
                            y++;
                        }
                        if (user_4_info != null && (int) (double) user_4_info.get("game_status") == 1) {
                            y++;
                        }
                        if (x == y && x != 0) {
                            lastcheck++;
                        } else {
                            lastcheck = 0;
                        }
                    }
                });
            }
        });

        if (lastcheck == 1) {
            System.out.println(lastcheck);
            user1button.setEnabled(false);
            user2button.setEnabled(false);
            user3button.setEnabled(false);
            user4button.setEnabled(false);
            String gameurl = MessageFormat.format("http://{0}:8080/GuessingGameAPI/GameStart?table_id={1}", ip, tableId);
            final Request gamerequest = new Request.Builder().url(gameurl).build();
            okHttpClient.newCall(gamerequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String content = response.body().string();
                }
            });


            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10 && !stopThread; i++) {

                        if (i == 0) {
                            ((TextView) findViewById(R.id.ribble)).setText("游戏即将开始");
                            try {
                                TimeUnit.SECONDS.sleep(3);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        int currentSecond = GAME_TIME;
                        ((TextView) findViewById(R.id.round)).setText(i + 1 + "/10");
                        OkHttpClient okHttpClient1 = new OkHttpClient();
                        String ribbleurl = MessageFormat.format("http://{0}:8080/GuessingGameAPI/GiveRibble?id={1}&table_id={2}", ip, i, tableId);
                        Request ribblerequest = new Request.Builder().url(ribbleurl).build();
                        okHttpClient1.newCall(ribblerequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String content = response.body().string();
                                Gson gson = new Gson();
                                final GuessingGameRibble ribbles = gson.fromJson(content, GuessingGameRibble.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tip = (String) ribbles.getTip();
                                        ribble = (String) ribbles.getRibble();
                                        answer = (String) ribbles.getAnswer();
                                        ((TextView) findViewById(R.id.ribble)).setText(ribble);
                                    }
                                });

                            }
                        });

                        while (currentSecond >= 0) {
                            try {
                                String second = String.format("%02d", currentSecond);
                                time.setText(second);
                                TimeUnit.SECONDS.sleep(1);
                                if (currentSecond == 30) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((TextView) findViewById(R.id.tip)).setText("提示：" + tip);
                                        }
                                    });
                                } else if (currentSecond == 60) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((TextView) findViewById(R.id.tip)).setText("");
                                        }
                                    });
                                }
                                if (gradestatus0 == gradestatus1) {
                                    gradestatus0 = 0;
                                    break;
                                }
                                currentSecond--;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.ribble)).setText("正确答案：" + answer);
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((Button) findViewById(R.id.btn)).setEnabled(true);
                            }
                        });

                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    double user1_grade = Double.valueOf(((TextView) findViewById(R.id.user1_grade)).getText().toString());
                    double user2_grade = Double.valueOf(((TextView) findViewById(R.id.user2_grade)).getText().toString());
                    double user3_grade = Double.valueOf(((TextView) findViewById(R.id.user3_grade)).getText().toString());
                    double user4_grade = Double.valueOf(((TextView) findViewById(R.id.user4_grade)).getText().toString());
                    String winner = "没有人";
                    if (user1_grade > user2_grade && user1_grade > user3_grade && user1_grade > user4_grade) {
                        winner = ((TextView) findViewById(R.id.user1)).getText().toString();
                    } else if (user2_grade > user1_grade && user2_grade > user3_grade && user2_grade > user4_grade) {
                        winner = ((TextView) findViewById(R.id.user2)).getText().toString();
                    } else if (user3_grade > user1_grade && user3_grade > user2_grade && user3_grade > user4_grade) {
                        winner = ((TextView) findViewById(R.id.user3)).getText().toString();
                    } else if (user4_grade > user1_grade && user4_grade > user2_grade && user4_grade > user3_grade) {
                        winner = ((TextView) findViewById(R.id.user4)).getText().toString();
                    }


                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            selfbutton(place);
                        }
                    });

                    Toast.makeText(getApplicationContext(), winner + "获得胜利！", Toast.LENGTH_SHORT).show();

                    changeStatus(0);
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String gameurl = MessageFormat.format("http://{0}:8080/GuessingGameAPI/GameStop?table_id={1}", ip, tableId);
                    Request gamerequest = new Request.Builder().url(gameurl).build();
                    okHttpClient.newCall(gamerequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String content = response.body().string();
                        }
                    });
                    Looper.loop();


                }
            });
            thread.start();


        } else if (lastcheck == 0) {
            String gameurl = MessageFormat.format("http://{0}:8080/GuessingGameAPI/GameStop?table_id={1}", ip, tableId);
            Request gamerequest = new Request.Builder().url(gameurl).build();
            okHttpClient.newCall(gamerequest).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String content = response.body().string();
                }
            });
        }
    }

    private void selfbutton(int place) {

        if (place == 1) {
            user1button.setEnabled(true);
            user2button.setEnabled(false);
            user3button.setEnabled(false);
            user4button.setEnabled(false);
        } else if (place == 2) {
            user2button.setEnabled(true);
            user1button.setEnabled(false);
            user3button.setEnabled(false);
            user4button.setEnabled(false);
        } else if (place == 3) {
            user3button.setEnabled(true);
            user1button.setEnabled(false);
            user2button.setEnabled(false);
            user4button.setEnabled(false);
        } else {
            user4button.setEnabled(true);
            user1button.setEnabled(false);
            user2button.setEnabled(false);
            user3button.setEnabled(false);
        }
    }

    private void changeStatus(int i) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/UserStatus?id={1}&status={2}", ip, userId, i);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Failed on change status", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content = response.body().string();
            }
        });
    }

    public void user1status(View view) {
        if ("准备".equals(user1button.getText().toString())) {
            changeStatus(1);
        } else {
            changeStatus(0);
        }
    }

    public void user2status(View view) {
        if ("准备".equals(user2button.getText().toString())) {
            changeStatus(1);
        } else {
            changeStatus(0);
        }
    }

    public void user3status(View view) {
        if ("准备".equals(user3button.getText().toString())) {
            changeStatus(1);
        } else {
            changeStatus(0);
        }
    }

    public void user4status(View view) {
        if ("准备".equals(user4button.getText().toString())) {
            changeStatus(1);
        } else {
            changeStatus(0);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            setupgame();
            chat();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    private void chat() {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/GetChat?table_id={1}", ip, tableId);
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content = response.body().string();
                Gson gson = new Gson();
                MyBean[] chat = gson.fromJson(content, MyBean[].class);
                ArrayList<MyBean> list = new ArrayList<MyBean>();
                if (length != chat.length) {
                    for (int i = 0; i < chat.length; i++) {
                        MyBean myBean = chat[i];
                        if (myBean.getName().equals(username)) {
                            myBean.setNumber(1);
                        } else {
                            myBean.setNumber(2);
                        }
                        list.add(myBean);
                    }
                    length = chat.length;
                    final MyAdapter adapter = new MyAdapter(getApplicationContext());
                    adapter.setData(list);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rv.setAdapter(adapter);
                            LinearLayoutManager manager = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.VERTICAL, false);
                            rv.setLayoutManager(manager);
                            rv.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    });
                }

            }
        });
    }

    public void doChat(View view) {
        String data = ((TextView) findViewById(R.id.et)).getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String systime = dateFormat.format(date);

        if (answer.equals(data)) {
            ((Button) findViewById(R.id.btn)).setEnabled(false);
            grade = grade + Integer.parseInt(time.getText().toString());
            data = "***";
            OkHttpClient okHttpClient = new OkHttpClient();
            String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/AddGrade?id={1}&grade={2}", ip, userId, grade);
            Request request = new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String content = response.body().string();
                }
            });
        }


        if (data != null && data != "") {
            OkHttpClient okHttpClient = new OkHttpClient();
            String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/SetChat?table_id={1}&name={2}&time={3}&data={4}", ip, tableId, username, systime, data);
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String content = response.body().string();
                }
            });
            ((TextView) findViewById(R.id.et)).setText("");
        }
    }
}
