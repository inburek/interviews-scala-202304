package itvcodingexercise.i20230413

object Main {
  /** Downloads video, validates against checksum, and saves a thumbnail.
    * @param args video asset ID, destination path/filename
    */
  def main(args: Array[String]): Unit = {
    args match {
      case Array(videoAssetId, destination) =>
        println(s"Video URL: $videoAssetId")
        println(s"Destination: $destination")
      case _ =>
        throw new IllegalArgumentException(s"""Expected 3 arguments (<video asset ID> <destination path/filename>) but got ${args.length}:\n${args.mkString("\n")}""")
    }
  }
}
