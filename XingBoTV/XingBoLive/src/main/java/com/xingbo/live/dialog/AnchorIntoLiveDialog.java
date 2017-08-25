package com.xingbo.live.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xingbo.live.R;

/**
 * 主播开始直播提示
 */
public class AnchorIntoLiveDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = "AnchorIntoLiveDialog";
    private TextView back;
    private TextView begin;
    private View rootView;

    public AnchorIntoLiveDialog(Context context) {
        super(context);
        init(context);
    }

    public AnchorIntoLiveDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public void init(Context context) {
        rootView = View.inflate(context, R.layout.anchor_into_live_dialog, null);
        this.setContentView(rootView);
        back = (TextView) rootView.findViewById(R.id.tv_back);
        begin = (TextView) (rootView =findViewById(R.id.tv_begin));
        back.setOnClickListener(this);
        begin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back:
                dismiss();
                break;
            case R.id.tv_begin:
                dismiss();

                break;
        }
    }
}
