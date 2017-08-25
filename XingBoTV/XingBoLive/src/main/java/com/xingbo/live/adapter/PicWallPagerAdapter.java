package com.xingbo.live.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xingbo.live.R;
import com.xingbo.live.entity.PicWall;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.IndexActWeb;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.view.FrescoImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuJinZhou on 2015/12/16.
 */
public class PicWallPagerAdapter extends PagerAdapter {
    private final static String TAG="PicWallPagerAdapter";
    private Context context;
    private List<PicWall> picWalls=new ArrayList<PicWall>();
    private LayoutInflater mInflater;
    public PicWallPagerAdapter(Context context, List<PicWall> picWalls) {
        this.context = context;
        this.picWalls = picWalls;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return picWalls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FrescoImageView imageView=(FrescoImageView)mInflater.inflate(R.layout.home_index_pic_wall_item_view,null);
        imageView.setImageURI(Uri.parse(HttpConfig.FILE_SERVER+picWalls.get(position).getLogo()));
        imageView.setOnClickListener(new OnItemClickListener(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    class OnItemClickListener implements View.OnClickListener{
        private int position=0;

        OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent indexAct=new Intent(context, IndexActWeb.class);
            CommonUtil.log(TAG,picWalls.get(position).getTitle()+"@@Href="+picWalls.get(position).getHref());
            indexAct.putExtra(IndexActWeb.EXTRA_ACT_WEB_URL,picWalls.get(position).getHref());
            indexAct.putExtra(IndexActWeb.EXTRA_ACT_WEB_TITLE,picWalls.get(position).getTitle());
            context.startActivity(indexAct);
        }
    }
}
