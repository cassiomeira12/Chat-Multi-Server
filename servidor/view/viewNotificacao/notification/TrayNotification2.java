package view.viewNotificacao.notification;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import view.viewNotificacao.animations.Animation;
import view.viewNotificacao.animations.Animations;
import view.viewNotificacao.models.CustomStage;

import java.io.IOException;
import java.net.URL;

public class TrayNotification2 extends AnchorPane {
  private final String FXML = "view/viewNotificacao/notificacao.fxml";//Caminho do FXML


  @FXML
  private Rectangle rectangleColor;
  @FXML
  private ImageView iconImageView;
  @FXML
  private Label tituloLabel, mensagemLabel, fecharLabel;

  private CustomStage stage;
  private Notifications notification;
  private Animation animation;
  private EventHandler<ActionEvent> onDismissedCallBack, onShownCallback;




  public TrayNotification2() {
    initializeFXML();
    //setTray("Titulo", "Mensagem", Notifications.NOTICE);
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

  /*********************************************
   * Metodo: Initialize
   * Funcao: Metodo para carregar a Interface
   * Parametros: void
   * Retorno: void
   *********************************************/
  @FXML
  public void initialize() {
    stage = new CustomStage(this, StageStyle.UNDECORATED);
    stage.setScene(new Scene(this));
    stage.setAlwaysOnTop(true);
    stage.setLocation(stage.getBottomRight());

    fecharLabel.setOnMouseClicked(e -> dismiss());

    setAnimation(Animations.SLIDE); // Default animation type
  }

  public void setNotification(Notifications nType) {
    notification = nType;

    URL imageLocation = getClass().getClassLoader().getResource(nType.getURLResource());
    setRectangleFill(Paint.valueOf(nType.getPrimaryColor()));
    setImage(new Image(imageLocation.toString()));
    setTrayIcon(iconImageView.getImage());
  }

  public Notifications getNotification() {
    return notification;
  }

  public void setTray(String title, String message, Notifications type) {
    setTitle(title);
    setMessage(message);
    //setNotification(type);
  }

  public void setTray(String title, String message, Image img, Paint rectangleFill, Animation animation) {
    setTitle(title);
    setMessage(message);
    setImage(img);
    setRectangleFill(rectangleFill);
    setAnimation(animation);
  }

  public boolean isTrayShowing() {
    return animation.isShowing();
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

  public void setOnDismiss(EventHandler<ActionEvent> event) {
    onDismissedCallBack = event;
  }

  public void setOnShown(EventHandler<ActionEvent> event) {
    onShownCallback = event;
  }

  public void setTrayIcon(Image img) {
    stage.getIcons().clear();
    stage.getIcons().add(img);
  }

  public Image getTrayIcon() {
    return stage.getIcons().get(0);
  }

  public void setTitle(String txt) {
    Platform.runLater(() -> tituloLabel.setText(txt));
  }

  public String getTitle() {
    return tituloLabel.getText();
  }

  public void setMessage(String txt) {
    mensagemLabel.setText(txt);
  }

  public String getMessage() {
    return mensagemLabel.getText();
  }

  public void setImage(Image img) {
    iconImageView.setImage(img);

    setTrayIcon(img);
  }

  public Image getImage() {
    return iconImageView.getImage();
  }

  public void setRectangleFill(Paint value) {
    rectangleColor.setFill(value);
  }

  public Paint getRectangleFill() {
    return rectangleColor.getFill();
  }

  public void setAnimation(Animation animation) {
    this.animation = animation;
  }

  public void setAnimation(Animations animation) {
    setAnimation(animation.newInstance(stage));
  }

  public Animation getAnimation() {
    return animation;
  }

}
