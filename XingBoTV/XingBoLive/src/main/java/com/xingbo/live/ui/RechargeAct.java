package com.xingbo.live.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.H5PayActivity;
import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.xingbo.live.Constants;
import com.xingbo.live.R;
import com.xingbo.live.adapter.RechargeProductAdapter;
import com.xingbo.live.entity.BaseUser;
import com.xingbo.live.entity.Product;
import com.xingbo.live.entity.WxPayReq;
import com.xingbo.live.entity.model.MainUserModel;
import com.xingbo.live.entity.model.OrderModel;
import com.xingbo.live.entity.model.ProductsModel;
import com.xingbo.live.entity.model.WxOrderModel;
import com.xingbo.live.eventbus.UpdateBalanceEvent;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.config.XingBo;
import com.xingbobase.extra.alipay.PayResult;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.vov.vitamio.utils.Log;


/**
 * 充值选购支付界面
 * Created by WuJinZhou on 2016/2/1.
 */
public class RechargeAct extends BaseAct implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = "RechargeAct";
    public final static int RECHARGE_MODEL_WX = 0x1;//微信支付
    public final static int RECHARGE_MODEL_ALI = 0x2;//支付宝支付
    private final static int HANDLER_MSG_SHOW_ALI_RESULT = 0x525;
    public final static String EXTRA_USER_COIN_VALUE = "extra_user_coin_value";//用户余额参数
    public final static String EXTRA_USER_COIN_FLAG = "extra_user_coin_flag";
    private List<Product> products_wx = new ArrayList<Product>();
    private List<Product> products_ali = new ArrayList<Product>();
    private List<Product> products_temp = new ArrayList<Product>();
    private ListView mListView;
    private RechargeProductAdapter mAdapter;
    private int rechargeModel = RECHARGE_MODEL_ALI;//默认支付宝支付方式
    private TextView coinInfo;
    private TextView payStyleSelected;
    private long coin = 0;
    private boolean coinFlag = false;

    private RadioGroup payStyleGroup;
    private RadioButton aliBtn, wxBtn;
    private boolean isCallPaying = false;//正在调用支付服务
    public final static String EXTRA_WXPAY_RESP_TYPE_VALUE = "extra_wxpay_resp_type_value";//是否微信支付回调
    public final static String EXTRA_WXPAY_RESP_ERROR_CODE_VALUE = "extra_wxpay_resp_error_code_value";//微信支付回调结果状态码


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册余额更新EventBus
        EventBus.getDefault().register(this);
        coin = getIntent().getLongExtra(EXTRA_USER_COIN_VALUE, 0);
        CommonUtil.log(TAG, "当前余额：" + coin);
        setContentView(R.layout.recharge);
        findViewById(R.id.top_back_btn).setOnClickListener(this);
        findViewById(R.id.recharge_log).setOnClickListener(this);
        init();
        request("alipay");
        coinFlag = getIntent().getBooleanExtra(EXTRA_USER_COIN_FLAG, false);
        if (coinFlag) {
            getCoin();
        }
        int wxrespType = getIntent().getIntExtra(EXTRA_WXPAY_RESP_TYPE_VALUE, 0);
        if (wxrespType == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int wxrespErrorCode = getIntent().getIntExtra(EXTRA_WXPAY_RESP_ERROR_CODE_VALUE, -99);
            this.showWxPayResult(wxrespErrorCode);
        }

        payStyleGroup = (RadioGroup) findViewById(R.id.recharge_model);
        payStyleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.ali_pay:
                        payStyleSelected.setText("支付方式:" + aliBtn.getText().toString());
                        request("alipay");
                        rechargeModel = RECHARGE_MODEL_ALI;
                        products_temp.clear();
                        products_temp.addAll(products_ali);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case R.id.wx_pay:
                        payStyleSelected.setText("支付方式：" + wxBtn.getText().toString());
                        request("weixin");
