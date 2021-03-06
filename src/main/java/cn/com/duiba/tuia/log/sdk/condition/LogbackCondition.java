package cn.com.duiba.tuia.log.sdk.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年04月25日 19:04
 * @descript:
 * @version: 1.0
 */
public class LogbackCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Resource resource = context.getResourceLoader().getResource("/logsdk/log-sdk.xml");
        return resource != null && resource.exists();
    }
}
