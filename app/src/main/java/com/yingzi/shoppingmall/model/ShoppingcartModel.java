package com.yingzi.shoppingmall.model;

import java.util.List;

/**
 * Created by yingzi on 2018/2/8.
 */

public class ShoppingcartModel {
    private List<GoodsListBean> goods_list;

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class GoodsListBean {
        private Boolean isCheck;
        private String name;
        private String price;
        private String num;
        private String good_id;

        public String getGood_id() {
            return good_id;
        }

        public void setGood_id(String good_id) {
            this.good_id = good_id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public Boolean getCheck() {
            return isCheck;
        }

        public void setCheck(Boolean check) {
            isCheck = check;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