//                        alert("暂未开放");
                        if (products_wx.size() == 0) {
                            request("weixin");
                        } else {
                            products_temp.clear();
                            products_temp.addAll(products_wx);
                            mAdapter.notifyDataSetChanged();
                        }
                        rechargeModel = RECHARGE_MODEL_WX;
                        break;
                }
            }
        });
    }

    private void getCoin() {

        RequestParam param = RequestParam.builder(this);
        CommonUtil.request(this, HttpConfig.API_USER_GET_PROFILE, param, TAG, new XingBoResponseHandler<MainUserModel>(MainUserModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {

            }

            @Override
            public void phpXiuSuccess(String response) {
                String coin = model.getD().getCoin();
                coinInfo.setText(coin + "");

            }
        });
    }


    private void init() {
        coinInfo = (TextView) findViewById(R.id.user_coin_value);
        coinInfo.setText(coin + "");
        aliBtn = (RadioButton) findViewById(R.id.ali_pay);
        aliBtn.setOnClickListener(this);
        wxBtn = (RadioButton) findViewById(R.id.wx_pay);
        wxBtn.setOnClickListener(this);
        payStyleSelected = (TextView) findViewById(R.id.recharge_model_selected);
        payStyleSelected.setText("支付方式：" + aliBtn.getText().toString());
        mListView = (ListView) findViewById(R.id.product_list);
        mListView.setOnItemClickListener(this);
        mAdapter = new RechargeProductAdapter(this, products_temp);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 请求订单列表
     */
    private void request(final String type) {
        RequestParam param = RequestParam.builder(this);
        param.put("type", type);
        CommonUtil.request(this, HttpConfig.API_APP_GET_PRODUCT_LIST, param, TAG, new XingBoResponseHandler<ProductsModel>(this, ProductsModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "订单结果列表" + response);
                if (type != null && type.equals("alipay")) {
                    products_ali.clear();
                    products_ali.addAll(model.getD());
                    products_temp.clear();
                    products_temp.addAll(products_ali);
                    mAdapter.notifyDataSetChanged();
                } else {
                    products_wx.clear();
                    products_wx.addAll(model.getD());
                    products_temp.clear();
                    products_temp.addAll(products_wx);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取订单号  支付宝
     */
    public void requestOrder(String productId, String value) {
        RequestParam param = RequestParam.builder(this);
        param.put("goodid", productId);
        final String coin = value;
        isCallPaying = true;
        CommonUtil.request(this, HttpConfig.API_APP_GET_PAY_ORDER, param, TAG, new XingBoResponseHandler<OrderModel>(this, OrderModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                isCallPaying = false;
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "获取订单结果：" + response);
                //根据订单结果调起支付
//                String d = model.getD();
//                payOrderH5();
                String d = model.getD();
                payOrder(model.getD(), coin);

            }
        });
    }


    /**
     * 获取微信订单号
     */
    public void requestWxPayOrder(String productId, String value) {
        RequestParam param = RequestParam.builder(this);
        param.put("goodid", productId);
        final String coin = value;

        //注册AppID
        final IWXAPI wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        //将app注册到微信
        wxApi.registerApp(Constants.APP_ID);
        isCallPaying = true;
        CommonUtil.request(this, HttpConfig.API_APP_GET_WX_PAY_ORDER, param, TAG, new XingBoResponseHandler<WxOrderModel>(this, WxOrderModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                isCallPaying = false;
                alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                CommonUtil.log(TAG, "获取订单结果：" + response);
//                payOrder(model.getD(),coin);
                if (!wxApi.isWXAppInstalled()) {
                    alert("没有安装微信");
                    return;
                }
                if (!wxApi.isWXAppSupportAPI()) {
                    alert("当前版本不支持支付功能");
                    return;
                }
                try {
                    //调起支付
                    WxPayReq wxPayReq = model.getD();
                    PayReq req = new PayReq();
                    req.appId = wxPayReq.getAppid();
                    req.partnerId = wxPayReq.getPartnerid();
                    req.prepayId = wxPayReq.getPrepayid();
                    req.nonceStr = wxPayReq.getNoncestr();
                    req.timeStamp = wxPayReq.getTimestamp();
                    req.packageValue = "Sign=WXPay";
                    req.sign = wxPayReq.getSign();
                    req.extData = "app data"; // optional
                    wxApi.sendReq(req);
                    Log.d("WX", req.sign);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    alert("支付异常");
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back_btn:
                onBackPressed();
                break;
            case R.id.recharge_log://充值记录
                Intent payLogAct = new Intent(this, UserPayLogAct.class);
                startActivity(payLogAct);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isCallPaying) {
            return;
        }
        if (view.getTag() instanceof Product) {
            Product p = (Product) view.getTag();
            if (p.getType().equals("weixin")) {
                requestWxPayOrder(p.getId(), p.getCoin());

            } else {

                requestOrder(p.getId(), p.getCoin());
            }
        }
    }

    /**
     * 请求支付H5页面
     */
//    public void payOrderH5(){
//        if(order==null){
//            isCallPaying=false;
//            //finish();
//            return;
//        }
//        final String coin=value;
//        if(isFinishing()) {
//            isCallPaying=false;
//            return;
//        }else{
//            if (order == null || order.equals("")) {
//                alert("生成订单失败！");
//                isCallPaying=false;
//                return;
//            }
//        }
//        Intent intent = new Intent(this, H5PayActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//         * 商户可以根据自己的需求来实现
//         */
//        String url = "http://m.taobao.com";
//        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        startActivity(intent);
//    }

    /**
     * 请求支付
     */
    public void payOrder(final String order, String value) {
        if (order == null) {
            isCallPaying = false;
            //finish();
            return;
        }
        final String coin = value;
        if (isFinishing()) {
            isCallPaying = false;
            return;
        } else {
            if (order == null || order.equals("")) {
                alert("生成订单失败！");
                isCallPaying = false;
                return;
            }
        }
        // 获取到购买的信息后 开启购买支付的线程
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(RechargeAct.this);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(order);
                    Message msg = handler.obtainMessage();
                    msg.what = HANDLER_MSG_SHOW_ALI_RESULT;
                    msg.obj = new PayInfo(coin, result);
                    handler.sendMessage(msg);
                } catch (NullPointerException e) {
                    isCallPaying = false;
                    alert("订单支付失败");
                }
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case HANDLER_MSG_SHOW_ALI_RESULT:
                isCallPaying = false;
                showResult(msg);
                break;
            default:
                break;
        }
    }

    /**
     * 显示处理充值结果
     */
    public void showResult(Message msg) {
        PayInfo payInfo = (PayInfo) msg.obj;
        PayResult payResult = new PayResult(payInfo.getResult());
        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
        String resultInfo = payResult.getResult();
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            long currentCoin = coin + Long.parseLong(payInfo.getCoin());
            //更新余额
            EventBus.getDefault().postSticky(new UpdateBalanceEvent(currentCoin + ""));
            alert("支付宝支付成功");
        } else {
            // 判断resultStatus 为非“9000”则代表可能支付失败
            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                CommonUtil.log(TAG, "支付结果确认中...");
            } else if (TextUtils.equals(resultStatus, "6001")) {
                CommonUtil.log(TAG, "已取消支付");
            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                alert("支付宝支付失败" + resultStatus);
            }
        }
    }

    public void showWxPayResult(int wxpayErrorCode) {
//			0	成功	展示成功页面
//			-1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//			-2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
        if (wxpayErrorCode == 0) {
            CommonUtil.tip(this, "微信支付成功", Gravity.CENTER);
        } else {
            if (wxpayErrorCode == -1) {
                CommonUtil.tip(this, "微信支付失败：" + wxpayErrorCode, Gravity.CENTER);
            } else if (wxpayErrorCode == -2) {
                CommonUtil.tip(this, "您已取消微信支付", Gravity.CENTER);
            } else {
                CommonUtil.tip(this, "微信支付失败", Gravity.CENTER);
            }
        }

        RequestParam param = RequestParam.builder(this);
        CommonUtil.request(this, HttpConfig.API_USER_GET_PROFILE, param, TAG, new XingBoResponseHandler<MainUserModel>(MainUserModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
            }

            @Override
            public void phpXiuSuccess(String response) {
                BaseUser mUser = model.getD();
                if (mUser == null) {
                    return;
                }
                coin = Long.parseLong(mUser.getCoin());
                coinInfo.setText(coin + "");
                EventBus.getDefault().postSticky(new UpdateBalanceEvent(mUser.getCoin() + ""));
            }
        });
    }

    /**
     * 更新余额
     */
    @Subscribe
    public void updateCoin(UpdateBalanceEvent event) {
        try {
            coin = Long.parseLong(event.getCurrentCoin());
            coinInfo.setText(coin + "");
        } catch (Exception e) {

        }
    }


    static class PayInfo {
        private String coin;
        private String result;

        PayInfo(String coin, String result) {
            this.coin = coin;
            this.result = result;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
