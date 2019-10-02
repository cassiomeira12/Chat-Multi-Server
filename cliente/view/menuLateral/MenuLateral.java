/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: MenuLateral
* Funcao: Controler do MenuLateral do JavaFX
***********************************************************************/

package view.menuLateral;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import socket.servidor.tcp.ServidorReceberTCP;
import view.controller.Conversa;

import java.io.IOException;

public class MenuLateral extends VBox {
  private final static String FXML = "view/menuLateral/menuLateral.fxml";//Caminho do FXML

  private TelaMenu telaMenu;

  @FXML
  private Label nomeLabel;
  @FXML
  private VBox menusVBox;

  private BoxMenu menuSelecionado = null;//Menu selecionado pelo usuario

  /*********************************************
  * Metodo: MenuLateral - Construtor
  * Funcao: Constroi objetos da classe MenuLateral
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public MenuLateral(TelaMenu telaMenu) {
    this.telaMenu = telaMenu;
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
  public void initialize() throws IOException {
//    SubMenu sub1 = new SubMenu("Sub1", servidor) {
//      @Override
//      public void onClick() {
//        interfacePai.setCenter(TelaInicial.telaServidorServidor);
//      }
//    };
  }

  /*********************************************
   * Metodo: adicionarMenu
   * Funcao: Adiciona MenuBox no MenuLateral
   * Parametros: BoxMenu boxMenus
   * Retorno: void
   *********************************************/
  public void adicionarMenu(BoxMenu... boxMenus) {
    Platform.runLater(() -> menusVBox.getChildren().addAll(boxMenus) );
  }

  /*********************************************
  * Metodo: adicionarMenu
  * Funcao: Adiciona um novo Menu na Interface do MenuLateral
  * Parametros: ServidroReceber telaCliente
  * Retorno: void
  *********************************************/
  public void adicionarMenu(ServidorReceberTCP cliente) {
//    ConversaServidor conversaServidor = new ConversaServidor();
//    Conversa conversa = new Conversa();//Cria um nova Conversa
//    //conversa.setCliente(cliente.getSocket());//Adiciona o socket do TelaCliente
//    conversaServidor.setConversa(conversa);
//    cliente.setInterfaceConversa(conversaServidor);
//    cliente.setIndex(menusVBox.getChildren().size());//Atribui o indice o Menu
//    //cliente.start();
//
//    BoxMenu menu = new BoxMenu(cliente.getIP(), this);
//    this.adicionarMenu(menu);
//    SubMenu subMenu = new SubMenu("Conversa", menu) {
//      @Override
//      public void onClick() {
//        telaMenu.setTela(conversaServidor);
//      }
//    };

  }

  /*********************************************
   * Metodo: adicionarMenu
   * Funcao: Adiciona um novo Menu com Conversa
   * Parametros: Conversa conversa
   * Retorno: void
   *********************************************/
  public void adicionarMenu(Conversa conversa) {
    conversa.setIndexMenu(menusVBox.getChildren().size());
    BoxMenu menu = new BoxMenu(conversa.getNomeSala(), this);
    this.adicionarMenu(menu);
    SubMenu subMenu = new SubMenu("Conversa", menu) {
      @Override
      public void onClick() {
        telaMenu.setTela(conversa);
      }
    };
    conversa.setSubMenu(subMenu);
  }

  //public void adicionarMenu(Socket socket) {
//    ServidorEnviarTCP servidorEnviarTCP = new ServidorEnviarTCP(socket);
//
//    ServidorReceberTCP servidorReceberTCP = new ServidorReceberTCP(socket);
//    //servidorReceberTCP.setSocketServer(TelaInicial.telaServidorServidor.getServidor());
//
//    //ConversaServidor conversaServidor = new ConversaServidor();
//
//    Conversa interfaceConversa = new Conversa();
//    //interfaceConversa.setCliente(socket);
//    conversaServidor.setConversa(interfaceConversa);
//
//    interfaceConversa.setEnviarNet(servidorEnviarTCP);
//
//    servidorReceberTCP.setInterfaceConversa(conversaServidor);
//    servidorReceberTCP.setIndex(menusVBox.getChildren().size());
//    servidorReceberTCP.start();
//
//    TelaInicial.telaServidorServidor.adicionarCliente(servidorReceberTCP);
//
//    BoxMenu menu = new BoxMenu(servidorReceberTCP.getIP(), this);
//    this.adicionarMenu(menu);
//    SubMenu subMenu = new SubMenu("Conversa", menu) {
//      @Override
//      public void onClick() {
//        telaMenu.setTela(conversaServidor);
//      }
//    };
  //}

  /*********************************************
  * Metodo: selecionarMenu
  * Funcao: Atribui o menu como selecionado
  * Parametros: BoxMenu boxMenu
  * Retorno: void
  *********************************************/
  public void selecionarMenu(BoxMenu boxMenu) {
    if (menuSelecionado == null) {
      menuSelecionado = boxMenu;
      menuSelecionado.setSelecionado(true);
    } else {
      menuSelecionado.setSelecionado(false);
      menuSelecionado = boxMenu;
      menuSelecionado.setSelecionado(true);
    }
  }

  /*********************************************
  * Metodo: removerMenu
  * Funcao: Remove um Menu na Interface do MenuLateral
  * Parametros: ServidorReceberTCP telaCliente
  * Retorno: void
  *********************************************/
  public void removerMenu(ServidorReceberTCP cliente) {
    Platform.runLater(() -> menusVBox.getChildren().remove(cliente.getIndex()) );
  }

  /*********************************************
   * Metodo: removerMenu
   * Funcao: Remove um Menu na Interface do MenuLateral
   * Parametros: Conversa conversa
   * Retorno: void
   *********************************************/
  public void removerMenu(Conversa conversa) {
    Platform.runLater(() -> menusVBox.getChildren().remove(conversa.getIndexMenu()) );
  }

  /*********************************************
  * Metodo: removerMenuTodos
  * Funcao: Remove todos os Menus, menos do Servidor
  * Parametros: void
  * Retorno: void
  *********************************************/
  public void removerMenuTodos() {
    while (menusVBox.getChildren().size() > 1) {
      BoxMenu menu = (BoxMenu) menusVBox.getChildren().get(1);
      //menu.getInterfaceConversa().getConversa().setConectado(false);
    }
  }

  /*********************************************
   * Metodo: TelaMenu
   * Funcao: Interface com os metodos pra atribuir a tela
   *********************************************/
  public interface TelaMenu {
    public void setTela(Node node);
  }

}//Fim class