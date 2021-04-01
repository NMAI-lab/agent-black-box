import jason.asSyntax.*;
import jason.environment.Environment;
import java.util.*;

public class BlackBoxEnvironment extends Environment {

    public BlackBoxEnvironment() {
        super();
    }

    @Override
    public List<Literal> getPercepts(String agName) {
        Collection<Literal> ps = super.getPercepts(agName);
        List<Literal> percepts = ps == null ? new ArrayList<>() : new ArrayList<>(ps);

        return percepts;

    }

    @Override
    public boolean executeAction(String agName, Structure act) {
        return super.executeAction(agName, act);
    }

    /**
     * Called before the end of MAS execution
     */
    @Override
    public void stop() {
        super.stop();
    }

}
