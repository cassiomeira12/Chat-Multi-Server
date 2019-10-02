/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 26/08/18
* Nome: HoraSistema
* Funcao: Utilizada para enviar a hora do sistema
***********************************************************************/

package script;

import javafx.application.Platform;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HoraSistema extends Thread {

  private Label horaSistemaLabel;//Label da interface do Servidor

  /*********************************************
  * Metodo: HoraSistema - Construtor
  * Funcao: Constroi objetos da classe HoraSistema
  * Parametros: Label horaSistemaLabel
  * Retorno: void
  *********************************************/
  public HoraSistema(Label horaSistemaLabel) {
    this.horaSistemaLabel = horaSistemaLabel;
    this.start();
  }

  public void run() {
    //Formatador de Hora
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
    while (true) {
      //Altera a informacao da Label da interface do Servidor
      Platform.runLater(() -> horaSistemaLabel.setText(dateFormat.format( new Date() )));
      try {
        //Espera 1 segundo pra atualizar novamente
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }
    }
  }

  /*********************************************
  * Metodo: getHoraSistema
  * Funcao: Retorna um Date com a hora do sistema
  * Parametros: void
  * Retorno: Date
  *********************************************/
  public static Date getHoraSistema() {
    return new Date();
  }

}//Fim class