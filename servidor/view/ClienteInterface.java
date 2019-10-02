/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 02/12/18
 * Ultima alteracao: 14/12/18
 * Nome: ClienteInterface
 * Funcao: Tela da interface do Cliente
 ***********************************************************************/

package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import socket.IReceberNet;
import socket.cliente.tcp.ConexaoServidorThread;
import socket.pdu.Apdu;
import socket.servidor.udp.ServidorEnviarUDP;
import view.controller.Conversa;
import view.controller.MenuSuperior;
import view.controller.TelaCliente;
import view.menuLateral.BoxMenu;
import view.menuLateral.MenuLateral;
import view.menuLateral.SubMenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClienteInterface extends BorderPane implements IReceberNet<DatagramPacket>, MenuLateral.TelaMenu, Apdu.IApdu, Conversa.IActions, ConexaoServidorThread.INovoServidor {
  private final String FXML = "view/telaPrincipal.fxml";//Caminho do FXML
  private final String titulo = "Cliente Chat";
  private Stage palco;

  private MenuSuperior menuSuperior;//Barra de Menu Superior
  private MenuLateral menuLateral;//Menu Lateral

  private BoxMenu menuPrincipal;

  private TelaCliente telaCliente;//Tela do Cliente

  private Map<String, Conversa> conversaMap = new HashMap<>();

  private Map<String, String[]> servidoresMap = new HashMap<>();

  /*********************************************
   * Metodo: ClienteInterface - Construtor
   * Funcao: Constroi objetos da classe ClienteInterface
   * Parametros: void
   * Retorno: void
   *********************************************/
  public ClienteInterface(Stage palco) {
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

    this.telaCliente = new TelaCliente();
    this.telaCliente.setPalco(palco);
    this.telaCliente.setInterfacePai(this);
    this.setCenter(telaCliente);

    menuPrincipal = new BoxMenu("Principal", menuLateral);
    menuLateral.adicionarMenu(menuPrincipal);
    SubMenu subMenu = new SubMenu("Cliente", menuPrincipal) {
      @Override
      public void onClick() {
        setCenter(telaCliente);
      }
    };
    menuPrincipal.onClick();
  }

  /*********************************************
   * Metodo: adicionarSala
   * Funcao: Adiciona uma nova sala no MenuLateral
   * Parametros: String sala
   * Retorno: void
   *********************************************/
  public void adicionarSala(String sala) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa == null) {
      criarSala(sala);
      adicionarSala(sala);
    } else {

      if (conversa.getIndexMenu() == -1) {
        conversa.mensagemCentral("Voce entrou no Grupo");
        conversa.setEstouNaSala(true);
      }

      if (conversa.getMeuIP() == null && conversa.getClientesList().size() != 0) {
        for (ServidorEnviarUDP enviar : conversa.getClienteMap().values()) {
          String meuIP = enviar.getSocket().getLocalAddress().getHostAddress();
          conversa.setMeuIP(meuIP);
          conversa.adicionarCliente(meuIP);
        }
      }

      menuLateral.adicionarMenu(conversa);
    }
  }

  /*********************************************
   * Metodo: receberMensagem
   * Funcao: Recebe um pacote via Socket
   * Parametros: DatagramPacket pacote
   * Retorno: void
   *********************************************/
  @Override
  public void receberMensagem(DatagramPacket pacote) {
    String mensagem = new String(pacote.getData());
    System.out.println("[CLIENTE] - Recbeu Mensagem UDP IP ["+pacote.getAddress().getHostAddress() +"] Mensagem: " + mensagem);
    Apdu apdu = new Apdu(this);
    apdu.tratarMensagem(mensagem, pacote.getAddress().getHostAddress());
  }

  /*********************************************
   * Metodo: setTela
   * Funcao: Adiciona uma nova interface no Centro
   * Parametros: Node node
   * Retorno: void
   *********************************************/
  @Override
  public void setTela(Node node) {
    this.setCenter(node);
  }

  /*********************************************
   * Metodo: mensagem
   * Funcao: Recebe uma nova Mensagem de uma sala
   * Parametros: String sala, String ip, String mensagem
   * Retorno: void
   *********************************************/
  @Override
  public void mensagem(String sala, String ip, String mensagem) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa != null) {
      conversa.receberMensagem(mensagem, ip);
    }

  }

  /*********************************************
   * Metodo: criarSala
   * Funcao: Recebe uma mensagem pra criar uma sala
   * Parametros: String sala
   * Retorno: void
   *********************************************/
  @Override
  public void criarSala(String sala) {
    Conversa conversa = conversaMap.get(sala);
    if (conversa == null) {
      conversa = new Conversa();
      conversa.setNomeSala(sala);
      conversa.setEnviarNet(telaCliente.getClienteEnviarUDP());
      conversa.setActions(this);
      conversa.setNumeroMembros(1);
      conversaMap.put(sala, conversa);
    }
  }

  /*********************************************
   * Metodo: entrarSala
   * Funcao: Recebe uma mensagem pra entrar numa sala
   * Parametros: String sala, String ip
   * Retorno: void
   *********************************************/
  @Override
  public synchronized void entrarSala(String sala, String ip) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa != null && !conversa.isClienteSala(ip)) {
      System.out.println("--------------------------------------");
      System.out.println("Cliente " + ip + " quer entrar na Sala " + sala);
      System.out.println("--------------------------------------");

      conversa.adicionarCliente(ip);
    }
  }

  /*********************************************
   * Metodo: sairSala
   * Funcao: Recebe uma mensagem que alguem saiu da sala
   * Parametros: String sala, String iṕ
   * Retorno: void
   *********************************************/
  @Override
  public void sairSala(String sala, String ip) {
    Conversa conversa = conversaMap.get(sala);
    if (conversa != null) {
      conversa.mensagemCentral(ip  + " saiu no Grupo");
    }
  }

  @Override
  public void novoServidor(String ip, String porta) {
    if (servidoresMap.get(ip) == null) {
      String[] novoServidor = {ip, porta};
      servidoresMap.put(ip, novoServidor);
      System.out.println("[CLIENTE] - Novo servidor adicionado: " +ip+ " " + porta);
    } else {
      System.out.println("[CLIENTE] - Servidor já exite: " +ip+ " " +porta);
    }
  }

  @Override
  public void reconectarServidor(String ip, String porta) {

  }

  /*********************************************
   * Metodo: sairConversa
   * Funcao: Recebe uma mensagem pra sair da sala
   * Parametros: String sala
   * Retorno: void
   *********************************************/
  @Override
  public void sairConversa(String sala) {
    Conversa conversa = conversaMap.get(sala);
    if (conversa != null) {
      menuLateral.removerMenu(conversa);
    }
    menuPrincipal.onClick();
  }

  public void sairTodasConversas() {
    for (Conversa conversa : conversaMap.values()) {
      conversa.sairConversa();
      //menuLateral.removerMenu(conversa);
    }
    menuLateral.removerMenuTodos();
    conversaMap.clear();
    menuPrincipal.onClick();
  }

  @Override
  public void buscarServidor() {
    Socket novoSocket = null;

    for (String[] servidor : servidoresMap.values()) {
      String ip = servidor[0];
      int porta = Integer.valueOf(servidor[1]);
      System.out.println("\nTestando um Novo Servidor: " + ip + " " + porta);
      try {
        novoSocket = new Socket(ip, porta);
        System.out.println("Conectou no novo Servidor\n");
        telaCliente.setNovoServidor(novoSocket, ip);
        return;
      } catch (Exception ex) {
        System.out.println("Não conseguiu conectar no servidor\n");
      }
    }

    telaCliente.desconectar();
  }

}
