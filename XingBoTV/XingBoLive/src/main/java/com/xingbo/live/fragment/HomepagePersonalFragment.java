package com.xingbo.live.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingbo.live.R;
import com.xingbo.live.config.XingBoConfig;
import com.xingbo.live.entity.Contribution;
import com.xingbo.live.entity.ContributionPage;
import com.xingbo.live.entity.MainUser;
import com.xingbo.live.entity.model.ContributionModle;
import com.xingbo.live.entity.model.UserHomeModel;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.UserFansContributeAct;
import com.xingbo.live.ui.UserHomepageAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import com.xingbobase.view.IFrescoImageView;

import java.util.List;

import io.vov.vitamio.utils.Log;

/**
 * Project: XingBoTV2.0
 * Author: MengruRen
 * Date: 2016/8/9
 */
public class HomepagePersonalFragment extends MBaseFragment implements View.OnClickListener {

    private final static String TAG = "HomepagePersonalFragment";
    public final static String EXTRA_USER_ID = "extra_user_id";

    private String uid = "";
    private RelativeLayout relativeLayout;
    private IFrescoImageView contribute1, contribute2, contribute3;
    private TextView xingBoId;
    private ImageView startLevel;
    private ImageView richLevel;
    private TextView personalSign;


    private boolean isNeedInit = true;//是否需要初始化
    private int mOpera = XingBo.REQUEST_OPERA_INIT;//请求初始化操作

    public MainUser mUser = null;
    public ContributionPage mContribute = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_personal_homepage_homefragment, null);
        uid = getActivity().getIntent().getStringExtra(UserHomepageAct.EXTRA_USER_ID);
        Log.d("tag", "HomepagePersonFragment-->" + uid);
        initView(rootView);
        requestContribute(XingBo.REQUEST_OPERA_INIT);
        requestBaseInfo(XingBo.REQUEST_OPERA_INIT);
        return rootView;
    }

    private void initView(View view) {
        relativeLayout = (RelativeLayout) view.findViewById(R.id.homepage_contribute);
        //贡献榜是三个头像
        contribute1 = (IFrescoImageView) view.findViewById(R.id.contributor1);
        contribute1.setOnClickListener(this);
        contribute2 = (IFrescoImageView) view.findViewById(R.id.contributor2);
        contribute2.setOnClickListener(this);
        contribute3 = (IFrescoImageView) view.findViewById(R.id.contributor3);
        contribute3.setOnClickListener(this);

        xingBoId = (TextView) view.findViewById(R.id.homepage_xingbo_id);
        startLevel = (ImageView) view.findViewById(R.id.homepage_userstar_level);
        richLevel = (ImageView) view.findViewById(R.id.homepage_userrich_level);
        personalSign = (TextView) view.findViewById(R.id.homepage_personal_sign);
        relativeLayout.setOnClickListener(this);
    }

    private void requestContribute(int opera) {
        mOpera = opera;
        RequestParam param = RequestParam.builder(getActivity());
        param.put("uid", uid);
        CommonUtil.request(getActivity(), HttpConfig.API_APP_GET_USER_FANS_LIST, param, TAG, new XingBoResponseHandler<ContributionModle>(this, ContributionModle.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                ContributionPage d = model.getD();
                List<Contribution> items = d.getItems();
                if (items.size() == 0) {
                    contribute1.setVisibility(View.GONE);
                    contribute2.setVisibility(View.GONE);
                    contribute3.setVisibility(View.GONE);
                }
                if (items.size() > 0) {
                    contribute1.setVisibility(View.VISIBLE);
                    contribute2.setVisibility(View.GONE);
                    contribute3.setVisibility(View.GONE);
                    contribute1.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + items.get(0).getAvatar()));
                }
                if (items.size() > 1) {
                    contribute2.setVisibility(View.VISIBLE);
                    contribute3.setVisibility(View.GONE);
                    contribute2.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + items.get(1).getAvatar()));
                }
                if (items.size() > 2) {
                    contribute3.setVisibility(View.VISIBLE);
                    contribute3.setImageURI(Uri.parse(HttpConfig.FILE_SERVER + items.get(2).getAvatar()));
                }
                //  isNeedInit=false;
            }
        });
    }

    private void requestBaseInfo(int opera) {
        mOpera = opera;
        RequestParam param = RequestParam.builder(getActivity());
        param.put("uid", uid);
        CommonUtil.request(getActivity(), HttpConfig.API_USER_GET_USER_INFO, param, TAG, new XingBoResponseHandler<UserHomeModel>(UserHomeModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {

                Log.d("tag", "response--->" + response);

                mUser = model.getD();

                if (!TextUtils.isEmpty(mUser.getId())) {
                    xingBoId.setText(mUser.getId());
                }
                String intro = mUser.getIntro();
                if (!TextUtils.isEmpty(mUser.getIntro())) {
                    personalSign.setText(intro);
                }
                //设置主播等级
                int slv = 0;
                try {
                    slv = Integer.parseInt(mUser.getAnchorlvl());
                } catch (NumberFormatException e) {
                    slv = 0;
                }
                if (slv < 41) {
                    startLevel.setImageResource(XingBoConfig.STAR_LV_ICONS[slv]);

                } else {
                    startLevel.setImageResource(XingBoConfig.STAR_LV_ICONS[40]);
                }
                //设置财富等级
                int rlv = 0;
                try {
                    rlv = Integer.parseInt(mUser.getRichlvl());
                } catch (NumberFormatException e) {
                    rlv = 0;
                }
                if (rlv < 35) {
                    richLevel.setImageResource(XingBoConfig.RICH_LV_ICONS[rlv]);
                } else {
                    richLevel.setImageResource(XingBoConfig.RICH_LV_ICONS[34]);
                }
            }
        });
        //   Log.d("mainuser","mainuser"+mUser.getIntro());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage_contribute://跳到粉丝贡献榜
                Intent contributionFans = new Intent(getActivity(), UserFansContributeAct.class);
                contributionFans.putExtra(UserFansContributeAct.ANCHOR_GUARD_ID, mUser.getId());
                contributionFans.putExtra(UserFansContributeAct.USER_COIN, "123333星");
                startActivity(contributionFans);
//               }
                break;
            case R.id.homepage_contribute_right:

                Intent contributionFans1r = new Intent(getActivity(), UserFansContributeAct.class);
                contributionFans1r.putExtra(UserFansContributeAct.ANCHOR_GUARD_ID, uid);
                contributionFans1r.putExtra(UserFansContributeAct.USER_COIN, "123333星");
                startActivity(contributionFans1r);

                break;
            case R.id.contributor1:

                Intent contributionFans1 = new Intent(getActivity(), UserFansContributeAct.class);
                contributionFans1.putExtra(UserFansContributeAct.ANCHOR_GUARD_ID, uid);
                contributionFans1.putExtra(UserFansContributeAct.USER_COIN, "123333星");
                startActivity(contributionFans1);
                break;
            case R.id.contributor2:

                Intent contributionFans2 = new Intent(getActivity(), UserFansContributeAct.class);
                contributionFans2.putExtra(UserFansContributeAct.ANCHOR_GUARD_ID, uid);
                contributionFans2.putExtra(UserFansContributeAct.USER_COIN, "123333星");
                startActivity(contributionFans2);

                break;
            case R.id.contributor3:

                Intent contributionFans3 = new Intent(getActivity(), UserFansContributeAct.class);
                contributionFans3.putExtra(UserFansContributeAct.ANCHOR_GUARD_ID, uid);
                contributionFans3.putExtra(UserFansContributeAct.USER_COIN, "123333星");
                startActivity(contributionFans3);

                break;
        }

    }
}
