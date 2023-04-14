package itvcodingexercise.i20230413.lib

object Hashing {
  def sha256(bytes: Seq[Byte]): String = {
    val md = java.security.MessageDigest.getInstance("SHA-256")
    md.digest(bytes.toArray).map("%02x".format(_)).mkString
  }
}
