/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteEnviarUDP
 * Funcao: Classe para enviar mensagens via UDP
 ***********************************************************************/

package socket.cliente.udp;

import socket.IEnviarNet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClienteEnviarUDP implements IEnviarNet {
  private DatagramSocket socket;
  private InetAddress ip;
  private int porta;
  private byte[] data;

  /*********************************************
   * Metodo: ClienteEnviarUDP
   * Funcao: Construtor
   * Parametros: Socket socket
   * Retorno: void
   *********************************************/
  public ClienteEnviarUDP(DatagramSocket socket, String ip, int porta) {
    this.socket = socket;
    try {
      this.ip = InetAddress.getByName(ip);
      this.porta = porta;
      this.data = new byte[1024];
    } catch (Exception ex) {
      this.ip = null;
    }
  }

  /*********************************************
   * Metodo: enviarMensagem
   * Funcao: Funcao para enviar mensagem UDP do Cliente
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  @Override
  public void enviarMensagem(String mensagem) {
    if (ip == null) {
      System.err.println("Ip nao definido");
      return;
    }
    data = mensagem.getBytes();
    DatagramPacket pacote = new DatagramPacket(data, data.length, ip, porta);
    try {
      socket.send(pacote);
      System.out.println("[CLIENTE] - Enviou mensagem para ["+ ip.getHostAddress() +"] ["+porta+"] Mensagem: " + mensagem);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void setIP(String ip) {
    try {
      this.ip = InetAddress.getByName(ip);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

}
