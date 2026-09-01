package lk.fujilanka.scm.ejb.jms;

import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;
import lk.fujilanka.scm.core.entity.AuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CargoEventConsumerMDBTest {

    @Mock
    private EntityManager em;

    @Mock
    private TextMessage textMessage;

    @Mock
    private JMSContext jmsContext;

    @Mock
    private JMSProducer jmsProducer;

    @Mock
    private Queue queue;

    @InjectMocks
    private CargoEventConsumerMDB consumerMDB;

    @InjectMocks
    private CargoEventProducerBean producerBean;

    @BeforeEach
    void setUp() {
        consumerMDB.setEntityManager(em);
        producerBean.setJmsContext(jmsContext);
        producerBean.setCargoQueue(queue);
    }

    @Test
    @DisplayName("Should successfully consume asynchronous JMS TextMessage and persist audit log")
    void testConsumeTextMessage() throws Exception {
        when(textMessage.getText()).thenReturn("Vessel departed Colombo Port for Nagoya");
        when(textMessage.getStringProperty("trackingNumber")).thenReturn("SCM-TRK-1001");
        when(textMessage.getStringProperty("eventType")).thenReturn("DEPARTURE");
        when(textMessage.getStringProperty("initiatedBy")).thenReturn("coordinator");

        consumerMDB.onMessage(textMessage);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(em, times(1)).persist(captor.capture());

        AuditLog persisted = captor.getValue();
        assertEquals("JMS_ASYNC_DEPARTURE", persisted.getAction());
        assertTrue(persisted.getDetails().contains("SCM-TRK-1001"));
        assertTrue(persisted.getDetails().contains("Vessel departed Colombo Port for Nagoya"));
    }

    @Test
    @DisplayName("Should produce JMS TextMessage to CargoEventQueue")
    void testProduceTextMessage() throws Exception {
        when(jmsContext.createTextMessage(anyString())).thenReturn(textMessage);
        when(jmsContext.createProducer()).thenReturn(jmsProducer);

        boolean result = producerBean.sendCargoEvent("SCM-TRK-1001", "DEPARTURE", "Vessel departed", "coordinator");

        assertTrue(result);
        verify(jmsProducer, times(1)).send(eq(queue), eq(textMessage));
    }
}
