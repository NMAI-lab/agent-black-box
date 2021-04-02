import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSemantics.Circumstance;
import jason.asSemantics.TransitionSystem;
import jason.infra.centralised.CentralisedAgArch;
import jason.runtime.Settings;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BBAgArch extends CentralisedAgArch {
    @Override
    public void init() throws Exception {
        super.init();

        super.setTS(createTSProxy());

    }

    private TransitionSystem createTSProxy() {
        if (this.getTS() == null) {
            logger.warning("Current TS is null. Could not create proxy.");
            return null;
        }

        if(this.getTS() instanceof Proxy)
            return this.getTS();

        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(TransitionSystem.class);

        try {
            // Check for empty constructors in case TS implementation changes in future
            Object tsInst = f.createClass()
                    .getConstructors()[0]
                    .newInstance(getTS().getAg(), createCProxy(createCProxy(getTS().getC())), getTS().getSettings(), getTS().getAgArch());

            // Create method handler
            ((Proxy) tsInst).setHandler(new TransitionSystemMethodHandler(this));

            return (TransitionSystem) tsInst;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create proxy", e);
        }
    }

    private Circumstance createCProxy(Circumstance circumstance) {
        if (circumstance == null) {
            logger.warning("Current C is null. Could not create proxy.");
            return null;
        }

        if(circumstance instanceof Proxy)
            return circumstance;

        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(Circumstance.class);

        try {
            // Check for empty constructors in case TS implementation changes in future
            Object cInst = f.createClass()
                    .getConstructors()[0]
                    .newInstance();

            // Create method handler
            ((Proxy) cInst).setHandler(new CircumstanceMethodHandler(this));

            return (Circumstance) cInst;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create proxy", e);
        }
    }
}
