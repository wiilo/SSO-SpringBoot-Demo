package com.wiilo.common.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 放入到threadLocal的属性
 *
 * @author Whitlock Wang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServletInfo {

    private String token;

    private String language;

    private String platform;

    private String originate;

    public ServletInfo(String token) {
        this.token = token;
    }

    public ServletInfo(String language, String platform, String originate) {
        this.language = language;
        this.platform = platform;
        this.originate = originate;
    }
}
