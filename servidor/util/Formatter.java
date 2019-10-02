/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 04/12/2018
 * Ultima alteracao: 14/12/2018
 * Nome: Formatter
 * Funcao: Formatador de componentes de entrada no JavaFX
 ***********************************************************************/

package util;

import javafx.scene.control.TextFormatter;

public class Formatter {

  private static final String ALFA_NUMERICO = "[^a-zA-Z0-9]";//Letra e numero
  private static final String ALFA_NUMERICO_MINUSCULO = "[^a-z0-9]";//Letra e numero minusculo
  private static final String NUMERICO = "[^0-9]";//Apenas numero
  private static final String ALFA = "[^a-zA-Z]";//Letra maiuscula e minuscula

  /*********************************************
   * Metodo: NUMERICO
   * Funcao:
   * Parametros:
   * Retorno: TextFormatter
   *********************************************/
  public static TextFormatter<String> NUMERICO() {
    return new TextFormatter<>(change -> {
      change.setText(change.getText().replaceAll(Formatter.NUMERICO, ""));
      return change;
    });
  }

}
