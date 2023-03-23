package top.popko.agentdemo.enhance.plugin;

import org.objectweb.asm.ClassVisitor;
import top.popko.agentdemo.enhance.ClassContext;
import top.popko.agentdemo.enhance.plugin.core.DispatchClassPlugin;
import top.popko.agentdemo.enhance.plugin.spring.DispatchSpringApplication;
import top.popko.agentdemo.handler.hookpoint.models.policy.Policy;

import java.util.ArrayList;
import java.util.List;

public class PluginManager {
    private List<DispatchPlugin> plugins = new ArrayList();
    private static PluginManager instance;

    public static PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
            instance.registerPlugins();
        }
        return instance;
    }

    private void registerPlugins() {
        this.plugins.add(new DispatchSpringApplication());
        this.plugins.add(new DispatchClassPlugin());
    }

    public List<DispatchPlugin> getPlugins() {
        return plugins;
    }

    public ClassVisitor matchNewClassVisitor(ClassVisitor classVisitor, ClassContext context, Policy policy) {
        for (DispatchPlugin plugin : plugins) {
            classVisitor = plugin.dispatch(classVisitor, context, policy);
        }
        return classVisitor;
    }
}