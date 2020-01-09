package easypath;

import java.util.function.Supplier;
import javafx.geometry.Point2D;

import java.util.function.Function;

public class PathUtil {

  /**
   * Legendre-Gauss abscissae for n=24 Please see the {@link #calculateLength calculateLength}
   * method
   */
  private static double[] LGAbscissae = {
      -0.0640568928626056260850430826247450385909,
      0.0640568928626056260850430826247450385909,
      -0.1911188674736163091586398207570696318404,
      0.1911188674736163091586398207570696318404,
      -0.3150426796961633743867932913198102407864,
      0.3150426796961633743867932913198102407864,
      -0.4337935076260451384870842319133497124524,
      0.4337935076260451384870842319133497124524,
      -0.5454214713888395356583756172183723700107,
      0.5454214713888395356583756172183723700107,
      -0.6480936519369755692524957869107476266696,
      0.6480936519369755692524957869107476266696,
      -0.7401241915785543642438281030999784255232,
      0.7401241915785543642438281030999784255232,
      -0.8200019859739029219539498726697452080761,
      0.8200019859739029219539498726697452080761,
      -0.8864155270044010342131543419821967550873,
      0.8864155270044010342131543419821967550873,
      -0.9382745520027327585236490017087214496548,
      0.9382745520027327585236490017087214496548,
      -0.9747285559713094981983919930081690617411,
      0.9747285559713094981983919930081690617411,
      -0.9951872199970213601799974097007368118745,
      0.9951872199970213601799974097007368118745
  };

  /**
   * Legendre-Gauss weights for n=24 Please see the {@link #calculateLength calculateLength} method
   */
  private static double[] LGWeights = {
      0.1279381953467521569740561652246953718517,
      0.1279381953467521569740561652246953718517,
      0.1258374563468282961213753825111836887264,
      0.1258374563468282961213753825111836887264,
      0.1216704729278033912044631534762624256070,
      0.1216704729278033912044631534762624256070,
      0.1155056680537256013533444839067835598622,
      0.1155056680537256013533444839067835598622,
      0.1074442701159656347825773424466062227946,
      0.1074442701159656347825773424466062227946,
      0.0976186521041138882698806644642471544279,
      0.0976186521041138882698806644642471544279,
      0.0861901615319532759171852029837426671850,
      0.0861901615319532759171852029837426671850,
      0.0733464814110803057340336152531165181193,
      0.0733464814110803057340336152531165181193,
      0.0592985849154367807463677585001085845412,
      0.0592985849154367807463677585001085845412,
      0.0442774388174198061686027482113382288593,
      0.0442774388174198061686027482113382288593,
      0.0285313886289336631813078159518782864491,
      0.0285313886289336631813078159518782864491,
      0.0123412297999871995468056670700372915759,
      0.0123412297999871995468056670700372915759
  };

  /**
   * Creates a straight path of a certain length.
   *
   * @param length the length to drive in a straight line
   * @return A Path object with a derivative of 0 (so that you drive in a straight line)
   */
  public static Path createStraightPath(double length) {
    return new Path(x -> 0.0, length);
  }

  /**
   * Creates a Path given coordinates for 4 control points in a 2D plane. The starting and end
   * points are simple - the starting and ending points of the path. point1 and point2 represent two
   * control points - the first control point is co-linear with the tangent line at the starting
   * point, while the second control point is co-linear with the tangent line at the ending point.
   * You can see an example illustration here: https://i.imgur.com/Seae3mn.png
   *
   * @param startX the x coordinate of the starting point
   * @param startY the y coordinate of the starting point
   * @param point1X the x coordinate of the first control point
   * @param point1Y the y coordinate of the first control point
   * @param point2X the x coordinate of the second control point
   * @param point2Y the y coordinate of the second control point
   * @param endX the x coordinate of the ending point
   * @param endY the y coordinate of the ending point
   * @return a Path with the shape of the Bezier curve described by the 4 points given
   */
  public static Path createFromPoints(
      double startX, double startY,
      double point1X, double point1Y,
      double point2X, double point2Y,
      double endX, double endY
  ) {
    return createFromPoints(
        new Point2D(startX, startY),
        new Point2D(point1X, point1Y),
        new Point2D(point2X, point2Y),
        new Point2D(endX, endY)
    );
  }

