package com.wiilo.common.enums;

import com.wiilo.common.utils.ResponseIEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 错误枚举类
 *
 * @author Whitlock Wang
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SystemErrorEnum implements ResponseIEnum {

    // 0 全局模块的错误 40000001-40009999
    PARAM_NULL(40000001, "request param %s is null"),// 请求参数为空
    NO_DATA_FOR_UPDATE(40000002, "no data for update"),//
    NEED_LOGIN(40000003, "need login"),//需要登錄
    USER_AGENT_NOT_FOUND(40000004, "User agent not in request head"),//请求头未携带User-Agent
    // 1 公共模块的错误 400110001-400119999
    PARAM_ILLEGAL(40010001, "request param illegal,%s"), // 请求参数异常
    DUPLICATE_DATA(40010002, "data error,expect 1 but found more than 1"), //
    NO_DATA(40010003, "no data!"), //
    DUPLICATE_USER(40010004, "user has been registered"), //
    REQUEST_METHOD_NOT_SUPPORTED(40010005, "request method not supported"), // 请求方式不支持
    REQUEST_BODY_IS_REQUIRE(40010006, "request body is required"), // 请求方式不支持
    COMM_LOCKKEY_NULL(40010007, "lockKey is null"),// 分布式锁的key为空
    ERROR_TIME_FORMAT(40010008, "time you provide cannot format,%s"),// 错误的时间格式
    ERROR_REQUEST_LIMIT(40010009, "api request limit error,limit %s times in %s ms"),//请求次数异常
    NO_SPACES_OF_PARM(40010010, "No Spaces are allowed in the argument"),//参数中不能有空格
    MD5_ERROR(40010011, "MD5 encode error"),//自定义MD5加密方法故障
    FREEZE_USER_ERROR(40010012, "Your account has been frozen. Please contact our customer services for details."),
    DUPLICATE_EMAIL(40010013, "This email address is already taken. Please try another."), //邮箱已经被注册了
    DUPLICATE_MOBILE(40010014, "This phone number is already taken. Please try another."), //手机号已经被注册了
    PLATFORM_ERROR(40010015, "Platform error ."),//请求超次数
    GP_CONFIG_ERROR(40010016, "Gp configuration error,%s"),//请求超次数
    REQUEST_PARAM_INVALID(40010017, "Request param %s is invalid"), //请求参数错误
    TIME_RANGE_TOO_LONG(40010018, "The time frame is too large, no more than %s days."),
    // 2 data-center模块错误 400210001-400219999
    // 3 user-center模块异常 400310001-400319999
    ACCOUNT_NOT_EXIST(40030001, "The account you entered doesn't exist. Please try a different one."),//用户邮箱账户不存在
    REGISTER_SERVICE_ERROR(40030002, "user register service execute Exception"), //SAPI用户服务异常
    ACCOUNT_NOT_MATCH(40030003, "The account name or password you entered is incorrect."),//用户邮箱或密码不正确
    USER_UPDATE_FAILED(40030004, "user information update operation failed"),//用户信息更新操作失败
    VALIDCODE_NOT_MATCH(40030005, "validCode does not match"),//用户输入的验证码不匹配
    VALIDTOKEN_NOT_MATCH(40030006, "validToken does not match,please verify again"),//重置密码操作用的token信息不匹配,重新验证
    PASSWORD_NOT_MATCH(40030007, "password not right"),//修改密码操作用的旧密码不对
    LOGOUT_DUPLICATED(40030008, "sessionId has been removed"),//重复注销同一个sessionId
    NICKNAME_NOT_MATCH(40030009, "nickname required not match the user information"),//用户手机或者昵称不相互匹配
    REGISTERED_PHONE(40030010, "phone number has been registered"),//改手机号已被使用
    PHONE_REGION_NOT_SUPPORT(40030011, "phone region not supported yet,only 86 and 1 are allowed"),//手机区域号不支持，暂时只支持86 or 1
    SMSCODE_SENDED(40030012, "ValidCode has been sent already,check it out"),//验证码操作过于频繁
    DUPLICATE_PASSWORD(40030013, "newPassword shouldn't be the same with the old one"),//修改后的密码不可与原密码相同
    EMAIL_REQUIRED_CHANGED(40030014, "email required should not change"),//验证邮箱发生更改
    PHONE_NUMBER_NOT_SUPPORTED(40030015, "The phone number you entered is invalid, please check and try again."),//手机号码不正确
    NEED_YOUR_OWN_MOBILE(40030016, "phone number is not the one registered"),//发送短信验证码的手机号不是本人的
    NEED_YOUR_OWN_Email(40030017, "email is not the one registered"),//发送邮箱验证码的邮箱地址不是本人的
    DUPLICATE_PHONE(40030018, "new phone number shouldn't be the same with old one"),//修改后的手机号不能与原号相同
    SMSCODE_TIMEOUT(40030019, "the code sent is invalid,please send again"),//操作超时，验证码失效请重新发送
    USERNAME_ILLEGAL(40030027, "username is not allowed to use,please check"),//用户名不合法，或者含有敏感词汇需重新注册
    USERADDR_DELETE_ERROR(40030020, "address delete failed,please try again later"),//用户地址信息删除失败，请重试
    USERADDR_QUERY_FAILED(40030021, "addressId required not exists,check it out"),//用户地址Id不存在，请检查
    COUPON_CLAIMED(40030022, "the coupon has been claimed already"),//该优惠券已经被该用户领取，无法再次领取
    COUPON_EXPIRED(40030023, "the coupon has been expired,can't use it now"),//该优惠券已经过期无法使用
    COUPON_NOT_EXIST(40030024, "the couponId required does not exists"),//该优惠券id查询不到详细信息,请核实
    USERADDR_ILLEGAL(40030028, "the countryName is not illegal,please check it out"),//用户地址国家名填写不正确，请检查
    EMAIL_ILLEGAL(40030029, "the email for registration is illegal,try another one"),//邮箱不符合正则匹配表达式
    PASSWORD_UPDATE_FAILED(40030035, "failed to change password"),//用户修改密码操作失败(与信用卡加解密服务有关)
    USER_THIRD_LOGIN_ERROR_BY_TYPE(40030036, "Failed to select the correct login method supported by the platform"),//第三方登录类型错误异常
    USER_THIRD_LOGIN_ERROR(40030037, "Third party logging exception"),//第三方登录异常
    USER_THIRD_REQUEST_FB_VERIFY_ERROR(40030038, "Request facebook to check for exceptions"),//请求facebook验签异常
    THIRD_REQUEST_FB_USER_ERROR(40030039, "Request facebook user data exceptions"),//请求facebook用户数据验签异常
    LOGIN_ERROR(40030040, "fail to create JWT token when login"),
    JWT_EXPIRED(40030041, "your JWT token is expired and need to login"),
    JWT_REFRESHED(40030044, "your JWT token has been refreshed"),
    IDENTITY_NUMBER_ILLEGAL(40030045, "ID card numbner is illegal,please check it"),
    THIRD_EMAIL_IS_NULL(40030046, "third email not is null"),
    EMAIL_NEED_VERIFY(40030047, "the email need be verified"),    //邮箱在保存的时候需要被验证过
    MOBILE_NEED_VERIFY(40030048, "the mobile need be verified"),    //手机号在保存的时候需要被验证过
    USER_LOGGED_OFF(40030049, "The account you entered has been deleted. Please register a new one."),
    CREDIT_QUERY_FAILED(40030025, "credit query exception"),//查询不到该信用卡信息
    CREDIT_UPDATE_FAILED(40030030, "creditCard update failed"),//更改信用卡信息失败
    CREDIT_DECODE_FAILED(40030026, "decode information failed"),//信用卡信息解码失败
    CREDIT_ENCODE_FAILED(40030034, "encode information failed"),//信用卡信息解码失败
    CREDIT_YEAR_ILLEGAL(40030031, "the length of expiryYear should be 2"),//有效期年份必须为两位数字
    USER_AUTHORIZATION_FAILED(40030032, "Code for Authorize your Email does not match,please check it out"),//授权邮箱的code不匹配
    INVITATION_REGISTER_ERROR(40030033, "Invite registration exception"),
    FILE_UPLOAD_FAILED(40030043, "fail to upload photo into server,please try again"),
    FILE_TOO_BIG(40030042, "image is more than 2M,please reduce the size!"),
    PICCODE_MATCH_FAILED(40030050, "Verification code invalid."),//图片验证码不匹配
    SMSCODE_MATCH_FAILED(40030051, "Verification code invalid."),//短信验证码不匹配
    SMSCODE_ISNULL(40030052, "opType can not be null"),//图片验证码不匹配
    SHOPNC_LOGIN_ERROR(40030053, "Login to gp failed msg->%s"),//图片验证码不匹配
    Account_BIND_ERROR(40030054, "您输入的账号已被绑定！"),//用户已绑定三方账号
    EXCESSIVE_NUMBER_REQUESTS(40030055, "Sorry, your submission has exceeded the limit."),//请求超次数
    REGION_NOT_MATCH(40030056, "Please select a valid country code."),
    REMOTE_USERSERVICE_ERROR(40030057, "User remote call exception"), //SAPI用户服务远程调用异常
    SHARE_GOODS_REACHED_LIMIT(40030058, "The goods sharing task has reached the upper limit today"),
    SHARE_GOODS_REQUEST_FREQUENT(40030059, "The goods sharing requests are too frequent."),
    MOBILE_ENCODE_FAILED(40030060, "encode mobile failed."),
    USER_HAS_NO_PERMISSIONS(40030061, "The user has no permissions."),
    USER_FOLLOW_EMAIL_OCCUPIED(40030062, "Your email address has been occupied"),
    ERROR_ADDING_RECORD(40030063, "Error adding record"),
    NEWCOMER_ACTIVITY_CONFIG_ERROR(40030064, "Activity not started"),
    NEWCOMER_ACTIVITY_IS_OVER(40030065, "Activity ended"),
    NEWCOMER_ACTIVITY_ADDR_CONFIG_ERROR(40030066, " Activity not started"),
    NEWCOMER_ACTIVITY_COUPON_CONFIG_ERROR(40030067, " Activity not started"),
    // 4 product-center模块异常 400410001-400419999
    /*PRODUCT_OP_ERROR(400410001, "product op error"),
    PRODUCT_OPTYPE_ISNULL(400410010, "opType can not be null"),
    CATEGORY_NOT_FOUND(40042001, "category not found"),
    SYNCHRONOUS_BRAND_ERROR(40042002, "Synchronous brand error"),
    ADD_OR_REMOVE_BRAND_EXCEPTIONS(40042003, "Add/remove brand exceptions"),
    QUERY_THE_BRAND_LIST_EXCEPTIONS(40042004, "Query the brand list exceptions"),
    QUERY_THE_BRAND_LIST_FOR_AN_EXCEPTION(40042005, "Query the brand list for an exception"),
    ADD_NEW_CATEGORY_EXCEPTIONS(40042006, "The new category exceptions"),
    MODIFY_CATEGORY_EXCEPTIONS(40042007, "Modify category exceptions"),
    QUERY_THE_CATEGORY_LIST_EXCEPTION(40042008, "Query the category list exception"),
    QUERY_FOR_COMMODITY_EXCEPTIONS(40042009, "Query for commodity exceptions"),
    SYNCHRONOUS_SPECIFIED_MERCHANDISE_OPERATION_EXCEPTION(40042010, "Synchronous specified merchandise operation exception"),
    QUERY_THE_COMMODITY_OPERATION_EXCEPTION(40042011, "Query the commodity operation exception"),
    QUERY_PRODUCT_ERROR(40042012, "Query the productList error"),//查询商品列表信息出错，可能用户输入参数没有对应的mapping，也可能es出错
    WISH_PUSH_ERROR(40042013, "wish push error"),
    WISH_NOT_FOUND(40042014, "wish is not found"),
    ACTIVITY_NOT_FOUND(40042015, "Sorry, this promotion has ended."),
    QUERY_RECOMMENDED_PRODUCTS_EXCEPTION(40042016, "Query recommended products exception"),
    // 5 cart-center模块异常 400510001-400519999
    NO_DATA_FOR_UPDATE_CART(40050001, "no data for update cart"),//
    BULK_UPDATE_CART_FAIL(40050002, "bulk update cart fail"),//
    BULK_PUSH_CART_FAIL(40050003, "bulk push cart fail"),//
    INSERT_INTO_CART_FAIL(40050004, "add into cart fail"),
    NO_DATA_FOR_SYNCHRO(40050005, "no cart data synchro"),
    NO_CART_WITH_CARTID(40050006, "no cart find by cartId:%s"),
    NO_STOCK_ADD_CART(40050007, "Sorry, we do not have enough stock for the quantity you have selected."),
    LACK_OF_STOCK(40050008, "Lack of stock due to : %s"),
    CART_QUANTITY_MUST_INT(40050009, "%s must int in cart"),
    SETTLEMENT_ERROR_PRODUCTSKU_OFF_SALE(40050010, "settlement error due to productsku off sale"),
    NO_PRODUCTSKU_FOR_SALE(40050011, "no productsku for sale"),
    REPEAT_SETTLEMENT_FAIL(40050012, "please not repeat settlement In short time"),
    NO_PRICE_SETTLEMENT_FAIL(40050013, "no price settlement"),
    NO_USERADDR_FOR_SETTLEMENT(40050014, "no useraddr selected for settlement"),
    CURRENCY_NOT_SUPPORT(40050015, "Currency not support"),
    BUYCOUNT_OUT_OF_RANGE(40050016, "Sorry, we do not have enough stock for the quantity you have selected."),
    BUYCOUNT_OUT_OF_ACTIVITY_RANGE(40050017, "You have reached the maximum quantity for this product."),
    TRANSPORTID_NOT_NULL(40050018, "Sorry, something went wrong. Please add this item to cart from the product detail page."),
    CART_REMOVED_WITH_SETTLEMENT(40050019, "Some items in cart have been removed, please check cart again before settlement."),
    // 6 order-center模块异常 400610001-400619999
    ORDER_NO_SUCH_ORDERLISTS(40060001, "no such orderLists"),
    ORDER_GET_STRIPETOKEN_FAIL(40060002, "get stripe token fail:%S"),
    ORDER_PARAM_WRONG(40060003, "request param %s is null"), //
    ORDER_STRIPE_PAY_ERROR(40060004, "order use stripe pay error"), //
    ORDER_NO_DATA_DELETE(40060005, "no such order for delete"),
    ORDERS_BULINSERT_FAIL(40060006, " paid order list insert error"),
    ORDERS_REPEAT_PAY_FAIL(40060007, "please not repeat pay order In short time"),
    QUERY_USERORDER_DETAIL_FAIL(40060008, "query user order detail fail"),
    ORDER_STATES_CANNOT_DELETE(40060009, "current order states no allowed to delete"),
    NO_USERADDR_FOR_ORDER(40060010, "no useraddr selected for order"),
    DETAIL_ADDRESS_SUPER_LONG(40060027, "The house number must be within 12 characters"),
    PUSH_ORDER_FAIL(40060011, "push orders fail"),
    THIRD_SYSTEM_ERROR(40060012, "third system return error:%s"),// 返回第三方系统错误
    ORDER_STATES_CANNOT_CONFIRM(40060013, "current order states no allowed to confirm"),
    ORDER_CARDERROR(40060014, "card error:%s"),
    ORDER_HAS_NOT_LOCK_STOCK(40060015, "please lock stock before order pay."),
    ORDER_CARDINFO_ERROR(40060016, "Whoops! It looks like there was a problem. Check your payment info and try again."),
    ORDER_CURRENCY_ERROR(40060017, "Alipay payment only supports CNY."),
    ORDER_NOT_MATCH(40060018, "required orderId does not belong to user,please try another one"),
    ORDER_CANCELED_PAYPAL(40060019, "order canceled pay by paypal"),
    ORDER_DISCOUNT_UNABLE(40060020, "Your order number is invalid. "),
    ORDER_ID_REPEAT(40060021, "Your order id is repeat, please back to the previous page and try again"),
    ORDER_REPAY_FAIL(40060022, "The order has been canceled"),
    ORDER_REPAY_FOUND_FAIL(40060023, "There is no pending payment in progress."),
    ORDER_REFUND_CANT(40060025, "Sorry, this order cannot be cancelled"),
    PURCHASE_LIMITATION(40050026, "Sorry, this item has the maximum purchase quantity of %s."),
    PLACE_THE_SHOPNC_ORDER_FAIL(40050027, "Place the gp order failed. msg-> %s"),//下单失败
    SHOPNC_ORDER_CONFIRM_FAIL(40050028, "Confirm gp order failed. msg-> %s"),//确认订单请求shopNc失败
    SHOPNC_ORDER_STATES_INFTERFACE_ERROR(40050029, "gp interface error. code %s"),
    SHOPNC_ORDER_CANCEL_FAIL(40050030, "Failed to cancel gp order. msg-> %s"),//取消订单请求shopNc失败
    SHOPNC_STORE_FAIL(40050038, "Store is null. msg-> %s"),//取消订单请求shopNc失败
    TRANSPORTID_IS_NULL(40050031, "skuId %s transportId is null"),//查询运费模板失败
    SHOPNC_TRANSLATE_FAIL(40050032, "translate fail"),//取消订单请求shopNc失败
    QUERY_USERORDER_FAIL(40060033, "The account does not match the order account."),
    QUERY_ORDER_COUNT_FAIL(40060035, "acquire order count fail."),
    POINTS_NOT_ENOUGH(40060034, "The account have not enough points to order."),
    SYNC_SIGN_ERROR(40060039, "Sign verification failed."),
    ORDER_DOES_NOT_EXIST(40060036, "The order:%s doesn't exist."),
    PAYPAL_PARTIC_EXCEED(40060037, "Sorry, you have participated in PayPal activity twice.This order cannot participate in PayPal activities"),
    ADDRESS_LOST_ID(40060040, "The address needs to be re-saved."),
    // 7 pay-center模块异常 400710001-400719999
    PAY_PARAM_WRONG(40070001, "request param %s is null"), //
    PAY_STRIPE_INVOKE_ERROR(40070002, "request stripe SDK error"), //
    PAY_SYSTEM_ERROR(40070003, "stripe api error"), //
    PAY_PAYPAL_ERROR(40070004, "paypal api error"), //
    PAY_ALIPAY_SIGNVERFIED_FAILURE(40070005, "Alipay signverfied failure"),
    PAY_ALIPAY_FAILED_CREATE_PAYMENT(40070006, "Alipay failed to create a payment form"),
    PAY_CHECK_SIGN_ERROR(40070007, "verification sign fails"),
    PAY_STRIPE_WEBHOOK_PAREM_ERROR(40070008, "webhook request parem is null error"),
    PAY_ALIPAY_NOT_SUPPORT_CURRENCY(400700011, "Global alipay not support currency: %s"),
    PAY_PAYPAL_SERVER_FORBBIDEN(400700012, "Paypal service is under maintaince,please try another pay method"),
    PAY_PAYPAL_PAY_ID_ERROR(400700013, "Paypal pay id error"),
    PAY_STRIPE_SERVER_FORBBIDEN(400700014, "We do not support credit card payment for this period of time."),
    PAY_PAYPAL_CANT_USED(400700015, "Functional Maintenance, Engineer's Urgent Development"),
    PAY_STRIPE_AMOUNT_OVER_LIMIT(400700016, "Sorry, the limit for payment with credit card per order is €50. Please change your payment method."),
    // 8 page-center模块异常 40080001-40089999
    BULK_UPDATE_ACTIVITY_FAIL(40080001, "bulk update activity goods fail"),//
    QUERY_ALL_ACTIVITY_FAIL(40080002, "query all activity goods fail"),//
    QUERY_ONE_ACTIVITY_FAIL(40080003, "query one activity goods by %s fail"),//
    QUERY_TB_REGIN_API_FAIL(40080004, "Failed to fetch \"taobao\" IP library. Please try again later."),//
    //9appsflyer归因数据异常
    ADD_APPSFLYER_DATA(60090001, "Appsflyer data addition exception"),
    //系统异常 50010001-500819999
    NO_TABLE_NAME(50010001, "%s,no table name"), // bean 没有定义表名
    DIFFERENT_CLASS_FOR_SQL(50010002, "%s,%s,sqlprovider params for class is difference"), // 更新时两个class不同
    ERROR(50019999, "system error,please retry later"),
    ERROR_REQUEST(50019998, "this an internal REST API service,You should apply an appid and secretKey for access"),//直接请求域名错误
    ERROR_REQUEST_NETWORK(50010003, "network error"),
    USER_PUSH_TO_QUEUE_FAILED(50030001, "fail to send to user queue"),//用户信息推送到queue异常
    EMAILSEND_ERROR(50030002, "fail to send email message"),//邮件发送失败
    USERADDR_CREATE_ERROR(50030003, "new userAddress data push failed"),//用户地址信息添加失败
    SMSCODE_SEND_FAILED(50030004, "sms code send failed,please try again later"),//短信验证码发送失败，请稍后再次请求服务
    USERADDR_UPDATE_ERROR(50030005, "address information update out of service,try again later"),//地址更新的server端服务异常
    USERADDR_SYNC_FAILED(50030006, "address information synchronize failed"),//地址信息从quene同步到es中失败
    CREDITCARD_INSERT_FAILED(50030007, "creditCard insert failed"),//银行卡信息添加失败
    CREDITCARD_DELETE_ERROR(50030008, "delete credit error"),//银行卡信息删除失败
    CREDITCARD_PUSH_ERROR(50030009, "error occurs when push creditCard data to server"),//信用卡信息推送到server端失败
    USERREAL_CHECK_ERROR(50030010, "The identity information you filled in is wrong. In order to ensure that your products can complete customs clearance, please fill in again, thank you."),
    //您填写的身份信息有误，为了保证您的商品可以完成清关，请您重新填写，谢谢。
    QUERY_REMINDER_ERROR(50040001, "error occurs when search subscribed reminder List in elasticSearch"),//在本地es查询订阅到货的商品列表操作报错
    INSERT_INTO_CART_ERROR(50050001, "insert into cart error"),
    UPDATE_CART_ERROR(50050002, "update cart error"),
    DELETE_CART_ERROR(50050007, "delete cart error"),
    QUERY_USER_CART_ERROR(50050003, "query user cart error"),
    USER_CART_SETTLE_ERROR(50050004, "user cart settle error"),
    USER_ORDER_SETTLE_ERROR(50050005, "user order settle error"),
    CHECK_LIMIT_ERROR(50050006, "check Limit error"),
    //订单模块异常码50060001-50069999
    ORDER_SYSTEM_ERROR(50060001, "order system error"),
    ORDER_SYNCHRO_ERROR(50060003, "order synchro error"),//订单同步失败
    ORDER_PAY_ERROR(50060002, "order pay error"),//
    ORDER_STATUS_ERROR(50060004, "order status error"),//
    ORDER_NOT_MATCH_USER(50060005, "The order does not match the user"),//
    //用户评论模块异常码
    //40090001-40099999业务异常，50090001-50099999系统异常
    CREATE_REVIEW_USER_EXCEPTION(40090001, "Comment on error"),
    QUERY_PRODUCT_REVIEWS_EXCEPTION(40090002, "Query product reviews error"),
    //POST方法 评论中心请求参数异常
    REVIEWS_PARAM_WRONG(40090003, "review center request param %s is null"),
    //GET方法 评论中心请求参数异常
    REVIEWS__PARM_ERROR(40090004, "api review center request parameter exception"),
    //没有收货的订单无法进行评论
    REVIEW_ORDER_COMMENTS_EROR(40090005, "No comments can be made on orders not received"),
    //一个订单下一个商品不可重复评论
    UNREPEATABLE_COMMENT_ERROR(40090006, "Unrepeatable comment"),
    //追评次数上限，最多追评4次
    REPLY_COMMENT_UPPER_LIMIT(40090007, "%s reply comment upper limit"),
    //追评时间超时，只能在第一次评论完成以后的180天内追评
    REPLY_COMMENT_OVERTIME(40090008, "%s reply comment overtime"),
    //未提交星级时返回提示文案
    COMMENT_STAR_NOT_FOUND(40090009, "please select a star rating"),
    //三方登录异常
    THIRD_LOGIN_NOT_FOUND(50070001, "Sorry, something went wrong."),//
    THIRD_LOGIN_NEED_ACCESS_TOKEN(50070002, "need accessToken"),//
    THIRD_LOGIN_NEED_EMAIL(50070003, "can not get email , please bind one"),//
    THIRD_LOGIN_NEED_PHONE_OR_EMAIL(50070004, "please bind email or phone"),
    THIRD_LOGIN_UNKNOWN_SOURCE(50070005, "unknown source"),
    THIRD_LOGIN_NEED_WECHAT_CODE(50070006, "need wechat code"),
    THIRD_LOGIN_NEED_WECHAT_AUTH(50070007, "need wechat authorize"),
    THIRD_LOGIN_WECHAT_CODE_OVERDUE(50070008, "wechat code overdue"),
    THIRD_LOGIN_WECHAT_ACCESS_TOKEN_OVERDUE(50070009, "wechat accessToken overdue"),
    THIRD_LOGIN_NEED_CODE(50070010, "need %s code"),
    THIRD_LOGIN_NEED_AUTH(50070011, "need %s authorize"),
    //优惠码异常
    PROMO_CODE_WRONG(50080001, "Incorrect coupon code"),//
    PROMO_CODE_NOT_MATCH(50080002, "Conditions not met"),//
    PROMO_CODE_ALL_USED(50080003, "This coupon code has already been used."),//
    PROMO_CODE_CLOSED(50080004, "This special offer has already ended."),//
    PROMO_CODE_WRONG_SKU(50080005, "There are no eligible items in this order for coupons."),//
    PROMO_CODE_WRONG_SITE(50080006, "The coupon code is not eligible for this site."),//
    PROMO_CODE_CANT_USE(50080007, "Can not use the coupon,please retry"),//*/
/*
	PROMO_CODE_ERROR(50080008,"优惠码错误"),
	PROMO_CODE_CONDITION(50080009,"未达到满足条件"),
	PROMO_CODE_USED(50080010,"该优惠码已经被使用"),
	PROMO_CODE_CLOSE(50080011,"该优惠活动已经结束"),
	PROMO_CODE_NOTFOUND(500800012,"优惠码不存在"),
*/
    //境内微信支付模块
    /*WECHAT_PAY_SIGN(50090001, "WeChat signature failed"),//签名
    WECHAT_PAY_ORDER(50090002, "Order query failed"),//查询订单
    WECHAT_PAY_UNIFIEDORDER(50090003, "Order query failed"),//统一下单
    WECHAT_PAY_CNY(50090004, "Please pay in RMB"),//统一下单
    // 微信退款失败
    WECHAT_PAY_REFUND(50090005, "Order refund failed"),
    // 微信退款 —— 一个订单多次退款
    WECHAT_PAY_REPEAT_REFUND(50090006, "Don't apply for multiple refunds for an order"),
    // 微信退款状态异常
    WECHAT_REFUND_STATUS_ABNORMAL(50090007, "Order status is abnormal"),
    // 签名校验失败
    WECHAT_REFUND_SHOPNC_SIGN(50090008, "Signature failed"),
    //发邮件验证
    EMAIL_CONFIRM_ERROR(50100001, "Please go to the mailbox for verification"),//未进行邮件验证
    EMAIL_VALIDATION_EXPIRED(50100002, "Validation Validation"),//验证已经过期
    EMAIL_HAVE_VALIDATION(50100003, "Verified, do not repeat"),//已验证，请勿重复
    PLACE_THE_ORDER_FAIL(50100013, "Place the order failed"),//下单失败
    DROP_SHIPPING_ORDER_FAIL(50110001, "excel error"),//表格异常
    DROP_SHIPPING_PRICE_FAIL(50110002, "skuId=%s not found price"),//无价格
    DROP_SHIPPING_PRODUCT_FAIL(50110003, "skuId=%s not found"),//不存在
    DROP_SHIPPING_PLATFORM_FAIL(50110004, "No Country/Region was found %s"),//没找到该国家站点
    DROP_SHIPPING_PLAT_FAIL(50110005, "Orders can only be placed in one Country/Region"),//只能在一个国家地区下单
    DROP_SHIPPING_ORDER_PAYMENT_FAIL(50110007, "Order paid orderIds: %s"),//订单已经支付
    DROP_SHIPPING_PAY_FAIL(50110008, "Please select an order for the same site"),//订单站点不统一站点错误
    DROP_SHIPPING_MORE_FAIL(50110009, "A maximum of 100 orders can be selected for payment"),//最多只能选择100条订单进行支付
    //nttPay
    NTT_PAY_FAIL(50100014, "Please pay in JPY or KRW"),
    //nttPay
    NTT_PAY_TRANSFER(50100004, "Please contact customer service"),
    *//**
     * 起购限制
     *//*
    GP_GOODS_MINIMUM(50100005, "Minimum purchase volume: %s"),
    *//**
     * 数量限购提示
     *//*
    GP_GOODS_MAXIMUM(50100006, "SKU Quantity exceeding limit : %s"),
    *//**
     * 下单限购提示
     *//*
    GP_MAXIMUM_PROMPT(50100007, " The available number of this item has exceeded the limit"),
    *//**
     * 创建支付订单失败
     *//*
    PLACE_THE_GPG_ORDER_FAIL(50100008, "Payment creation failed"),//下单失败
    *//**
     * 翻译数据为空
     *//*
    TRANSLATION_IS_NULL(50100009, "The translation data is empty!"),
    *//**
     * 当前站点不支持paypal
     *//*
    PAYPAL_NOT_SUPPORT(50100010, "Sorry, we do not support Paypal for payment."),
    BROADCAST_CN_NOT_ERROR(50100011, "直播间不存在"),*/
    /**
     * 类目数据为空
     */
    /*CATEGORY_IS_NULL(50100012, "The category data is empty!"),
    // 抽奖中心异常码 40100001-40199999
    // 用户已参与过这个抽奖活动
    USER_REPEAT_JOIN_LOTTERY(40100001, "您已参与此抽奖活动！"),
    // 用户没有分享抽奖活动
    USER_NOT_SHARE_LOTTERY(40100002, "请分享后再参与抽奖"),
    // 抽奖活动不存在
    LOTTERY_NOT_FOUND(40100003, "当前抽奖活动不存在！"),
    // 抽奖活动状态异常
    LOTTERY_STATE_ERROR(40100004, "当前抽奖活动状态异常！"),
    // 抽奖活动超时
    LOTTERY_TIME_OVER(40100005, "此抽奖活动已结束，看看其它的活动吧！"),
    // 抽奖活动参与人数已满
    LOTTERY_MEMBER_OVER(40100006, "此抽奖活动参与人数已满"),
    // 抽奖活动类型错误
    LOTTERY_TYPE_ERROR(40100007, "当前抽奖活动类型错误！"),
    // 服务器出错
    LOTTERY_SYSTEM_ERROR(40100008, "服务器出错，请重新再试！"),
    // 保存中奖记录失败
    SAVE_LOTTERY_RECORD_LOG_FAILED(40100009, "保存中奖记录失败！"),
    //商品重复收藏提示
    PRO_REPEAT_COLLECT(40100010, "has been added to favoriates"),
    //收藏失败
    PRO_COLLECT_FAILED(40100011, "failed to add"),
    PROMO_CODE_ERROR(50080008, "优惠码错误"),
    PROMO_CODE_CONDITION(50080009, "未达到满足条件"),
    PROMO_CODE_USED(50080010, "该优惠码已达到使用次数上限"),
    PROMO_CODE_CLOSE(50080011, "该优惠活动已经结束"),
    PROMO_CODE_NOTFOUND(500800012, "优惠码不存在"),
    PROMO_CODE_FAIL(500800013, "领取失败"),
    PROMO_CODE_REUSE(500800014, "请勿重复领取"),
    PROMO_NOT_SHARE_WITH_COUPONS(500800017, "促销活动与优惠券不共享"),
    ITEM_DISCOUNT_HAS_EXPIRED(500800018, "抱歉，促销活动已过期"),
    //暂时无法添加中国地区收货地址
    ADDRESS_ADD_CN_ERROR(40100050, "暂时无法添加中国地区收货地址"),
    //邮件、短信 不存在的发送方式
    NOT_FOUNT_SEND_CONTENT(500800015, "短信、邮件未找到对应的发送方式"),
    //短时间内重复请求
    REPEAT_ASK_FAIL(500800016, "请不要在短时间内重复请求"),
    //不支持的上报类型
    NOT_REPORTING_TYPE(500800050, "Reporting type [%s] not supported"),
    //Im相关
    IM_MESSAGE_SENDING_FAILURE(500800051, "Message sending failure"),//信息发送失败
    IM_STORE_CUSTOMER_SERVICE_DOES_NOT_EXIST(500800052, "Store customer service does not exist"),//门店客服不存在
    // 您24小时内已评价过该客服
    CANNOT_REPEAT_EVALUATE_OF_24_HOURS(500800053, "You have rated this customer service within 24 hours"),
    // 您24小时内还未咨询客服，请咨询后再评价
    NOT_CHAT_OF_24_HOURS(500800054, "You have not consulted customer service within 24 hours, please consult and then rate"),
    // 该评价已过期，不能进行修改
    EVALUATION_EXPIRED(500800055, "This review has expired and cannot be modified."),
    // 不可以修改太多次哦
    MODIFY_TIMES_LIMIT(500800056, "Please do not modify too many times!"),
    IM_WELCOME_TO_THE_STORE(500800057, "Hi, welcome to our shop! I am the customer service for you this time. May I help you?"),//欢迎光临本店！hi，我是本次接待您的客服，请问有什么可以帮助到您？
    ORDER_CANNOT_BE_INVOICED(500800059, "Sorry, this order cannot be invoiced"),*/
    ;

    /**
     * 错误码
     */
    private long code;
    /**
     * 错误提示信息
     */
    private String msg;

}
