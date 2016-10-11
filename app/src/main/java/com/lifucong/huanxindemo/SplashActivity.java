package com.lifucong.huanxindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lifucong.huanxindemo.user.LoginFragment;
import com.lifucong.huanxindemo.user.RegisterFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SplashActivity extends AppCompatActivity {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.button_login)
    public void showLoginDialog() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        loginFragment.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.button_register)
    public void showRegisterDialog() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        registerFragment.show(getSupportFragmentManager(), null);
    }

}
