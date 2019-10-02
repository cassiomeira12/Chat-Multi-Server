/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: BoxMenu
* Funcao: Controler do BoxMenu do JavaFX
***********************************************************************/

package view.menuLateral;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BoxMenu extends VBox {
  private final String FXML = "view/menuLateral/boxMenu.fxml";//Caminho do FXML

  private MenuLateral menuLateral;//MenuLateral que contem esse BoxMenu
  private boolean selecionado = false;

  public static String COR_SELECIONADO = "#ffff";
  public static String COR_DESELECIONADO = "#a4b0be";
  public static String COR_HOVER = "#dfe6e9";

  private SubMenu subMenuSelecionado;
  public ObservableList<SubMenu> subMenuList;

  @FXML
  private HBox menuHBox;
  @FXML
  private Label nomeLabel;//Label do nome
  @FXML
  private ImageView iconeImageView;
  @FXML
  private VBox subMenuBox;


  /*********************************************
  * Metodo: BoxMenu - Construtor
  * Funcao: Constroi objetos da classe BoxMenu
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public BoxMenu(String nome, MenuLateral menuLateral) {
    try {
      //Cria o carregador de arquivos FXML
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(FXML));
      fxmlLoader.setRoot(this);//Atribui essa classe como o Root da View
      fxmlLoader.setController(this);//Atribui essa classe como o Controller da View
      fxmlLoader.load();//Carrega a View FXML
      this.nomeLabel.setText(nome);
      this.menuLateral = menuLateral;
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
    this.subMenuList = FXCollections.observableArrayList();
    menuHBox.setStyle("-fx-background-color: " + COR_DESELECIONADO);
    this.subMenuSelecionado = null;

    //Evento de Clicar com o Mouse
    this.setOnMouseClicked((Event) -> {
      if (!selecionado) {
        onClick();
      } else {
        Event.consume();
      }
    });
    //Evento ao passar o Mouse por cima do SubMenu
    this.setOnMouseEntered((Event) -> {
      if (!selecionado) {//Quando o Menu nao estiver selecionado
        menuHBox.setStyle("-fx-background-color: " + COR_HOVER);//Alterar a cor para Semi-selecionado
      }
    });
    //Evento ao passar o Mouse por cima do SubMenu
    this.setOnMouseExited((Event) -> {
      if (!selecionado) {//Quando o Menu nao estiver selecionado
        menuHBox.setStyle("-fx-background-color: " + COR_DESELECIONADO);//Altera a cor para deselecionado
      }
    });

  }

  /*********************************************
   * Metodo: onClick
   * Funcao: Acao de Click no Menu
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void onClick() {
    menuLateral.selecionarMenu(this);
  }

  /*********************************************
   * Metodo: subMenuSelecionado
   * Funcao: Atribui um SubMenu como selecionado
   * Parametros: SubMenu subMenu
   * Retorno: void
   *********************************************/
  public void subMenuSelecionado(SubMenu subMenu) {
    if (subMenu == null) {
      setSelecionado(false);
    } else {

      if (subMenuSelecionado != null) {
        subMenuSelecionado.setSelecionado(false);
      }

      this.subMenuSelecionado = subMenu;
      this.subMenuSelecionado.setSelecionado(true);
    }
  }

  /*********************************************
   * Metodo: setSelecionado
   * Funcao: Atribui esse MenuBox como selecionado
   * Parametros: boolean selecionado
   * Retorno: void
   *********************************************/
  public void setSelecionado(boolean selecionado) {
    this.selecionado = selecionado;
    Platform.runLater(() -> {
      this.menuHBox.setStyle("-fx-background-color: " + (selecionado ? COR_SELECIONADO : COR_DESELECIONADO));
      if (selecionado) {
        adicionarSubMenu();
      } else {
        removerSubMenus();
      }
    });
  }

  /*********************************************
  * Metodo: setNome
  * Funcao: Altera a Label de Nome do TelaCliente
  * Parametros: String nome
  * Retorno: void
  *********************************************/
  public void setNome(String nome) {
    this.nomeLabel.setText(nome);
  }

  /*********************************************
   * Metodo: setSubMenu
   * Funcao: Adiciona um novo SubMenu
   * Parametros: String nome
   * Retorno: void
   *********************************************/
  public void addSubMenu(SubMenu... subMenus) {
    this.subMenuList.addAll(subMenus);
  }

  /*********************************************
   * Metodo: removeSubMenu
   * Funcao: Remove um subMenu
   * Parametros: SubMenu subMenu
   * Retorno: void
   *********************************************/
  public void removeSubMenu(SubMenu subMenu) {

  }

  /*********************************************
   * Metodo: adicionarSubMenu
   * Funcao: Adiciona um novo SubMenu
   * Parametros: void
   * Retorno: void
   *********************************************/
  private void adicionarSubMenu() {
    if (subMenuList.isEmpty()) {
      System.out.println("SubMenu vazio - Adicionar");
      return;
    }
    subMenuBox.getChildren().addAll(subMenuList);//Adicionando os SubMenus
    SubMenu primeiroSubMenu = (SubMenu) subMenuBox.getChildren().get(0);
    primeiroSubMenu.setSelecionado(true);
    primeiroSubMenu.onClick();
    subMenuSelecionado = primeiroSubMenu;
  }

  /*********************************************
   * Metodo: removeSubMenu
   * Funcao: Remove um subMenu
   * Parametros: void
   * Retorno: void
   *********************************************/
  private void removerSubMenus() {
    if (subMenuList.isEmpty()) {
      System.out.println("SubMenu vazio - Remover");
      return;
    }
    subMenuList.forEach((subMenu -> subMenu.setSelecionado(false)));
    subMenuBox.getChildren().clear();
    subMenuSelecionado = null;
    //interfaceConversa.getConversa().setConversaAberta(false);
  }

}//Fim class