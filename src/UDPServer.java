import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
    public final static int MCAST_PORT = 50001;
    public static void main(String[] args) {
        try {
// 1. Create a DatagramSocket object
            DatagramSocket serverSocket = new DatagramSocket(MCAST_PORT);

// 2. Create Buffers for storing datagram data in DatagramPacket object
            byte[] buffReceiveData = new byte[1024]; // for incoming data
            byte[] buffSendData = new byte[1024]; // for outgoing data

// 3. Create DatagramPacket obj for wrapping incoming packet (datagram)
            DatagramPacket packetIn = new DatagramPacket(buffReceiveData,
                    buffReceiveData.length);

// 4. Receive incoming datagram in to DatagramPacket object.
            try { // This is a blocking system call
                serverSocket.receive(packetIn); // Program blocks here
            } catch (IOException e) { // Until a datagram is received
                e.printStackTrace();
            }

// 5. Get the data from received packet
            String strInData = new String(packetIn.getData());
            String[] numberStrings = strInData.split(",");

// Create a list to store the numbers
            List<Integer> numberList = new ArrayList<>();

// Convert and add each substring to the list
            for (String numberStr : numberStrings) {
                try {
                    System.out.println(numberStr);
                    int number = Integer.parseInt(numberStr.trim());
                    numberList.add(number);
                } catch (NumberFormatException e) { // Handle invalid number format if needed
                    System.err.println(e);
                }
            }
            System.out.println("SERVER RECEIVED DATA : " + strInData);
            int sum = 0;
            for (int num : numberList) {
                sum += num;
            }
            buffSendData = ("Total = " + sum + " Average = " + sum /
                    numberList.size()).getBytes();

// 6. Find sender's address and port from the received packet
            InetAddress inAddress = packetIn.getAddress();
            int inPort = packetIn.getPort();

// 7. Creat datagram to send
            DatagramPacket packetOut = new DatagramPacket(buffSendData,
                    buffSendData.length, inAddress, inPort);

// 8. Send the response datagram
            try {
                serverSocket.send(packetOut);
            } catch (IOException e) {
                e.printStackTrace();
            }

// 9. Close the DatagramSocket
            serverSocket.close();
        } catch (IOException e) { // Top Level Try-Catch
            e.printStackTrace();
        }
    } // Main method Ends Here
} // Class Ends Here