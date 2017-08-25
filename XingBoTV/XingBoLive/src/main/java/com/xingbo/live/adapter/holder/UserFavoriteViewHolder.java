package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.UserFavorite;
import com.xingbobase.view.FrescoImageView;

/**
 * Created by WuJinZhou on 2016/2/4.
 */
public class UserFavoriteViewHolder {
    private String uid;
    public FrescoImageView header;
    public TextView nick;
    public ImageView richIcon;
    public TextView roomLink;
    public TextView personalSign;
    public ImageView uSex;
    public Button btnFcous;

    private UserFavorite favorite;

    public UserFavorite getFavorite() {
        return favorite;
    }

    public void setFavorite(UserFavorite favorite) {
        this.favorite = favorite;
    }

    public UserFavoriteViewHolder(View itemView) {
        header=(FrescoImageView)itemView.findViewById(R.id.user_header);
        nick=(TextView)itemView.findViewById(R.id.nick);
        richIcon=(ImageView)itemView.findViewById(R.id.rich_icon);
     //   roomLink=(TextView)itemView.findViewById(R.id.room_linker_btn);
        personalSign= (TextView) itemView.findViewById(R.id.list_item_personal_sign);
        uSex= (ImageView) itemView.findViewById(R.id.list_item_sex);
        btnFcous= (Button) itemView.findViewById(R.id.list_item_fcous);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
