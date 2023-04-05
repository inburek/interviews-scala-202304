package inburek.interviews.mercator.i20230405

final case class ShoppingCart(items: List[Fruit]) {
  def total: Pence = items.foldLeft(Pence(0))((acc, fruit) => acc + Pricing.price(fruit))
}
