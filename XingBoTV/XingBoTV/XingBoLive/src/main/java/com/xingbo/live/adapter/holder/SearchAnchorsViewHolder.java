package com.xingbo.live.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbobase.view.FrescoImageView;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/12
 */
public class SearchAnchorsViewHolder {

    public FrescoImageView  avatar;
    public TextView nick;
    public TextView personalSign;
    public  TextView idNum;
    public  TextView fansNum;
    public Button btnFcous;

     public  SearchAnchorsViewHolder(View itemView){
         avatar= (FrescoImageView) itemView.findViewById(R.id.user_header);
         nick= (TextView) itemView.findViewById(R.id.nick);
         personalSign = (TextView) itemView.findViewById(R.id.list_item_personal_sign);
         idNum= (TextView) itemView.findViewById(R.id.id_num);
         fansNum= (TextView) itemView.findViewById(R.id.fans_num);
         btnFcous= (Button) itemView.findViewById(R.id.list_item_fcous);
    }
}
