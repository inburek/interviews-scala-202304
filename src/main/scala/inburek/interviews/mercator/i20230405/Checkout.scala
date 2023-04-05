package inburek.interviews.mercator.i20230405

final case class ShoppingCart(items: List[Fruit]) {
  def total: Pence = Pence(0)
}
