package inburek.interviews.mercator.i20230405

final case class Pence(value: Int)

case object Pricing {
  def price(fruit: Fruit): Pence = ???
}

sealed abstract class Fruit private(val name: String)
object Fruit {
  case object Apple extends Fruit("Apple")
  case object Orange extends Fruit("Orange")
}
