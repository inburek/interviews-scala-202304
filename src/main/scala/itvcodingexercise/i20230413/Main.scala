package itvcodingexercise.i20230413

import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging {
  /** Downloads video, validates against checksum, and saves a thumbnail. */
  def main(args: Array[String]): Unit = {
    val argsDoc: String = """<video asset ID> <thumbnail timestamp> <destination path/filename>"""

    args match {
      case Array(videoAssetId, thumbnailTimestamp, destination) =>
        logger.info(s"Video URL: $videoAssetId")
        logger.info(s"Thumbnail timestamp: $thumbnailTimestamp")
        logger.info(s"Destination: $destination")
      case _ =>
        logger.error(s"""Expected 3 arguments ($argsDoc) but got ${args.length}:\n${args.mkString("\n")}""")
        System.exit(1)
    }
  }
}
