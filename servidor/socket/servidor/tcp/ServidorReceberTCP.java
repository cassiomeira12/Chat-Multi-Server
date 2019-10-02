/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: ServidorReceberTCP
* Funcao: Thread utilizada para receber dados do TelaCliente
***********************************************************************/

package socket.servidor.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import socket.pdu.Apdu;
import socket.pdu.Apdu.IApdu;
import util.ComputadorInfo;

public class ServidorReceberTCP extends Thread {
  private Socket socket;//Socket de conexao com o TelaCliente

  private String ip;//IP do TelaCliente

  //private ConversaServidor conversa;//Area de conversa (Proximos Trabalhos)

  private int indexMenu;//Indice do TelaCliente no Menu (Proximos Trabalhos)

  //Interface que implementa metodos da APDU
  private IApdu iApdu;
  
  /*********************************************
  * Metodo: ServidorReceberTCP - Construtor
  * Funcao: Constroi objetos da classe ServidorReceberTCP
  * Parametros: Socket socket
  * Retorno: void
  *********************************************/
  public ServidorReceberTCP(Socket socket) {
    this.socket = socket;//Socket da Conexao com o TelaCliente
    this.ip = socket.getInetAddress().getHostAddress();//Pegando IP do TelaCliente
    this.start();//Iniciando essa Thread
  }
    
  public void run() {
    System.out.println("[SERVIDOR] - [ServidorReceberTCP] Cliente [" + ip + "] - Iniciou");
//    try {
//      receberComputadorInfo();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    }

    //----------------------------------------------------

    Scanner scanner = null;
    try {
      scanner = new Scanner(socket.getInputStream());
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    while (!socket.isClosed()) {
      try {
        receberMensagem(scanner);
      } catch (NoSuchElementException e) {
        try {
          socket.close();
          scanner.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }

    System.out.println("[SERVIDOR] - [ServidorReceberTCP] Cliente [" + ip + "] - Finalizou");
  }

  /*********************************************
   * Metodo: receberMensagem
   * Funcao: Recebe mensagens TCP no Servidor
   * Parametros: Scanner scanner
   * Retorno: void
   *********************************************/
  private void receberMensagem(Scanner scanner) throws NoSuchElementException {
    String mensagem = scanner.nextLine();//Recendo mensagem do TelaCliente

    System.out.println("[SERVIDOR] - [ServidorReceberTCP] ["+ip+"] Recebeu mensagem: " + mensagem);

    //Inicia a classe Apdu para tratar a mensagem recebido
    Apdu apdu = new Apdu(iApdu);
    apdu.tratarMensagem(mensagem, ip);//Inicia a verificacao da mensagem

    //Platform.runLater(() -> {
//      if (!conversa.getConversa().getConversaAberta()) {
//        Notificacao not = new Notificacao(Notifications.INFORMATION);
//        not.setText("Nova Mensagem", "Nova mensagem de " + ip);
//        not.showAndDismiss(Duration.seconds(2));
//      }
      //conversa.getConversa().receberMensagem(mensagem);
    //});
  }

  /*********************************************
   * Metodo: setIApdu
   * Funcao: Atribui a classe que implementa os metodos da APDU
   * Parametros: IApdu iApdu
   * Retorno: void
   *********************************************/
  public void setIApdu(IApdu iApdu) {
    this.iApdu = iApdu;
  }

  /*********************************************
   * Metodo: close
   * Funcao: Fecha a conexao TCP do Servidor
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void close() {
    try {
      socket.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /*********************************************
   * Metodo: receberComputadorInfo
   * Funcao: Recebe as informacoes do TelaServidor TelaCliente
   * Parametros: void
   * Retorno: void
   *********************************************/
  private void receberComputadorInfo() throws IOException, ClassNotFoundException {
    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
    ComputadorInfo info = (ComputadorInfo) input.readObject();
    //conversa.setComputadorInfo(info);
  }

  /*********************************************
  * Metodo: setInterfaceConversa
  * Funcao: Atribui uma interface Conversa do TelaCliente
  * Parametros: ConversaServidor conversa
  * Retorno: void
  *********************************************/
//  public void setInterfaceConversa(ConversaServidor conversa) {
//    this.conversa = conversa;
//  }

  /*********************************************
  * Metodo: setSocketServer
  * Funcao: ServerSocket server
  * Parametros: Atribui o socket do Servidor
  * Retorno: void
  *********************************************/
  public void setSocketServer(ServerSocket server) {
    //this.socketServer = server;
  }

  /*********************************************
  * Metodo: getIP
  * Funcao: Retorna o ip do TelaCliente
  * Parametros: void
  * Retorno: String
  *********************************************/
  public String getIP() {
    return ip;
  }

  /*********************************************
  * Metodo: setIndexMenu
  * Funcao: Atribui o index do TelaCliente no Menu
  * Parametros: int index
  * Retorno: void
  *********************************************/
  public void setIndex(int index) {
    this.indexMenu = index;
  }

  /*********************************************
  * Metodo: getIndexMenu
  * Funcao: Retorna o index do TelaCliente no Menu
  * Parametros: void
  * Retorno: int
  *********************************************/
  public int getIndex() {
    return indexMenu;
  }

  /*********************************************
  * Metodo: getSocket
  * Funcao: Retonra o socket do TelaCliente
  * Parametros: void
  * Retorno: Socket
  *********************************************/
  public Socket getSocket() {
    return socket;
  }
  
}//Fim class