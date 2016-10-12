package com.lifucong.huanxindemo.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lifucong.huanxindemo.HomeActivity;
import com.lifucong.huanxindemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/11.
 */

public class LoginFragment extends DialogFragment {

    @BindView(R.id.edit_username)
    TextInputEditText editUsername;
    @BindView(R.id.edit_password)
    TextInputEditText editPassword;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.button_confirm)
    Button buttonConfirm;
    private Unbinder unbinder;

    // 用來create视图
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置对话框无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_confirm)
    public void login() {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        handleLogin(username, password);
    }

    private void handleLogin(String username, String password) {
        startLoading();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            String info = getString(R.string.user_error_not_null);
            stopLoading();
            showLoginFail(info);
            return;
        }
        EMClient.getInstance().login(username, password, new EMCallBack() {
            // 登录成功
            @Override public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        stopLoading();
                        navigateToHome();
                    }
                });
            }

            // 登录失败
            @Override public void onError(int i, final String s) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        stopLoading();
                        showLoginFail(s);
                    }
                });
            }

            @Override public void onProgress(int i, String s) {
            }
        });
    }
    // 视图实现 start ----------------------------------------------
    public void startLoading() {
        buttonConfirm.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        setCancelable(false);
    }

    public void stopLoading() {
        buttonConfirm.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        setCancelable(true);
    }

    public void showLoginFail(String msg) {
        String info = getString(R.string.user_error_login_fail, msg);
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

    public void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    // 视图实现 end ----------------------------------------------
}
