package swu.xl.a0228_pinunlock;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //文本框
    private TextView textView = null;
    //编辑框
    private EditText editText = null;
    //锁视图
    private Lock lock = null;

    //密码
    private String password = null;
    //第一次设置的密码
    private String firPassword = null;

    //SharedPreference Key_Name
    private String SHARE_NAME = "Shared";
    private String SHARE_KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);

        //Editor
        final SharedPreferences.Editor edit = sharedPreferences.edit();

        //屏幕的密度
        final float density = getResources().getDisplayMetrics().density;

        //找到根布局
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root_layout);

        //找到线性容器布局
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.liner_content);

        //找到控件
        textView = findViewById(R.id.pin_unlock_text);
        editText = findViewById(R.id.pin_unlock_edit);

        //根据密码的情况判断显示的文本内容
        password = sharedPreferences.getString(SHARE_KEY, null);
        textView.setText(password == null ? "请设置密码" : "请输入密码");

        //锁视图
        lock = new Lock(this);

        //添加锁视图
        editText.post(new Runnable() {
            @Override
            public void run() {
                //设置布局参数
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        Utils.dpToPx(80, density),
                        Utils.dpToPx(30, density));

                //设置坐标
                layoutParams.leftMargin = editText.getLeft()+Utils.dpToPx(16, density);
                layoutParams.topMargin = linearLayout.getTop()+editText.getTop()+Utils.dpToPx(10, density);

                //添加
                relativeLayout.addView(lock,layoutParams);
            }
        });

        //监听控件是否获得焦点
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //上移动画
                    lockMoveWithAnimation(0, Utils.dpToPx(-25, density));
                }else{
                    //下移动画
                    lockMoveWithAnimation(Utils.dpToPx(-25, density), 0);
                }
            }
        });

        //监听控件文本改变
        editText.addTextChangedListener(new TextWatcher() {
            //文本改变前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文本改变时
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //文本改变后
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //监听键盘Return被点击
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //判断Return
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    //判断是设置密码还是输入密码
                    if (password == null){
                        //判断是否是第一次设置密码
                        if (firPassword == null){
                            firPassword = editText.getText().toString();

                            textView.setText("请再次输入你的密码");
                        }else {
                            //判断两次输入的密码是否正确
                            if (firPassword.equals(editText.getText().toString())){
                                textView.setText("密码设置成功");

                                //清空firPassword
                                firPassword = null;

                                //保存密码
                                edit.putString(SHARE_KEY,editText.getText().toString());
                                edit.apply();

                                //界面跳转
                                jumpToNextActivity();
                            }
                        }
                    }else{
                        //判断密码是否正确
                        if (password.equals(editText.getText().toString())){
                            textView.setText("密码正确");

                            //跳入到下一个界面
                            jumpToNextActivity();
                        }else {
                            textView.setText("密码错误");
                        }
                    }

                    //清空输入框的文本内容
                    editText.setText("");

                    //隐藏键盘
                    closeKeyboard();
                }

                return false;
            }
        });

        //重置密码按钮
        Button restBtn = (Button) findViewById(R.id.reset_btn);
        restBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = null;
                edit.putString(SHARE_KEY, null);
                edit.apply();

                textView.setText("请重新设置密码");
            }
        });
    }

    //监听屏幕的触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //判断是否点击屏幕
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            closeKeyboard();
        }

        return super.onTouchEvent(event);
    }

    /**
     * 隐藏键盘
     */
    private void closeKeyboard(){
        //1.获取系统输入的管理器
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //2.隐藏键盘
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);

        //3.取消焦点
        if(getCurrentFocus() != null){
            getCurrentFocus().clearFocus();
        }
    }

    /**
     * 跳转界面
     */
    private void jumpToNextActivity(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    /**
     * 动画
     * @param start
     * @param end
     */
    private void lockMoveWithAnimation(int start, int end){
        //移动动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(lock, "translationY", start,end);
        animator.setDuration(500);
        animator.start();

        //颜色变化
        if(start == 0){
            //锁视图 变色
            lock.getLockImgView().setBackgroundResource(R.drawable.lock2);
            lock.getLockTextView().setTextColor(Color.GREEN);
        }else {
            //锁视图 变色
            lock.getLockImgView().setBackgroundResource(R.drawable.lock1);
            lock.getLockTextView().setTextColor(Color.BLACK);
        }
    }
}
