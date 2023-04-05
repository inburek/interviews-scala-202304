package inburek.interviews.mercator.i20230405

import cats.data.ValidatedNel
import cats.implicits._

final case class ShoppingCart(items: List[Fruit]) {
  def total: Pence = items.foldLeft(Pence(0))((acc, fruit) => acc + Pricing.price(fruit))
}
object ShoppingCart {
  def fromStrings(items: List[String]): ValidatedNel[String, ShoppingCart] = {
    items.traverse(Fruit.apply).map(ShoppingCart(_))
  }
}
