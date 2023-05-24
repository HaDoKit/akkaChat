package General.Controller

import Actor.Sender.{MemberUps, MessagePost, OfferToJoins}
import General.SelfName
import General.System.ActorSystems
import Room.Room
import javafx.concurrent._
import javafx.fxml.FXML
import javafx.scene.control.{Button, ListView, TextField}
import javafx.scene.layout.{AnchorPane, VBox}

// обработчик комнат
class RoomController {

  @FXML
  protected var anchorPane: AnchorPane = _

  @FXML
  protected var userList: ListView[String] = _

  @FXML
  protected var messageList: ListView[String] = _

  @FXML
  protected var sendButton: Button = _

  @FXML
  protected var roomScene: VBox = _

  @FXML
  protected var sendField: TextField = _

  @FXML
  protected var connectNewChatButton:Button = _

  private var room: Room = null


  def addMessage(message: String): Unit = {
    if (message != (SelfName.getName() + ":")) {
      messageList.getItems.add(message)
    }
  }

  def addUserList(user : String): Unit = {
      ActorSystems.get() ! MemberUps(user, room.getRoom(), room.chatName())
      ActorSystems.get() ! MessagePost(user, room.getRoom(), "Присоединился к чату:" + user + "отправитель" + SelfName.getName(), room.chatName())

  }

  def addUser(user: String): Unit = {
    // надоело искать способ получения из ListView текст,
    // отфильтровал перевел в строку, удалил скобки!
    val listUserDirty = userList.getItems().filtered(element => element == user).toString
    val listUser = listUserDirty.substring(1 , listUserDirty.length - 1)
    if (user != listUser) {
      userList.getItems.add(user)
      addMessage("Подключился:" + user)
    }
  }

  def removeUser(user: String): Unit = {
    userList.getItems.removeIf(element => element == user)
    ActorSystems.get() ! MessagePost(user, room.getRoom(), "Отключился:" + user, room.chatName())
  }

  def setRoom(chatName: String): Unit = {
    this.room = Room(chatName, ActorSystems.get())
    room.setController(this)
  }

  def initialize(): Unit = {
    sendButton.setOnAction(event => {
      val message = sendField.getText
      ActorSystems.get() ! MessagePost(SelfName.getName(), room.getRoom(), SelfName.getName() + ":" + message, room.chatName())
    })
    connectNewChatButton.setOnAction(event => {
      val chatName = (userList.selectionModelProperty().get().getSelectedItems().get(0) + "To" + SelfName.getName() )
      ActorSystems.get() ! OfferToJoins(userList.selectionModelProperty().get().getSelectedItems.get(0), room.getRoom(), chatName)
    })
  }


}
