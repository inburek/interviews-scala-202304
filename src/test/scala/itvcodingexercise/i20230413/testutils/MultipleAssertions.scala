package itvcodingexercise.i20230413.testutils

import org.scalatest.Checkpoints._

/** Allows multiple assertions to be run in parallel. All failures will be reported instead of short-circuiting.
  * ScalaTest's official syntax is a bit clunky and unsafe due to the trailing `reportAll`.
  *
  * {{{
  *   MultipleAssertions
  *     .assert { myValue shouldBe expectation }
  *     .assert { (myMock.myMethod _).verify(data, data2).once() }
  *     .verify()
  * }}}
  */
object MultipleAssertions extends MultipleAssertions(Seq.empty)

sealed abstract class MultipleAssertions(assertions: Seq[() => Unit]) {
  def assert(assertion: => Unit): MultipleAssertions = {
    val newAssertion = () => assertion
    new MultipleAssertions(assertions :+ newAssertion) {}
  }

  def verify(): Unit = {
    val cp = new Checkpoint()
    assertions.foreach(assertion => cp(assertion()))
    cp.reportAll()
  }
}
