package itvcodingexercise.i20230413

object Main {
  /** Downloads video, validates against checksum, and saves a thumbnail. */
  def main(args: Array[String]): Unit = {
    val argsDoc: String = """<video asset ID> <thumbnail timestamp> <destination path/filename>"""

    args match {
      case Array(videoAssetId, thumbnailTimestamp, destination) =>
        println(s"Video URL: $videoAssetId")
        println(s"Thumbnail timestamp: $thumbnailTimestamp")
        println(s"Destination: $destination")
      case _ =>
        System.err.println(s"""Expected 3 arguments ($argsDoc) but got ${args.length}:\n${args.mkString("\n")}""")
        System.exit(1)
    }
  }
}
