package reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by antong on 16/1/18.
 */
public class hook_by_corpId {
    public static void hookByCorpId(String thing,String corpId,Map<String,Object> map) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Object o = Class.forName("reflect.corp_" + corpId).newInstance();
        CorpMethod corpMethod = (CorpMethod) o;
        corpMethod.process();
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        hookByCorpId("test","1001",new HashMap<String, Object>());
    }
}
