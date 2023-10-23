package org.lw5hr.contest.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPListener {
  private static final int PORT = 6789;
  private static final int BUFFER_SIZE = 1024;

  private DatagramSocket socket;
  private byte[] buffer = new byte[BUFFER_SIZE];

  private volatile boolean running = true;


  public UDPListener() throws IOException {
    socket = new DatagramSocket(PORT);
  }

  @SuppressWarnings("InfiniteLoopStatement")
  public void listen() throws IOException {
    System.out.println("Listening on port: " + PORT);
    while (true) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      socket.receive(packet);
      String message = new String(packet.getData(), 0, packet.getLength());
      InetAddress address = packet.getAddress();
      int port = packet.getPort();
      System.out.println("Received message from " + address.getHostAddress() + ":" + port + " - " + message);
    }
  }

  public void stop() {
    running = false;
    socket.close();
    System.out.println("Stopped listening on port: " + PORT);
  }
}