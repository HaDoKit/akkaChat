package App

import javafx.application.{Application, Platform}
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, WindowEvent}

import java.io.IOException
import java.util.Objects

class AppRun extends Application {
    @throws(classOf[IOException])
    override def start(primaryStage: Stage): Unit = {
      val root: Parent = FXMLLoader.load(Objects.requireNonNull(getClass.getResource("/View/loginView.fxml")))
      primaryStage.setScene(new Scene(root))
      primaryStage.setTitle("akkaChat")
      primaryStage.setResizable(false)
      primaryStage.show()

      primaryStage.getScene.getWindow.setOnCloseRequest((_: WindowEvent) => {
        Platform.exit()
        System.exit(0)
      })
    }
}
