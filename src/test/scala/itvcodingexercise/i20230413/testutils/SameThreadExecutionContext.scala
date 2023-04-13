package itvcodingexercise.i20230413.testutils

object SameThreadExecutionContext extends scala.concurrent.ExecutionContext {
  def execute(runnable: Runnable): Unit = runnable.run()
  def reportFailure(cause: Throwable): Unit = cause.printStackTrace()
}
