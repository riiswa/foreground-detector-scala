import java.nio.file.{Files, Paths}

import javax.swing.WindowConstants.EXIT_ON_CLOSE
import org.bytedeco.javacpp.opencv_core.{CV_8U, Mat}
import org.bytedeco.javacv.FrameGrabber.ImageMode
import org.bytedeco.javacv.{CanvasFrame, OpenCVFrameGrabber}

import scala.collection.mutable.ArrayBuffer

object ForegroundDetectorApp {
  def main(args: Array[String]): Unit = {
    import ForegroundDetector._
    import ForegroundDetector.Implicits._

    val canvas1 = new CanvasFrame("Video")
    canvas1.setDefaultCloseOperation(EXIT_ON_CLOSE)

    println(args.length)

    val grabber =
      if (args.length < 2 || !Files.exists(Paths.get(args(1)))) {
        if (args.length > 1) println("Fail to load file, run camera by default")
        new OpenCVFrameGrabber(0)
      } else new OpenCVFrameGrabber(args(1))

    grabber.setFormat("mp4")
    grabber.setBitsPerPixel(CV_8U)
    grabber.setImageMode(ImageMode.COLOR)
    grabber.start()

    val canvas2 = new CanvasFrame("Foreground Detector")
    canvas2.setDefaultCloseOperation(EXIT_ON_CLOSE)

    var lastSubtractionTime = 0L
    val previousImages = ArrayBuffer.empty[Mat]

    while (true) {
      val img = grabber.grab()

      val mat = filterNoise(MediaConversion.toMat(img))

      if (System
            .currentTimeMillis() - lastSubtractionTime > 50 & previousImages.nonEmpty) {

        val bg = mean(previousImages.map(_.getPixels))

        val foreground = meanFilter(bg, mat.getPixels, 30)

        canvas2.showImage(
          MediaConversion.toFrame(foreground.toMat(mat.rows, mat.cols)))

        lastSubtractionTime = System.currentTimeMillis()

        previousImages.clear()
      }

      previousImages += mat

      canvas1.showImage(img)
    }
  }
}
