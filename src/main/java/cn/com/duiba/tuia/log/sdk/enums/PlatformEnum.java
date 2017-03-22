package cn.com.duiba.tuia.log.sdk.enums;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月20日 16:53
 * @descript:
 * @version: 1.0
 */
public enum PlatformEnum {

    manager("广告管理后台"),

    adver("广告代理商后台"),

    tuiaCore("广告核心木块");

    private String name;

    PlatformEnum(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
