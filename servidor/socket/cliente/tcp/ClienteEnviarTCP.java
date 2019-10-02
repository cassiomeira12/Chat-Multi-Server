/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteEnviarTCP
 * Funcao: Classe para enviar mensagens via TCP
 ***********************************************************************/

package socket.cliente.tcp;

import socket.IEnviarNet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClienteEnviarTCP implements IEnviarNet {
  private Socket socket;
  private String ip;
  private int porta;

  /*********************************************
   * Metodo: ClienteEnviarTCP
   * Funcao: Construtor
   * Parametros: Socket socket
   * Retorno: void
   *********************************************/
  public ClienteEnviarTCP(Socket socket) {
    this.socket = socket;
    this.ip = socket.getLocalAddress().getHostAddress();
    this.porta = socket.getPort();
  }

  /*********************************************
   * Metodo: EnviarMensagem
   * Funcao: Envia mensagem TCP
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  @Override
  public void enviarMensagem(String mensagem) {
    try {
      PrintStream saida = new PrintStream(socket.getOutputStream());
      saida.println(mensagem);//Enviando msg
      System.out.print("[CLIENTE] - [ClienteEnviarTCP] Enviou mensagem para ["+ip+"] ["+porta+"] ");
      System.out.println("Mensagem: " + mensagem);
    } catch (IOException ex) {
      try {
        socket.close();
      } catch (IOException e) {
        System.out.println("1");
        e.printStackTrace();
      }
      System.out.println("2");
      ex.printStackTrace();
    }
  }

}
