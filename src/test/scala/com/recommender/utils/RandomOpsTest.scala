package com.recommender.utils

import org.scalatest.freespec.AnyFreeSpecLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.util.Random

final class RandomOpsTest extends AnyFreeSpecLike {
  "RandomOps" - {
    "RichRandom" - {
      import RandomOps.RichRandom

      ".choose" - {
        "chooses nothing for empty seq" in {
          val random = new Random(0)
          random.choose(Seq.empty) shouldBe None
        }

        "chooses only one element when only one element is available" in {
          val random = new Random(0)
          (1 to 10).map(_ => random.choose(Seq(1))).toSet shouldBe Set(Some(1))
        }

        "chooses one of the elements when multiple elements are available" in {
          val random = new Random(0)
          (1 to 10).map(_ => random.choose(Seq(1, 2, 3))).toSet shouldBe Set(Some(1), Some(2), Some(3))
        }
      }
    }
  }
}
