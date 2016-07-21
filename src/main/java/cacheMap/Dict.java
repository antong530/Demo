package cacheMap;

/**
 * Created by antong on 16/7/12.
 */
public class Dict {
    private final CacheMap cacheMap;
    private static Dict dict;

    private static Dict getInstance() {
        if (null == dict) {
            synchronized (Dict.class) {
                if (null == dict) {
                    dict = new Dict();
                }
            }
        }
        return dict;
    }

    private Dict() {
        this.cacheMap = new CacheMap();
    }

    public void put(String s1, String s2) {
        this.cacheMap.put(s1, s2);
    }

    public String get(String s1) {
        return this.cacheMap.get(s1);
    }

    public static void main(String[] args) {
        Dict.getInstance().put("2", "3");
        Dict.getInstance().put("3", "2");
        Dict.getInstance().put("4", "5");
        Dict.getInstance().put("5", "6");
        System.out.println("" + Dict.getInstance().get("2"));
        System.out.println("" + Dict.getInstance().get("2"));
    }
}
