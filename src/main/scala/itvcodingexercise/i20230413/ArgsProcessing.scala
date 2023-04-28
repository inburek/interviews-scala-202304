package itvcodingexercise.i20230413

import com.typesafe.scalalogging.LazyLogging

object ArgsProcessing extends LazyLogging {
  def processDownloadVideoArgs(args: Seq[String]): Either[String, ItvVideoProcessing.ProcessingArgs] = {
    val argsDoc: String = """<video asset ID> <thumbnail timestamp> <destination path/filename>"""

    args match {
      case Seq(videoAssetId: String, thumbnailTimestamp: String, destination: String) =>
        logger.info(s"Video URL: $videoAssetId")
        logger.info(s"Thumbnail timestamp: $thumbnailTimestamp")
        logger.info(s"Destination: $destination")

        Right(ItvVideoProcessing.ProcessingArgs(
          videoAssetId = videoAssetId,
          thumbnailTimestamp = thumbnailTimestamp,
          destination = destination,
        ))
      case _ =>
        Left(
          s"""The arguments provided for video downloading/validation are incorrect;
             |Found: ${args.mkString(", ")}
             |Expected: $argsDoc""".stripMargin
        )
    }

  }
}
