package itvcodingexercise.i20230413.testutils

import java.io.File

/** Files used by tests will be collected here.
  * Some may be in dynamic or strange locations.
  */
object TestFiles {
  object Videos {
    val validFile = new File(getClass.getClassLoader.getResource("samples/valid.mov").getFile)
    val invalidFile = new File(getClass.getClassLoader.getResource("samples/invalid.mov").getFile)
  }
}
