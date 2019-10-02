/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 24/08/18
 * Ultima alteracao: 14/12/18
 * Nome: SubMenu
 * Funcao: SubMenu do Manu Lateral
 ***********************************************************************/

package view.menuLateral;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class SubMenu extends HBox {
  private BoxMenu boxMenu;
  private Label nomeLabel;
  private boolean selecionado = false;

  /*********************************************
   * Metodo: SubMenu - Construtor
   * Funcao: Constroi objetos da classe SubMenu
   * Parametros: Stage PALCO
   * Retorno: void
   *********************************************/
  public SubMenu(String nome, BoxMenu boxMenu) {
    this.setStyle("-fx-background-color: " + BoxMenu.COR_DESELECIONADO);
    this.setCursor(Cursor.HAND);
    this.nomeLabel = new Label(nome);
    this.getChildren().add(nomeLabel);
    this.setMinHeight(30);
    this.setAlignment(Pos.CENTER_LEFT);
    VBox.setMargin(this, new Insets(1,0,0,10));

    this.nomeLabel.setPadding(new Insets(0,0,0,20));
    this.boxMenu = boxMenu;
    this.boxMenu.addSubMenu(this);

    //Evento de Click com o Mouse
    this.setOnMouseClicked((e) -> {
      boxMenu.subMenuSelecionado(this);
      onClick();
    });

    //Evento ao passar o Mouse por cima do SubMenu
    this.setOnMouseEntered((e) -> {
      if (!selecionado) {//Quando o SubMenu nao estiver selecionado
        this.setStyle("-fx-background-color: " + BoxMenu.COR_HOVER);//Alterar a cor dele para COR_SELECIONADO
      }
    });
    //Evento ao tirar o Mouse de cima do SubMenu
    this.setOnMouseExited((e) -> {//Quando o SubMenu nao estiver selecionado
      if (!selecionado) {
        this.setStyle("-fx-background-color: " + BoxMenu.COR_DESELECIONADO);//Altera a cor dele para COR_DESELECIONADO
      }
    });

  }

  /*********************************************
   * Metodo: onClick
   * Funcao: Acao do SubMenu quando e clicado
   * Parametros: void
   * Retorno: void
   *********************************************/
  public abstract void onClick();

  /*********************************************
   * Metodo: setSelecionado
   * Funcao: Atribui o subMenu como selecionado
   * Parametros: boolean selecionado
   * Retorno: void
   *********************************************/
  public void setSelecionado(boolean selecionado) {
    this.selecionado = selecionado;
    this.setStyle("-fx-background-color: " + (selecionado ? BoxMenu.COR_SELECIONADO : BoxMenu.COR_DESELECIONADO));
  }

  /*********************************************
   * Metodo: getSelecionado
   * Funcao: Verifica se o submenu esta selecionado
   * Parametros: void
   * Retorno: boolean
   *********************************************/
  public boolean getSelecionado() {
    return this.selecionado;
  }

}