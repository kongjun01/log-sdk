package cn.com.duiba.tuia.log.sdk.autoconfigure;

import cn.com.duiba.tuia.log.sdk.aspect.LogAspect;
import cn.com.duiba.tuia.log.sdk.condition.LogSdkCondition;
import cn.com.duiba.tuia.log.sdk.mybatis.plugin.LogMybatisPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年04月25日 16:20
 * @descript:
 * @version: 1.0
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Conditional(LogSdkCondition.class)
public class LogAutoConfiguration {

    @Configuration
    public static class LogAspectConfiguration {

        @Bean
        public LogAspect getLogAspect(){
            return new LogAspect();
        }
    }

    @Configuration
    public static class MybatisPluginConfiguration {

        @Bean
        public BeanPostProcessor myBatisPostProcessorConfigurer(){

            return new BeanPostProcessor() {

                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                    return bean;
                }

                @Override
                public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

                    SqlSessionFactory s = null;
                    if(bean instanceof SqlSessionFactory){
                        s = (SqlSessionFactory)bean;
                    }
                    if(bean instanceof SqlSessionTemplate){
                        s = ((SqlSessionTemplate)bean).getSqlSessionFactory();
                    }
                    if(s == null){
                        return bean;
                    }

                    boolean hasPlugin = false;
                    if(s.getConfiguration().getInterceptors() != null && !s.getConfiguration().getInterceptors().isEmpty()){

                        for (Interceptor plugin : s.getConfiguration().getInterceptors()){

                            if (plugin instanceof LogMybatisPlugin) {
                                hasPlugin = true;
                                break;
                            }
                        }
                    }

                    if(!hasPlugin) {
                        s.getConfiguration().addInterceptor(new LogMybatisPlugin());
                    }

                    return bean;
                }
            };
        }

    }
}
