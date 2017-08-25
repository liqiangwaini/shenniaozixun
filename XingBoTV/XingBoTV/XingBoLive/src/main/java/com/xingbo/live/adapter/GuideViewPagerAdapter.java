package com.xingbo.live.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xingbo.live.R;
import com.xingbo.live.ui.Guide;
import com.xingbo.live.ui.LoginAct;
import com.xingbobase.view.FrescoImageView;
import java.lang.ref.WeakReference;

public class GuideViewPagerAdapter extends PagerAdapter {
    private WeakReference<Guide>guideRef;
    private LayoutInflater mInflater;
    private Context context;

    public GuideViewPagerAdapter(Context context,Guide guide) {
        this.context=context;
        this.guideRef=new WeakReference<Guide>(guide);
        this.mInflater = LayoutInflater.from(guide);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RelativeLayout page=(RelativeLayout)mInflater.inflate(R.layout.guide_page, null);
        FrescoImageView imageView=(FrescoImageView)page.findViewById(R.id.guide_image);
        TextView skip= (TextView) page.findViewById(R.id.guide_page_skip);
        skip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        skip.getPaint().setAntiAlias(true);//抗锯齿
        Uri uri=null;
        switch (position){
            case 0:
                uri=Uri.parse("res:///"+ R.drawable.guide_1);
                skip.setText("跳  过");
                break;
            case 1:
                uri=Uri.parse("res:///"+ R.drawable.guide_2);
                skip.setText("跳  过");
                break;
            case 2:
                uri=Uri.parse("res:///"+ R.drawable.guide_3);
                skip.setText("跳  过");
                break;
            case 3:
                uri=Uri.parse("res:///"+ R.drawable.guide_4);
                skip.setText("立即体验");
//                skip.getPaint().setFlags(Paint.)
                /*Button finishGuideBtn=(Button)page.findViewById(R.id.finish_btn);
//                finishGuideBtn.setVisibility(View.VISIBLE);
                if (guideRef != null && guideRef.get() != null) {
//                            Intent main=new Intent(guideRef.get(),HomeActivity.class);
                    Intent main = new Intent(guideRef.get(), LoginAct.class);
                    guideRef.get().startActivity(main);
                    guideRef.get().finish();
                }*/
                break;
            default:
                break;
        }
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guideRef != null && guideRef.get() != null) {
//                            Intent main=new Intent(guideRef.get(),HomeActivity.class);
                    Intent main = new Intent(guideRef.get(), LoginAct.class);
                    guideRef.get().startActivity(main);
                    guideRef.get().finish();
                }
            }
        });
        if(uri!=null) {
            imageView.setImageURI(uri);
        }
        container.addView(page);
        return page;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
