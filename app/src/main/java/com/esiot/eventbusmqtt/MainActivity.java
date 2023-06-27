package com.esiot.eventbusmqtt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnKonek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean b = MqttManager.getInstance().creatConnect(
                                "tcp://192.168.1.7:1883",
                                "baru",
                                "iotubuntu",
                                "dariandroid");
                    }
                }).start();
            }
        });

        findViewById(R.id.btnPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String smqttsubtopic = "polines/trk/data";
                        String smqttpublishtext = "0";

                        MqttManager.getInstance().publish(smqttsubtopic, 2, smqttpublishtext.getBytes());
                    }
                }).start();
            }
        });

        findViewById(R.id.btnPublish2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String smqttsubtopic = "polines/trk/data";
                        String smqttpublishtext = "1";

                        MqttManager.getInstance().publish(smqttsubtopic, 2, smqttpublishtext.getBytes());
                    }
                }).start();
            }
        });


        findViewById(R.id.btnSubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String smqttsubtopic = "polines/trk/data";
                        MqttManager.getInstance().subscribe(smqttsubtopic, 2);
                    }
                }).start();
            }
        });

        findViewById(R.id.btnDiskonek).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MqttManager.getInstance().disConnect();
                        }catch(MqttException e){

                        }
                    }
                }).start();
            }
        });

        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEvent(MqttMessage message){
        TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtMessage.setText(message.toString()); //set message as text
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}