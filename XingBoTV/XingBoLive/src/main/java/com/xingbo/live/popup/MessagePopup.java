package com.xingbo.live.popup;

import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.xingbo.live.R;
import com.xingbo.live.adapter.SecretMsgNotifyAdapter;
import com.xingbo.live.entity.msg.CommonMsg;
import com.xingbo.live.ui.MainRoom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/18.
 */
public class MessagePopup extends PopupWindow {
    private static final String TAG = "MessagePopup";

    private List<CommonMsg> msgList = new ArrayList<CommonMsg>();

    private MainRoom act;

    public MessagePopup(MainRoom act) {
        this.act = act;
        View view = View.inflate(act, R.layout.popup_message, null);
        TextView secret = (TextView) view.findViewById(R.id.popup_message_secret);
        TextView systemMsf = (TextView) view.findViewById(R.id.popup_message_system_msg);
        TextView neglectAll = (TextView) view.findViewById(R.id.popup_message_neglect_all);
        ListView listView = (ListView) view.findViewById(R.id.listview_popup_message);
        SecretMsgNotifyAdapter mAdapter = new SecretMsgNotifyAdapter(act, msgList);
        listView.setAdapter(mAdapter);

    }
}
