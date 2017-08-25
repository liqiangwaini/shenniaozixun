package com.xingbobase.extra.pickerview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.xingbobase.R;


/**
 * Created by WuJinZhou on 2015/8/15.
 */
public class TwoOptionsPopupWindow extends PopupWindow implements View.OnClickListener,View.OnTouchListener {
    private View rootView;
    private Button male,female,cancel;
    private View.OnClickListener listener;
    public TwoOptionsPopupWindow(Context context) {
        super(context);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //Drawable drawable=new ColorDrawable(context.getResources().getColor(R.color.rgb_black_65));
        this.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能点击屏幕外dismiss窗口
        //getBackground().setAlpha(204);
        //setFocusable(true);
        //setTouchable(true);
        //setOutsideTouchable(true);
        //this.setAnimationStyle(R.style.timepopwindow_anim_style);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.picker_view_sex, null);
        rootView.findViewById(R.id.root).setOnTouchListener(this);
        male= (Button)rootView.findViewById(R.id.male);
        male.setOnClickListener(this);
        female= (Button)rootView.findViewById(R.id.female);
        female.setOnClickListener(this);
        cancel= (Button)rootView.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(this);
        setContentView(rootView);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId()==R.id.root&&event.getAction()==MotionEvent.ACTION_DOWN){
            dismiss();
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cancel_btn){
            dismiss();
            return;
        }
        if(listener!=null){
            listener.onClick(v);

        }
        dismiss();
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}

