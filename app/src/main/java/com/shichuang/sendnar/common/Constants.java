package com.shichuang.sendnar.common;

/**
 * Created by Administrator on 2018/3/14.
 */

public interface Constants {
    //String MAIN_ENGINE = "http://192.168.2.154:8080/songnaer";
    String MAIN_ENGINE = "http://www.sendnar.com/songnaer";

    //String MAIN_ENGINE_PIC = "http://192.168.2.154:8080";
    String MAIN_ENGINE_PIC = "http://www.sendnar.com";

    // 平台标识码
    int PLATFORM = 2;  // 1=IOS|2=Android
    // 支付宝
    String ALIPAY_PID = "2088921843444683";
    String ALIPAY_SELLER = "yz-fuqiang@163.com";
    String ALIPAY_NOTIFY_URL = MAIN_ENGINE + "/api/payment/alipaypc/notify_url";
    String ALIPAY_RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMf/6BwEp3mC1vMKT7UgZzT2eakprAYxa9B0WYFsIlphMYVHUJ9a6etUcXgENdBRa6" +
            "t3obTrWi42fBjZT71ZZAkLCRY17Y6K27+c4GeXfWbve3H3F0jIlzDj7TxVu6MTdN0dZ1o9FtqceeDOP5oMLdYinFjcuWw/x5C5psMLWec5AgMBAAECgYBgsXvBmAp0N+q+RDz3" +
            "H/xl4cg+HfSaZehvNuFTLjmJorXzbuAuuyCe8wuM79QorES2+LbIpoTKFPN4fBAORpqS3Yd9knJbV3rYKJ4Awtc9ourTfMSmf7cALI8LwGM2fSizjMU+dhrXjZ4GCzjka1I0lY2h" +
            "oHWRNeT5RnAM+BXUgQJBAO+vB4GFJ4k0U69ETHl+vBdf80OkR+EBw1cIIBQElua8ex3s7kesU9k/jJY7Ds3HTOnHyef9EuSR2e9YfBlDsBECQQDVnU11fjoV6GqRK6j2dBuLVwu/qA" +
            "LdcJoxf1zAiizBphOyBXoHx0g/X4/ssVoT/GuScl3MpL5KSB4qIFucq+ypAkEAs/9tEp6t10M9yXTwPZmopaFALj80X80UJssKVB4yOSu5BftF4vuOqKsp6DSH5I/Uugh5H8iVpKPH2B" +
            "BKeiq0EQJAeWhuHdkgKlNuPhQSdACJpyhSBRv4hOL6wCcjEEt4JJe/me6C7OjWpy4pHt2t1K4idZmKtkTr4kwa2NaLOwB8AQJAZgaEDx+fsver6Cvnf7CXSDVDnIz80GKHh7WrteVDv7z" +
            "ZKMSX27DiYrMMIqT3E9azyP0hpVuwv2P5Yy0qz/iySA==";

    String WX_TOTAL_ORDER = "";
    String wxMakeOrderUrl = MAIN_ENGINE + "/api/payment/weixinpay/app/makePreOrder";

