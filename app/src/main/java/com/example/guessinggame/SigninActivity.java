package com.example.guessinggame;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class SigninActivity extends AppCompatActivity {
    private final static String TAG=SigninActivity.class.getSimpleName();
    private String ip="192.168.1.102";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void Signin(View view) {
        String username=((EditText)findViewById(R.id.username)).getText().toString();
        String password=((EditText)findViewById(R.id.password)).getText().toString();
        String passwordAgain=((EditText)findViewById(R.id.passwordAgain)).getText().toString();
        if(username==null||username.isEmpty()) {
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if (password==null||password.isEmpty()) {
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordAgain==null||passwordAgain.isEmpty()) {
            Toast.makeText(getApplicationContext(), "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.length()<6){
            Toast.makeText(getApplicationContext(),"密码长度过短",Toast.LENGTH_SHORT).show();
            return;
        }else if (!password.equals(passwordAgain)){
            Toast.makeText(getApplicationContext(),"两次密码不同",Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient okHttpClient=new OkHttpClient();
        String url= MessageFormat.format("http://{0}:8080/GuessingGameAPI/Signin?username={1}&password={2}",ip,username,MD5Utils.md5(password));
        final Request request=new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"Failed on Signin",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String content=response.body().string();
                Gson gson = new Gson();
                Map<String,Object> result = gson.fromJson(content, Map.class);
                String status = (String) result.get("status");
                if ("success".equals(status)){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    SigninActivity.this.finish();
                    Looper.loop();
                }else{
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });


    }
}
