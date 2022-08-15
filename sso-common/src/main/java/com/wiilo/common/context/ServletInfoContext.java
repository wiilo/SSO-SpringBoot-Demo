package com.wiilo.common.context;

/**
 * 服务信息上下文
 *
 * @author Whitlock Wang
 */
public class ServletInfoContext {

    private static final ThreadLocal<ServletInfo> THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<ServletInfo> HEADER_LOCAL = new ThreadLocal<>();

    public static void save(ServletInfo value) {
        THREAD_LOCAL.set(value);
    }

    public static ServletInfo get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static void saveHeaderLocal(ServletInfo servletInfo) {
        HEADER_LOCAL.set(servletInfo);
    }

    public static ServletInfo getHeaderLocal() {
        return HEADER_LOCAL.get();
    }

    public static void removeHeaderLocal() {
        HEADER_LOCAL.remove();
    }

}
