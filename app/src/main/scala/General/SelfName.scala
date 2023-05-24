package General


// вывел Имя пользователя в отдельный singleton для получения на любом уровне!
object SelfName {
  private var name : String = null

  def apply(name : String): Unit = {
    this.name = name
  }

  def getName(): String = {
    this.name
  }

}
