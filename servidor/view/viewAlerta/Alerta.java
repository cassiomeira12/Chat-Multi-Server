/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 24/08/18
 * Ultima alteracao: 15/12/18
 * Nome: Alerta
 * Funcao: Componente pra criar um Alerta na Interface
 ***********************************************************************/

package view.viewAlerta;

import app.Interface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Alerta {
  protected static Painel painel;// = new Painel();
  private static void init() {
    painel = new Painel();
  }

  /*********************************************
   * Metodo: ALERTA
   * Funcao: Cria um Alerta do tipo Alerta
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  public static void ALERTA(String mensagem) {
    init();
    ALERTA("ALERTA", mensagem);
  }

  /*********************************************
   * Metodo: ALERTA
   * Funcao: Cria um Alerta do tipo Alerta
   * Parametros: String titulo, String mensagem
   * Retorno: void
   *********************************************/
  public static void ALERTA(String titulo, String mensagem) {
    Platform.runLater(() -> {
      init();
      painel.setTipo(Painel.Tipo.ALERTA);
      painel.setText(titulo, mensagem);
      painel.show();
    });
  }

  /*********************************************
   * Metodo: SUCESS
   * Funcao: Cria um Alerta do tipo Sucesso
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  public static void SUCESS(String mensagem) {
    init();
    SUCESS("SUCESSO", mensagem);
  }

  /*********************************************
   * Metodo: SUCESS
   * Funcao: Cria um Alerta do tipo Sucesso
   * Parametros: String titulo, String mensagem
   * Retorno: void
   *********************************************/
  public static void SUCESS(String titulo, String mensagem) {
    Platform.runLater(() -> {
      painel.setTipo(Painel.Tipo.SUCESS);
      painel.setText(titulo, mensagem);
      painel.show();
    });
  }

  /*********************************************
   * Metodo: ERRO
   * Funcao: Cria um Alerta do tipo Erro
   * Parametros: String mensagem
   * Retorno: void
   *********************************************/
  public static void ERRO(String mensagem) {
    ERRO("ERRO", mensagem);
  }

  /*********************************************
   * Metodo: ERRO
   * Funcao: Cria um Alerta do tipo Erro
   * Parametros: String mensagem, Stage palco, Pane pane
   * Retorno: void
   *********************************************/
  public static void ERRO(String mensagem, Stage palco, Pane pane) {
    Platform.runLater(() -> {
      painel = new Painel(palco, pane);
      painel.setTipo(Painel.Tipo.ERRO);
      painel.setText("ERRO", mensagem);
      painel.show();
    });
  }

  /*********************************************
   * Metodo: ERRO
   * Funcao: Cria um Alerta do tipo Erro
   * Parametros: String titulo, String mensagem
   * Retorno: void
   *********************************************/
  public static void ERRO(String titulo, String mensagem) {
    Platform.runLater(() -> {
      init();
      painel.setTipo(Painel.Tipo.ERRO);
      painel.setText(titulo, mensagem);
      painel.show();
    });
  }

  /*********************************************
   * Metodo: EXCEPTION
   * Funcao: Cria um Alerta do tipo Exception
   * Parametros: Exception ex
   * Retorno: void
   *********************************************/
  public static void EXCEPTION(Exception ex) {
    Platform.runLater(() -> {
      init();
      painel.setTipo(Painel.Tipo.ERRO);
      painel.setText("EXCEPTION", ex.toString());
      painel.show();
      ex.printStackTrace();
    });
  }

  /*********************************************
   * Metodo: EXCEPTION
   * Funcao: Cria um Alerta do tipo Exception
   * Parametros: String mensagem, Stage palco, Pane pane
   * Retorno: void
   *********************************************/
  public static void EXCEPTION(Exception ex, Stage palco, Pane pane) {
    Platform.runLater(() -> {
      painel = new Painel(palco, pane);
      painel.setTipo(Painel.Tipo.ERRO);
      painel.setText("EXCEPTION", ex.toString());
      painel.show();
      ex.printStackTrace();
    });
  }

  /*********************************************
   * Classe: Painel
   * Funcao: Classe que compoe o corpo do Alerta
   *********************************************/
  public static class Painel extends BorderPane {
    private final String FXML = "view/viewAlerta/Alerta.fxml";//Caminho do FXML

    private Stage palcoPrincipal;
    private Pane interfacePrincipal;// = Interface.TELA_INICIAL;
    private Stage palcoAlerta;

    @FXML
    private Label tituloLabel, mensagemLabel;
    @FXML
    private ImageView imagem;
    @FXML
    private HBox acoesHBox;

    private boolean mostrando = false;
    public Resposta RESPOSTA;

    protected enum Tipo {
      ALERTA, SUCESS, ERRO, CONFIR
    }

    public Painel() {
      this.palcoPrincipal = Interface.PALCO;
      initializeFXML();
    }

    public Painel(Stage palcoPrincipal, Pane painelInterface) {
      this.palcoPrincipal = palcoPrincipal;
      this.interfacePrincipal = painelInterface;
      initializeFXML();
    }

    private void initializeFXML() {
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
      this.palcoAlerta = new Stage();
      this.palcoAlerta.initStyle(StageStyle.TRANSPARENT);
      this.palcoAlerta.initModality(Modality.APPLICATION_MODAL);
      this.palcoAlerta.initOwner(Interface.PALCO);
      this.palcoAlerta.setScene(new Scene(this, Color.TRANSPARENT));
      this.palcoAlerta.setOnCloseRequest((e) -> fechar() );

      this.palcoPrincipal.xProperty().addListener((observable, oldValue, newValue) -> {
        this.palcoAlerta.setX(newValue.doubleValue());
      });
      this.palcoPrincipal.yProperty().addListener((observable, oldValue, newValue) -> {
        this.palcoAlerta.setY(newValue.doubleValue());
      });

    }

    protected void setTipo(Tipo tipo) {
      this.acoesHBox.getChildren().clear();//Limpando Box de acoes
      switch (tipo) {
        case ALERTA:
          this.acoesHBox.getChildren().add(confirmarButton());
          this.imagem.setImage(new Image(getClass().getResourceAsStream("alerta.png")));
          break;
        case SUCESS:
          this.acoesHBox.getChildren().add(confirmarButton());
          this.imagem.setImage(new Image(getClass().getResourceAsStream("confirmar.png")));
          break;
        case ERRO:
          this.acoesHBox.getChildren().add(confirmarButton());
          this.imagem.setImage(new Image(getClass().getResourceAsStream("erro.png")));
          break;
        case CONFIR:
          this.acoesHBox.getChildren().add(simButton());
          this.acoesHBox.getChildren().add(naoButton());
          this.imagem.setImage(new Image(getClass().getResourceAsStream("info.png")));
          break;
      }
    }

    public void setText(String titulo, String mensagem) {
      this.tituloLabel.setText(titulo);
      this.mensagemLabel.setText(mensagem);
    }

    public void show() {

      if (mostrando) {
        System.err.println("O Alerta ja esta aberto");
        return;
      }

      this.setPrefWidth(palcoPrincipal.getWidth());
      this.setPrefHeight(palcoPrincipal.getHeight());
      this.palcoAlerta.setX(palcoPrincipal.getX());
      this.palcoAlerta.setY(palcoPrincipal.getY());
      this.interfacePrincipal.setEffect(new GaussianBlur());
      this.mostrando = true;
      this.palcoAlerta.show();

//      this.setPrefWidth(Interface.PALCO.getWidth());
//      this.setPrefHeight(Interface.PALCO.getHeight());
//      this.palcoAlerta.setX(Interface.PALCO.getX());
//      this.palcoAlerta.setY(Interface.PALCO.getY());
//      this.interfacePrincipal.setEffect(new GaussianBlur());
//      this.mostrando = true;
//      this.palcoAlerta.show();
    }

    public void showAndWait() {
      if (mostrando) {
        System.err.println("O Alerta ja esta aberto");
        return;
      }

      this.setPrefWidth(palcoPrincipal.getWidth());
      this.setPrefHeight(palcoPrincipal.getHeight());
      this.palcoAlerta.setX(palcoPrincipal.getX());
      this.palcoAlerta.setY(palcoPrincipal.getY());
      this.interfacePrincipal.setEffect(new GaussianBlur());
      this.mostrando = true;
      this.palcoAlerta.showAndWait();

//      this.setPrefWidth(Interface.PALCO.getWidth());
//      this.setPrefHeight(Interface.PALCO.getHeight());
//      this.palcoAlerta.setX(Interface.PALCO.getX());
//      this.palcoAlerta.setY(Interface.PALCO.getY());
//      this.interfacePrincipal.setEffect(new GaussianBlur());
//      this.mostrando = true;
//      this.palcoAlerta.showAndWait();
    }

    @FXML
    private void fechar() {
      this.interfacePrincipal.setEffect(null);
      this.mostrando = false;
      this.palcoAlerta.close();
    }

    private Button cancelarButton() {
      Button cancelar = new Button("CANCELAR");
      cancelar.getStyleClass().add("button-nao");
      cancelar.setOnAction((Event) -> {
        this.RESPOSTA = Resposta.CANCELAR;
        fechar();
      });
      cancelar.setOnKeyPressed((Key) -> {
        if (Key.getCode() == KeyCode.ENTER) {
          this.RESPOSTA = Resposta.CANCELAR;
          fechar();
        }
      });
      return cancelar;
    }

    private Button confirmarButton() {
      Button confirmar = new Button("OK");
      confirmar.getStyleClass().add("button-sim");
      confirmar.setOnMouseClicked((Event) -> {
        this.RESPOSTA = Resposta.CONFIRMAR;
        fechar();
      });
      confirmar.setOnKeyPressed((Key) -> {
        if (Key.getCode() == KeyCode.ENTER) {
          this.RESPOSTA = Resposta.CONFIRMAR;
          fechar();
        }
      });
      return confirmar;
    }

    private Button simButton() {
      Button sim = new Button("SIM");
      sim.getStyleClass().add("button-sim");
      sim.setOnMouseClicked((Event) -> {
        this.RESPOSTA = Resposta.SIM;
        fechar();
      });
      sim.setOnKeyPressed((Key) -> {
        if (Key.getCode() == KeyCode.ENTER) {
          this.RESPOSTA = Resposta.SIM;
          fechar();
        }
      });
      return sim;
    }

    private Button naoButton() {
      Button nao = new Button("NÃO");
      nao.getStyleClass().add("button-nao");
      nao.setOnMouseClicked((Event) -> {
        this.RESPOSTA = Resposta.NAO;
        fechar();
      });
      nao.setOnKeyPressed((Key) -> {
        if (Key.getCode() == KeyCode.ENTER) {
          this.RESPOSTA = Resposta.NAO;
          fechar();
        }
      });
      return nao;
    }

  }

}
