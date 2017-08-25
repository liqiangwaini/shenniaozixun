package com.xingbo.live.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.xingbo.live.R;
import com.xingbobase.fragment.BaseFragment;


/**
 * 视频和照片输入页面
 */
public class FragmentPublish extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "FragmentLiveList";
    private ImageButton mBtn_videoCreate, mBtn_JoinRoom;


    public FragmentPublish() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.publishfragment_layout, container, false);
//        mBtn_videoCreate = (ImageButton) view.findViewById(R.id.test_createRoom);
//        mBtn_JoinRoom = (ImageButton) view.findViewById(R.id.test_showPhoto);
//        mBtn_JoinRoom.setOnClickListener(this);
//        mBtn_videoCreate.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
    }


}
