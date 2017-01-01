package com.sunny.homemate;

/**
 * Created by sunny on 2017/1/2.
 */

import android.app.Activity;
import android.content.Intent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MateMqttClient implements MqttCallback {

    private MqttClient myClient;
    private MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    static final String M2MIO_DOMAIN = "com/sunny";
    static final String M2MIO_STUFF = "homemate";
    static final String M2MIO_THING = "";
    //static final String M2MIO_USERNAME = "<m2m.io username>";
    //static final String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";
    static final String M2MIO_USERNAME = "";
    static final String M2MIO_PASSWORD_MD5 = "";

    // the following two flags control whether this example is a publisher, a subscriber or both
    static final Boolean subscriber = true;
    static final Boolean publisher = true;

    private Activity myParentActivity;

    /**
     *
     * connectionLost
     * This callback is invoked upon losing the MQTT connection.
     *
     */
    @Override
    public void connectionLost(Throwable t) {
        System.out.println("Connection lost!");
        // code to reconnect to the broker would go here if desired
    }

    /**
     *
     * deliveryComplete
     * This callback is invoked when a message published by this client
     * is successfully received by the broker.
     *
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    public boolean isConnected(){
        if (myClient==null) return false;
        return myClient.isConnected();
    }

    public void doDisconnect(){
        if (myClient==null) return;
        try {
            myClient.disconnect();
            System.out.println("doDisconnect(): disconnect from MQTT server");
        }catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
            return;
        }

    }

    public void setParentActivity(Activity parentActivity){
        myParentActivity = parentActivity;
    }
    public boolean doConnect(String clientId) {
        // setup MQTT Client
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        if (M2MIO_USERNAME.length()>0)       connOpt.setUserName(M2MIO_USERNAME);
        if (M2MIO_PASSWORD_MD5.length()>0)  connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

        MemoryPersistence persistence = new MemoryPersistence();
        // Connect to Broker
        try {
            myClient = new MqttClient(BROKER_URL, clientId, persistence);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     *
     * runClient
     * The main functionality of this simple example.
     * Create a MQTT client, connect to broker, pub/sub, disconnect.
     *
     */
    public void runClient() {
        // setup MQTT Client
        String clientID = M2MIO_THING;
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        connOpt.setUserName(M2MIO_USERNAME);
        connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

        // Connect to Broker
        try {
            myClient = new MqttClient(BROKER_URL, clientID);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Connected to " + BROKER_URL);

        // setup topic
        // topics on m2m.io are in the form <domain>/<stuff>/<thing>
        String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
        MqttTopic topic = myClient.getTopic(myTopic);

        // subscribe to topic if subscriber
        if (subscriber) {
            try {
                int subQoS = 0;
                myClient.subscribe(myTopic, subQoS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // publish messages if publisher
        if (publisher) {
            for (int i=1; i<=10; i++) {
                String pubMsg = "{\"pubmsg\":" + i + "}";
                int pubQoS = 0;
                MqttMessage message = new MqttMessage(pubMsg.getBytes());
                message.setQos(pubQoS);
                message.setRetained(false);

                // Publish the message
                System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
                MqttDeliveryToken token = null;
                try {
                    // publish message to broker
                    token = topic.publish(message);
                    // Wait until the message has been delivered to the broker
                    token.waitForCompletion();
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // disconnect
        try {
            // wait to ensure subscribed messages are delivered
            if (subscriber) {
                Thread.sleep(5000);
            }
            myClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * messageArrived
     * This callback is invoked when a message is received on a subscribed topic.
     *
     */
    @Override
    //public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        //System.out.println("| Topic:" + topic.getName());
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
        //啟動播放video的Activity，並將影片資訊帶過去給Activity
        Intent intent = new Intent(myParentActivity, MainActivity.class);
        intent.putExtra("url", message.getPayload());
        myParentActivity.startActivity(intent);
    }

}