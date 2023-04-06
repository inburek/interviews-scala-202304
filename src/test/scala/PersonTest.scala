import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

final class PersonTest extends org.scalatest.freespec.AnyFreeSpecLike {
  "Person" - {
    ".oddNumberOfNonVowels" - {
      "for empty name" in {
        Person("").oddNumberOfNonVowels shouldBe false
      }
      "for one consonant" in {
        Person("c").oddNumberOfNonVowels shouldBe true
      }
      "for one vowel" in {
        Person("a").oddNumberOfNonVowels shouldBe false
      }
      "for a name with an even number of non-vowels" in {
        Person("CaCeCiC").oddNumberOfNonVowels shouldBe false
      }
      "for a name with an odd number of non-vowels" in {
        Person("CaCeCi'C").oddNumberOfNonVowels shouldBe true
      }
    }

    ".hasAnyVowels" - {
      "for empty name" in {
        Person("").hasAnyVowels shouldBe false
      }
      "for one vowel" in {
        Person("y").hasAnyVowels shouldBe true
      }
      "for one consonant" in {
        Person("c").hasAnyVowels shouldBe false
      }
      "for complex name without vowels" in {
        Person("Bcttrsh's").hasAnyVowels shouldBe false
      }
      "for complex name with vowels" in {
        Person("Abcde").hasAnyVowels shouldBe true
      }
    }

    ".trialGroup" - {
      "for a placebo person" in {
        Person("Greg").trialGroup shouldBe TrialGroup.Placebo
      }
      "for a control group person" in {
        Person("Grsh;s").trialGroup shouldBe TrialGroup.ControlGroup
      }
      "for a medicated person" in {
        Person("Michael").trialGroup shouldBe TrialGroup.Medicine
      }
    }
  }
}
