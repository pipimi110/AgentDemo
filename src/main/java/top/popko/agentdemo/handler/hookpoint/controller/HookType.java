package top.popko.agentdemo.handler.hookpoint.controller;

public enum HookType {
    RPC("rpc", 15),
    HTTP("http", 10),
    SOURCE("source", 11),
    PROPAGATOR("propagator", 12),
    SINK("sink", 13),
    SPRINGAPPLICATION("springApplication", 14);

    int value;
    String name;

    private HookType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean equals(int target) {
        return this.value == target;
    }
}
