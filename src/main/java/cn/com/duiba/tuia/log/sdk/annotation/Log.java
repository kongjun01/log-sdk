package cn.com.duiba.tuia.log.sdk.annotation;

import cn.com.duiba.tuia.log.sdk.enums.PlatformEnum;

import java.lang.annotation.*;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月20日 16:49
 * @descript:
 * @version: 1.0
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 系统名称
     * @return
     */
    PlatformEnum platform();

    /**
     * 模块名称
     * @return
     */
    String moduleName();

    /**
     * 子模块名称
     * @return
     */
    String subModuleName();

    /**
     * 操作描述
     * @return
     */
    String optionName();

}
