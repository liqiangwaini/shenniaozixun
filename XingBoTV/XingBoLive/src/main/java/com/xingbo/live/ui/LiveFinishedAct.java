package com.xingbo.live.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.controller.RoomController;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.FastBlur;
import com.xingbobase.view.FrescoImageView;

import java.net.URI;

/**
 * Created by xingbo_szd on 2016/8/11.
 */
public class LiveFinishedAct extends BaseAct implements View.OnClickListener{
    public static final String LIVE_ROOM_ANCHOR_ID="live_room_anchor_id";
    public static final String LIVE_ROOM_BG_LOGO="live_room_bg_logo";
    public static final String LIVE_ROOM_ANCHOR_ONLINES="live_room_anchor_onlines";
    public static final String LIVE_ROOM_IS_FOLLOWED="live_room_is_followed";
    private View rootView;
    private TextView guests;
    private TextView homepage;
    private TextView concern;
    private TextView back;

    private String anchorId="";
    private String onlines="";
    private boolean isFollowed;
    private FrescoImageView background;
    private String logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.act_live_finished, null);
        setContentView(rootView);
        anchorId = getIntent().getStringExtra(LIVE_ROOM_ANCHOR_ID);
        onlines=getIntent().getStringExtra(LIVE_ROOM_ANCHOR_ONLINES);
        isFollowed = getIntent().getBooleanExtra(LIVE_ROOM_IS_FOLLOWED, false);
        logo = getIntent().getStringExtra(LIVE_ROOM_BG_LOGO);
        //initview
        background = (FrescoImageView) rootView.findViewById(R.id.live_finished_bg);
        guests = (TextView) rootView.findViewById(R.id.live_finished_guests);
        homepage = (TextView) rootView.findViewById(R.id.live_finished_homepage);
        concern = (TextView) rootView.findViewById(R.id.live_finished_concern);
        back = (TextView) rootView.findViewById(R.id.live_finished_back);
        //initdata
        FastBlur.show(background, HttpConfig.FILE_SERVER + logo, 50, this);
        String text="人观看";
        SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(onlines+text);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)),0,onlines.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        guests.setText(spannableStringBuilder);
        if (isFollowed) {
            concern.setText("已关注");
        } else {
            concern.setText("+关注");
        }
        //onclick
        homepage.setOnClickListener(this);
        concern.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.live_finished_back:
                finish();
                break;
            case R.id.live_finished_homepage:
                finish();
                Intent homepageIntent=new Intent(this,UserHomepageAct.class);
                homepageIntent.putExtra(UserHomepageAct.EXTRA_USER_ID,anchorId);
                startActivity(homepageIntent);
                break;
            case R.id.live_finished_concern:
                RoomController controller=new RoomController();
                controller.favoriteUser(this,isFollowed,anchorId);
                controller.setMainroomControllerCallback(new RoomController.MainRoomControllerListener() {
                    @Override
                    public void onError(int tag, String msg) {
                        alert(msg);
                    }
                    @Override
                    public void onSuccess(int tag, Object modelD) {
                        isFollowed = !isFollowed;
                        if (isFollowed) {
                            concern.setText("已关注");
                        } else {
                            concern.setText("+关注");
                        }
                    }
                });
                break;
        }
    }
}
