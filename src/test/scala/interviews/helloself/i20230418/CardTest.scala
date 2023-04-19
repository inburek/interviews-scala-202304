package interviews.helloself.i20230418

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers._

final class CardTest extends AnyFreeSpecLike {
  "Card" - {
    "deck" - {
      "should have 52 cards" in {
        assert(Card.deck.deck.size == 52)
      }
      "for each suit" - {
        for (suit <- Suit.all) {
          s"should have the right number of cards of suit <$suit>" in {
            Card.deck.deck.count(_.suit == suit) shouldBe 13
          }
        }
      }
    }
  }
}
