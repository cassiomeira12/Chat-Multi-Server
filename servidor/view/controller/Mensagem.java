/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: BoxMenu
* Funcao: Controler da Mensagem do JavaFX
***********************************************************************/

package view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Mensagem extends HBox {
  private final String FXML = "view/mensagem.fxml";//Caminho do FXML

  @FXML
  private VBox boxMensagem;
  @FXML
  private Label remetenteLabel;
  @FXML
  private HBox boxConteudo;
  @FXML
  private Label mensagemLabel;
  @FXML
  private Label horaLabel;

  /*********************************************
  * Metodo: Mensagem - Construtor
  * Funcao: Constroi objetos da classe Mensagem
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public Mensagem() {
    try {
      //Cria o carregador de arquivos FXML
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(FXML));
      fxmlLoader.setRoot(this);//Atribui essa classe como o Root da View
      fxmlLoader.setController(this);//Atribui essa classe como o Controller da View
      fxmlLoader.load();//Carrega a View FXML
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /*********************************************
   * Metodo: mensagemCentral
   * Funcao: Atribui ao tipo da Mensagem como CENTRAL
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  public void mensagemCentral(String mensagem) {
    this.setAlignment(Pos.CENTER);//Posicao centralizada
    this.boxMensagem.setStyle("-fx-background-color: #dfe6e9");
    this.mensagemLabel.setText(mensagem);//Adicionando Texto da mensagem
    this.boxMensagem.getChildren().remove(0);//Removendo label Quem Eviou
    this.boxMensagem.getChildren().remove(1);//Removendo label Hora
  }

  /*********************************************
  * Metodo: mensagemEnviada
  * Funcao: Atribui ao tipo da Mensagem como ENVIADA
  * Parametros: String mensagem
  * Retorno: void
  *********************************************/
  public void mensagemEnviada(String mensagem) {
    this.setAlignment(Pos.CENTER_RIGHT);
    this.boxMensagem.setStyle("-fx-background-color: #81ecec");
    //this.boxMensagem.setAlignment(Pos.CENTER_RIGHT);
    this.mensagemLabel.setText(mensagem);
    this.boxMensagem.getChildren().remove(0);
    String hora = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
    this.horaLabel.setText(hora);
  }

  /*********************************************
  * Metodo: mensagemRecebida
  * Funcao: Atribui ao tipo da Mensagem como RECEBIDA
  * Parametros: String mensagem
  * Retorno: void
  *********************************************/
  public void mensagemRecebida(String mensagem) {
    this.setAlignment(Pos.CENTER_LEFT);
    this.boxMensagem.setStyle("-fx-background-color: #dcdde1");
    //this.boxMensagem.setAlignment(Pos.CENTER_LEFT);
    this.mensagemLabel.setText(mensagem);
    String hora = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
    this.horaLabel.setText(hora);
  }

  /*********************************************
   * Metodo: mensagemRecebida
   * Funcao: Atribui ao tipo da Mensagem como RECEBIDA
   * Parametros: String mensagem, String remetente
   * Retorno: void
   *********************************************/
  public void mensagemRecebida(String mensagem, String remetente) {
    this.setAlignment(Pos.CENTER_LEFT);
    this.boxMensagem.setStyle("-fx-background-color: #dcdde1");
    this.boxMensagem.setAlignment(Pos.CENTER_LEFT);
    this.mensagemLabel.setText(mensagem);
    this.remetenteLabel.setText(remetente);
    String hora = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(new Date());
    this.horaLabel.setText(hora);
  }

}//Fim class