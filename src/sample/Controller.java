package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.lang.Thread;


public class Controller {


    @FXML
    private TextArea textChat;
    @FXML
    private TextArea textChat2;

    private String destIp = "localhost";
    private int destPort = 4040;
    private String message;
    private String oldmsg;


    public Controller() {

        Receiver r = new Receiver();
        r.start();
    }

    public void Send() {

        message = textChat.getText();
        System.out.println(message);
        try {
            // -- Datagram construction --
            InetAddress receiverHost = InetAddress.getByName(destIp);
            int receiverPort = destPort;
            DatagramSocket mySocket = new DatagramSocket();
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverHost, receiverPort);
            mySocket.send(packet);
            mySocket.close();

            //-- Capturing old messages -- //
            oldmsg = textChat2.getText();

            if (oldmsg == null) {
                textChat2.setText("Fortune:" + message +"\n");
            } else {
                textChat2.setText(oldmsg + "Fortune:" + message + "\n");
                textChat.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class Receiver implements Runnable {

        private Thread t;
        //public String threadName;

        public Receiver() {
           // threadName = name;
        }

        public void run() {
            //the run method calls the receiver method
            receive();
        }

        public void start() {
            t = new Thread(this);
            t.start();
        }


        public void receive() {

            System.out.println("Receive Called");
            try {

                int MAX_LEN = 40;

                int localPortNum = 4052;
                DatagramSocket mySocket = new DatagramSocket(localPortNum);
                byte[] buffer = new byte[MAX_LEN];
                DatagramPacket packet = new DatagramPacket(buffer, MAX_LEN);

                int i = 0;

                do {

                    mySocket.receive(packet);
                    String message2 = new String(buffer);
                    message2 = oldmsg + message2;
                    textChat2.setText("JP:" + message2);
                    //System.out.println(message);
                }
                while (i < 20);
                mySocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
