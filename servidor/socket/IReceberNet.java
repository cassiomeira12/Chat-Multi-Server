/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: IReceberNet
 * Funcao: Interface para os Socket de Receber
 ***********************************************************************/

package socket;

public interface IReceberNet<T> {

  /*********************************************
   * Metodo: ReceberMensagem
   * Funcao: Interface da funcao de receber mensagem
   * Parametros: T mensagem
   * Retorno: void
   *********************************************/
  public void receberMensagem(T pacote);

}
