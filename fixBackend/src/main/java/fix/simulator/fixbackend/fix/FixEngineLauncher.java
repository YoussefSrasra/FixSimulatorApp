package fix.simulator.fixbackend.fix;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import quickfix.ThreadedSocketAcceptor;
import quickfix.ThreadedSocketInitiator;

@Component
public class FixEngineLauncher implements CommandLineRunner {
    private final ThreadedSocketAcceptor acceptor;
    private final ThreadedSocketInitiator initiator;
    private static final Logger log = LoggerFactory.getLogger(FixAcceptorApp.class);


    public FixEngineLauncher (ThreadedSocketAcceptor acceptor,ThreadedSocketInitiator initiator ){
        this.acceptor= acceptor;
        this.initiator=initiator;
    }

    @Override
    public void run(String... args) throws Exception {
        acceptor.start();
        initiator.start();
        log.info("Moteurs FIX (Initiator & Acceptor) démarrés.");
    }
}
