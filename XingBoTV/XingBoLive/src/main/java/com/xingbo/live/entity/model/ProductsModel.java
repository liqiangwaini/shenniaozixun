package com.xingbo.live.entity.model;

import com.xingbobase.http.BaseResponseModel;
import com.xingbo.live.entity.Product;

import java.util.List;

/**
 * Created by WuJinZhou on 2016/2/18.
 */
public class ProductsModel extends BaseResponseModel {
    private List<Product>d;

    public List<Product> getD() {
        return d;
    }

    public void setD(List<Product> d) {
        this.d = d;
    }
}
