import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UDPClient {
    public final static int MCAST_PORT = 50001;
    public static void main(String[] args) {
        try {
// 1. Create a DatagramSocket object
            DatagramSocket clientSocket = new DatagramSocket();

// 2. Find the IP address of the server by name
            InetAddress IPAddress = null;
            try {
                IPAddress = InetAddress.getByName("localhost");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

// 3. Create Buffers for storing datagram data in DatagramPacket object
            byte[] buffReceiveData = new byte[1024]; // for incoming data
            byte[] buffSendData = new byte[1024]; // for outgoing data

// 4. Data to send to the server
            Scanner scanner = new Scanner(System.in);
            List<Integer> numberList = new ArrayList<>();

// Prompt the user to enter numbers until they're done
            while (true) {
                System.out.print("Enter a number (or type 'terminate' to finish): ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("terminate")) {
                    break;
                }
                try {
                    int number = Integer.parseInt(input);
                    numberList.add(number);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number or 'terminate' to finish.");
                }
            }

// Print the list of numbers
            System.out.println("List of numbers: " + numberList);

// Close the scanner
            scanner.close();
            String commaSeparatedString =
                    numberList.stream().map(Object::toString).collect(Collectors.joining(","));

// String sentence = "Hello world from MCast client...";
            buffSendData = commaSeparatedString.getBytes();

// 5. Create DatagramPacket obj for wrapping outgoing packet (datagram)
            DatagramPacket sendPacket = new DatagramPacket(buffSendData,
                    buffSendData.length, IPAddress, MCAST_PORT);
            try { // 6. Send the datagram to the above specified destination
                clientSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

// 7. Create DatagramPacket obj for wrapping incoming packet (datagram)
            DatagramPacket receivePacket = new DatagramPacket(buffReceiveData,
                    buffReceiveData.length);

// 8. Receive incoming datagram in to DatagramPacket object.
            try { // This is a blocking system call
                clientSocket.receive(receivePacket); // Program blocks here
            } catch (IOException e) { // Until a datagram is received
                e.printStackTrace();
            }

// 9. Display the received data
            String strReceived = new String(receivePacket.getData());
            System.out.println("CLIENT RECEIVED DATA : " + strReceived);

// 10. Close the DatagramSocket
            clientSocket.close();
        } catch (IOException e) { // Top Level Try-Catch
            e.printStackTrace();
        }
    } // Main method Ends Here
} // Class Ends Here