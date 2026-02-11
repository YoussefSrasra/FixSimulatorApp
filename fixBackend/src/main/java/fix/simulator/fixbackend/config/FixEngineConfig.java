package fix.simulator.fixbackend.config;


import fix.simulator.fixbackend.fix.FixAcceptorApp;
import fix.simulator.fixbackend.fix.FixInitiatorApp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.*;

import java.beans.BeanProperty;

@Configuration
public class FixEngineConfig {

    @Value("${fix.initiator.config}")
    private String initiatorConfigFile;

    @Value("${fix.acceptor.config}")
    private String acceptorConfigFile;


    @Bean
    public ThreadedSocketInitiator initiator (FixInitiatorApp application)throws ConfigError, FieldConvertError {
        SessionSettings settings = new SessionSettings(initiatorConfigFile);
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        return new ThreadedSocketInitiator(application, storeFactory,settings, logFactory, messageFactory);
    }

    @Bean
    public ThreadedSocketAcceptor acceptor (FixAcceptorApp application) throws ConfigError, FieldConvertError{
        SessionSettings settings = new SessionSettings(acceptorConfigFile);
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        return new ThreadedSocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
    }
}
