package top.popko.agentdemo.handler.hookpoint.models.policy;

import top.popko.agentdemo.enhance.MethodContext;

public interface MethodMatcher {
  boolean match(MethodContext paramMethodContext);
}
