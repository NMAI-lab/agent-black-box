import jason.architecture.AgArch;
import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * This class is a general proxy method handler that obtains metrics of various called methods in the agent's architecture.
 */
public abstract class AgentMethodHandler implements MethodHandler {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final AgArch agArch;

    protected AgentMethodHandler(AgArch agArch) {
        this.agArch = agArch;
    }

    /**
     * Runs before the proxy method.
     *
     * @return True if the method should be run, false otherwise.
     * @throws Throwable
     */
    protected boolean beforeMethod(Object self, Method thisMethod, Object[] args) {
        return true;
    }

    /**
     * Runs the proxy method: proceed.invoke(self, args). This method is timed.
     *
     * @return The method return value
     */
    protected Object invokeMethod(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        return proceed.invoke(self, args);
    }

    /**
     * Runs after the proxy method
     *
     * @param self
     * @param thisMethod
     * @param args
     */
    protected void afterMethod(Object self, Method thisMethod, Object[] args, Object returnVal) {

    }

    @Override
    public final Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        if (self == null)
            return null;

        // Log method entry (with arguments)
        getLogger().info(thisMethod.getDeclaringClass().getSimpleName() + " Method entry: " + thisMethod + ", " + args.length + " args.");

        if(!beforeMethod(self, thisMethod, args))
            getLogger().info("beforeMethod blocked execution of: " + thisMethod.getName());

        // Time method execution and store result as var
        long start = System.currentTimeMillis();
        Object result = invokeMethod(self, thisMethod, proceed, args);
        long runTime = System.currentTimeMillis() - start;

        // Log method return var
        getLogger().info("method runtime: " + runTime + ", result: " + result);

        afterMethod(self,thisMethod, args, result);

        return result;
    }

    public Logger getLogger() {
        return logger;
    }
}
