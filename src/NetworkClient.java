// A simple Client Server Protocol .. Client for Echo Server


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class NetworkClient {

    private static InetAddress neighbourAddress;

    NetworkClient() {
        String response = null;
    }

    public static void main(String args[]) throws IOException {
        boolean running = true;
        while (running) {
            System.out.println("XXXXx1");

            NetworkClient3 client = new NetworkClient3();
            System.out.println("XXXXx2");
            ClientInClient clientInClientObject = new ClientInClient(InetAddress.getLocalHost());
            System.out.println("XXXXx3");
            Thread s = new Thread(clientInClientObject);
            s.start();
            System.out.println("XXXXx4");


            System.out.println("XXXXx5");

            if (validIP(clientInClientObject.response)) {

                // TWORZENIE WATKU SERWERA W KLIENCEI, DO ODBIERANIA WIADOMOSCI OD INNYCH KLIENTOW
                ServerInClient serverInClientobject = new ServerInClient();
                System.out.println("XXXXx3");
                Thread serverinClientThread = new Thread(serverInClientobject);
                serverinClientThread.start();

                // TWORZENIE WATKU KLIENTA W KLIENCIE DO WYMIANY KOMUNIKATOW Z INNYMI KLIENTAMI
                neighbourAddress = InetAddress.getByName(clientInClientObject.response);
                System.out.println(clientInClientObject.response + "XXXXx");
                ClientInClient clientInClientObject2 = new ClientInClient(neighbourAddress);
                System.out.println("XXXXx3");
                Thread s2 = new Thread(clientInClientObject2);
                s2.start();
                System.out.println("XXXXXXXXXXXDDDDDDDDDDDDDd");
            }
        }


    }

    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


}

