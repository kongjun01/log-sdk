package cn.com.duiba.tuia.log.sdk.autoconfigure;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import cn.com.duiba.tuia.log.sdk.condition.LogbackCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年04月25日 18:56
 * @descript:
 * @version: 1.0
 */
@Configuration
public class LogbackAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LogbackAutoConfiguration.class);

    @Bean
    @Conditional(LogbackCondition.class)
    public ApplicationListener logbackConfuration(){

        return new ApplicationListener<ContextRefreshedEvent>(){

            private boolean flag = false;

            @Override
            public void onApplicationEvent(ContextRefreshedEvent event) {

                if(flag)
                    return;

                try {
                    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                    JoranConfigurator configurator = new JoranConfigurator();
                    configurator.setContext(loggerContext);
                    //logback 添加新的配置文件
                    configurator.doConfigure(new ClassPathResource("/logsdk/log-sdk.xml").getInputStream());
                    //Appender logsdkAppender = loggerContext.getLogger("cn.com.duiba.tuia.log.sdk.SdkInfoLog").getAppender("ASYNC_APPENDER");
                    //loggerContext.getLogger("cn.com.duiba.tuia.log.sdk.SdkInfoLog").addAppender(logsdkAppender);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                flag = true;
            }
        };
    }
}
