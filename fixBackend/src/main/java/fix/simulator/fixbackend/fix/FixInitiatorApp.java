package fix.simulator.fixbackend.fix;

import fix.simulator.fixbackend.service.ExecutionReportService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import fix.simulator.fixbackend.model.Order;
import fix.simulator.fixbackend.repository.OrderRepository;

@Component
public class FixInitiatorApp extends MessageCracker implements Application {
    private static final Logger log = LoggerFactory.getLogger(FixInitiatorApp.class);
    @Autowired
    private ExecutionReportService executionReportService;

    @Autowired
    private OrderRepository orderRepository;

    @Override public void onLogon(SessionID sessionId){
        log.info("Logon réussi pour la session : {}", sessionId);
    }
    @Override public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Message FIX reçu de l'application : {}", message);
        crack(message, sessionId);
    }
    @Override public void onCreate(SessionID sessionId) {
        log.info("Session créée : {}", sessionId);
    }
    @Override public void onLogout(SessionID sessionId) {
        log.info("Logout pour la session : {}", sessionId);
    }
    @Override public void toAdmin(Message message, SessionID sessionId) {}
    @Override public void fromAdmin(Message message, SessionID sessionId)throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {}
    @Override public void toApp(Message message, SessionID sessionId) throws DoNotSend{}
    @Handler
    public void onMessage(ExecutionReport message, SessionID sessionId) throws FieldNotFound {
        String clOrdID = message.getClOrdID().getValue();
        String brokerOrderID = message.getOrderID().getValue(); // Tag 37
        String execID = message.getExecID().getValue();         // Tag 17
        String ordStatus = String.valueOf(message.getOrdStatus().getValue());

        Double cumQty = message.isSetCumQty() ? message.getCumQty().getValue() : 0.0;
        Double avgPx = message.isSetAvgPx() ? message.getAvgPx().getValue() : 0.0;
        Double lastPx = message.isSetLastPx() ? message.getLastPx().getValue() : 0.0;
        Double lastQty = message.isSetLastQty() ? message.getLastQty().getValue() : 0.0;

        executionReportService.processExecutionReport(clOrdID, brokerOrderID, execID, ordStatus, cumQty, avgPx, lastPx, lastQty);
    }

    public void sendOrder (Order orderEntity){
        NewOrderSingle nos = new NewOrderSingle(
                new ClOrdID(orderEntity.getClOrdID()),
                new Side(orderEntity.getSide()== Order.Side.BUY ? Side.BUY : Side.SELL),
                new TransactTime(),
                new OrdType(OrdType.LIMIT)
        );

        nos.set(new Symbol(orderEntity.getSymbol()));
        nos.set(new OrderQty(orderEntity.getQuantity()));
        nos.set(new Price(orderEntity.getPrice()));
        nos.set(new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE_NO_BROKER_INTERVENTION));

        try {
            SessionID sessionId = new SessionID ("FIX.4.4", "CLIENT_WEB_1", "BROKER_EXCHANGE_1");
            boolean sent = Session.sendToTarget(nos, sessionId);

            if (sent){
                log.info("Ordre FIX envoyé avec succès pour l'ID " + orderEntity.getClOrdID());
            } else{
                log.error("Echec de l'envoi de l'ordre FIX.");
            }
        }catch (SessionNotFound e){
            e.printStackTrace();
        }
    }
}
