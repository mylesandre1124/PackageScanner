package xyz.the1124.packagescanner.Socket;


import java.io.*;
import java.net.Socket;
import java.util.TreeMap;

/**
 * Created by Myles on 8/3/17.
 */
public class Client {

    private Socket socket;
    private String ip;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(){}

    public Client(String ipAddress, int port)
    {
        this.ip = ipAddress;
        this.port = port;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() throws IOException, ClassNotFoundException {
        socket = new Socket(ip, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return in.readObject();
    }

    public void send(Object data) throws IOException {
        out.writeObject(data);
        out.flush();
    }

    public void sendAcknowledgement(int code) throws IOException {
        send(code);
    }

    public int receiveAcknowledgement() throws IOException {
        return in.readInt();
    }

    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client();
        client.setIp("192.168.1.211");
        client.setPort(1025);
        client.connect();
        client.close();
    }

}
