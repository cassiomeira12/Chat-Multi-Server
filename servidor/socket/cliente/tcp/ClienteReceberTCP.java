/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteReceberTCP
 * Funcao: Classe para receber mensagens via TCP
 ***********************************************************************/

package socket.cliente.tcp;

import socket.IReceberNet;

import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClienteReceberTCP extends Thread {
  private Socket socket;//Socket de conexao com o TelaCliente
  private int porta;

  //Interface que implementa o metodo de receber mensagens
  private IReceberNet receberNet;

  /*********************************************
   * Metodo: ClienteReceberTCP
   * Funcao: Construtor
   * Parametros: Socket socket
   * Retorno: void
   *********************************************/
  public ClienteReceberTCP(Socket socket) {
    this.socket = socket;
    this.porta = socket.getLocalPort();
    this.start();
  }

  /*********************************************
   * Metodo: ClienteEnviarTCP
   * Funcao: Construtor
   * Parametros: Socket socket, IReceberNet receberNet
   * Retorno: void
   *********************************************/
  public ClienteReceberTCP(Socket socket, IReceberNet receberNet) {
    this.socket = socket;
    this.receberNet = receberNet;
    this.porta = socket.getLocalPort();
    this.start();
  }

  public void run() {
    System.out.println("[CLIENTE] - [ClienteReceberTCP] ["+porta+"] ["+socket.getPort()+"] - Iniciou");

    Scanner scanner = null;
    try {
      scanner = new Scanner(socket.getInputStream());
      while (!socket.isClosed()) {
        receberMensagem(scanner);
      }
    } catch (Exception ex) {
      scanner.close();
      close();
    }

    System.out.println("[CLIENTE] - [ClienteReceberTCP] ["+porta+"] - Finalizou");
  }

  /*********************************************
   * Metodo: ReceberMensagem
   * Funcao: Receber uma mensagem TCP no Cliente
   * Parametros: Scanner scanner
   * Retorno: void
   *********************************************/
  private void receberMensagem(Scanner scanner) throws NoSuchElementException {
    String mensagem = scanner.nextLine();//Mensagem recebida do TelaCliente
    System.out.println("[CLIENTE] - [ClienteReceberTCP] recebeu: " + mensagem);
    //Platform.runLater(() -> receberNet.receberMensagem(mensagem) );
  }

  /*********************************************
   * Metodo: close
   * Funcao: Fecha a conexao TCP do Cliente
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void close() {
    try {
      this.socket.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
