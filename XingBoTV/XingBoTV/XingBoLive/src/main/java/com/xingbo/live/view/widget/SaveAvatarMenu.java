package com.xingbo.live.view.widget;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xingbo.live.R;
import com.xingbo.live.eventbus.SaveAvatarEvent;

import de.greenrobot.event.EventBus;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/9/6
 */
public class SaveAvatarMenu extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private Activity activity;
    private int sourceTagCode=0;

    public SaveAvatarMenu(Activity activity,int sourceTagCode){
        super(activity);
        this.activity= activity;
        this.sourceTagCode= sourceTagCode;
        rootView= LayoutInflater.from(activity).inflate(R.layout.avatar_save_menu,null);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setTouchable(true);
        rootView.setOnClickListener(this);
        rootView.findViewById(R.id.avatar_save_menu_save).setOnClickListener(this);
        rootView.findViewById(R.id.avatar_save_menu_close).setOnClickListener(this);
    }

    public void setSourceTagCode(int sourceTagCode){
        this.sourceTagCode=sourceTagCode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.base_bottom_pop_menu:
                dismiss();
                break;
            case R.id.avatar_save_menu_save:
                EventBus.getDefault().post(new SaveAvatarEvent());
                dismiss();
                break;
            case R.id.avatar_save_menu_close:
                dismiss();
                break;
        }
    }
}
