package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTCP {

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        Socket srv=new Socket(InetAddress.getLocalHost(),6666);
        BufferedReader in=new BufferedReader(new InputStreamReader(srv.getInputStream()));
        System.out.println(in.readLine());
    }

}