package redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by antong on 16/7/19.
 */
public class RedisFactory {
    private static final JedisCluster jedisCluster;

    static {
        HostAndPort hp = new HostAndPort("localhost", 7000);
        jedisCluster = new JedisCluster(new HashSet<HostAndPort>(Arrays.asList(new HostAndPort[]{hp})));
    }

    public interface Operation {

    }

    static class StringOperation implements Operation {
        private JedisCluster jedis;

        public StringOperation(JedisCluster jedisCluster) {
            this.jedis = jedisCluster;
        }

        public String set(String string1, String string2) {
            return jedis.set(string1, string2);
        }

        public boolean setNx(String string1, String string2) {
            return jedis.setnx(string1, string2) == 1 ? true : false;
        }

        public String getSet(String s1, String s2) {
            return jedis.getSet(s1, s2);
        }

        public String get(String s1) {
            return jedis.get(s1);
        }

        public boolean del(String s1) {
            return jedis.del(s1) == 1 ? true : false;
        }
    }

    public static StringOperation getStringOperation() {
        return new StringOperation(jedisCluster);
    }
}
