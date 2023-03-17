package top.popko.agentdemo.context;

import java.util.ArrayList;
import java.util.Iterator;

public class TraceChain {
    private static TraceChain instance;

    public static TraceChain getInstance() {
        if (instance == null) {
			instance = new TraceChain();
        }
        return instance;
    }

    public ArrayList<String> traceElements = new ArrayList<>();

    public void addElement(String name) {
        traceElements.add(name);
    }

    public void show() {
        Iterator<String> iterator = traceElements.iterator();
        System.out.println("---TraceChain show start---");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("---TraceChain show end---");
    }

    public static void main(String[] args) {
        top.popko.agentdemo.context.TraceChain traceChain = top.popko.agentdemo.context.TraceChain.getInstance();
        traceChain.addElement(Thread.currentThread().getStackTrace()[1].getMethodName());//方法内当前方法名
//        TraceChain traceChain = TraceChain.getInstance();
        traceChain.addElement("qwe");
        traceChain.addElement("qwe123");
        TraceChain traceChain1 = TraceChain.getInstance();
        traceChain1.addElement("qwe123");
        traceChain.show();
    }
    //	private TraceElement[] traceElements;
//
//	public TraceElement[] getTraceElements() {
//		return traceElements;
//	}
//
//	public void setTraceElements(TraceElement[] traceElements) {
//		this.traceElements = traceElements;
//	}
//
//	public class TraceElement{
//		private String methodname;
//		TraceElement(){}
//	}
}
