package com.xingbo.live.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.entity.Product;
import com.xingbobase.adapter.XingBoBaseAdapter;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/1.
 */
public class RechargeProductAdapter extends XingBoBaseAdapter<Product> {
    private LayoutInflater mInflater;
    public RechargeProductAdapter(Context context, List<Product> data) {
        super(context, data);
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product p=data.get(position);
        if(p==null){
            return null;
        }
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.recharge_product_item,null);
        }
        TextView v=(TextView)convertView.findViewById(R.id.value);
        v.setText(p.getCoin()+"星币");
        TextView price=(TextView)convertView.findViewById(R.id.price);
        price.setText("￥"+p.getMoney());
        convertView.setTag(p);
        return convertView;
    }
}
