package cn.com.duiba.tuia.log.sdk;

import cn.com.duiba.tuia.log.sdk.dto.LogDTO;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月22日 10:41
 * @descript:
 * @version: 1.0
 */
public class SdkInfoLog {

    private  static Logger log = LoggerFactory.getLogger(SdkInfoLog.class);

    public static void log(LogDTO logDTO) {
        log.info(JSONObject.toJSONString(logDTO));
    }
}