    // 上传文件
    String uploadFile = MAIN_ENGINE + "/upload/uploadFile";
    // 首页活动轮播
    String homeUrl = MAIN_ENGINE + "/api/v1/basic/getIndexTablelList";
    // 正在进行中的活动
    String nowActionUrl = MAIN_ENGINE + "/api/action_info/get_now_action_list";
    // 往期活动
    String pastActionListUrl = MAIN_ENGINE + "/api/action_info/get_past_action_list";
    // 扶贫详情
    String povertyAlleviationActivitiesDetailsUrl = MAIN_ENGINE + "/api/v1/action_info/get_actionAndGoods_detail_list";
    // 节日 获取礼包和轮播图
    String actionCarouselListUrl = MAIN_ENGINE + "/api/v1/action_info/get_action_Carousel_list";
    String actionCarouselList2Url = MAIN_ENGINE + "/api/v1/action_info/get_action_Carousel_list2";
    // 分类-获取左边分类数据
    String getCategoryUrl = MAIN_ENGINE + "/api/shop_goods_price/get_list";
    // 分类，右侧类型1数据
    String giftsCategoryType1Url = MAIN_ENGINE + "/api/shop_goods_festival/get_list";
    // 分类，右侧类型2数据
    String giftsCategoryType2Url = MAIN_ENGINE + "/api/v1/action_info/get_action_page_list";
    // 节日——活动商品
    String actionListUrl = MAIN_ENGINE + "/api/action_info/get_action_item_list";
    String actionList2Url = MAIN_ENGINE + "/api/action_info/get_action_item_list2";
    // 礼品、专区，商品列表
    String goodsListUrl = MAIN_ENGINE + "/api/v1/goods/get_goods_List";
    // 关键词搜索商品
    String searchGoodsByGoodsNameUrl = MAIN_ENGINE + "/api/v1/goods/goods_name_seo";
    // 礼物详情
    String giftsDetailsUrl = MAIN_ENGINE + "/api/action_info/get_detail";
    // 注册
    String userRegisterUrl = MAIN_ENGINE + "/api/v1/user/register";
    // 获取验证码
    String getSmsCodeUrl = MAIN_ENGINE + "/api/v1/basic/getSmsCode";
    // 登录
    String userLoginUrl = MAIN_ENGINE + "/api/v1/user/userlogin";
    // 忘记密码
    String forgotPasswordUrl = MAIN_ENGINE + "/api/v1/user/forget_pwd";
    // 判断第三方是否绑定
    String checkOpenIdUrl = MAIN_ENGINE + "/api/v1/oauth_login/check_openid";
    // 第三方登录或者绑定
    String oauthLoginUrl = MAIN_ENGINE + "/api/v1/oauth_login/handler";
    // 添加地址
    String addAddressUrl = MAIN_ENGINE + "/api/v1/order/address_add";
    // 编辑地址
    String editAddressUrl = MAIN_ENGINE + "/api/v1/order/address_edit";
    // 省
    String getProvinceUrl = MAIN_ENGINE + "/manage/region/province_json_list";
    // 市
    String getCityUrl = MAIN_ENGINE + "/manage/region/city_json_list";
    // 区
    String getAreaUrl = MAIN_ENGINE + "/manage/region/area_json_list";
    // 地址
    String getAddressUrl = MAIN_ENGINE + "/api/v1/order/getAddress";
    // 删除地址
    String deleteAddressUrl = MAIN_ENGINE + "/api/v1/order/Address_delete";
    // 设置默认地址
    String setDefaultAddressUrl = MAIN_ENGINE + "/api/v1/order/replacedefaultAddress";
    // 添加到购物车
    String addShoppingCartUrl = MAIN_ENGINE + "/api/v1/goods/addsongnaerShoppingCart";
    // 查看购物车
    String lookShoppingCartUrl = MAIN_ENGINE + "/api/v2/goods/lookSongnaerShoppingCart";
    // 更新购物车数量
    String updateShoppingCartGiftCountUrl = MAIN_ENGINE + "/api/v1/goods/updatesongnaerShoppingCart";
    // 删除购物车商品
    String deleteShoppingCartGiftUrl = MAIN_ENGINE + "/api/v1/goods/deletesongnaerShoppingCart";
    // 推荐商品
    String recommendGoodsUrl = MAIN_ENGINE + "/api/v1/goods/recommendcGoods";
    // 获取个人信息
    String selfInfoUrl = MAIN_ENGINE + "/api/v1/myself/songnaermyselfInfo";
    // 我的订单
    String myOrderUrl = MAIN_ENGINE + "/api/v1/my_shop_goods_order";
    // 订单详情
    String orderDetailsUrl = MAIN_ENGINE + "/api/v1/my_shop_goods_order_detail_info";
    // 物流信息
    String getLogisticsMsgUrl = MAIN_ENGINE + "/api/v1/message/getLogisticsMsgList";
    // 确认订单
    String confirmOrderUrl = MAIN_ENGINE + "/api/v1/generate_Order";
    // 购物车-确认订单
    String confirmShoppingCartOrderUrl = MAIN_ENGINE + "/api/v1/generate_Order2";
    // 订单结算
    String submitOrderUrl = MAIN_ENGINE + "/api/v1/order/submitOrder3";
    // 购物车-订单结算
    String submitShoppingCartOrderUrl = MAIN_ENGINE + "/api/v1/order/submitOrder2";
    // 取消订单
    String cancelOrderUrl = MAIN_ENGINE + "/api/v1/shop_goods_order_cancel";
    // 删除订单
    String deleteOrderUrl = MAIN_ENGINE + "/api/v1/delete_goods_order";
    // 确认收货
    String confirmGoodsUrl = MAIN_ENGINE + "/api/v1/Confirmation_of_receipt";
    // 更换个人信息
    String changeMySelfInfoUrl = MAIN_ENGINE + "/api/v1/myself/changeSongnaerMyselfInfo";
    // 通过订单号获取订单信息
    String getOrderInfoByOrderNoUrl = MAIN_ENGINE + "/api/v1/my_shop_goods_order_detail_info2";
    // 礼物列表
    String getGiftListUrl = MAIN_ENGINE + "/api/v1/goods/getGiftList";
    // 送礼
    String sendGiftUrl = MAIN_ENGINE + "/api/v1/goods/sendGift";
    // 转赠
    String examplesGiftUrl = MAIN_ENGINE + "/api/v1/goods/transferGift";
    // 查看送礼收礼信息
    String lookGiftInfoUrl = MAIN_ENGINE + "/api/v1/goods/lookGiftInfo";
    // 意见反馈
    String feedbackUrl = MAIN_ENGINE + "/api/v1/user_feekback";
    // 商务合作
    String businessCooperationUrl = MAIN_ENGINE + "/api/v1/business/business_cooperation";
    // 常见问题
    String getCommonProblemsUrl = MAIN_ENGINE + "/api/v1/user/normal_problem";
    // 常见问题
    String getAboutUsUrl = MAIN_ENGINE + "/api/v1/user/about_us";
    // 送礼提交订单
    String receiveGiftSubmitOrderUrl = MAIN_ENGINE + "/api/v1/goods/ReceiveGift_submit_Order";
    // 我的积分
    String myPointUrl = MAIN_ENGINE + "/api/v1/user/my_point";
    // 积分列表
    String pointListUrl = MAIN_ENGINE + "/api/v1/user/point_list";
    // 积分指南
    String integralGuideUrl = MAIN_ENGINE + "/api/v1/user/single_point";
    // 我的团队
    String myTeamUrl = MAIN_ENGINE + "/api/v1/charitable_main";
    // 排行榜
    String rankUrl = MAIN_ENGINE + "/api/v1/charitable/rank";
    // 发票须知
    String invoiceInstructionsUrl = MAIN_ENGINE + "/api/v1/user/fp";
    // 历史发票信息
    String invoiceHistoryInformationUrl = MAIN_ENGINE + "/api/v1/invoice_info/invoice_info1";
    // 申请提现
    String applyWithdrawUrl = MAIN_ENGINE + "/api/v1/commission/applyCommissionWithdrawSongnaer";
    // 收益信息
    String getEarningsInformationThisMonthUrl = MAIN_ENGINE + "/api/v1/charitable/zongjine";
    // 设置合伙人
    String setPartnerUrl = MAIN_ENGINE + "/api1/teamt_partner_member";
    // 慈善贡献度
    String getHonoraryContributionUrl = MAIN_ENGINE + "/api/v1/charitable";
    // 通用单页
    String getSinglePageUrl = MAIN_ENGINE + "/api/v1/user/songnaer_single_page";
    // 是否微信授权
    String checkAuthorizationOpenidUrl = MAIN_ENGINE + "/api/v1/oauth_login/check_authorization_openid";
    // 获取提现税率
    String getTaxRateUrl = MAIN_ENGINE + "/api/v1/commission/get_tax_rate";
    // 提现记录
    String withdrawalRecordUrl = MAIN_ENGINE + "/api/v1/commission/myWithdrawDetail";
    // 填写物流单号
    String fillInTheLogisticsUrl = MAIN_ENGINE + "/api/v1/goods_refund_transform/submit";
    // 获取退货换货原因
    String refundReturnGoodsReasonUrl = MAIN_ENGINE + "/api/v1/refund/refund_cause";
    // 退货退款，提交
    String refundReturnGoodsCommitUrl = MAIN_ENGINE + "/api/v1/goods_refund/submit";
    // 物流公司
    String logisticsCompanyListUrl = MAIN_ENGINE + "/api/v1/logistics_company";
    // 退换货详情
    String refundInfoUrl = MAIN_ENGINE + "/api/v1/goods_refund_cancel/refundInfo";
    // 我的物品，送给自己
    String myItemsToSendMeConfirmOrderUrl = MAIN_ENGINE + "/api/v1/gift/gift_pack_receive_page";
    // 我的物品，提交订单
    String myItemsToSendMeCommitOrderUrl = MAIN_ENGINE + "/api/v1/gift/gift_pack_receive";
}
