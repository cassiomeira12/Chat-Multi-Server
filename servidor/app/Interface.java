/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: Interface
* Funcao: Inicia a Interface do Sistema
***********************************************************************/

package app;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ClienteInterface;
import view.ServidorInterface;
import view.controller.TelaCliente;

public class Interface {

  public static Stage PALCO;//Palco principal do Sistema
  private static final int WIDTH = 750;//Largura
  private static final int HEIGHT = 500;//Altura

  public static TelaCliente telaClienteController;//Controller do TelaCliente

  /*********************************************
  * Metodo: Interface - Construtor
  * Funcao: Constroi objetos da classe Interface
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public Interface(Stage PALCO) {
    this.PALCO = PALCO;

    try {
      this.iniciarInterface();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*********************************************
  * Metodo: iniciarInterface
  * Funcao: Funcao inicia a interface do Sistema
  * Parametros: void
  * Retorno: void
  *********************************************/
  private void iniciarInterface() throws Exception {

    //Interface do Servidor
    Stage palcoServidor = new Stage();
    ServidorInterface servidorInterface = new ServidorInterface(palcoServidor);
    palcoServidor.setScene(new Scene(servidorInterface));
    palcoServidor.setMinWidth(WIDTH);
    palcoServidor.setMinHeight(HEIGHT);
    palcoServidor.show();

    //Interface do TelaCliente
//    Stage palcoCliente = new Stage();
//    ClienteInterface clienteInterface = new ClienteInterface(palcoCliente);
//    palcoCliente.setScene(new Scene(clienteInterface));
//    palcoCliente.setMinWidth(WIDTH);
//    palcoCliente.setMinHeight(HEIGHT);
//    palcoCliente.show();

  }

}//Fim class