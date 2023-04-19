package interviews.quantexa.i20230418

import scala.util.Random

sealed trait Suit
object Suit {
  case object Clubs extends Suit
  case object Diamonds extends Suit
  case object Hearts extends Suit
  case object Spades extends Suit

  val all: Seq[Suit] = Seq(Clubs, Diamonds, Hearts, Spades)
}

sealed trait Rank
object Rank {
  case object Ace extends Rank
  case object Two extends Rank
  case object Three extends Rank
  case object Four extends Rank
  case object Five extends Rank
  case object Six extends Rank
  case object Seven extends Rank
  case object Eight extends Rank
  case object Nine extends Rank
  case object Ten extends Rank
  case object Jack extends Rank
  case object Queen extends Rank
  case object King extends Rank

  val all: Seq[Rank] = Seq(Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King)
}

final case class Card(suit: Suit, rank: Rank)

object Card {
  def deck: CardSeq = CardSeq(
    for {
      suit <- Suit.all
      rank <- Rank.all
    } yield Card(suit, rank)
  )
}

final case class CardSeq(deck: Seq[Card]) {
  def filter(predicate: Card => Boolean): CardSeq = CardSeq(deck.filter(predicate))
}


