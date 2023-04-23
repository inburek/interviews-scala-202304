package inburek.interviews.quantexa.i20230419.domain

final case class TransactionId(value: String) extends AnyVal {
  override def toString: String = value
}

final case class AccountId(value: String) extends AnyVal {
  override def toString: String = value
}

final case class TransactionDay(value: Int) extends AnyVal {
  override def toString: String = value.toString
}

final case class TransactionCategory(value: String) extends AnyVal {
  override def toString: String = value
}

object TransactionCategory {
  val categoryAA: TransactionCategory = TransactionCategory("AA")
  val categoryBB: TransactionCategory = TransactionCategory("BB")
  val categoryCC: TransactionCategory = TransactionCategory("CC")
  val categoryDD: TransactionCategory = TransactionCategory("DD")
  val categoryEE: TransactionCategory = TransactionCategory("EE")
  val categoryFF: TransactionCategory = TransactionCategory("FF")
  val categoryGG: TransactionCategory = TransactionCategory("GG")

  val allKnown: Seq[TransactionCategory] = Seq(
    categoryAA,
    categoryBB,
    categoryCC,
    categoryDD,
    categoryEE,
    categoryFF,
    categoryGG,
  )
}

final case class TransactionAmount(value: Double) extends AnyVal {
  override def toString: String = value.toString

  def +(other: TransactionAmount): TransactionAmount = TransactionAmount(value + other.value)
}

object TransactionAmount {
  def empty: TransactionAmount = TransactionAmount(0.0)
}
