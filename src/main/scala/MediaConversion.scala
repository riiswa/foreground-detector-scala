import java.util.function.Supplier

import org.bytedeco.javacpp.opencv_core.Mat
import org.bytedeco.javacv.{Frame, OpenCVFrameConverter}

object MediaConversion {
  // Each thread gets its own greyMat for safety
  private val frameToMatConverter =
    ThreadLocal.withInitial(new Supplier[OpenCVFrameConverter.ToMat] {
      def get(): OpenCVFrameConverter.ToMat = new OpenCVFrameConverter.ToMat
    })

  /**
    * Returns an OpenCV Mat for a given JavaCV frame
    */
  def toMat(frame: Frame): Mat = frameToMatConverter.get().convert(frame)

  /**
    * Returns a JavaCV Frame for a given OpenCV Mat
    */
  def toFrame(mat: Mat): Frame = frameToMatConverter.get().convert(mat)
}
