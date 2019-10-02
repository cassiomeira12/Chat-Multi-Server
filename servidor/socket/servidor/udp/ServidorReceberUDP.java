/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteReceberUDP
 * Funcao: Classe para receber mensagens via UDP
 ***********************************************************************/

package socket.servidor.udp;

import socket.IReceberNet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServidorReceberUDP extends Thread {
  private DatagramSocket socket;
  private int porta;
  private byte[] data;

  //Interface que implementa os metodos de receber APDU
  IReceberNet<DatagramPacket> receberNet;

  /*********************************************
   * Metodo: ServidorReceberUDP
   * Funcao: Construtor
   * Parametros: DatagramSocket socket
   * Retorno: void
   *********************************************/
  public ServidorReceberUDP(DatagramSocket socket) {
    this.socket = socket;
    this.porta = socket.getLocalPort();
    this.data = new byte[1024];
    this.start();
  }

  public void run() {
    try {
      System.out.println("[SERVIDOR] - [ServidorReceberUDP] ["+porta+"] INICIOU");
      while (!socket.isClosed()) {
        data = new byte[1024];
        receber(new DatagramPacket(data, data.length));
      }
    } catch (Exception ex) {
      System.out.println("[SERVIDOR] - [ServidorReceberUDP] ["+porta+"] FINALIZOU");
    }
  }

  /*********************************************
   * Metodo: receber
   * Funcao: Recebe mensagens UDP no Servidor
   * Parametros: DatagramPacket pacote
   * Retorno: void
   *********************************************/
  private void receber(DatagramPacket pacote) throws Exception {
    socket.receive(pacote);
    receberNet.receberMensagem(pacote);
  }

  /*********************************************
   * Metodo: setReceberNet
   * Funcao: Atribui a classe que implementa o recebimento de mensagens
   * Parametros: IReceberNet receberNet
   * Retorno: void
   *********************************************/
  public void setReceberNet(IReceberNet<DatagramPacket> receberNet) {
    this.receberNet = receberNet;
  }

  /*********************************************
   * Metodo: close
   * Funcao: Fecha a conexao UDP do Servidor
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void close() {
    socket.close();
  }

}
