package inburek.interviews.mercator.i20230405

final case class Pence(value: Int)

case object Pricing {
  def price(fruit: Fruit): Pence = {
    fruit match {
      case Fruit.Apple => Pence(60)
      case Fruit.Orange => Pence(25)
    }
  }
}

sealed abstract class Fruit private(val name: String)
object Fruit {
  case object Apple extends Fruit("Apple")
  case object Orange extends Fruit("Orange")
}
