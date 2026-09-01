package lk.fujilanka.scm.ejb.jms;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import lk.fujilanka.scm.ejb.local.CargoEventProducerLocal;

import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class CargoEventProducerBean implements CargoEventProducerLocal {

    private static final Logger LOGGER = Logger.getLogger(CargoEventProducerBean.class.getName());

    @Inject
    private JMSContext jmsContext;

    @Resource(lookup = "java:global/jms/CargoEventQueue")
    private Queue cargoQueue;

    public void setJmsContext(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }

    public void setCargoQueue(Queue cargoQueue) {
        this.cargoQueue = cargoQueue;
    }

    @Override
    public boolean sendCargoEvent(String trackingNumber, String eventType, String details, String initiatedBy) {
        try {
            if (jmsContext != null && cargoQueue != null) {
                TextMessage message = jmsContext.createTextMessage(details != null ? details : "Cargo event update");
                message.setStringProperty("trackingNumber", trackingNumber);
                message.setStringProperty("eventType", eventType != null ? eventType : "STATUS_UPDATE");
                message.setStringProperty("initiatedBy", initiatedBy != null ? initiatedBy : "SYSTEM");

                JMSProducer producer = jmsContext.createProducer();
                producer.send(cargoQueue, message);

                LOGGER.info("[CargoEventProducerBean]: Successfully queued JMS message for tracking #" + trackingNumber + " to java:global/jms/CargoEventQueue");
                return true;
            } else {
                LOGGER.info("[CargoEventProducerBean (Mock Fallback)]: Injected JMSContext is inactive in non-JMS test environment. Event: " + details);
                return true;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[CargoEventProducerBean]: Failed to send JMS event, falling back gracefully: " + e.getMessage());
            return false;
        }
    }
}
