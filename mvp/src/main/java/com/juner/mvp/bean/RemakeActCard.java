package com.juner.mvp.bean;


//纸质卡补录
public class RemakeActCard {
    //卡号，界面唯一识别码
    private String activitySn;
    //套餐分类id
    private Integer activityId;
    //商品id
    private Integer goodsId;
    //会员id
    private Integer userId;
    //套餐名
    private String activityName;
    //此商品剩余次数
    private Integer goodsNum;


    //商品名
    private String goodsName;
    //商品编码
    private String goodsCode;
    //价格
    private String price;
    //开始时间
    private Long addTime;
    //结束时间
    private Long endTime;
    //套餐id  对应套餐列表里对象的主键id
    private Integer detailsId;
    //车牌
    private String carNo;
    //5.30新加字段规格id和名字
    private String standardId;
    private String goodsStandardTitle;

    public String getStandarId() {
        return standardId;
    }

    public void setStandarId(String standarId) {
        this.standardId = standarId;
    }

    public String getGoodsStandardTitle() {
        return goodsStandardTitle;
    }

    public void setGoodsStandardTitle(String goodsStandardTitle) {
        this.goodsStandardTitle = goodsStandardTitle;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getActivitySn() {
        return activitySn;
    }

    public void setActivitySn(String activitySn) {
        this.activitySn = activitySn;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

}
