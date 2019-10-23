
package com.robust.study.netty.lion.common.condition;


import com.robust.study.netty.lion.api.common.Condition;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.Map;

/**
 *
 *
 *
 */
public final class ScriptCondition implements Condition {
    private static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    private static final ScriptEngine jsEngine = scriptEngineManager.getEngineByName("js");

    private final String script;

    public ScriptCondition(String script) {
        this.script = script;
    }

    @Override
    public boolean test(Map<String, Object> env) {
        try {
            return (Boolean) jsEngine.eval(script, new SimpleBindings(env));
        } catch (Exception e) {
            return false;
        }
    }
}
