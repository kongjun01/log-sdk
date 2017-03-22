package cn.com.duiba.tuia.log.sdk.tool;

import cn.com.duiba.tuia.log.sdk.constant.CookieKey;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月22日 12:28
 * @descript:
 * @version: 1.0
 */
public class CookieUtils {

    /**
     * The time out set.
     */
    private static final Long COOKIE_TIMEOUT = 24 * 60 * 60 * 1000L;


    /**
     * 从Cookie中获取用户ID.<br>
     *
     * @return the cid
     */
    public static Long getAccountId(HttpServletRequest request) {

        // 获取账号信息
        String accound = getCookie(request,CookieKey.ACCOUNT_KEY);
        if (StringUtils.isBlank(accound)) {
            return null;
        }

        String content = SecureTool.decryptConsumerCookie(accound);
        if (StringUtils.isBlank(content)) {
            return null;
        }

        JSONObject json = JSON.parseObject(content);
        if (new Date().getTime() - json.getLong(CookieKey.LOGIN_TIME_KEY) < COOKIE_TIMEOUT) {
            // 24小时过期
            return json.getLong(CookieKey.ACCOUNT_ID_KEY);
        }

        return null;
    }

    /**
     * 根据key获取cookie中对应的值.<br>
     * 如果cookie为空或者不存在改key则返回null<br>
     *
     * @param request
     * @param key 键
     * @return cookie中对应的值
     */
    public static String getCookie(HttpServletRequest request,String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (key.equals(cookie.getName())) {
                    String value = cookie.getValue();
                    if (StringUtils.isNotBlank(value)) {
                        return value;
                    }
                }
            }
        }

        return null;
    }
}
