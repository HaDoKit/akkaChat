package App

import javafx.application.Application

// Имя singleton и имя класса Application должны быть разными!
// Иначе загрузке мешает класс FXHelper
object App {
  def main(args:Array[String]) : Unit = {
    Application.launch(classOf[AppRun], args: _*)
  }
}
