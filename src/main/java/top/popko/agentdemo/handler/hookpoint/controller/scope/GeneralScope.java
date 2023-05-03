package top.popko.agentdemo.handler.hookpoint.controller.scope;

public class GeneralScope {
    private int level;

    public GeneralScope() {
    }

    public boolean in() {
        if(System.getProperty("skiphttp")!=null){
            return true;//方便测试,实际使用不能开启
        }
        return this.level != 0;
    }

    public boolean isFirst() {
        return this.level == 1;
    }

    public void enter() {
        ++this.level;
    }

    public void leave() {
        this.level = this.decrement(this.level);
    }

    private int decrement(int level) {
        return level > 0 ? level - 1 : 0;
    }
}
