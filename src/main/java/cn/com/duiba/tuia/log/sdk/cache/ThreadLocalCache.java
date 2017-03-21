package cn.com.duiba.tuia.log.sdk.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: <a href="http://www.panaihua.com">panaihua</a>
 * @date: 2017年03月20日 17:27
 * @descript:
 * @version: 1.0
 */
public class ThreadLocalCache {

    private static final ThreadLocal<Map<Object, Object>> store = new ThreadLocal() {
        @Override
        protected Map<Object, Object> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };

    public static void put(Object key, Object value) {
        store.get().put(key, value);
    }

    public static Object get(Object key) {
        return store.get().get(key);
    }

    public static boolean isKeyExist(Object key) {
        return store.get().containsKey(key);
    }

    public static void clear() {
        store.get().clear();
    }


}
