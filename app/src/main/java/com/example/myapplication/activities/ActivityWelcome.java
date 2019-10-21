package com.example.myapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.models.ModelAuthHash;
import com.example.myapplication.models.ModelResponse;
import com.example.myapplication.models.ModelResponseLogin;
import com.example.myapplication.utils.API;
import com.example.myapplication.utils.APIs;
import com.example.myapplication.utils.Tools;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityWelcome extends AppCompatActivity {
    public static boolean active = false;
    private final ArrayList<Animator> animators = new ArrayList<Animator>();
    Animator animator;
    Button buttonSignIn;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextCaptcha;
    ImageButton imageButtonRefresh;
    ImageButton imageButtonHelp;
    ImageView imageViewCaptcha;
    LinearLayout linearLayout;
    TextView textViewHint;
    TextView textViewLOGO;
    ModelAuthHash modelAuthHash;
    ModelResponseLogin modelResponseLogin;
    // LOGO 的 params
    RelativeLayout.LayoutParams params;

    public Animation shake() {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(5)); // 抖动 5 次
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    // infinite rotate
    public Animation rotate() {
        Animation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true); // 设置保持动画最后的状态
        animation.setDuration(1000); // 设置动画时间
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new LinearInterpolator()); // 设置插入器
        return animation;
    }

    protected void prepareAnimation() {
        // 因为一开始控件都是可见的，所以我们在这里把他们都弄不可见
        editTextUsername.setVisibility(View.GONE);
        editTextPassword.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        textViewHint.setVisibility(View.GONE);

        animator = ObjectAnimator.ofFloat(editTextUsername, "alpha", 0.f, 0.5f, 1.f);
        animator.setDuration(500).setStartDelay(100);
        animators.add(animator);
        animator = ObjectAnimator.ofFloat(editTextPassword, "alpha", 0.f, 0.5f, 1.f);
        animator.setDuration(500).setStartDelay(200);
        animators.add(animator);
        animator = ObjectAnimator.ofFloat(linearLayout, "alpha", 0.f, 0.5f, 1.f);
        animator.setDuration(500).setStartDelay(300);
        animators.add(animator);
        animator = ObjectAnimator.ofFloat(textViewHint, "alpha", 0.f, 0.5f, 1.f);
        animator.setDuration(500).setStartDelay(400);
        animators.add(animator);
    }

    protected void prepareOnClickListener() {
        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The anonymous Runnable has an implicit reference to the
                // enclosing class (e.g. your Activity or Fragment),
                // preventing it from being garbage collected until the thread completes.
                // Instead of creating a new thread each time you want to perform a network
                // operation, you could use a single thread executor service too.

                new RefreshCaptchaTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果动画还没播放过（editTextUsername还不可见）
                if (editTextUsername.getVisibility() == View.GONE) {
                    animationShowingWidgets();
                } else {
                    // 在執行 AsyncTask 的時候，不要用 execute() 方法，要用 executeOnExecutor()，前者會
                    // 等之前的Task完成才繼續執行，而有一個 Task 用來實時更新時間，於是用 execute() 就永遠無法執行
                    new LoginTask(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
    }

    protected void animationShowingWidgets() {
        final int topMargin = params.topMargin;

        // 按下按钮 EditText 以动画形式播放
        editTextUsername.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        textViewHint.setVisibility(View.VISIBLE);
        editTextUsername.setAlpha(0f);
        editTextPassword.setAlpha(0f);
        linearLayout.setAlpha(0f);
        textViewHint.setAlpha(0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.start();

        // 弹簧动画系统
        SpringSystem springSystem = SpringSystem.create();
        Spring springLOGO = springSystem.createSpring();
        springLOGO.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(20, 5));
        springLOGO.setCurrentValue(0f);
        springLOGO.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                params.topMargin = topMargin + (int) value;
                textViewLOGO.setLayoutParams(params);
            }
        });
        springLOGO.setEndValue(-320f);
    }

    // start animate and disable buttons when refreshing
    protected void animationRefreshingCaptcha(final boolean setRefreshing) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (setRefreshing)
                    imageButtonRefresh.startAnimation(rotate());
                else
                    imageButtonRefresh.clearAnimation();

                buttonSignIn.setClickable(!setRefreshing);
                imageButtonRefresh.setClickable(!setRefreshing);
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        textViewLOGO = (TextView) findViewById(R.id.textViewLOGO);
        textViewHint = (TextView) findViewById(R.id.textViewHint);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextCaptcha = (EditText) findViewById(R.id.editTextCaptcha);
        imageViewCaptcha = (ImageView) findViewById(R.id.imageViewCaptcha);
        imageButtonRefresh = (ImageButton) findViewById(R.id.imageButtonRefresh);
        imageButtonHelp = (ImageButton) findViewById(R.id.imageButtonHelp);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        // set position
        params = (RelativeLayout.LayoutParams) textViewLOGO.getLayoutParams();
        params.topMargin = Tools.getScreenHeight() / 3;
        textViewLOGO.setLayoutParams(params);
        prepareAnimation();
        prepareOnClickListener();
        active = true;
        TooltipCompat.setTooltipText(imageButtonRefresh, "刷新验证码");
        TooltipCompat.setTooltipText(imageButtonHelp, "获取帮助");
    }

    public void setEnableStatus(boolean enable) {
        buttonSignIn.setEnabled(enable);
        imageButtonRefresh.setEnabled(enable);
        editTextUsername.setEnabled(enable);
        editTextPassword.setEnabled(enable);
        editTextCaptcha.setEnabled(enable);
    }

    private class RefreshCaptchaTask extends AsyncTask<Void, Void, String> {
        Bitmap decodedByte;

        @Override
        protected void onPreExecute() {
            // 执行任务前，我们给按钮加个动画
            animationRefreshingCaptcha(true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 执行完任务后，动画停止
            animationRefreshingCaptcha(false);
            // 因为我们自己规定：返回空字符串表示正常
            if (s.isEmpty()) {
                imageViewCaptcha.setImageBitmap(decodedByte);
            } else {
                Toast.makeText(getApplicationContext(), "刷新验证码失败：" + s, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                API api = new API(getApplicationContext());
                String raw = api.authHash();

                modelAuthHash = JSON.parseObject(raw, ModelAuthHash.class);
                if (modelAuthHash.getCode() != 0) {
                    return modelAuthHash.getMsg();
                } else {
                    byte[] decodedString = Base64.decode(modelAuthHash.getCaptcha(), Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }
                // 正常，返回空字符串
                return "";
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onCancelled() {
            // 如果任务被取消也是要停止动画
            animationRefreshingCaptcha(false);
        }
    }

    private class LoginTask extends AsyncTask<Void, String, String> {
        String studentName;
        String token;
        Context context;

        private LoginTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
            // 正常判断处理登录
            if (editTextUsername.getText().toString().trim().isEmpty() || editTextUsername.getText().toString().length() != 18) {
                editTextUsername.startAnimation(shake());
                cancel(true); // 取消任务
                return;
            }
            if (editTextPassword.getText().toString().trim().isEmpty()) {
                editTextPassword.startAnimation(shake());
                cancel(true);
                return;
            }
            if (editTextCaptcha.getText().toString().trim().isEmpty()) {
                editTextCaptcha.startAnimation(shake());
                cancel(true);
                return;
            }
            // 防止用户乱点
            setEnableStatus(false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.isEmpty()) {
                // 登陆成功并且课表也加载成功
                context.startActivity(new Intent(context, ActivityMain.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "错误：" + s, Toast.LENGTH_SHORT).show();
                textViewHint.setText(R.string.login_hint_text);
                setEnableStatus(true);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[0].equals("0")) {
                textViewHint.setText(R.string.login_hint_text);
            } else {
                textViewHint.setText(values[0]);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            setEnableStatus(true);
        }


        @Override
        protected String doInBackground(Void... voids) {
            DBHelper db = new DBHelper(getApplicationContext());
            API api = new API(getApplicationContext());
            try {// 试一下状态机的写法
                int status = 0;
                final int STATUS_BEGIN = 0;
                final int STATUS_END = 1;
                final int STATUS_OTHER_ERROR = 2;
                final int STATUS_CAPTCHA_NOT_INITIALIZED = 3;
                final int STATUS_CAPTCHA_INITIALIZED = 4;
                final int STATUS_RESULT_IS_NULL = 5;
                final int STATUS_RESULT_IS_NOT_NULL = 6;
                final int STATUS_RET_CODE_OK = 7;
                final int STATUS_RET_CODE_ERROR = 8;
                String result = null;

                while (true) {
                    switch (status) {
                        case STATUS_BEGIN:
                            if (modelAuthHash == null)
                                status = STATUS_CAPTCHA_NOT_INITIALIZED;
                            else
                                status = STATUS_CAPTCHA_INITIALIZED;
                            break;
                        case STATUS_CAPTCHA_INITIALIZED:
                            publishProgress("正在请求登录");

                            api.setForceUpdate(true);
                            result = api.authLogin(modelAuthHash.getKey(),
                                    editTextUsername.getText().toString(),
                                    editTextPassword.getText().toString(),
                                    modelAuthHash.getToken(),
                                    modelAuthHash.getCookies(),
                                    editTextCaptcha.getText().toString());
                            if (result.isEmpty()) {
                                status = STATUS_RESULT_IS_NULL;
                            } else {
                                status = STATUS_RESULT_IS_NOT_NULL;
                            }
                            break;
                        case STATUS_RESULT_IS_NOT_NULL:
                            publishProgress("正在解析数据");
                            modelResponseLogin = JSON.parseObject(result, ModelResponseLogin.class);
                            if (modelResponseLogin.getCode() != 0)
                                status = STATUS_RET_CODE_ERROR;
                            else  // 登陆成功，可以进行下一步操作
                                status = STATUS_RET_CODE_OK;
                            break;
                        case STATUS_CAPTCHA_NOT_INITIALIZED:
                            publishProgress("0");
                            return "请先刷新验证码";
                        case STATUS_RESULT_IS_NULL:
                            publishProgress("0");
                            return "服务器返回空 NULL";
                        case STATUS_RET_CODE_ERROR:
                            publishProgress("0");
                            return modelResponseLogin.getMsg() + " " + modelResponseLogin.getDetail();
                        case STATUS_RET_CODE_OK:
                            studentName = modelResponseLogin.getStudent_name();
                            token = modelResponseLogin.getToken();

                            db.setAPIRecord(APIs.AUTH_LOGIN, result);

                            publishProgress("欢迎您，" + studentName + "！正在为您更新课表，请稍后...");

                            result = api.timetable(modelResponseLogin.getToken(), api.semester_get().getSemester(), 0);
                            ModelResponse response = JSON.parseObject(result, ModelResponse.class);

                            if (response.getCode() == 0) {
                                db.setAPIRecord(APIs.TIMETABLE, result);
                                publishProgress("一切准备就绪，欢迎使用MUST+！");
                                Thread.sleep(1000);
                                return "";
                            } else {
                                if (response.getCode() == -7003) { // Cookie 过期
                                    db.removeRecord(APIs.AUTH_LOGIN);
                                }
                                db.removeRecord(APIs.TIMETABLE);
                                publishProgress("0");
                                return response.getMsg();
                            }
                    }
                }

            } catch (IOException | InterruptedException e) {
                db.removeRecord(APIs.TIMETABLE);
                publishProgress("0");
                return e.getMessage();
            }

        }
    }
}
