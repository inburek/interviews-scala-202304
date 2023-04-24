package com.recommender.utils

import scala.util.Random

object RandomOps {
  implicit class RichRandom(random: Random) {
    def choose[T](items: Seq[T]): Option[T] = {
      if (items.isEmpty) {
        None
      } else {
        Some(items(random.nextInt(items.size)))
      }
    }
  }
}
