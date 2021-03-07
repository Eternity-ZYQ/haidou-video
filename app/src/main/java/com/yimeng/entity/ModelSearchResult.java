package com.yimeng.entity;

import java.util.LinkedList;

/**
 * Created by user on 2018/6/27.
 */

public class ModelSearchResult {

    private String total;
    private LinkedList<ModelSearchProduct> modelSearchProducts;

    public ModelSearchResult(String total, LinkedList<ModelSearchProduct> modelSearchProducts) {
        this.total = total;
        this.modelSearchProducts = modelSearchProducts;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public LinkedList<ModelSearchProduct> getModelSearchProducts() {
        return modelSearchProducts;
    }

    public void setModelSearchProducts(LinkedList<ModelSearchProduct> modelSearchProducts) {
        this.modelSearchProducts = modelSearchProducts;
    }
}
