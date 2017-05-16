package cn.com.duiba.tuia.log.sdk.tool;

import cn.com.duiba.tuia.log.sdk.constant.CookieKey;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月22日 12:28
 * @descript:
 * @version: 1.0
 */
public class CookieUtils {

    private final static Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 从Cookie中获取用户ID.<br>
     *
     * @return the cid
     */
    public static Long getAccountId(HttpServletRequest request,String managerAccountEncryptKey) {

        // 获取账号信息
        Long accoutId = -1L;
        String accound = getCookie(request, CookieKey.ACCOUNT_KEY);
        if (StringUtils.isBlank(accound)) {
            return accoutId;
        }

        String content = SecureTool.decryptConsumerCookie(accound,managerAccountEncryptKey);
        if (StringUtils.isBlank(content)) {
            return accoutId;
        }

        JSONObject json = JSON.parseObject(content);
        try {
            accoutId = json.getLong(CookieKey.ACCOUNT_ID_KEY);
        } catch (Exception e) {
            logger.error("获取账户id异常", e);
        }

        return accoutId;
    }

    /**
     * 根据key获取cookie中对应的值.<br>
     * 如果cookie为空或者不存在改key则返回null<br>
     *
     * @param request
     * @param key     键
     * @return cookie中对应的值
     */
    public static String getCookie(HttpServletRequest request, String key) {
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
