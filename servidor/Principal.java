/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 26/08/18
* Nome: Principal
* Funcao: Chamar Tela do Programa
***********************************************************************/

import app.Interface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Principal extends Application {

  /*********************************************
  * Metodo: main
  * Funcao: Metodo main do programa
  * Parametros: String[] args
  * Retorno: void
  *********************************************/
  public static void main(String[] args) {
    launch(args);
  }

  /*********************************************
  * Metodo: start
  * Funcao: Inicia a interface JavaFX
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  @Override
  public void start(Stage palco) throws Exception {
    new Interface(palco);//Inicia a interface do Sistema
  }

  /*********************************************
  * Metodo: stop
  * Funcao: Finaliza o programa
  * Parametros: void
  * Retorno: void
  *********************************************/
  @Override
  public void stop() throws Exception {
    Platform.exit();
    System.exit(0);
  }

}//Fim class