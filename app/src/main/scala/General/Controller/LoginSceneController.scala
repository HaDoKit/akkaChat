package General.Controller

import General.Constants
import General.System.System
import javafx.application.Platform
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, TextField}
import javafx.scene.layout.GridPane

import java.util.Objects


class LoginSceneController {
    @FXML
  protected var loginScene : GridPane = _
    @FXML
  protected var loginText : TextField = _
    @FXML
  protected var connectButton : Button = _

  private def isLoginValid(login: String): Boolean = login.length >= 3

  def initialize() : Unit = {
    Platform.runLater(() => loginScene.requestFocus())
    loginScene.setOnMouseClicked(_ => loginScene.requestFocus())

    loginText.textProperty().addListener(new ChangeListener[String]() {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
        if (!newValue.matches("[\\\\da-zA-Z]")) {
          loginText.setText(newValue.replaceAll("[^\\a-zA-Z]", ""))
        }
        if (newValue.length > 16) {
          loginText.setText(newValue.substring(0, 16))
        }
      }
    })
    connectButton.setOnAction( event => {
      val login =loginText.getText
      if (isLoginValid(login)) {
        System(login)
        val chatScene =new MainSceneController()
        val loader: FXMLLoader = new FXMLLoader(Objects.requireNonNull(
          getClass.getResource("/View/mainScene.fxml")))
        loader.setController(chatScene)

        chatScene.loadScene(Constants.GeneralRoom, event, loader)
        chatScene.newRoom(Constants.GeneralRoom)
      }
    })
  }
}
