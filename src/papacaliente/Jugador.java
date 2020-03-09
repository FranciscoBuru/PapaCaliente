/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papacaliente;
import papacaliente.papa;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author sdist
 */
public class Jugador {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    
    public static void main(String[] args) throws InterruptedException {
        try {
            papa papas = new papa();
            
            //Session de envio
            String jugador = "J1";
            String otroJugador = "J2";
            
            
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            connectionFactory.setTrustAllPackages(true);
            Connection connection = connectionFactory.createConnection();
            
            connection.start();
            
            Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(otroJugador);

            MessageProducer messageProducer = session.createProducer(destination);
            ObjectMessage ObjMessage = session.createObjectMessage();
            
            //Session de recepción
            
            Session sessionr = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE);

            Destination destinationr = sessionr.createTopic(jugador);

            MessageConsumer messageConsumer = sessionr.createConsumer(destinationr);
            
            //Mete PAPA a la cola del otro jugador.
            ObjMessage.setObject(papas);
            messageProducer.send(ObjMessage);
            
            //Revisa si su cola tiene papas metidas.
            boolean aux;
            aux = true;
           
            
            ObjectMessage ObjMessager;
            while(aux){
                
                ObjMessager = (ObjectMessage)messageConsumer.receive();
                papas = (papa)ObjMessager.getObject();
                if(ObjMessager != null){
                   
                        if(papas.tieneVida()){
                            System.out.println("No perdió");
                            ObjMessage.setObject(papas);
                            messageProducer.send(ObjMessage);
                        }else{

                           System.out.println("Perdí :( Soy: " + jugador);
                           aux = false;
                           
                           
                       }
                    
                }
                
                Thread.sleep(1000);
            }
            messageConsumer.close();
            messageProducer.close();
            session.close();
            sessionr.close();
            connection.close();
            
        } catch (JMSException ex) {
            Logger.getLogger(Jugador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

