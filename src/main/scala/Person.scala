
final case class Person(name: String) {
  private val vowels: Set[Char] = Set('a', 'e', 'i', 'o', 'u', 'y')

  def trialGroup: TrialGroup = {
    if (oddNumberOfNonVowels) {
      TrialGroup.Placebo
    } else if (!hasAnyVowels) {
      TrialGroup.ControlGroup
    } else {
      TrialGroup.Medicine
    }
  }

  def oddNumberOfNonVowels: Boolean = {
    val numberOfNonVowels = name.count(char => !vowels.contains(char))
    numberOfNonVowels % 2 == 1
  }

  def hasAnyVowels: Boolean = {
    name.count(char => vowels.contains(char)) > 0
  }
}

sealed trait TrialGroup
object TrialGroup {
  case object Placebo extends TrialGroup
  case object Medicine extends TrialGroup
  case object ControlGroup extends TrialGroup
}
