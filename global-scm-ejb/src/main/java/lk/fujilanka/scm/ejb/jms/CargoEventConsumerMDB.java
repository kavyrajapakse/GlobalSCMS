package lk.fujilanka.scm.ejb.jms;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.core.entity.AuditLog;

import java.util.logging.Level;
import java.util.logging.Logger;

@JMSDestinationDefinition(
        name = "java:global/jms/CargoEventQueue",
        interfaceName = "jakarta.jms.Queue",
        destinationName = "CargoEventQueue",
        description = "Asynchronous GlobalSCM Cargo Logistics and Telemetry Event Queue"
)
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/CargoEventQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class CargoEventConsumerMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(CargoEventConsumerMDB.class.getName());

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String payload = textMessage.getText();
                String trackingNumber = message.getStringProperty("trackingNumber");
                String eventType = message.getStringProperty("eventType");
                String initiatedBy = message.getStringProperty("initiatedBy");

                LOGGER.info(String.format("[CargoEventConsumerMDB]: Asynchronously consumed JMS cargo event | Type: %s | Tracking: %s | User: %s | Payload: %s",
                        eventType != null ? eventType : "EVENT",
                        trackingNumber != null ? trackingNumber : "N/A",
                        initiatedBy != null ? initiatedBy : "SYSTEM",
                        payload));

                // Process background persistence / audit recording
                if (em != null) {
                    String userStr = (initiatedBy != null) ? initiatedBy : "SYSTEM";
                    AuditLog audit = new AuditLog("JMS_ASYNC_" + (eventType != null ? eventType : "EVENT"), 
                            userStr, 
                            "Async JMS Event for #" + trackingNumber + ": " + payload);
                    em.persist(audit);
                }
            } else {
                LOGGER.warning("[CargoEventConsumerMDB]: Received unsupported message type: " + message.getClass().getName());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "[CargoEventConsumerMDB]: Error processing asynchronous JMS cargo event", e);
        }
    }
}
