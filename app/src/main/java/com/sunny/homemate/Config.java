package com.sunny.homemate;

/**
 * Created by sunny.sun on 2017/1/3.
 */

public final class Config {
    private Config() {
    }

    public static final String GC_YOUTUBE_API_KEY = "AIzaSyBX-vty3BlKqVl2pHI4fnsBJfNVkkY2nP0"; //YouTube API的金鑰

    public static final String GC_MQTT_BROKER_URL = "tcp://test.mosquitto.org:1883";
    public static final String GC_MQTT_M2MIO_DOMAIN = "com/sunny";
    public static final String GC_MQTT_M2MIO_STUFF = "homemate";
}
