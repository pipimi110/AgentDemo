package top.popko.agentdemo.handler.hookpoint.models.policy;

import top.popko.agentdemo.util.StringUtils;


public enum Inheritable {
    ALL("all"),
    SUBCLASS("true"),
    SELF("false");

    private final String value;


    Inheritable(String value) {
        this.value = value;
    }

    public static Inheritable parse(String str) throws PolicyException {
        if (!StringUtils.isEmpty(str)) {
            Inheritable[] values = values();
            for (Inheritable inheritable : values) {
                if (inheritable.value.equalsIgnoreCase(str)) {
                    return inheritable;
                }
            }
        }
        throw new PolicyException("policy node inheritable value is invalid: " + str);
    }
}
