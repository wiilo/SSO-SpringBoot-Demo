package com.wiilo.common.constant;

/**
 * 系统常量类
 *
 * @author Whitlock Wang
 */
public class SystemConstant {

    private SystemConstant() {
    }

    /**
     * 字符
     */
    public static final String All = "all";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String BRAND_NAME = "brandName";
    public static final String BRAND_DETAIL = "detail";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    public static final String PAY_TIME = "payTime";
    public static final String THUMBNAIL = "thumbnail";
    // 参数类型
    public static final String QUERY = "query";
    public static final String HEADER = "header";
    public static final String PATH = "path";
    public static final String BODY = "body";
    public static final String FORM = "form";
    public static final String TO = "to";
    public static final String SIZE = "size";
    public static final String NUM = "num";

    /**
     * 数字
     */
    public static final Integer ZERO_INT = 0;
    public static final Integer ONE_INT = 1;
    public static final Integer TWO_INT = 2;
    public static final Integer THREE_INT = 3;
    public static final Integer FIFTY_INT = 50;
    public static final Integer ONE_HUNDRED_INT = 100;
    public static final Integer TWO_HUNDRED_INT = 200;
    public static final Long THREE_LONG = 3L;
    public static final Long TEN_LONG = 10L;
    public static final Long TWENTY_LONG = 20L;
    public static final Long THIRTY_LONG = 30L;
    public static final Long SIXTY_LONG = 60L;

    /**
     * 默认短信模板内容
     */
    public static final String DEFAULT_SMS_CONTENT = "Respected customer. The following is your phone verification code: %s (valid for 30 minutes)";

    /**
     * 字符串分隔符
     */
    public static final String UNDERSCORE = "_";
    public static final String COMMA = ",";
    public static final String DOT = ".";

    /**
     * redis缓存
     */
    // 登录缓存前缀
    public static final String LOGIN_EMAIL = "login:";
    // 登录用户的超时时间，一个月
    public static final long LOGIN_TIME_OUT_MONTH = 3600 * 24 * 30;
    // 登录用户的超时时间，一周
    public static final long LOGIN_TIME_OUT_WEEK = 3600 * 24 * 7;
    // 登录用户的超时时间，一天
    public static final long LOGIN_TIME_OUT_DAY = 3600 * 24;
    // 登录用户的超时时间，一小时
    public static final long LOGIN_TIME_OUT_HOUR = 3600;
    // 登录用户的超时时间，5分钟
    public static final long LOGIN_TIME_OUT_MINUTE = 300;

    /**
     * 正则表达式
     */
    // 手机号正则
    public static String PHONE_REGEX = "^1[3|4|5|6|7|8|9][0-9]{9}$";
    // 邮箱正则
    public final static String EMAIL_REGEX = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    /**
     * 时间格式
     */
    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

}
