package org.lw5hr.contest.utils;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPListener {
  private static final int PORT = 12060;
  private final DatagramSocket socket;
  private final byte[] buffer = new byte[1024];

  public UDPListener() throws IOException {
    socket = new DatagramSocket(PORT);
  }
  @SuppressWarnings("InfiniteLoopStatement") /*We know this is an infinite loop what is what we need here*/
  public void listen() throws IOException {
    System.out.println("Listening on port: " + PORT);
    DxLogParser dxLogParser = new DxLogParser();
    try {
      while (true) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength());
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        try {
          dxLogParser.parse(message);
        } catch (SAXException e) {
          throw new RuntimeException(e);
        }
        System.out.println("Received message from " + address.getHostAddress() + ":" + port + " - " + message);
      }
    } catch (IOException ignored) {

    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    try {
      UDPListener listener = new UDPListener();
      listener.listen();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    socket.close();
  }
}
