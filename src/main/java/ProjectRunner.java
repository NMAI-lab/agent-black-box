import jason.JasonException;
import jason.infra.centralised.RunCentralisedMAS;
import javassist.util.proxy.ProxyFactory;

public class ProjectRunner {
    public static void main(String[] args) throws JasonException {
        RunCentralisedMAS.main(new String[] {"black-box-agent.mas2j"});

    }
}
