
package com.robust.study.netty.lion.test.gson;

import com.google.common.collect.Maps;
import com.robust.study.netty.lion.api.push.MsgType;
import com.robust.study.netty.lion.api.push.PushMsg;
import com.robust.study.netty.lion.tools.Jsons;
import org.junit.Test;

import java.util.Map;

public class GsonTest {

    @Test
    public void test() {
        Map<String, String> map = Maps.newHashMap();
        map.put("key1", 1121 + "");
        map.put("key2", "value2");

        PushMsg content = PushMsg.build(MsgType.MESSAGE, Jsons.toJson(map));


        System.out.println(Jsons.toJson(content));

    }

    @Test
    public void test2() {

        System.out.println(Jsons.toJson(new ValueMap("xxx")));


    }

    private static class ValueMap {

        private String key1;
        private String key2;
        transient private boolean key3;

        public ValueMap(String key1, String key2) {
            this.key1 = key1;
            this.key2 = key2;
        }

        public ValueMap(String key1) {
            this.key1 = key1;
        }

        public String getKey1() {
            return key1;
        }

        public String getKey2() {
            return key2;
        }


        public boolean isKey3() {
            return key3;
        }

        public ValueMap setKey3(boolean key3) {
            this.key3 = key3;
            return this;
        }
    }

}
