package inburek.interviews.mercator.i20230405

import cats.data.ValidatedNel

final case class Pence(value: Int) {
  def +(that: Pence): Pence = Pence(this.value + that.value)
}

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
  def apply(name: String): ValidatedNel[String, Fruit] = ???

  case object Apple extends Fruit("Apple")
  case object Orange extends Fruit("Orange")
}
