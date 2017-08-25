package com.xingbo.live.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingbo.live.R;
import com.xingbo.live.adapter.PhotosAdapterPersonal;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.UserPhotos;
import com.xingbo.live.entity.UserPhotosPage;
import com.xingbo.live.entity.model.UserPhotosModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.PhotosDeleteAct;
import com.xingbo.live.ui.PhotosSaveAct;
import com.xingbo.live.ui.PicturesShowAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.extra.pulltorefresh.PullToRefreshBase;
import com.xingbobase.extra.pulltorefresh.PullToRefreshStickyHeaderGridView;
import com.xingbobase.extra.sticky.StickyGridHeadersGridView;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.FrescoImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/9
 */
public class HomepagePhotosFragment extends MBaseFragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2 {
    private final static String TAG = "HomepagePhotosFragment";
    public final static String EXTRA_USER_ID = "extra_user_id";
    private String uid;
    private PullToRefreshStickyHeaderGridView pullToRefreshStickyHeaderGridView;
    private List<UserPhotos> userPhotosList = new ArrayList<UserPhotos>();
    private ArrayList<UserPhotos> currentPhotos = new ArrayList<UserPhotos>();

    private PhotosAdapterPersonal mPhotosAdapter;

    private TextView errMsg;
    private Button errBtn;
    private RelativeLayout emptyViewBox;
    private StickyGridHeadersGridView stickyGridHeadersGridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_personal_homepage_photofragment, null);
        pullToRefreshStickyHeaderGridView = (PullToRefreshStickyHeaderGridView) rootView.findViewById(R.id.gridview);
        stickyGridHeadersGridView = pullToRefreshStickyHeaderGridView.getRefreshableView();
//        uid= getActivity().getIntent().getStringExtra(UserHomepageAct.EXTRA_USER_ID);
        uid = getArguments().getString(EXTRA_USER_ID);
        mPhotosAdapter = new PhotosAdapterPersonal(getActivity(), userPhotosList, R.layout.header, R.layout.item);
        stickyGridHeadersGridView.setAdapter(mPhotosAdapter);
        pullToRefreshStickyHeaderGridView.setOnRefreshListener(this);
        if (stickyGridHeadersGridView != null) {
            stickyGridHeadersGridView.setOnItemClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage=1;
        viewImageShow();
    }

    private int currentPage = 1;

    @Override
    public void handleMsg(Message message) {
        if (message.what == 0) {
            mPhotosAdapter.upDataAdapter(userPhotosList);
            mPhotosAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 相册列表显示
     */
    private void viewImageShow() {
        Log.d(TAG, "相册列表显示");
        RequestParam param = RequestParam.builder(getActivity());
        param.put("uid", uid);
        param.put("page", currentPage + "");
        param.put("pagesize", XingBoConfig.COMMON_PAGE_SIZE + "");
        CommonUtil.request(getActivity(), HttpConfig.API_USER_GET_ONE_PHOTOS, param, TAG, new XingBoResponseHandler<UserPhotosModel>(this, UserPhotosModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                if (pullToRefreshStickyHeaderGridView != null) {
                    pullToRefreshStickyHeaderGridView.onRefreshComplete();
                }
                ((BaseAct) getActivity()).alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                if (pullToRefreshStickyHeaderGridView != null) {
                    pullToRefreshStickyHeaderGridView.onRefreshComplete();
                }
                if (model.getD() != null) {
                    UserPhotosPage d = model.getD();
                    if (currentPage == 1) {
                        userPhotosList.clear();
                    }
                    userPhotosList.addAll(d.getItems());
                    /*for (int i = 0; i < userPhotosList.size(); i++) {
                        if ("".equals(userPhotosList.get(i).getUrl()) || userPhotosList.get(i).getUrl() == null) {
                            userPhotosList.remove(i);
                        }
                    }*/
                    if (userPhotosList.size() == 0) {
                        if (emptyViewBox == null) {
                            showErrView();
                        }
                        if (emptyViewBox.getVisibility() == View.GONE) {
                            emptyViewBox.setVisibility(View.VISIBLE);
                        }
                        errMsg.setText("还没有上传任何照片");
                    } else {
                        if (emptyViewBox != null && emptyViewBox.getVisibility() == View.VISIBLE) {
                            emptyViewBox.setVisibility(View.GONE);
                        }
                    }
                    if (userPhotosList.size() >= d.getTotal()) {
                        currentPage = -1;
                    }
                    handler.sendEmptyMessage(0);
                } else {
                    ((BaseAct) getActivity()).alert("请求错误！");
                }

            }
        });
    }

    public void showErrView() {
        ViewStub stub = (ViewStub) rootView.findViewById(R.id.loading_err_view);
        stub.inflate();
        errMsg = (TextView) rootView.findViewById(R.id.empty_view_err_msg);
        FrescoImageView imageView = (FrescoImageView) rootView.findViewById(R.id.empty_view_bg_icon);
        imageView.setImageURI(Uri.parse("res:///" + R.mipmap.common_empty_view_icon));
        errBtn = (Button) rootView.findViewById(R.id.empty_view_refresh_btn);
        errBtn.setVisibility(View.INVISIBLE);
        emptyViewBox = (RelativeLayout) rootView.findViewById(R.id.empty_view_box);
    }

    //    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent= new Intent(getActivity(), PicturesShowAct.class);
//        intent.putExtra(PicturesShowAct.CURRENT_POSITION,position+"");
//        intent.putExtra(PicturesShowAct.EXTRA_USER_ID,uid);
////        intent.putParcelableArrayListExtra("photolist", (ArrayList) userPhotosList);
//        intent.putExtra(PicturesShowAct.PHOTOS_LIST__FLAG,(Serializable)userPhotosList);
//        startActivity(intent);
//    }
    public void arragePhotos(List<UserPhotos> photoses, String time) {
        currentPhotos.clear();
        for (int i = 0; i < photoses.size(); i++) {
            if (photoses.get(i).getUptime().equals(time)) {
                currentPhotos.add(photoses.get(i));
            }
        }
    }

    //点击编辑进行删除
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (userPhotosList != null) {
            arragePhotos(userPhotosList, userPhotosList.get(position).getUptime());
            int curentPosition = 0;
            for (int i = 0; i < currentPhotos.size(); i++) {
                if (currentPhotos.get(i).getId().equals(userPhotosList.get(position).getId())) {
                    curentPosition = i;
                    break;
                }
            }
            Intent photoDelete = new Intent(act, PhotosSaveAct.class);
            photoDelete.putExtra(PhotosSaveAct.IMAGE_URL, userPhotosList.get(position).getUrl());
            photoDelete.putExtra(PhotosSaveAct.IMAGE_POSITION, (curentPosition + 1) + "");
            photoDelete.putExtra(PhotosSaveAct.IMAGE_UPTIME, userPhotosList.get(position).getUptime());
            photoDelete.putExtra(PhotosSaveAct.IMAGE_PHOTOS, currentPhotos);
            startActivity(photoDelete);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        currentPage = 1;
        viewImageShow();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (currentPage == -1) {
            if (pullToRefreshStickyHeaderGridView != null) {
                pullToRefreshStickyHeaderGridView.onRefreshComplete();
            }
            Toast.makeText(getActivity(), "数据已完全加载", Toast.LENGTH_SHORT).show();
            return;
        }
        viewImageShow();
    }

}
