/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: ServidorConexoesTCP
* Funcao: Thread de conexao do Servidor com o Clientes
***********************************************************************/

package socket.servidor.tcp;

import javafx.application.Platform;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import view.ServidorInterface;
import view.viewNotificacao.Notificacao;
import view.viewNotificacao.notification.Notifications;

public class ServidorConexoesTCP extends Thread {
  private ServerSocket server;//Socket do Servidor
  private ServidorInterface servidorInterface;

  private int porta;

  /*********************************************
  * Metodo: ServidorConexoesTCP - Construtor
  * Funcao: Constroi objetos da classe ServidorConexoesTCP
  * Parametros: ServerSocket server, List clientes
  * Retorno: void
  *********************************************/
  public ServidorConexoesTCP(ServerSocket socket) {
    this.server = socket;
    this.porta = socket.getLocalPort();
    this.start();
  }
  
  public void run() {
    try {
      System.out.println("[SERVIDOR] - [ServidorConexoesTCP] ["+porta+"] INICIOU");
      while (!server.isClosed()) {
        conexao();
      }
    } catch (Exception ex) {
      System.out.println("[SERVIDOR] - [ServidorConexoesTCP] ["+porta+"] FINALIZOU");
    }

  }

  /*********************************************
   * Metodo: conexao
   * Funcao: Aguarda uma nova conexao de Cliente
   * Parametros: void
   * Retorno: void
   *********************************************/
  private void conexao() throws IOException {
    Socket socket = server.accept();//Aguarda um receberMensagem com o TelaCliente
    String ip = socket.getInetAddress().getHostAddress();//Pega o IP do telaCliente

    Platform.runLater(() -> {
      Notificacao notificacao = new Notificacao(Notifications.INFORMATION);
      notificacao.show("TelaCliente conectado", ip);
    });

    System.out.println("[SERVIDOR] - Cliente Conectado IP: " + ip + " Porta: " + socket.getLocalPort());
    ServidorReceberTCP receberTCP = new ServidorReceberTCP(socket);
    receberTCP.setIApdu(servidorInterface.getTelaServidor());

    //ServidorEnviarTCP enviarTCP = new ServidorEnviarTCP(socket);
    //enviarTCP.enviarMensagem("Oi eu sou o servidor");

    Platform.runLater(() -> {
      servidorInterface.adicionarCliente(receberTCP);
    });

  }

  /*********************************************
   * Metodo: setServidorInterface
   * Funcao: Referencia a classe da interface do Servidor
   * Parametros: ServidorInterface servidorInterface
   * Retorno: void
   *********************************************/
  public void setServidorInterface(ServidorInterface servidorInterface) {
    this.servidorInterface = servidorInterface;
  }

  /*********************************************
   * Metodo: close
   * Funcao: Fecha a conexao do SocketServer
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void close() {
    try {
      server.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}//Fim class
