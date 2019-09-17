package com.chengzi.app.ui.mine.bean;

/**
 * @Desc:
 * @ClassName:WalletBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/25 0025
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class WalletBean {

    /**
     * "id": 1,                // 名字
     * "sum": "8888.88",       // 钱包余额
     * "type": 4,              // 类型 1-用户 2-医生 3-咨询师 4-机构',
     * "bind_wx": 0,           // 是否绑定微信 0未绑定，1已绑定
     * "sum": "8888.88",       // 钱包余额
     * "bind_ali": 0,          // 是否绑定支付宝 0未绑定，1已绑定
     * "bind_acount": 1        // 是否绑定对公账户 0未绑定，1已绑定
     */

    private int id;
    private String sum;
    private int type;
    private int bind_wx;
    private int bind_ali;
    private int bind_acount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBind_wx() {
        return bind_wx;
    }

    public void setBind_wx(int bind_wx) {
        this.bind_wx = bind_wx;
    }

    public int getBind_ali() {
        return bind_ali;
    }

    public void setBind_ali(int bind_ali) {
        this.bind_ali = bind_ali;
    }

    public int getBind_acount() {
        return bind_acount;
    }

    public void setBind_acount(int bind_acount) {
        this.bind_acount = bind_acount;
    }
}
