package cn.com.duiba.tuia.log.sdk.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年04月25日 16:51
 * @descript:
 * @version: 1.0
 */
public class LogSdkCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return true;
    }
}
