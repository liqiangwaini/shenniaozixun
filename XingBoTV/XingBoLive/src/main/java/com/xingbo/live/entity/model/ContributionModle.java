package com.xingbo.live.entity.model;

import com.xingbo.live.entity.ContributionPage;
import com.xingbobase.http.BaseResponseModel;

/**
 * Created by 123 on 2016/6/2.
 *
 * @package com.xingbo.live.entity.model
 * @description 描述 最新星币余额 数据
 */
public class ContributionModle extends BaseResponseModel {
    private ContributionPage d;

    public ContributionPage getD() {
        return d;
    }

    public void setD(ContributionPage d) {
        this.d = d;
    }
}
