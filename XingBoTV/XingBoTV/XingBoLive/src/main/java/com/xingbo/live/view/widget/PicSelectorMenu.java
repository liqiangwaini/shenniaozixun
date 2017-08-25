package com.xingbo.live.view.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xingbo.live.R;
import com.xingbo.live.ui.PictureEditAct;
import com.xingbobase.extra.cropzoom.GOTOConstants;


/**
 * Created by tangtianxu on 2016/7/13.
 */
public class PicSelectorMenu extends PopupWindow implements View.OnClickListener {
    private View rootView;
    private Activity activity;
    private int sourceTagCode=0;
    public PicSelectorMenu(Activity activity, int sourceTagCode) {
        super(activity);
        this.activity=activity;
        this.sourceTagCode=sourceTagCode;
        rootView = LayoutInflater.from(activity).inflate(R.layout.pic_selector_menu, null);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setTouchable(true);
        rootView.setOnClickListener(this);
        rootView.findViewById(R.id.open_my_camera).setOnClickListener(this);
        rootView.findViewById(R.id.open_my_photo_piker).setOnClickListener(this);
        rootView.findViewById(R.id.pic_selector_menu_close).setOnClickListener(this);
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
            case R.id.open_my_camera:
                onPicModeSelected(GOTOConstants.PicModes.CAMERA);
                dismiss();
                break;
            case R.id.open_my_photo_piker:
                onPicModeSelected(GOTOConstants.PicModes.GALLERY);
                dismiss();
                break;
            case R.id.pic_selector_menu_close:
                dismiss();
                break;
        }
    }

    private void actionProfilePic(String action){
        Intent intent= new Intent(activity,PictureEditAct.class);
        intent.putExtra("ACTION",action);
        intent.putExtra(PictureEditAct.SOURCE_TAG_CODE_KEY,sourceTagCode);
        activity.startActivity(intent);
    }
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
        actionProfilePic(action);
    }
}

