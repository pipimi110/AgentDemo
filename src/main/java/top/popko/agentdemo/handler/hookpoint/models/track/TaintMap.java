package top.popko.agentdemo.handler.hookpoint.models.track;

import top.popko.agentdemo.handler.hookpoint.models.MethodEvent;

import java.util.Map;

public class TaintMap extends ThreadLocal<Map<Integer, MethodEvent>>  {

    public TaintMap() {
    }

    protected Map<Integer, MethodEvent> initialValue() {
        return null;
    }

    public void addTrackMethod(Integer invokeId, MethodEvent event) {
        ((Map)this.get()).put(invokeId, event);
    }

    @Override
    public String toString() {
        for(Integer key:this.get().keySet()){
            MethodEvent methodEvent = this.get().get(key);
            System.out.println(methodEvent);
        }
        return super.toString();
    }
}