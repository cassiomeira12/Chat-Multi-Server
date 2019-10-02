/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteEnviarTCP
 * Funcao: Classe para receber mensagens via UDP
 ***********************************************************************/

package socket.cliente.udp;

import socket.IReceberNet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClienteReceberUDP extends Thread {
  private DatagramSocket socket;
  private int porta;
  private byte[] data;

  //Interface que implementa o recebimento da mensagem
  private IReceberNet<DatagramPacket> receberNet;

  /*********************************************
   * Metodo: ClienteReceberUDP
   * Funcao: Construtor
   * Parametros: DatagramSocket socket, IReceberNet receberNeta
   * Retorno: void
   *********************************************/
  public ClienteReceberUDP(DatagramSocket socket, IReceberNet<DatagramPacket> receberNet) {
    this.socket = socket;
    this.receberNet = receberNet;
    this.porta = socket.getLocalPort();
    this.data= new byte[1024];
    this.start();
  }

  public void run() {
    System.out.println("[CLIENTE] - [ClienteReceberUDP] ["+porta+"] - Iniciado");

    while (!socket.isClosed()) {
      data = new byte[1024];
      try {
        receber(new DatagramPacket(data, data.length));
      } catch (Exception ex) {
        close();
        //ex.printStackTrace();
      }
    }

    System.out.println("[CLIENTE] - [ClienteReceberUDP] ["+porta+"] - Finalizou");
  }

  /*********************************************
   * Metodo: receber
   * Funcao: Recebe pacotes UDP no Cliente
   * Parametros: DatagramPacket pacote
   * Retorno: void
   *********************************************/
  private void receber(DatagramPacket pacote) throws Exception {
    socket.receive(pacote);
    receberNet.receberMensagem(pacote);
  }

  /*********************************************
   * Metodo: setReceberNet
   * Funcao: Atribui a classe que implementa a interface IReceberNet
   * Parametros: IReceberNet receberNet
   * Retorno: void
   *********************************************/
  public void setReceberNet(IReceberNet<DatagramPacket> receberNet) {
    this.receberNet = receberNet;
  }

  /*********************************************
   * Metodo: close
   * Funcao: Fechar a conexao UDP do Cliente
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void close() {
    this.socket.close();
  }

  /*********************************************
   * Metodo: isClosed
   * Funcao: Verifica se a conexao UDP do cliente foi fechada
   * Parametros: void
   * Retorno: boolean
   *********************************************/
  public boolean isClosed() {
    return socket.isClosed();
  }

}
