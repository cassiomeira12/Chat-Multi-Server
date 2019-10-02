/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: TelaServidor
* Funcao: Controler do TelaServidor Servidor JavaFX
***********************************************************************/

package view.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import script.HoraSistema;
import socket.IReceberNet;
import socket.pdu.Apdu;
import socket.servidor.tcp.ServidorConexoesTCP;
import socket.servidor.tcp.ServidorEnviarTCP;
import socket.servidor.tcp.ServidorReceberTCP;
import socket.servidor.udp.ServidorEnviarUDP;
import socket.servidor.udp.ServidorReceberUDP;
import view.ServidorInterface;
import view.viewAlerta.Alerta;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class TelaServidor extends AnchorPane implements IReceberNet<DatagramPacket>, Apdu.IApdu, Conversa.IActions {
  private final String FXML = "view/viewServidor/telaServidor.fxml";//Caminho do FXML
  private Stage palco;
  private ServidorInterface interfacePai;

  @FXML
  private Label computadorLabel, ipLabel, sistemaOperacionalLabel, arquiteturaLabel, statusServidorLabel;
  @FXML
  private TextField portaServerTCPTextField, portaServerUDPTextField, portaClientesUDPTextField;
  @FXML
  private ImageView conexaoImageView;
  @FXML
  private Label conexaoLabel;
  @FXML
  private Label horaSistemaLabel;

  private Image conectarImage, desconectarImage;//Image de status Conectar / Desconectar

  private boolean servidorDesconectado = true;

  //--------------------------------------------

  private Map<String, ServidorReceberTCP> clientes = new HashMap<>();
  private Map<String, Conversa> conversaMap = new HashMap<>();

  private Map<String, String[]> servidoresMap = new HashMap<>();

  private int portaServidorTCP = 1604;
  private int portaServidorUDP = 1605;
  private int portaClienteUDP = 1606;

  //Conexao TCP
  private ServidorConexoesTCP conexoesTCP;
  private ServidorEnviarTCP enviarTCP;
  //Conexao UDP
  private ServidorEnviarUDP enviarUDP;
  private ServidorReceberUDP receberUDP;


  /*********************************************
  * Metodo: TelaServidor - Construtor
  * Funcao: Constroi objetos da classe TelaServidor
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public TelaServidor() {
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
    conectarImage = new Image("view/img/conectar.png");//Caminho da imagem de Conectar
    desconectarImage = new Image("view/img/desconectar.png");//Caminho da imagem de Desconectar
    atualizarInformacoes();//Atualizando informacoes do Servidor
    //conexaoImageView.disableProperty().bind(portaServerTCPTextField.textProperty().isEmpty());
    new HoraSistema(horaSistemaLabel);//Thread pra atualizar label de Hora
  }

  /*********************************************
   * Metodo: setPalco
   * Funcao: Atribui uma referencia ao Palco
   * Parametros: Stage palco
   * Retorno: void
   *********************************************/
  public void setPalco(Stage palco) {
    this.palco = palco;
  }

  /*********************************************
   * Metodo: setInterfacePai
   * Funcao: Atribui uma referencia da Interface Pai
   * Parametros: ServidorInterface interfacePai
   * Retorno: void
   *********************************************/
  public void setInterfacePai(ServidorInterface interfacePai) {
    this.interfacePai = interfacePai;
  }

  /*********************************************
  * Metodo: onClick
  * Funcao: Evento de Click
  * Parametros: void
  * Retorno: void
  *********************************************/
  @FXML
  private void onClick() {
    if (servidorDesconectado) {//Se o Servidor estiver Desconectado
      this.conectarServidor();//O Servidor Conecta
    } else {//Se o Servidor estiver Conectado
      this.desconectarServidor();//O Servidor Desconecta
    }
  }

  /*********************************************
  * Metodo: conectarServidor
  * Funcao: Metodo para iniciar a conexao do Servidor
  * Parametros: void
  * Retorno: void
  *********************************************/
  public void conectarServidor() {
    try {

      iniciarServidor();

      this.statusServidorLabel.setText("Servidor Conectado!");
      this.statusServidorLabel.setTextFill(Color.web("#127c29"));
      this.conexaoImageView.setImage(desconectarImage);
      this.conexaoLabel.setText("Desligar Servidor");
      this.servidorDesconectado = false;

    } catch (IOException ex) {
      Alerta.EXCEPTION(ex);
    }
  }

  /*********************************************
   * Metodo: iniciarServidorSocket
   * Funcao: Inicializa o socket do Servidor
   * Parametros: void
   * Retorno: void
   *********************************************/
  private void iniciarServidor() throws IOException {
    String portaServerTCP = portaServerTCPTextField.getText();
    String portaServerUDP = portaServerUDPTextField.getText();
    String portaClientesUDP = portaClientesUDPTextField.getText();

    if (portaServerTCP.isEmpty() || portaServerUDP.isEmpty() || portaClientesUDP.isEmpty()) {
      Alerta.ERRO("Dados insuficentes para Conectar no Servidor", palco, interfacePai);
      return;
    }

    this.portaServidorTCP = Integer.parseInt(portaServerTCP);
    this.portaServidorUDP = Integer.parseInt(portaServerUDP);
    this.portaClienteUDP = Integer.parseInt(portaClientesUDP);

    new Thread(() -> {

      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }

      try {
        ServerSocket serverSocket = new ServerSocket(portaServidorTCP);
        conexoesTCP = new ServidorConexoesTCP(serverSocket);
        conexoesTCP.setServidorInterface(interfacePai);

        System.out.println("TCP Local Port: " + serverSocket.getLocalPort());

        DatagramSocket socketUDP = new DatagramSocket(portaServidorUDP);
        receberUDP = new ServidorReceberUDP(socketUDP);
        receberUDP.setReceberNet(this);

        System.out.println("UDP Local Port: " + socketUDP.getLocalPort());
        System.out.println("UDP Port: " + socketUDP.getPort());

        //Mensagem BROADCAST
        ServidorEnviarUDP mensagemBroadcast = new ServidorEnviarUDP("255.255.255.255", portaServidorUDP);
        mensagemBroadcast.enviarMensagem(Apdu.NEWSERVER(portaServidorTCP));


        for (String[] servidor : servidoresMap.values()) {
          String ip = servidor[0];
          int porta = Integer.valueOf(servidor[1]);
          new ServidorEnviarUDP(ip, portaServidorUDP).enviarMensagem(Apdu.RECONECTSERVER(portaServidorUDP));
        }

        interfaceConexao(true);

      } catch (Exception ex) {
        ex.printStackTrace();
        interfaceConexao(false);
      }

    }).start();

  }

  /*********************************************
  * Metodo: desconectarServidor
  * Funcao: Metodo para fechar a conexao do Servidor
  * Parametros: void
  * Retorno: void
  *********************************************/
  public void desconectarServidor() {
    this.statusServidorLabel.setText("Servidor Desconectado!");
    this.statusServidorLabel.setTextFill(Color.web("#f80000"));
    this.conexaoImageView.setImage(conectarImage);
    this.conexaoLabel.setText("Ligar Servidor");
    this.servidorDesconectado = true;

    conexoesTCP.close();
    receberUDP.close();

    interfaceConexao(false);

    for (ServidorReceberTCP cliente : clientes.values()) {
      cliente.close();
    }

//    try {
//
//      //servidor.close();//Fecha a conexao
//      //servidor = null;
//      Interface.TELA_INICIAL.removerTodosClientes();//Remove todos os Clientes
//
//    } catch (IOException ex) {
//      ex.printStackTrace();
//    }

  }

  private void interfaceConexao(boolean conexao) {
    Platform.runLater(() -> {
      portaServerTCPTextField.setDisable(conexao);
      portaServerUDPTextField.setDisable(conexao);
      portaClientesUDPTextField.setDisable(conexao);
    });
  }

  /*********************************************
   * Metodo: receberMensagem
   * Funcao: O servidor recebe um pacote
   * Parametros: DatagramPacket pacote
   * Retorno: void
   *********************************************/
  @Override
  public void receberMensagem(DatagramPacket pacote) {
    try {
      String mensagem = new String(pacote.getData(), "UTF-8");

      String ip = pacote.getAddress().getHostAddress();

      System.out.println("[SERVIDOR] - Recebeu Mensagem UDP IP ["+ip+"] Mensagem: " + mensagem);

      Apdu apdu = new Apdu(this);
      apdu.tratarMensagem(mensagem, ip);//porta

    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
  }

  /*********************************************
   * Metodo: mensagem
   * Funcao: O servidor recebe uma mensagem de Cliente
   * Parametros: String sala, String ip, String mensagem
   * Retorno: void
   *********************************************/
  @Override
  public void mensagem(String sala, String ip, String mensagem) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa == null) {
      System.err.println("Sala nao existe");
      criarSala(sala);
      Conversa conversa2 = conversaMap.get(sala);

      Platform.runLater(() -> conversa2.receberMensagem(mensagem, ip) );
      Platform.runLater(() -> conversa2.espalharMensagem(ip, mensagem) );
      return;
    }

    Platform.runLater(() -> conversa.receberMensagem(mensagem, ip) );
    Platform.runLater(() -> conversa.espalharMensagem(ip, mensagem) );
  }

  /*********************************************
   * Metodo: criarSala
   * Funcao: Um cliente cria uma sala
   * Parametros: String sala
   * Retorno: void
   *********************************************/
  @Override
  public synchronized void criarSala(String sala) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa == null) {
      System.out.println("[SERVIDOR] - Nova Sala: " + sala);
      conversa = new Conversa();
      conversa.setNomeSala(sala);
      conversa.setActions(this);
      conversa.setNotificacao(false);
      //conversa.setConectado(false);
      conversaMap.put(sala, conversa);
      interfacePai.criarSala(conversa);
    } else {
      System.out.println("Sala " + sala + " ja existe");
    }

  }

  /*********************************************
   * Metodo: entrarSala
   * Funcao: Um cliente entra numa sala
   * Parametros: String sala, String ip
   * Retorno: void
   *********************************************/
  @Override
  public void entrarSala(String sala, String ip) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa != null) {

      if (conversa.isClienteSala(ip)) {
        System.out.println("[SERVIDOR] - Cliente ["+ip+"] ja esta na Sala ["+sala+"]");
        return;
      }

      System.out.println("[SERVIDOR] - Cliente [" + ip + "] entrou na Sala [" + sala + "]");
      ServidorEnviarUDP servidorEnviarUDP = new ServidorEnviarUDP(ip, portaClienteUDP);
      conversa.adicionarCliente(ip, servidorEnviarUDP);

      for (String[] servidor : servidoresMap.values()) {
        String ipServidor = servidor[0];
        int portaUDP = Integer.parseInt(servidor[1]) + 1;
        new ServidorEnviarUDP(ipServidor, portaUDP).enviarMensagem(Apdu.JOIN(sala, ip));
      }

    } else {
      criarSala(sala);
      conversa = conversaMap.get(sala);
      ServidorEnviarUDP servidorEnviarUDP = new ServidorEnviarUDP(ip, portaClienteUDP);
      conversa.adicionarCliente(ip, servidorEnviarUDP);
    }

  }

  /*********************************************
   * Metodo: sairSala
   * Funcao: Um cliente sai da sala
   * Parametros: String sala, String ip
   * Retorno: void
   *********************************************/
  @Override
  public void sairSala(String sala, String ip) {
    Conversa conversa = conversaMap.get(sala);

    if (conversa != null) {
      conversa.removerCliente(ip);

      for (String[] servidor : servidoresMap.values()) {
        String ipServidor = servidor[0];
        int portaServidor = Integer.parseInt(servidor[0]);
        new ServidorEnviarUDP(ipServidor, portaServidor).enviarMensagem(Apdu.LEAVE(sala, ip));
      }

      if (conversa.getClientesSize() == 0) {
        conversaMap.remove(sala);
        interfacePai.removerSala(conversa);
      }

    }

  }

  @Override
  public void novoServidor(String ip, String porta) {
    if (servidoresMap.get(ip) == null) {

      int portaServer = portaServidorUDP;

      //Enviando Novo Servidor para Todos os Servidotes
      for (String[] servidor : servidoresMap.values()) {
        String ipServidor = servidor[0];
        int portaServidor = Integer.parseInt(servidor[1]);
        ServidorEnviarUDP enviarUDP = new ServidorEnviarUDP(ip, portaServer);
        enviarUDP.enviarMensagem(Apdu.NEWSERVER(ipServidor, portaServidor));
        for (Conversa conversa : conversaMap.values()) {
          for (String ipC : conversa.getClientesList()) {
            enviarUDP.enviarMensagem(Apdu.JOIN(conversa.getNomeSala(), ipC));
          }
        }
        System.out.println("[SERVIDOR] - Enviando Novo Servidor ["+ipServidor+"] para Servidor ["+ip+"]");
      }

      //Enviando Novo Servidor para Todos os Clientes
      for (ServidorReceberTCP cliente : clientes.values()) {
        String ipCliente = cliente.getIP();
        new ServidorEnviarUDP(ipCliente, portaClienteUDP).enviarMensagem(Apdu.NEWSERVER(ip, Integer.valueOf(porta)));
        System.out.println("[SERVIDOR] - Enviando Novo Servidor ["+ip+"] para Cliente ["+ipCliente+"]");
      }

      String[] novoServidor = {ip, porta};
      servidoresMap.put(ip, novoServidor);
      System.out.println("[SERVIDOR] - Novo servidor adicionado: ["+ip+ "] ["+porta+"]");

    } else {
      System.out.println("[SERVIDOR] - Servidor já exite na Lista: ["+ip+"] ["+porta+"]");
    }

  }

  @Override
  public void reconectarServidor(String ip, String porta) {
    System.out.println("[SERVIDOR] - Reconectando ["+ip+"] porta ["+porta+"]");
    //Enviando Novo Servidor para Todos os Servidotes
    for (String[] servidor : servidoresMap.values()) {
      String ipServidor = servidor[0];
      int portaServidor = Integer.parseInt(servidor[1]);
      ServidorEnviarUDP enviarUDP = new ServidorEnviarUDP(ip, portaServidorUDP);
      enviarUDP.enviarMensagem(Apdu.NEWSERVER(ipServidor, portaServidor));
      for (Conversa conversa : conversaMap.values()) {
        for (String ipC : conversa.getClientesList()) {
          enviarUDP.enviarMensagem(Apdu.JOIN(conversa.getNomeSala(), ipC));
        }
      }
      System.out.println("[SERVIDOR] - Enviando Novo Servidor ["+ipServidor+"] para Servidor ["+ip+"]");
    }

    //Enviando Novo Servidor para Todos os Clientes
    for (ServidorReceberTCP cliente : clientes.values()) {
      String ipCliente = cliente.getIP();
      new ServidorEnviarUDP(ipCliente, portaClienteUDP).enviarMensagem(Apdu.NEWSERVER(ip, Integer.valueOf(porta)));
      System.out.println("[SERVIDOR] - Enviando Novo Servidor ["+ip+"] para Cliente ["+ipCliente+"]");
    }
  }

  /*********************************************
   * Metodo: sairConversa
   * Funcao: Essa funcao no Servidor nao executa nada
   * Parametros: String sala
   * Retorno: void
   *********************************************/
  @Override
  public void sairConversa(String sala) {
    System.out.println("Botao sair no Servidor nao faz nada");
  }

  /*********************************************
   * Metodo: adicionarCliente
   * Funcao: Adiciona a conexao de um cliente
   * Parametros: ServidorReceberTCP cliente
   * Retorno: void
   *********************************************/
  public void adicionarCliente(ServidorReceberTCP cliente) {
    clientes.put(cliente.getIP(), cliente);

    String ipCliente = cliente.getIP();
    int portaCliente = portaClienteUDP;

    //cliente.
    for (String[] servidor : servidoresMap.values()) {
      String ipServidor = servidor[0];
      int portaServidor = Integer.parseInt(servidor[1]);
      new ServidorEnviarUDP(ipCliente, portaCliente).enviarMensagem(Apdu.NEWSERVER(ipServidor, portaServidor));
      System.out.println("[SERVIDOR] - Enviando para Cliente "+ipCliente+" "+portaCliente+" ipServidor " + ipServidor);
    }

  }

  /*********************************************
  * Metodo: atualizarInformacoes
  * Funcao: Atualiza as informacoes do Servidor na Interface
  * Parametros: void
  * Retorno: void
  *********************************************/
  private void atualizarInformacoes() {
    try {
      InetAddress address = InetAddress.getLocalHost();
      //System.out.println("Using InetAddress");
      //System.out.println("Host Address: "+ address.getHostAddress());
      this.ipLabel.setText(address.getHostAddress());
      //System.out.println("Host Name: "+ address.getHostName());
      this.computadorLabel.setText(address.getHostName());
      //System.out.println("CanonicalHostName: "+ address.getCanonicalHostName());
      //System.out.println("Address: "+ address.getAddress());
      //System.out.println("LocalHost: "+ address.getLocalHost());
      //System.out.println("LoopbackAddress: "+ address.getLoopbackAddress());

      String os = "os.name";
      String version = "os.version";
      String arch = "os.arch";
      //System.out.println("Name of the OS: "+ System.getProperty(os));
      this.sistemaOperacionalLabel.setText(System.getProperty(os));
      //System.out.println("Version of the OS: "+ System.getProperty(version));
      //System.out.println("Architecture of the OS: "+ System.getProperty(arch));
      this.arquiteturaLabel.setText(System.getProperty(arch));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}//Fim class