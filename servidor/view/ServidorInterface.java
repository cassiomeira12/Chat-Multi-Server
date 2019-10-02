/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 02/12/18
 * Ultima alteracao: 15/12/18
 * Nome: ServidorInterface
 * Funcao: Tela da Interface do Servidor
 ***********************************************************************/

package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import socket.servidor.tcp.ServidorReceberTCP;
import view.controller.Conversa;
import view.controller.TelaServidor;
import view.controller.MenuSuperior;
import view.menuLateral.BoxMenu;
import view.menuLateral.MenuLateral;
import view.menuLateral.SubMenu;

import java.io.IOException;

public class ServidorInterface extends BorderPane implements MenuLateral.TelaMenu {
  private final String FXML = "view/telaPrincipal.fxml";//Caminho do FXML
  private String titulo = "Servidor Chat";
  private Stage palco;

  private MenuSuperior menuSuperior;//Barra de Menu Superior
  private MenuLateral menuLateral;//Menu Lateral

  private TelaServidor telaServidor;//Tela TelaServidor do Servidor

  /*********************************************
   * Metodo: ServidorInterface - Construtor
   * Funcao: Constroi objetos da classe ServidorInterface
   * Parametros: Stage PALCO
   * Retorno: void
   *********************************************/
  public ServidorInterface(Stage palco) {
    this.palco = palco;
    try {
      this.palco.setTitle(titulo);
      //Cria o carregador de arquivos FXML
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(FXML));
      fxmlLoader.setRoot(this);//Atribui essa classe como o Root da View
      fxmlLoader.setController(this);//Atribui essa classe como o Controller da View
      fxmlLoader.load();//Carrega a View FXML
    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println("aqui");
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
    this.menuSuperior = new MenuSuperior();//Instanciando MenuSuperior
    this.setTop(menuSuperior);//Adicionando na Interface

    this.menuLateral = new MenuLateral(this);//Instanciando MenuLateral
    this.setLeft(menuLateral);//Adicionando na Interface

    this.telaServidor = new TelaServidor();//Instanciando tela do TelaServidor
    this.telaServidor.setPalco(palco);
    this.telaServidor.setInterfacePai(this);
    this.setCenter(telaServidor);//Adicionando na Interface

    BoxMenu menu = new BoxMenu("Principal", menuLateral);
    menuLateral.adicionarMenu(menu);
    SubMenu subMenu = new SubMenu("Servidor", menu) {
      @Override
      public void onClick() {
        setCenter(telaServidor);
      }
    };
    menu.onClick();
  }

  /*********************************************
   * Metodo: adicionarCliente
   * Funcao: Adiciona um novo TelaCliente
   * Parametros: ServidorReceberTCP telaCliente
   * Retorno: void
   *********************************************/
  public void criarSala(Conversa conversa) {
    menuLateral.adicionarMenu(conversa);
  }

  /*********************************************
   * Metodo: removerSala
   * Funcao: Remove uma sala do MenuLateral
   * Parametros: Conversa conversa
   * Retorno: void
   *********************************************/
  public void removerSala(Conversa conversa) {
    menuLateral.removerMenu(conversa);
  }

  /*********************************************
   * Metodo: adicionarCliente
   * Funcao: Adiciona um novo Cliente conectado
   * Parametros: ServidorReceberTCP cliente
   * Retorno: void
   *********************************************/
  public void adicionarCliente(ServidorReceberTCP cliente) {
    telaServidor.adicionarCliente(cliente);
  }

  /*********************************************
   * Metodo: removerCliente
   * Funcao: Remove o telaCliente da Interface
   * Parametros: ServidorReceberTCP telaCliente
   * Retorno: void
   *********************************************/
  public void removerCliente(ServidorReceberTCP cliente) {
    menuLateral.removerMenu(cliente);
  }

  /*********************************************
   * Metodo: removerTodosClientes
   * Funcao: Remove todos os Clientes da Interface
   * Parametros: void
   * Retorno: void
   *********************************************/
  public void removerTodosClientes() {
    menuLateral.removerMenuTodos();
  }

  /*********************************************
   * Metodo: getTelaServidor
   * Funcao: Retorna uma referencia da tela do Servidor
   * Parametros: void
   * Retorno: TelaServidor
   *********************************************/
  public TelaServidor getTelaServidor() {
    return telaServidor;
  }

  /*********************************************
   * Metodo: setTela
   * Funcao: Atribui uma tela ao centro da interface
   * Parametros: Node node
   * Retorno: void
   *********************************************/
  @Override
  public void setTela(Node node) {
    this.setCenter(node);
  }

}
