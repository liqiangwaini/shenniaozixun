package com.xingbo.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.adapter.UserMsgPrivateDetailAdapter;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.MessageOwner;
import com.xingbo.live.entity.MessagePrivateDetail;
import com.xingbo.live.entity.MessagePrivateDetailPage;
import com.xingbo.live.entity.MessageUser;
import com.xingbo.live.entity.model.MessagePrivateDetailModel;
import com.xingbo.live.fragment.UserMsgPrivateFragment;
import com.xingbo.live.fragment.UserMsgSystemFragment;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.CommonUtil;
import com.xingbo.live.util.SoftInputUtils;
import com.xingbobase.config.XingBo;
import com.xingbobase.fragment.BaseFragment;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/8/8.
 */
public class PrivateMsgDetailDialog extends DialogFragment implements View.OnClickListener/*,View.OnLayoutChangeListener*/, AbsListView.OnScrollListener {
    private static final String TAG = "PrivateMsgDialog";
    public static final String SENDER_ID = "sernder_id";

    private View view;
    private FrameLayout back;
    private TextView nick;
    private ListView listView;

    private String senderId;
    private String nickname;
    private List<MessagePrivateDetail> detailList = new ArrayList<>();
    private MessageOwner owner;
    private MessageUser user;
    private UserMsgPrivateDetailAdapter mAdapter;
    private EditText edit;
    private TextView send;
    private FrameLayout placeHolder;

    public PrivateMsgDetailDialog() {
    }

    public void setInitData(String senderId, String nickname) {
        this.senderId = senderId;
        this.nickname = nickname;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_room_pri_msg_detail, null);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = XingBoConfig.MAIN_ROOM_PRI_MSG_HEIGHT;
        getDialog().getWindow().setAttributes(lp);
        initView();
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setCanceledOnTouchOutside(true);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.style_popup);
        window.setBackgroundDrawableResource(R.color.transparent);
        return dialog;
    }

    public void initView() {
        back = (FrameLayout) view.findViewById(R.id.pri_msg_detail_back);
        nick = (TextView) view.findViewById(R.id.pri_msg_detail_nick);
        listView = (ListView) view.findViewById(R.id.pri_msg_detail_lv);
        edit = (EditText) view.findViewById(R.id.pri_msg_detail_edit);

        send = (TextView) view.findViewById(R.id.pri_msg_detail_send);
//        placeHolder = (FrameLayout) view.findViewById(R.id.view_placeholder);
        //click
        nick.setText(nickname);
        edit.addTextChangedListener(new MyTextWatcher());
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        listView.setOnScrollListener(this);
//        placeHolder.addOnLayoutChangeListener(this);
        request();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.e(TAG, "firstVisibleItem:" + firstVisibleItem);
//        Log.e(TAG, "visibleItemCount:" + visibleItemCount + "");
//        Log.e(TAG, "totalItemCount:" + totalItemCount + "");
        if (firstVisibleItem == 0) {
            if (total != detailList.size()) {
                currentPage++;
                request();
            } /*else {
                ((MainRoom) getActivity()).alert("已加载到最后一页了");
            }*/
        }
    }
//    @Override
//    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) placeHolder.getLayoutParams();
//        if(bottom < oldBottom){
////            onKeyboardState(true);
//            Log.e(TAG,"键盘收起");
//            params.height=0;
//        }else if(bottom > oldBottom){
////            onKeyboardState(false);
//            Log.e(TAG,"键盘弹出");
//            params.height=100;
//        }
//        placeHolder.setLayoutParams(params);
//    }

    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable.toString())) {
                send.setBackgroundResource(R.drawable.chat_input_bar_send_btn_bg);
            }
        }
    }

    private int currentPage = 1;
    private int pageTotal = 1;
    private int total = 0;

    //获取私信详情
    public void request() {
        RequestParam param = RequestParam.builder(getActivity());
        param.put("uid", senderId);  //发送人id
        param.put("page", currentPage + "");
        param.put("pagesize", XingBoConfig.COMMON_PAGE_SIZE + "");
        CommonUtil.request(getActivity(), HttpConfig.API_USER_GET_PRIVITE_MSG_DETAIL, param, TAG, new XingBoResponseHandler<MessagePrivateDetailModel>((MainRoom) getActivity(), MessagePrivateDetailModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                ((MainRoom) getActivity()).alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                MessagePrivateDetailPage messagePrivateDetailPage = model.getD();
                if (mAdapter == null) {
                    detailList.addAll(messagePrivateDetailPage.getItems());
                    Collections.reverse(detailList);
                    owner = messagePrivateDetailPage.getOwner();
                    user = messagePrivateDetailPage.getUser();
                    mAdapter = new UserMsgPrivateDetailAdapter(getActivity(), detailList, owner, user);
                    listView.setAdapter(mAdapter);
                    listView.setSelection(detailList.size());
                } else {
                    Collections.reverse(messagePrivateDetailPage.getItems());
                    detailList.addAll(0, messagePrivateDetailPage.getItems());
                    mAdapter.setData(detailList);
                    mAdapter.notifyDataSetChanged();
                }
                pageTotal = messagePrivateDetailPage.getPagetotal();
                total = messagePrivateDetailPage.getTotal();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pri_msg_detail_back:
                dismiss();
                break;
            case R.id.pri_msg_detail_send:
                String msg = edit.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    ((MainRoom) getActivity()).alert("发送内容不能为空");
                } else {
                    send(senderId, msg);
                }
                break;
        }
    }

    //发送私信
    public void send(String uid, String msg) {
        RequestParam param = RequestParam.builder(getActivity());
        param.put("rid", getActivity().getSharedPreferences(XingBo.PX_USER_LOGIN_CACHE, Context.MODE_PRIVATE).getString(XingBo.PX_ROOM_CURRENT_ANCHOR_ID, ""));
        param.put("uid", uid);
        param.put("msg", msg);
        CommonUtil.request(getActivity(), HttpConfig.API_SEND_PRIVATE_MSG, param, TAG, new XingBoResponseHandler<BaseResponseModel>((MainRoom) getActivity(), BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                ((MainRoom) getActivity()).alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                edit.setText("");
                SoftInputUtils.hideInput(getActivity(),edit);
                clearInitData();
                request();
            }
        });
    }

    public void clearInitData() {
        currentPage = 1;
        detailList = new ArrayList<MessagePrivateDetail>();
        mAdapter = null;
    }

}

