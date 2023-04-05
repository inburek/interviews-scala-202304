package inburek.interviews.mercator.i20230405

import cats.data.{Validated, ValidatedNel}

final case class Pence(value: Int) {
  def +(that: Pence): Pence = Pence(this.value + that.value)
  def *(that: Int): Pence = Pence(this.value * that)
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
  def apply(name: String): ValidatedNel[String, Fruit] = {
    name match {
      case "Apple" => Validated.validNel(Apple)
      case "Orange" => Validated.validNel(Orange)
      case unknown => Validated.invalidNel(s"Unknown fruit: $unknown")
    }
  }

  case object Apple extends Fruit("Apple")
  case object Orange extends Fruit("Orange")
}
