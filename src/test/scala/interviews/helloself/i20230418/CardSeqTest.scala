package interviews.helloself.i20230418

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

final class CardSeqTest extends AnyFreeSpecLike {
  "CardSeq" - {
    ".filter" - {
      "works on empty if all are accepted" in {
        CardSeq(Seq()).filter(_ => true) shouldBe CardSeq(Seq())
      }
      "works on empty if no cards are accepted" in {
        CardSeq(Seq()).filter(_ => false) shouldBe CardSeq(Seq())
      }

      "works a bunch of cards" - {
        val twoCards = CardSeq(Seq(Card(Suit.Spades, Rank.Six), Card(Suit.Clubs, Rank.Eight)))

        "if all are accepted" in {
          twoCards.filter(_ => true) shouldBe twoCards
        }

        "if no cards are accepted" in {
          twoCards.filter(_ => false) shouldBe CardSeq(Seq())
        }
      }

      "works on the Solitaire cards" in {
        val solitaireRanks: Set[Rank] = Set(Rank.Two, Rank.Three, Rank.Four, Rank.Five, Rank.Six)
        Card.deck.filter(card => !solitaireRanks.contains(card.rank)).deck.size shouldBe 32
      }
    }
  }
}
