package view.viewNotificacao;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import view.viewNotificacao.animations.Animation;
import view.viewNotificacao.animations.Animations;
import view.viewNotificacao.models.CustomStage;
import view.viewNotificacao.notification.Notifications;

import java.io.IOException;
import java.net.URL;

public class Notificacao extends AnchorPane {
  private final String FXML = "view/viewNotificacao/notificacao.fxml";//Caminho do FXML

  private CustomStage stage;
  private Notifications notification;
  private Animation animation;
  private EventHandler<ActionEvent> onDismissedCallBack, onShownCallback;

  @FXML
  private AnchorPane dark1Pane, dark2Pane, cleanPane;
  @FXML
  private ImageView iconImageView;
  @FXML
  private Label tituloLabel, mensagemLabel, fecharLabel;

  private Notificacao() {
    initializeFXML();
    stage = new CustomStage(this, StageStyle.TRANSPARENT);
    stage.setScene(new Scene(this, Color.TRANSPARENT));
    stage.setAlwaysOnTop(false);
    stage.setLocation(stage.getBottomRight());
    stage.initModality(Modality.NONE);

    stage.setOnShown((Event) -> {

    });

    this.setFocused(false);
    this.setFocusTraversable(false);
    setAnimation(Animations.FADE); // Default animation type
    fecharLabel.setOnMouseClicked(e -> dismiss() );
  }

  public Notificacao(Notifications tipo) {
    this();
    setNotification(tipo);
  }

  /*********************************************
   * Metodo: BoxMenu - Construtor
   * Funcao: Constroi objetos da classe BoxMenu
   * Parametros: Stage PALCO
   * Retorno: void
   *********************************************/
  public void initializeFXML() {
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

  public void setText(String titulo, String mensagem) {
    tituloLabel.setText(titulo);
    mensagemLabel.setText(mensagem);
  }

  public void show(String titulo, String mensagem) {
    tituloLabel.setText(titulo);
    mensagemLabel.setText(mensagem);
    showAndDismiss(Duration.seconds(2));
  }

  public void showAndDismiss(Duration dismissDelay) {
    if (!isTrayShowing()) {
      stage.show();

      onShown();

      animation.playSequential(dismissDelay);
    } else dismiss();

    onDismissed();
  }

  public void showAndWait() {
    if (!isTrayShowing()) {
      stage.show();

      animation.playShowAnimation();

      onShown();
    }
  }

  public void dismiss() {
    if (isTrayShowing()) {
      animation.playDismissAnimation();
      onDismissed();
    }
  }

  private void onShown() {
    if (onShownCallback != null)
      onShownCallback.handle(new ActionEvent());
  }

  private void onDismissed() {
    if (onDismissedCallBack != null)
      onDismissedCallBack.handle(new ActionEvent());
  }

  public void setNotification(Notifications nType) {
    notification = nType;

    URL imageLocation = getClass().getClassLoader().getResource(nType.getURLResource());
    setRectangleFill(nType);
    setImage(new Image(imageLocation.toString()));
    //setTrayIcon(iconImageView.getImage());
  }

  public void setRectangleFill(Notifications colors) {
    dark1Pane.setStyle("-fx-background-radius: 10; -fx-background-color: " + colors.getPrimaryColor());
    dark2Pane.setStyle("-fx-background-color: " + colors.getPrimaryColor());
    cleanPane.setStyle("-fx-background-radius: 10; -fx-background-color: " + colors.getSecondColor());

  }

  public void setImage(Image img) {
    iconImageView.setImage(img);
    //setTrayIcon(img);
  }

  public void setTrayIcon(Image img) {
    stage.getIcons().clear();
    stage.getIcons().add(img);
  }

  public boolean isTrayShowing() {
    return animation.isShowing();
  }

  public void setAnimation(Animation animation) {
    this.animation = animation;
  }

  public void setAnimation(Animations animation) {
    setAnimation(animation.newInstance(stage));
  }

}
