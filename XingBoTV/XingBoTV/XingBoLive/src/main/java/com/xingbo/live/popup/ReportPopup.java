package com.xingbo.live.popup;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.xingbo.live.R;
import com.xingbo.live.http.HttpConfig;
import com.xingbo.live.ui.BaseAct;
import com.xingbo.live.util.CommonUtil;
import com.xingbobase.http.BaseResponseModel;
import com.xingbobase.http.RequestParam;
import com.xingbobase.http.XingBoResponseHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingbo_szd on 2016/7/12.
 */
public class ReportPopup extends PopupWindow  implements AdapterView.OnItemClickListener,View.OnClickListener{
    private static final String TAG="ReportPopup";

    private List<String> reportList;
    private BaseAct act;
    private String uid;

    public ReportPopup(BaseAct act,String uid) {
        this.act = act;
        this.uid=uid;
        reportList=new ArrayList<>();
        reportList.add("政治敏感");
        reportList.add("色情低俗");
        reportList.add("广告骚扰");
        reportList.add("人身攻击");
        reportList.add("声音违规");
        reportList.add("其他");
        View rootView = View.inflate(act, R.layout.popup_report, null);
        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        ListView listView = (ListView) rootView.findViewById(R.id.lv_popup_report);
        TextView cancel= (TextView) rootView.findViewById(R.id.tv_popup_report_cancel);
        listView.setAdapter(new ReportAdapter());
        listView.setOnItemClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String reportText=reportList.get(position);
        RequestParam param=RequestParam.builder(act);
        param.put("uid",uid);
        CommonUtil.request(act, HttpConfig.API_APP_REPORT_USER, param, TAG, new XingBoResponseHandler<BaseResponseModel>(act, BaseResponseModel.class) {
            @Override
            public void phpXiuErr(int errCode, String msg) {
                act.alert(msg);
            }

            @Override
            public void phpXiuSuccess(String response) {
                dismiss();
                act.alert(model.getM());
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_popup_report_cancel){
            dismiss();
        }
    }

    class ReportAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return reportList.size();
        }

        @Override
        public Object getItem(int i) {
            return reportList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(act, R.layout.item_popup_report, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_item_popup_report);
            textView.setText(reportList.get(i));
            return view;
        }
    }

}
