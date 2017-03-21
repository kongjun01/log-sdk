## 接入步骤
##### 1. 加入依赖
```groovy
dependency('cn.com.duiba.tuia:log-sdk:1.0-SNAPSHOT')
```
##### 2. 在spring容器中声明bean
```xml
<bean class="cn.com.duiba.tuia.log.sdk.aspect.LogAspect"></bean>
```
##### 3. 加入mybaits的插件,有两种方式
- 在sqlSessionFactory加入LogMybatisPlugin

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"></property>
    <property name="configLocation" value="classpath:mybatis/sqlMapConfig.xml"></property>
    <property name="mapperLocations" value="classpath:mybatis/mapper/*/*.xml"></property>
    <property name="typeAliasesPackage" value="cn.com.duiba.tuia.manager.dao.*"></property>
    <property name="plugins">
        <array>
            <bean class="cn.com.duiba.tuia.log.sdk.mybatis.plugin.LogMybatisPlugin" />
        </array>
    </property>
</bean>
```

- 如果有多个sqlSessionFactory，可以在sqlMapConfig.xml声明，这样所有sqlSessionFactory都会加载LogMybatisPlugin

```xml
<configuration>
	<settings></settings>
	<typeAliases></typeAliases>
	<plugins>
		<plugin interceptor="cn.com.duiba.tuia.log.sdk.mybatis.plugin.LogMybatisPlugin"></plugin>
	</plugins>
</configuration>
```
## @Log注解 使用方法
TestController有个方法doMethod：

```java
@Log(platform = PlatformEnum.manager,moduleName = "广告管理",subModuleName="限流媒体",optionName="删除限流媒体")
public void doMethod(Request req, HttpServletResponse response) {
   testService.doOther();
}
```

TestService里面有个doOther： 
 
```java
@Log(platform = PlatformEnum.manager,moduleName = "广告管理2",subModuleName="限流媒体2",optionName="删除限流媒体服务")
public void doMethod() {
    ....
}
```

*注解可以嵌套调用，除了optionName属性会在嵌套中叠加，其它默认取的是最外面的配置。比如上面的配置最后日志打印的结果是：*  

```json
{
   "platform":"广告管理后台",
   "moduleame":"广告管理",
   "subModuleName":"限流媒体",
   "optionName":"删除限流媒体:删除限流媒体服务",
   ....
}
```
  
## @Log注解参数说明  
1. platform ：系统名称 详细见PlatformEnum枚举
2. moduleName ： 模块名称
3. subModuleName : 子木块名称
4. optionName ： 操作

## 其它说明
- 打印日志如果出现异常不会影响业务罗杰的输出
- 如果不是web项目不会记录url和IP的数据
- 如果是web项目记录请求参数时，打印会过滤掉ServletResponse，是为了防止影响业务正常输出


## 注意事项

- 如果在DAO方法上加@Log注解，只会修改optionName的内容,所以最好不要加
- 目前LogMybatisPlugin只会记录update和delte操作的数据
