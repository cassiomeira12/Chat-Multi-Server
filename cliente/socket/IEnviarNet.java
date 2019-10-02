/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: IEnviarNet
 * Funcao: Interface para os Socket de Enviar
 ***********************************************************************/

package socket;

public interface IEnviarNet {

  /*********************************************
   * Metodo: EnviarMensagem
   * Funcao: Interface da funcao de enviar mensagem
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  public void enviarMensagem(String mensagem);

}
