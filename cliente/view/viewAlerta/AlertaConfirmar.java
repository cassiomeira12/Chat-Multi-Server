/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 24/08/18
 * Ultima alteracao: 14/12/18
 * Nome: AlertaConfirmacao
 * Funcao: Cria um Alerta com retorno de Confirmacao
 ***********************************************************************/

package view.viewAlerta;

import javafx.application.Platform;
import view.viewAlerta.Alerta.Painel.Tipo;

public abstract class AlertaConfirmar extends Alerta {

  /*********************************************
   * Metodo: AlertaConfirmar - Construtor
   * Funcao: Constroi objetos da classe AlertaConfirmar
   * Parametros: String titulo, String mensagem
   * Retorno: void
   *********************************************/
  public AlertaConfirmar(String titulo, String mensagem) {
    Platform.runLater(() -> {
      painel.setTipo(Tipo.CONFIR);
      painel.setText(titulo, mensagem);
      painel.RESPOSTA = Resposta.NAO;
      painel.showAndWait();
      aguardandoResposta();
    });
  }

  /*********************************************
   * Metodo: SIM
   * Funcao: Prototipo do metodo que executa quando selecionar botao SIM
   * Parametros: void
   * Retorno: void
   *********************************************/
  protected abstract void SIM();

  /*********************************************
   * Metodo: NAO
   * Funcao: Prototipo do metodo que executa quando selecionar botao NAO
   * Parametros: void
   * Retorno: void
   *********************************************/
  protected abstract void NAO();

  /*********************************************
   * Metodo: aguardandoResposta
   * Funcao: Retorna a resposta selecionada pelo usuario
   * Parametros: void
   * Retorno: void
   *********************************************/
  private void aguardandoResposta() {
    switch (painel.RESPOSTA) {
      case SIM:
        SIM();
        break;
      case NAO:
        NAO();
        break;
    }
  }

}
