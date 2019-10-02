/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: ClienteEnviar
* Funcao: Thread utilizada para enviar dados ao TelaCliente
***********************************************************************/

package socket.servidor.tcp;

import socket.IEnviarNet;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ServidorEnviarTCP implements IEnviarNet {
  private Socket socket;//Socket de conexao com o TelaCliente

  /*********************************************
  * Metodo: ServidorEnviarTCP - Construtor
  * Funcao: Constroi objetos da classe ServidorEnviarTCP
  * Parametros: Socket socket
  * Retorno: void
  *********************************************/
  public ServidorEnviarTCP(Socket socket) {
    this.socket = socket;
  }

  /*********************************************
   * Metodo: enviarMensagem
   * Funcao: Envia uma mensagem TCP do Servidor
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  @Override
  public void enviarMensagem(String mensagem) {
    try {
      PrintStream saida = new PrintStream(socket.getOutputStream());
      saida.println(mensagem);//Enviando msg
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}//Fim class