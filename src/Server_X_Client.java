// echo server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server_X_Client {
    List<ServerThread> listaKlientów = new ArrayList<ServerThread>();
    public static HashMap<ServerThread, Integer> threadMap = new HashMap<ServerThread, Integer>();
    public static LinkedHashMap<ServerThread, InetAddress> addressMap = new LinkedHashMap<ServerThread, InetAddress>();
    static int globalId = 0;
    InetAddress backAddress;

    public static void main(String args[]) {
        socketMethod();


    }

    public static void socketMethod() {
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
                threadMap.put(st, globalId++);
                addressMap.put(st, st.s.getInetAddress());
                System.out.println(threadMap);
                if (threadMap.size() >= 3) {
                    for (ServerThread i : threadMap.keySet()) {
                        if (!i.haveId)
                            i.sendMessageAboutIdToClient(threadMap.get(i));
                    }
                    for (ServerThread i : threadMap.keySet()) {
                        if (!i.haveIP) {
                            InetAddress sddress = addressMap.get(i);
                            if (backAddress != null)
                                i.sendMessageAboutIPNeighbours(backAddress);
                            backAddress = sddress;
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }

    }

    public List getListaKlientów() {
        return listaKlientów;
    }
}

class ServerThread extends Thread {
    String line = null;
    BufferedReader is = null;
    PrintWriter os = null;
    Socket s = null;
    boolean haveId = false;
    boolean haveIP = false;
    private List listaKlientowServerThread;

    public ServerThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
            line = is.readLine();
            while (line.compareTo("QUIT") != 0) {

                os.println(line);
                os.flush();
                System.out.println("Response to Client  :  " + line);
                line = is.readLine();

            }
        } catch (IOException e) {

            line = this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        } catch (NullPointerException e) {
            line = this.getName(); //reused String line for getting thread name
            System.out.println("Client " + line + " Closed");
        } finally {
            try {
                System.out.println("Connection Closing..");
                if (is != null) {
                    is.close();
                    System.out.println(" Socket Input Stream Closed");
                }

                if (os != null) {
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (s != null) {
                    s.close();
                    System.out.println("Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }//end finally
    }

    public void sendMessageAboutIdToClient(Integer integers) {
        boolean sended = false;
        InetAddress address = s.getLocalAddress();
        try {
//            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        //            line=is.readLine();
        while (!sended) {

            os.println(integers);
            os.flush();
            System.out.println("Response to Client  :  " + integers);
//                line=is.readLine();
            sended = true;
            haveId = true;
        }
    }

    public void sendMessageAboutIPNeighbours(InetAddress address) {
        boolean sended = false;
        try {
//            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        //            line=is.readLine();
        while (!sended) {

            os.println(address);
            os.flush();
            System.out.println("Response to Client  :  " + address);
//                line=is.readLine();
            sended = true;
            haveIP = true;
        }
    }
}