package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbobase.view.FrescoImageView;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFansViewHolder {
    private String uid;
    public FrescoImageView header;
    public TextView nick;
    public ImageView richIcon;
    //public TextView roomLink;
    //新增字段
    public TextView personalSign;
    public ImageView uSex;
    public Button btnFcous;


    public UserFansViewHolder(View itemView) {

        header=(FrescoImageView)itemView.findViewById(R.id.user_fans_header);
        nick=(TextView)itemView.findViewById(R.id.fans_nick);
        richIcon=(ImageView)itemView.findViewById(R.id.rich_icon_fans);
       // roomLink=(TextView)itemView.findViewById(R.id.room_linker_btn);
        uSex= (ImageView) itemView.findViewById(R.id.list_item_fans_sex);
        personalSign= (TextView) itemView.findViewById(R.id.list_item_fans_personal_sign);
        btnFcous= (Button) itemView.findViewById(R.id.list_item_fans_fcous);

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
