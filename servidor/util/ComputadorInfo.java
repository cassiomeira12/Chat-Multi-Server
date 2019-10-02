/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 24/08/18
 * Ultima alteracao: 14/12/18
 * Nome: ComputadorInfo
 * Funcao: Guarda informacoes do Computador para enviar pela rede
 ***********************************************************************/

package util;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ComputadorInfo implements Serializable {

  public String hostName;
  public String ip;
  public String porta;

  public String sistemaOperacional;
  public String arquitetura;

  /*********************************************
   * Metodo: ComputadorInfo
   * Funcao: Construtor
   * Parametros: void
   * Retorno: void
   *********************************************/
  public ComputadorInfo() {
    this.sistemaOperacional = System.getProperty("os.name");
    this.arquitetura = System.getProperty("os.arch" );
  }

  /*********************************************
   * Metodo: ComputadorInfor
   * Funcao: Construtor
   * Parametros: Socket socket
   * Retorno: void
   *********************************************/
  public ComputadorInfo(Socket socket) {
    try {
      InetAddress address = InetAddress.getLocalHost();
      this.hostName = address.getHostName();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    this.ip = socket.getInetAddress().getHostAddress();
    this.porta = String.valueOf(socket.getLocalPort());
    this.sistemaOperacional = System.getProperty("os.name");
    this.arquitetura = System.getProperty("os.arch");
  }

}
