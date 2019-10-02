/***********************************************************************
* Autor: Cassio Meira Silva
* Matricula: 201610373
* Inicio: 24/08/18
* Ultima alteracao: 14/12/18
* Nome: Conversa
* Funcao: Controler da Conversa do JavaFX
***********************************************************************/

package view.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.AudioClip;
import socket.IEnviarNet;
import socket.IReceberNet;
import socket.pdu.Apdu;
import socket.servidor.udp.ServidorEnviarUDP;
import view.menuLateral.SubMenu;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conversa extends BorderPane implements IReceberNet<DatagramPacket> {
  private final String FXML = "view/conversa.fxml";//Caminho do FXML

  private int indexMenu = -1;
  private String nomeSala = "Sala";

  private AudioClip clip;

  private IEnviarNet enviarNet = null;
  private IActions actions = null;

  private SubMenu subMenu;

  private int quantidadeClientes = 0;
  private List<String> clientesList = new ArrayList<>();
  private Map<String, ServidorEnviarUDP> clienteMap = new HashMap<>();

  private boolean estouNaSala = false;
  private String meuIP = null;

  @FXML
  private Label salaLabel;
  @FXML
  private Label membrosLabel;
  @FXML
  private CheckBox notificacaoCheckBox;
  @FXML
  private CheckBox somCheckBox;
  @FXML
  private MenuButton menuButton;

  @FXML
  private FlowPane mensagensVBox;
  @FXML
  private ScrollPane scrollPane;
  @FXML
  private TextField mensagemText;
  @FXML
  private ImageView enviarImage;

  private Image enviarSemTextoImage, enviarComTextoImage, enviarSelecionadoImage;

  /*********************************************
  * Metodo: Conversa - Construtor
  * Funcao: Constroi objetos da classe Conversa
  * Parametros: Stage PALCO
  * Retorno: void
  *********************************************/
  public Conversa() {
    String audioNovaMensagem = getClass().getClassLoader().getResource("view/media/nova_mensagem.mp3").toString();
    clip = new AudioClip(audioNovaMensagem);
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
    enviarSemTextoImage = new Image("view/img/enviar1.png");
    enviarComTextoImage = new Image("view/img/enviar2.png");
    enviarSelecionadoImage = new Image("view/img/enviar3.png");
    membrosLabel.setVisible(false);

    mensagensVBox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));

    mensagemText.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        enviarImage.setImage( newValue.isEmpty() ? enviarSemTextoImage : enviarComTextoImage );
      }
    });

    mensagemText.setOnKeyPressed((Key) -> {
      if (Key.getCode() == KeyCode.ENTER) {
        enviar();
      }
    });

    enviarImage.focusedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        enviarImage.setImage( newValue ? enviarSelecionadoImage : enviarComTextoImage );
      }
    });

  }

  /*********************************************
  * Metodo: enviar
  * Funcao: Evento de enviar Mensagem ao TelaCliente
  * Parametros: void
  * Retorno: void
  *********************************************/
  @FXML
  private void enviar() {
    String mensagem = mensagemText.getText();//Mensagem a interface

    if (mensagem.trim().isEmpty()) {
      mensagemText.requestFocus();
      return;
    }

    if (enviarNet == null) {
      return;
    }

    enviarNet.enviarMensagem(Apdu.SEND(nomeSala, mensagem));
    this.enviarMensagem(mensagem);//Mostrar mensagem enviada na Interface
  }

  /*********************************************
  * Metodo: mensagem
  * Funcao: Mostra na interface a mensagem enviada
  * Parametros: String mensagem
  * Retorno: void
  *********************************************/
  public void enviarMensagem(String mensagem) {
    Mensagem msg = new Mensagem();//Novo objeto de Mensagem

    msg.setMinWidth(mensagensVBox.getWidth());
    mensagensVBox.widthProperty().addListener((observable, oldValue, newValue) -> {
      msg.setMinWidth((Double) newValue);
    });

    msg.mensagemEnviada(mensagem);//Adiciona o texto da Mensagem

    mensagensVBox.getChildren().add(msg);//Adiciona a mensagem na conversa
    mensagemText.clear();//Limpa o campo de mensagem
    mensagemText.requestFocus();
  }

  /*********************************************
  * Metodo: receberMensagem
  * Funcao: Mostra na interface a mensagem recebida
  * Parametros: String mensagem
  * Retorno: void
  *********************************************/
  @Override
  public void receberMensagem(DatagramPacket pacote) {
    String mensagem = new String(pacote.getData());
    String remetente = pacote.getAddress().getHostAddress();

    Mensagem msg = new Mensagem();//Novo objeto de Mensagem

    msg.setMinWidth(mensagensVBox.getWidth());
    mensagensVBox.widthProperty().addListener((observable, oldValue, newValue) -> {
      msg.setMinWidth((Double) newValue);
    });
    msg.mensagemRecebida(mensagem, remetente);//Adiciona o texto da Mensagem

    //clip.play();

    Platform.runLater(() -> {
      mensagensVBox.getChildren().add(msg);//Adiciona a mensagem na conversa
    });
  }

  public void receberMensagem(String mensagem, String remetente) {
    Mensagem msg = new Mensagem();//Novo objeto de Mensagem

    msg.setMinWidth(mensagensVBox.getWidth());
    mensagensVBox.widthProperty().addListener((observable, oldValue, newValue) -> {
      msg.setMinWidth((Double) newValue);
    });
    msg.mensagemRecebida(mensagem, remetente);//Adiciona o texto da Mensagem

//    if (notificacaoCheckBox.isSelected()) {
//      Platform.runLater(() -> {
//        Notificacao notificacao = new Notificacao(Notifications.MENSAGEM);
//        notificacao.show(nomeSala, "Nova mensagem!");
//      });
//    }

    if (clip != null && somCheckBox.isSelected()) {
      clip.play();
    }

    Platform.runLater(() -> {
      mensagensVBox.getChildren().add(msg);//Adiciona a mensagem na conversa
    });
  }

  /*********************************************
   * Metodo: mensagemCentral
   * Funcao: Adiciona uma mensagem no centro da Conversa
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  public void mensagemCentral(String mensagem) {
    Mensagem msg = new Mensagem();//Novo objeto de Mensagem

    msg.setMinWidth(mensagensVBox.getWidth());
    mensagensVBox.widthProperty().addListener((observable, oldValue, newValue) -> {
      msg.setMinWidth((Double) newValue);
    });

    msg.mensagemCentral(mensagem);//Adiciona o texto da Mensagem

    Platform.runLater(() -> {
      mensagensVBox.getChildren().add(msg);//Adiciona a mensagem na conversa
    });

  }

  /*********************************************
   * Metodo: sairConversa
   * Funcao: Sai da conversa
   * Parametros: void
   * Retorno: void
   *********************************************/
  @FXML
  public void sairConversa() {
    if (enviarNet != null) {
      enviarNet.enviarMensagem(Apdu.LEAVE(nomeSala));
    }
    this.actions.sairConversa(nomeSala);
  }

  /*********************************************
   * Metodo: setConectado
   * Funcao: Atribui se a conexao esta ativa ou nao
   * Parametros: boolean conectado
   * Retorno: void
   *********************************************/
  public void setConectado(boolean conectado) {
    Platform.runLater(() -> setDisable(!conectado) );
  }

  /*********************************************
   * Metodo: setEnviarNet
   * Funcao: Atribui uma referencia a classe que implementa os metodos de enviar
   * Parametros: IEnviarNet enviarNet
   * Retorno: void
   *********************************************/
  public void setEnviarNet(IEnviarNet enviarNet) {
    this.enviarNet = enviarNet;
  }

  /*********************************************
   * Metodo: setActions
   * Funcao: Atribui uma referencia a classe que implementa acoes na interface
   * Parametros: IActions actions
   * Retorno: void
   *********************************************/
  public void setActions(IActions actions) {
    this.actions = actions;
  }

  /*********************************************
   * Metodo: setNomeSala
   * Funcao: Atribui o nome da Sala
   * Parametros: String sala
   * Retorno: void
   *********************************************/
  public void setNomeSala(String sala) {
    this.nomeSala = sala;
    Platform.runLater(() -> salaLabel.setText(sala));
  }

  /*********************************************
   * Metodo: setNotificacao
   * Funcao: Define se a conversa iniciara com notificacao e som
   * Parametros: boolean notificacao
   * Retorno: void
   *********************************************/
  public void setNotificacao(boolean notificacao) {
    notificacaoCheckBox.setSelected(notificacao);
    somCheckBox.setSelected(notificacao);
  }

  /*********************************************
   * Metodo: getNomeSala
   * Funcao: Retorna o nome da sala
   * Parametros: void
   * Retorno: String
   *********************************************/
  public String getNomeSala() {
    return nomeSala;
  }

  /*********************************************
   * Metodo: getClientesSize
   * Funcao: Retorna a quantidade de clientes conectados na sala
   * Parametros: void
   * Retorno: int
   *********************************************/
  public int getClientesSize() {
    return clienteMap.size();
  }

  /*********************************************
   * Metodo: setIndexMenu
   * Funcao: Atribui o indice dessa conversa no MenuLateral
   * Parametros: int indexMenu
   * Retorno: void
   *********************************************/
  public void setIndexMenu(int indexMenu) {
    this.indexMenu = indexMenu;
  }

  /*********************************************
   * Metodo: getIndexMenu
   * Funcao: Retorna o indice da conversa no MenuLateral
   * Parametros: void
   * Retorno: int
   *********************************************/
  public int getIndexMenu() {
    return indexMenu;
  }

  public String getMeuIP() {
    return meuIP;
  }

  public void setMeuIP(String meuIP) {
    this.meuIP = meuIP;
    System.out.println("Meu IP foi Definido como " + meuIP);
  }

  public Map<String, ServidorEnviarUDP> getClienteMap() {
    return clienteMap;
  }

  public boolean getEstouNaSala() {
    return estouNaSala;
  }

  public void setEstouNaSala(boolean estou) {
    this.estouNaSala = estou;
  }

  /*********************************************
   * Metodo: adicionarCliente
   * Funcao: Adiciona um novo cliente na conversa
   * Parametros: String ip, ServidorEnviarUDP enviarUDP
   * Retorno: void
   *********************************************/
  public void adicionarCliente(String ip, ServidorEnviarUDP enviarUDP) {
    //this.mensagemCentral(ip + " entrou no Grupo");
    for (ServidorEnviarUDP enviar : clienteMap.values()) {
      enviar.enviarMensagem(Apdu.JOIN(nomeSala, ip));
    }
    clientesList.add(ip);
    clienteMap.put(ip, enviarUDP);
    Platform.runLater(() -> membrosLabel.setText(++quantidadeClientes + " membros") );
  }

  public void setNumeroMembros(int num) {
    this.quantidadeClientes = num;
    this.membrosLabel.setText(quantidadeClientes + " membros");
  }

  public void adicionarCliente(String ip) {
    this.mensagemCentral(ip + " entrou no Grupo");
    clientesList.add(ip);
  }

  public boolean isClienteSala(String ip) {
    return clientesList.contains(ip);
  }

  public List<String> getClientesList() {
    return clientesList;
  }

  /*********************************************
   * Metodo: removerCliente
   * Funcao: Remove o cliente da Sala
   * Parametros: String ip
   * Retorno: void
   *********************************************/
  public void removerCliente(String ip) {
    clientesList.remove(ip);
    clienteMap.remove(ip);

    this.mensagemCentral(ip + " saiu do Grupo");

    for (ServidorEnviarUDP enviar : clienteMap.values()) {
      enviar.enviarMensagem(Apdu.LEAVE(nomeSala, ip));
    }

  }

  /*********************************************
   * Metodo: espalharMensagem
   * Funcao: Espalha as mensagens recebidas para todos os clientes
   * Parametros: String ipQuemEnviou, String mensagem
   * Retorno: void
   *********************************************/
  public void espalharMensagem(String ipQuemEnviou, String mensagem) {
    for (ServidorEnviarUDP enviarUDP : clienteMap.values()) {
      if (!ipQuemEnviou.equals(enviarUDP.getIP())) {
        enviarUDP.enviarMensagem(Apdu.SEND(nomeSala, ipQuemEnviou, mensagem));
      }
    }
  }

  /*********************************************
   * Metodo: setSubMenu
   * Funcao: Atribui uma referencia ao SubMenu dessa conversa
   * Parametros: SubMenu subMenu
   * Retorno: void
   *********************************************/
  public void setSubMenu(SubMenu subMenu) {
    this.subMenu = subMenu;
  }

  /*********************************************
   * Classe: Interface IActions
   * Funcao: Interface que define metodos da interface
   *********************************************/
  public interface IActions {
    public void sairConversa(String sala);
  }

}//Fim class