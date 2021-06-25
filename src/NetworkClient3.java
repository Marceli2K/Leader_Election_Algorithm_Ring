// A simple Client Server Protocol .. Client for Echo Server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class NetworkClient3 {

    public static void main(String args[]) throws IOException {
        clientInClient();

    }


    public static void clientInClient() throws IOException {
        List<String> listaparametrow = new ArrayList();

        InetAddress address = InetAddress.getLocalHost();
        Socket s1 = null;
        String line = null;
        BufferedReader br = null;
        BufferedReader is = null;
        PrintWriter os = null;

        try {
            s1 = new Socket(address, 4445); // You can use static final constant PORT_NUM
            br = new BufferedReader(new InputStreamReader(System.in));
            is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os = new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : " + address);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        String response = null;
        try {
            line = "hello";
            while (line.compareTo("QUIT") != 0) {
                os.println(line);
                os.flush();
                response = is.readLine();
                System.out.println("Server Response : " + response);
                listaparametrow.add(response);
                line = br.readLine();

            }


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        } finally {

            is.close();
            os.close();
            br.close();
            s1.close();
            System.out.println("Connection Closed");

        }
    }

    public void serverInClient() {
        InetAddress backAddress = null;

        Socket s = null;
        ServerSocket ss2 = null;
        System.out.println("Server Listening......");
        try {
            ss2 = new ServerSocket(4445); // can also use static final PORT_NUM , when defined

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                s = ss2.accept();
                System.out.println("connection Established");
                ServerThread st = new ServerThread(s);
                st.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }

    }
}