package com.example.android.sketch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientSend extends AsyncTask<String, Void, String> {

    private DatagramSocket clientSocket = null;
    String checkStr = "";

    protected String doInBackground(String... sample) {
        try {
            clientSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatagramPacket sendPacket = null;
        try {
            byte sendData[];
            Log.d("Size -- ", sample[0]);
            sendData = sample[0].getBytes();
            sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("192.168.0.127"), 63185);
            clientSocket.send(sendPacket);
            checkStr = sample[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        clientSocket.close();
        return null;
    }
}

