/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: MenuSuperior
* Funcao: Controler do MenuSuperior do JavaFX
***********************************************************************/

package view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import view.viewAlerta.AlertaConfirmar;

import java.io.IOException;

public class MenuSuperior extends MenuBar {
  private final String FXML = "view/menuSuperior.fxml";//Caminho do FXML

  /*********************************************
  * Metodo: MenuSuperior - Construtor
  * Funcao: Constroi objetos da classe MenuSuperior
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public MenuSuperior() {
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
  * Metodo: Initialize
  * Funcao: Metodo para carregar a Interface
  * Parametros: void
  * Retorno: void
  *********************************************/
  @FXML
  public void initialize() {

  }

  /*********************************************
  * Metodo: fechar
  * Funcao: Fecha o Sistema
  * Parametros: void
  * Retorno: void
  *********************************************/
  @FXML
  public void fechar() {
    //System.exit(0);

    //Alerta.ERRO("Erro");
    //Alerta.ALERTA("Alerta");
    //Alerta.SUCESS("info");


    new AlertaConfirmar("Titulo", "Mensagem") {
      @Override
      protected void SIM() {
        System.out.println("Sim");
      }

      @Override
      protected void NAO() {
        System.out.println("Nao");
      }
    };

  }



}//Fim class