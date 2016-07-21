package cacheMap;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by antong on 16/7/12.
 */
public class CacheMap {
    private ConcurrentHashMap cache;
    public CacheMap(){
        cache = new ConcurrentHashMap();
    }

    public void put(String s1, String s2){
        cache.put(s1,s2);
    }

    public String get(String s1){
        return (String) cache.get(s1);
    }
}
