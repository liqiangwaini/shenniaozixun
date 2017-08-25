package com.xingbo.live.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xingbo.live.Constants;
import com.xingbo.live.R;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.ui.RechargeAct;
import com.xingbo.live.util.CommonUtil;

public class WXPayEntryActivity extends BaseAct implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		CommonUtil.log(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			0	成功	展示成功页面
//			-1	错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//			-2	用户取消	无需处理。发生场景：用户不支付了，点击取消，返回APP。
			Intent recharge=new Intent(WXPayEntryActivity.this,RechargeAct.class);
			recharge.putExtra(RechargeAct.EXTRA_WXPAY_RESP_TYPE_VALUE, resp.getType());
			recharge.putExtra(RechargeAct.EXTRA_WXPAY_RESP_ERROR_CODE_VALUE, resp.errCode);
			startActivity(recharge);
			finish();
//			String message = "微信支付失败";
//			if (TextUtils.equals(""+resp.errCode, "0")) {
////				CommonUtil.tip(this, "微信支付成功", Gravity.CENTER);
//				message = "微信支付成功";
//			} else {
//				if (TextUtils.equals(""+resp.errCode, "-1")) {
////					CommonUtil.tip(this, "微信支付失败：" + resp.errCode, Gravity.CENTER);
//					message = "微信支付失败：" + resp.errCode;
//				}else if(TextUtils.equals(""+resp.errCode, "-2")){
////					CommonUtil.tip(this, "您已取消微信支付", Gravity.CENTER);
//					message = "您已取消微信支付";
//				} else {
//
//				}
//			}
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(message);
//			builder.show();
//
//			this.refresh();
		}
	}

//	private void refresh(){
//		RequestParam param=RequestParam.builder(this);
//		CommonUtil.request(this, HttpConfig.API_USER_GET_PROFILE, param, TAG, new PHPXiuResponseHandler<MainUserModel>(MainUserModel.class) {
//			@Override
//			public void phpXiuErr(int errCode, String msg) {
//				Intent recharge = new Intent(WXPayEntryActivity.this, RechargeAct.class);
//				startActivity(recharge);
//				finish();
//			}
//
//			@Override
//			public void phpXiuSuccess(String response) {
//				BaseUser mUser = model.getD();
//				if (mUser == null) {
//					alert("初始化失败！");
//					return;
//				}
//
//				Intent recharge=new Intent(WXPayEntryActivity.this,RechargeAct.class);
//				recharge.putExtra(RechargeAct.EXTRA_USER_COIN_VALUE, Long.parseLong(mUser.getCoin()));
////				EventBus.getDefault().postSticky(new UpdateBalanceEvent(mUser.getCoin() + ""));
//				startActivity(recharge);
//				finish();
//			}
//		});
//	}
}