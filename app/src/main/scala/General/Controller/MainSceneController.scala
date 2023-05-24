package General.Controller

import General.Constants
import General.System.ActorSystems
import Room.Room
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Tab, TabPane}
import javafx.scene.layout.AnchorPane
import javafx.scene.{Node, Scene}
import javafx.stage.Stage

import java.util.Objects

object MainSceneControllers {

  private var scene : MainSceneController = null

  def apply(scene : MainSceneController) : Unit = {
    if (this.scene == null) {
      this.scene == scene
    }
  }
  def get() : MainSceneController = {
    scene
  }
}

class MainSceneController {
  @FXML
  protected var tabPane : TabPane = _
  @FXML
  protected var anchorPane: AnchorPane = _

  def setMessage(chatName: String, message : String) = {
    val room = Room(chatName,ActorSystems.get()).getController()
    room.addMessage(message)
  }
  def setUser(chatName : String, user : String) : Unit = {
    val room = Room(chatName,ActorSystems.get()).getController()
    room.addUser(user)
  }

  def removeUser(chatName: String, user: String): Unit = {
    val room = Room(chatName, ActorSystems.get()).getController()
    room.removeUser(user)
  }

  def newRoom(chatName : String) : Unit = {
    val room = Room(chatName, ActorSystems.get())
    room.subscribe()
    if (room.chatName() == Constants.GeneralRoom) {
      room.setMainSceneController(this)
    }
    val tab = new Tab(room.chatNameVisible())
    val loaders: FXMLLoader = new FXMLLoader(Objects.requireNonNull(getClass.getResource(
      "/View/RoomView.fxml")))
    val node: AnchorPane = loaders.load()
    loaders.getController.asInstanceOf[RoomController].setRoom(chatName)
    tab.setContent(node)
    tabPane.getTabs().add(tab)
  }

  def getInstance() : MainSceneController = {
  this
  }

  def loadScene(chatName : String, event: Event, loader: FXMLLoader) : Unit = {
    val window = event.getSource.asInstanceOf[Node].getScene.getWindow

    val stage = window.asInstanceOf[Stage]
    stage.setScene(new Scene(loader.load()))
//    stage.setOnCloseRequest((_: WindowEvent) => {
//      actorSystem ! Logout(login)
//      actorSystem.terminate()
//    })
  }
  def initialize() : Unit = {
  }
}
