package inburek.interviews.mercator.i20230405

import cats.data.ValidatedNel
import cats.implicits._

final case class ShoppingCart(items: List[Fruit]) {
  def total: Pence = {
    val counts: Map[Fruit, Int] = items.groupBy(identity).view.mapValues(_.size).toMap

    val prices: Map[Fruit, Pence] = counts.map {
      case (fruit @ Fruit.Apple, count) => (fruit, Pricing.price(fruit) * (count / 2 + count % 2))
      case (fruit @ Fruit.Orange, count) => (fruit, Pricing.price(fruit) * (count / 3 * 2 + count % 3))
    }

    prices.values.foldLeft(Pence(0))(_ + _)
  }
}
object ShoppingCart {
  def fromStrings(items: List[String]): ValidatedNel[String, ShoppingCart] = {
    items.traverse(Fruit.apply).map(ShoppingCart(_))
  }
}
