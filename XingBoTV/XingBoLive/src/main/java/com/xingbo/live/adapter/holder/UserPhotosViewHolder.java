package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.UserPhotos;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/1
 */
public class UserPhotosViewHolder {

    private String id;
    public TextView upLoadTime;
    public LinearLayout linearLayoutGrid;

    public UserPhotosViewHolder(View itemView){
        upLoadTime= (TextView) itemView.findViewById(R.id.user_photos_upload_ctime);
        linearLayoutGrid= (LinearLayout) itemView.findViewById(R.id.user_photos_linearlayout);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
