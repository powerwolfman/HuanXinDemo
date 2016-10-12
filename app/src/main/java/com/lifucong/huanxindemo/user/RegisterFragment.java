package com.lifucong.huanxindemo.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lifucong.huanxindemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/11.
 */

public class RegisterFragment extends DialogFragment {

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
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    //用来初始设置视图
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
    public void register() {
        String username=editUsername.getText().toString().trim();
        String password=editPassword.getText().toString().trim();
        handleRegister(username,password);
    }

    private void handleRegister(final String username,final String password){
        //核心Api
        //是不是有網絡連接呢？？？应该是有！能在这里使用，你不怕ANR？？
        //这样做？视图表现都没有？

        //开始Loading视图
        startLoading();
        //执行注册api
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(username,password);
                    //未出现异常，表示成功，开始做一些视图上的工作
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopLoading();
                            showRegisterSuccess();
                            dismiss();
                        }
                    });
                } catch (final HyphenateException e) {
                    //出现异常，表示失败，开始做一些视图上的工作
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopLoading();
                            showRegisterFail(e.getDescription());
                        }
                    });
                }
            }
        }).start();
    }

    public void startLoading(){
        setCancelable(false);
        buttonConfirm.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void stopLoading(){
        setCancelable(true);
        buttonConfirm.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    //注册成功视图
    public void showRegisterSuccess(){
        Toast.makeText(getContext(), R.string.user_register_success, Toast.LENGTH_SHORT).show();
    }

    //注册失败视图
    public void showRegisterFail(String msg){
        String info=getString(R.string.user_error_register_fail,msg);
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }
}
