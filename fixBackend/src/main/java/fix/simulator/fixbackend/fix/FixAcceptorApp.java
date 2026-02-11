package fix.simulator.fixbackend.fix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

@Component
public class FixAcceptorApp extends MessageCracker implements Application {

    private static final Logger log = LoggerFactory.getLogger(FixAcceptorApp.class);

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Broker : Message reçu du client : {}", message);
        crack(message, sessionId);
    }

    @Handler
    public void onMessage(NewOrderSingle order, SessionID sessionId) throws FieldNotFound, SessionNotFound {
        log.info("Broker : Nouvel ordre reçu pour {}. Simulation de l'exécution...", order.getSymbol().getValue());

        ExecutionReport accept = new ExecutionReport(
                new OrderID("BKR-" + System.currentTimeMillis()),
                new ExecID("EXEC-" + System.currentTimeMillis()),
                new ExecType(ExecType.FILL),
                new OrdStatus(OrdStatus.FILLED),
                order.getSide(),
                new LeavesQty(0),
                new CumQty(order.getOrderQty().getValue()),
                new AvgPx(order.getPrice().getValue())
        );

        accept.set(order.getClOrdID());
        accept.set(order.getSymbol());
        accept.set(order.getOrderQty());

        Session.sendToTarget(accept, sessionId);
        log.info("Broker : Execution Report (FILLED) envoyé au client.");
    }

    @Override public void onCreate(SessionID sessionId) {}
    @Override public void onLogon(SessionID sessionId) { log.info("Broker : Client connecté : {}", sessionId); }
    @Override public void onLogout(SessionID sessionId) { log.info("Broker : Client déconnecté : {}", sessionId); }
    @Override public void toAdmin(Message message, SessionID sessionId) {}
    @Override public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {}
    @Override public void toApp(Message message, SessionID sessionId) throws DoNotSend {}
}