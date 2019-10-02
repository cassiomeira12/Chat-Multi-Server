/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteEnviarUDP
 * Funcao: Classe para enviar mensagens via UDP
 ***********************************************************************/

package socket.servidor.udp;

import socket.IEnviarNet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServidorEnviarUDP implements IEnviarNet {
  private DatagramSocket socket;
  private InetAddress ip;
  private int porta;
  private byte[] data;

  /*********************************************
   * Metodo: ClienteEnviarUDP
   * Funcao: Construtor
   * Parametros: String ip, int porta
   * Retorno: void
   *********************************************/
  public ServidorEnviarUDP(String ip, int porta) {
    try {
      this.socket = new DatagramSocket();
      this.ip = InetAddress.getByName(ip);
      this.porta = porta;
      this.data = new byte[1024];
    } catch (Exception ex) {
      this.ip = null;
    }
  }

  /*********************************************
   * Metodo: enviarMensagem
   * Funcao: Envia uma mensagem UDP do Servidor
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  @Override
  public void enviarMensagem(String mensagem) {
    if (ip == null) {
      System.err.println("IP nao definido");
      return;
    }
    data = mensagem.getBytes();
    DatagramPacket pacote = new DatagramPacket(data, data.length, ip, porta);
    try {
      socket.send(pacote);
      System.out.println("[SERVIDOR] - Enviou mensagem para ["+ip+"] ["+porta+"] Mensagem: " + mensagem);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /*********************************************
   * Metodo: getIP
   * Funcao: Retorna o IP do Cliente para o qual o Servidor envia mensagens
   * Parametros: void
   * Retorno: String
   *********************************************/
  public String getIP() {
    return ip.getHostAddress();
  }

  public DatagramSocket getSocket() {
    return socket;
  }

}
