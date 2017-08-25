package com.xingbo.live.popup;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.xingbo.live.R;
import com.xingbo.live.ui.MainRoom;
import com.xingbo.live.util.SoftInputUtils;

/**
 * Created by xingbo_szd on 2016/7/27.
 */
public class EditGiftNumberPopup extends PopupWindow {

    private MainRoom act;
    public EditText et;

    public EditGiftNumberPopup(final MainRoom act) {
        this.act=act;
        View view = View.inflate(act, R.layout.popup_edit_gift_number, null);
        et = (EditText) view.findViewById(R.id.et_edit_gift_number);
        Button confirm = (Button) view.findViewById(R.id.btn_edit_gift_number);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editText = et.getText().toString().trim();
                /*f (TextUtils.isEmpty(editText)) {
                    act.alert("数量不能为空");
                    editText=1;
                    return;
                }*/
                editText=1+"";
//                if (Integer.parseInt(editText) <= 0) {
//                    act.alert("数量要大于0");
//                    return;
//                }
                dismiss();
                listener.getGiftNumber(Integer.parseInt(editText));
            }
        });
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void showSoft(){
        SoftInputUtils.showInput(act, et);
    }


    public void setGiftNumeberConfirmed(GiftNumberConfirmedListener listener) {
        this.listener = listener;
    }

    private GiftNumberConfirmedListener listener;

    public interface GiftNumberConfirmedListener {
        public void getGiftNumber(int number);
    }
}
