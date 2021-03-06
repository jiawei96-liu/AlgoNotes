package com.software.ustc.superspy.activity;
//ipackage com.software.ustc.superspy.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.software.ustc.superspy.R;
import com.software.ustc.superspy.db.litepal.LoginData;
import com.software.ustc.superspy.kits.AppUsageUtil;
import com.software.ustc.superspy.kits.BaseActivity;
import com.software.ustc.superspy.service.AppDbPrepareService;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView mBtnLogin;

    private View progress;

    private View mInputLayout;

    private float mWidth, mHeight;

    private LinearLayout mName, mPsw;

    private CheckBox rememberpass;
    private EditText accountEdit;
    private  EditText passwordEdit;
    private TextView roll;
    private TextView getpass;
    private ImageView disply1;
    private int isvisiable=0;
    private ImageView delete;

    private final int WELCONE_DISPLAY_LENGHT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //权限检查
        AppUsageUtil.checkUsageStateAccessPermission(this);
        initView();




    }

    private void initView() {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        getpass = findViewById(R.id.getpass);

        /////////////////////////////
        LitePal.initialize(this);

        Connector.getDatabase();
        roll=findViewById(R.id.cbLogin);
        accountEdit=findViewById(R.id.cUsername);
        passwordEdit=findViewById(R.id.cPassward);
        rememberpass=findViewById(R.id.b_remember_pwd);
        LoginData movie = LitePal.find(LoginData.class,1);
        if(movie!=null) {
            LoginData loginId1 = LitePal.find(LoginData.class, 1);
            accountEdit.setText(loginId1.getUsername());
            passwordEdit.setText(loginId1.getPassward());
        }
        else {
            LoginData login1 = new LoginData();
            login1.setUsername("");
            login1.setPassward("");
            login1.setVertify1("");
            login1.setVertifyid1("");
            login1.setVertify2("");
            login1.setVertifyid2("");
            login1.setId(1);
            login1.save();
            LoginData login2 = new LoginData();
            login2.setUsername("");
            login2.setPassward("");
            login2.setVertify1("");
            login2.setVertifyid1("");
            login2.setVertify2("");
            login2.setVertifyid2("");
            login2.setId(2);
            login2.save();
            LoginData login3 = new LoginData();
            login3.setUsername("");
            login3.setPassward("");
            login3.setVertify1("");
            login3.setVertifyid1("");
            login3.setVertify2("");
            login3.setVertifyid2("");
            login3.setId(3);
            login3.save();


        }

        //////////////////////////

        mBtnLogin.setOnClickListener(this);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RollActivity.class);
                startActivity(intent);
                finish();

            }
        });
        getpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,PasswordGetActivity.class);
                startActivity(intent);
                finish();
            }
        });

        disply1=findViewById(R.id.displypass);
        disply1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isvisiable==0) {
                    disply1.setImageResource(R.drawable.xianshi);
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isvisiable=1;
                }
                else {
                    disply1.setImageResource(R.drawable.yincang);
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isvisiable=0;

                }

            }
        });
        delete=findViewById(R.id.delete1);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountEdit.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {

        // 计算出控件的高与宽
        ///////////////////////
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        List<LoginData> logins = LitePal.findAll(LoginData.class);
        int flag=0;
        for (LoginData data1 : logins) {
            if (account.equals(data1.getUsername()) && password.equals(data1.getPassward())) {
                flag = 1;
                break;
            }
        }

        Toast toast = Toast.makeText(LoginActivity.this,null,Toast.LENGTH_SHORT);
        if(account.equals("") && password.equals(""))
            flag=0;
        if (flag == 1) {
            if (rememberpass.isChecked()) {
                LoginData login1 = new LoginData();
                login1.setUsername(account);
                login1.setPassward(password);
                login1.updateAll("id=?", "1");

            }
            else{
                LoginData login1 = new LoginData();
                login1.setUsername("");
                login1.setPassward("");
                login1.updateAll("id=?", "1");
            }
            LoginData login2 = new LoginData();
            login2.setUsername(account);
            login2.setPassward(password);
            login2.updateAll("id=?", "2");
            toast.setText("登录成功");
            mWidth = mBtnLogin.getMeasuredWidth();
            mHeight = mBtnLogin.getMeasuredHeight();
            // 隐藏输入框
            mName.setVisibility(View.INVISIBLE);
            mPsw.setVisibility(View.INVISIBLE);

            inputAnimator(mInputLayout, mWidth, mHeight);

        } else {
            toast.setText("用户名或密码错误");
        }
        toast.show();
        ///////////////////////////



    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                Thread myThread=new Thread(){//创建子线程
                    @Override
                    public void run() {
                        try{
                            sleep(WELCONE_DISPLAY_LENGHT);
                            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent1);
                            finish();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                myThread.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });


    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                return 0;
            }
        });
//        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }
}