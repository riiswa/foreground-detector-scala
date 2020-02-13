import breeze.linalg.DenseVector
import breeze.numerics.abs
import org.bytedeco.javacpp.indexer.UByteIndexer
import org.bytedeco.javacpp.opencv_core._
import org.bytedeco.javacpp.opencv_imgproc._

object ForegroundDetector {
  private val BLACK = 0x00
  private val WHITE = 0xFF

  object Implicits {
    implicit class MatToPixel(img: Mat) {
      def getPixels: DenseVector[Int] = {
        val indexer = img.createIndexer().asInstanceOf[UByteIndexer]
        val nbElements = img.rows * img.cols * img.channels
        val px = new DenseVector[Int](nbElements)
        for (i <- 0 until nbElements) px(i) = indexer.get(i) & 0xFF
        px
      }
    }

    implicit class PixelToMat(px: DenseVector[Int]) {
      def toMat(rows: Int, cols: Int): Mat = {
        val img = new Mat(rows, cols, 16)
        val indexer = img.createIndexer().asInstanceOf[UByteIndexer]
        for (i <- 0 until px.length) indexer.put(i, px(i))
        img
      }
    }
  }

  def mean(pixels: Seq[DenseVector[Int]]): DenseVector[Int] =
    pixels.reduce(_ + _) / pixels.length

  def filterNoise(img: Mat): Mat = {
    val dest = new Mat(img.rows, img.cols, img.`type`)
    medianBlur(img, dest, 5)
    dest
  }

  def meanFilter(bg: DenseVector[Int],
                v: DenseVector[Int],
                threshold: Int): DenseVector[Int] = {
    abs(bg - v).map {
    case x if x > threshold => WHITE
    case _                  => BLACK
  }}

}
