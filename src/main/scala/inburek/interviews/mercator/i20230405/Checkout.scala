package inburek.interviews.mercator.i20230405

import cats.data.{Validated, ValidatedNel}

final case class ShoppingCart(items: List[Fruit]) {
  def total: Pence = items.foldLeft(Pence(0))((acc, fruit) => acc + Pricing.price(fruit))
}
object ShoppingCart {
  def fromStrings(items: List[String]): ValidatedNel[String, ShoppingCart] = {
    Validated.validNel(ShoppingCart(Nil))
  }
}
