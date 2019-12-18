package com.example.guessinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private final static String TAG=MainActivity.class.getSimpleName();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private String ip="192.168.43.30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText usernameEdit=((EditText)findViewById(R.id.username));
        EditText passwordEdit=((EditText)findViewById(R.id.password));
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass=((CheckBox)findViewById(R.id.remember_pass));
        boolean isRemenber=pref.getBoolean("remember_password",false);
        if(isRemenber){
            String username=pref.getString("username","");
            String password=pref.getString("password","");
            usernameEdit.setText(username);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
    }

    public void doLogin(View view) {
        final String username=((EditText)findViewById(R.id.username)).getText().toString();
        final String password=((EditText)findViewById(R.id.password)).getText().toString();
        if(username==null||username.isEmpty()) {
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if (password==null||password.isEmpty()) {
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        Runnable runnable = new Runnable() {
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = MessageFormat.format("http://{0}:8080/GuessingGameAPI/Login?username={1}&password={2}", ip, username, MD5Utils.md5(password));
                final Request request = new Request.Builder().url(url).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "Failed on Login", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String content = response.body().string();
                        Log.i(TAG, "onResponse: " + content);
                        Gson gson = new Gson();
                        Map<String, Object> result = gson.fromJson(content, Map.class);
                        String status = (String) result.get("status");
                        if ("success".equals(status)) {
                            Map<String, Object> user = (Map<String, Object>) result.get("user");
                            int id = (int) (double) user.get("id");
                            SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editoruser = sp.edit();
                            editoruser.putInt("id", id);
                            editoruser.putString("username", username);
                            editoruser.commit();

                            editor = pref.edit();
                            if (rememberPass.isChecked()) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("username", username);
                                editor.putString("password", password);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
                            intent.putExtra("user_id", id);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            Looper.loop();

                        } else if ("failed_exist".equals(status)) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "该账号已登录", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            }
        };new Thread(runnable).start();
    }

    public void doSignin(View view) {
        Intent i = new Intent(MainActivity.this, SigninActivity.class);
        startActivity(i);
    }

}
