package com.lifucong.apphx.conversation;

import android.os.Bundle;

import com.hyphenate.easeui.ui.EaseConversationListFragment;

public class HxConversationListFragment extends EaseConversationListFragment{

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customUi();
    }

    private void customUi() {
        hideTitleBar();
    }
}