  /**
   * Creates a Path given coordinates for 4 control points in a 2D plane. The starting and end
   * points are simple - the starting and ending points of the path. point1 and point2 represent two
   * control points - the first control point is co-linear with the tangent line at the starting
   * point, while the second control point is co-linear with the tangent line at the ending point.
   * You can see an example illustration here: https://i.imgur.com/Seae3mn.png
   *
   * @param start A Point2D object representing the starting point
   * @param point1 A Point2D object representing the first control point
   * @param point2 A Point2D object representing the second control point
   * @param end A Point2D object representing the ending point
   * @return a Path with the shape of the Bezier curve described by the 4 points given
   */
  public static Path createFromPoints(Point2D start, Point2D point1, Point2D point2, Point2D end) {
    Function<Double, Double> yDeriv = calculateParameterizedDerivative(start, point1, point2, end,
        Point2D::getY);
    Function<Double, Double> xDeriv = calculateParameterizedDerivative(start, point1, point2, end,
        Point2D::getX);

    return new Path(t -> yDeriv.apply(t) / xDeriv.apply(t), calculateLength(xDeriv, yDeriv));
  }

  /**
   * Helper function to calculate the derivative for a parameterized curve - that is, calculate the
   * x or y parameter's derivative, given the four points.
   *
   * @param start A Point2D object representing the starting point
   * @param point1 A Point2D object representing the first control point
   * @param point2 A Point2D object representing the second control point
   * @param end A Point2D object representing the ending point
   * @param getParameter A function that maps a Point2D object to it's value; either Point2D::getX
   * or Point2D::getY
   * @return A function representing either the X or Y parameterized derivative
   */
  private static Function<Double, Double> calculateParameterizedDerivative(Point2D start,
      Point2D point1, Point2D point2, Point2D end, Function<Point2D, Double> getParameter) {
    double startCoord = getParameter.apply(start);
    double endCoord = getParameter.apply(end);
    double point1Coord = getParameter.apply(point1);
    double point2Coord = getParameter.apply(point2);

    double linearTerm = 3 * (point1Coord - startCoord);
    double squareTerm = 3 * (startCoord + point2Coord - 2 * point1Coord);
    double cubicTerm = endCoord - startCoord + 3 * point1Coord - 3 * point2Coord;
    return t -> 3 * cubicTerm * Math.pow(t, 2) + 2 * squareTerm * t + linearTerm;
  }

  /**
   * Helper function to calculate the length of a path from scratch. Uses Legendre-Gauss quadrature
   * with n=24.
   *
   * @param xDeriv the function representing the X derivative of the Bezier curve
   * @param yDeriv the function representing the Y derivative of the Bezier curve
   * @return the approximate total length of the curve
   * @see <a href="https://pomax.github.io/bezierinfo/legendre-gauss.html">Mathematical
   * Explanation</a>
   * @see <a href="https://github.com/Pomax/bezierjs/blob/362ab0724d6710fd75659f6846035f3c743d8447/lib/utils.js#L99">Original
   * source</a>
   */
  private static double calculateLength(Function<Double, Double> xDeriv,
      Function<Double, Double> yDeriv) {
    // Here be dragons
    double z = 0.5, sum = 0.0;
    for (int i = 0; i < LGAbscissae.length; i++) {
      double t = z * LGAbscissae[i] + z;
      sum += LGWeights[i] * Math.hypot(xDeriv.apply(t), yDeriv.apply(t));
    }

    return z * sum;
  }

  /**
   * Simple function that estimates total distance traveled by taking the average of the encoder
   * readings on the left and right side of the drive train
   *
   * @param getLeftEncoderDistance the function that returns the distance reading that the left
   * encoder provides, in inches
   * @param getRightEncoderDistance the function that returns the distance reading that the right
   * encoder provides, in inches
   * @return the average of leftEncoderDistance and rightEncoderDistance
   */
  public static double defaultLengthDrivenEstimator(Supplier<Double> getLeftEncoderDistance,
      Supplier<Double> getRightEncoderDistance) {
    return (getLeftEncoderDistance.get() + getRightEncoderDistance.get()) / 2.0;
  }
}
