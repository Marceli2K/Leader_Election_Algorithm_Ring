import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientInClient extends Thread {
    protected static String response;
    InetAddress address;
    public ClientInClient(InetAddress localHost) {
        this.address = localHost;
    }

    public void run() {
        List<String> listaparametrow = new ArrayList();

//        address = InetAddress.getLocalHost();
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

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            os.close();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                s1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection Closed");

        }
    }

}
