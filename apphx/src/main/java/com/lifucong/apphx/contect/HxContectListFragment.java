package com.lifucong.apphx.contect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.lifucong.apphx.R;
import com.lifucong.apphx.contect.search.AddContactActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */

public class HxContectListFragment extends EaseContactListFragment implements EMContactListener {

    private EMContactManager mEMContactManager;// 联系人管理APi
    private List<String> contacts;// 联系人集合(从环信服务器获取到的)

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 自定制UI
        customUI();
        mEMContactManager= EMClient.getInstance().contactManager();
        mEMContactManager.setContactListener(this);
        //异步获取联系人
        asyncGetContactFromServer();
        
        //获取一次联系人列表数据，且刷新一次当前List控件
        //1.Emclient---EMContectManager-getAll联系人（从环信获取）
        //1.1 拿到的将是一个List<String>
        //1.2 当前EaseUI的EaseContactListFragment中，联系人EaseUser
        //2. 将联系人EaseUser设置入到当前列表数据中EaseContactListFragment中的setContectsMap()
       //3. EaseContactListFragment中的refresh方法

        //最後 再结合上联系人监听 ，一切搞定。
    }

    private void customUI() {
        super.hideTitleBar();
        setHeaderView();
    }

    private void setHeaderView() {
        View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.partial_hx_contact_list_header, listView, false);
        // “添加新朋友”view
        View addContacts = headerView.findViewById(R.id.layout_add_contacts);
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddContactActivity.class);
                startActivity(intent);
            }
        });
        // "邀请和通知"view
        View notifications = headerView.findViewById(R.id.layout_notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(getContext(), "邀请和通知", Toast.LENGTH_SHORT).show();
            }
        });
        ListAdapter adapter = listView.getAdapter();
        listView.setAdapter(null);
        // note: 在Android4.4之前，此方法只能在ListView设置适配器之前调用
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
    }

    private void asyncGetContactFromServer() {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    //从环信服务器获取到所有联系人
                    contacts = mEMContactManager.getAllContactsFromServer();
                    //刷新联系人
                    refreshContacts();
                } catch (HyphenateException e) {
                    Log.e("apphx","asyncGetContactFromServer! Exception");
                }
            }
        };
        new Thread(runnable).start();
    }

    private void refreshContacts() {
        Map<String, EaseUser>hashMap=new HashMap<>();
        for (String hxId:contacts) {
            EaseUser easeUser=new EaseUser(hxId);
            hashMap.put(hxId,easeUser);
        }
        //设置当前视图上的联系人
        super.setContactsMap(hashMap);
        //刷新当前视图上的联系人
        super.refresh();
    }
    // 联系人监听 start ----------------------------------------
    // 添加联系人
    @Override public void onContactAdded(String s) {
        contacts.add(s);
        refreshContacts();
    }

    // 删除联系人
    @Override public void onContactDeleted(String s) {
        contacts.remove(s);
        refreshContacts();
    }

    // 收到好友邀请
    @Override public void onContactInvited(String s, String s1) {
        // TODO: 2016/10/11 0011 显示好友邀请信息(同意，拒绝的交互按钮)
    }

    // 好友请求被同意
    @Override public void onContactAgreed(String s) {
        // TODO: 2016/10/11 0011 当对方同意你的好友申请, 显示对方已接受
    }

    // 好友请求被拒绝
    @Override public void onContactRefused(String s) {
        // TODO: 2016/10/11 0011 当对方拒绝你的好友申请，显示对方已拒绝(一般不做处理)
    }
    // 联系人监听 end ----------------------------------------
}
