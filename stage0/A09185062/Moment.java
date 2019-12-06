import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Moment implements PlugInFilter {
    public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }

    double moment(ImageProcessor I, int p, int q) {
        double Mpq = 0.0;
        for (int v = 0; v < I.getHeight(); v++) {
            for (int u = 0; u < I.getWidth(); u++) {
                if (I.getPixel(u, v) > 0) {
                    Mpq += Math.pow(u, p) * Math.pow(v, q);
                }
            }
        }
        return Mpq;
    }

    /**
     * Central moments
     * @param I
     * @param p
     * @param q
     * @return
     */
    double centralMoment(ImageProcessor I, int p, int q) {
        double m00 = moment(I, 0, 0); // region area
        double xCtr = moment(I, 1, 0) / m00;
        double yCtr = moment(I, 0, 1) / m00;
        double cMpq = 0.0;
        for (int v = 0; v < I.getHeight(); v++) {
            for (int u = 0; u < I.getWidth(); u++) {
                if (I.getPixel(u, v) > 0) {
                    cMpq += Math.pow(u - xCtr, p) * Math.pow(v - yCtr, q);
                }
            }
        }
        return cMpq;
    }

    /**
     * Normalized central moments:
     * @param I
     * @param p
     * @param q
     * @return
     */
    double nCentralMoment(ImageProcessor I, int p, int q) {
        double m00 = moment(I, 0, 0);
        double norm = Math.pow(m00, 0.5 * (p + q + 2));
        return centralMoment(I, p, q) / norm;
    }

    /**
     * Orientation describes the direction of the major axis, that is,
     * the axis that runs through the centroid and along the widest part of the region
     * @param I
     * @return
     */
    double orientation(ImageProcessor I) {
        return (0.5 * Math.toDegrees(Math.atan(2 * centralMoment(I, 1, 1) / centralMoment(I, 2, 0) - centralMoment(I, 0, 2))));
    }

    /**
     *  A naive approach for computing the eccentricity could be to rotate
     *  the region until we can fit a bounding box (or enclosing ellipse) with a maximum aspect ratio
     * @param I
     * @return
     */
    double eccentricity(ImageProcessor I) {
        return ((centralMoment(I, 2, 0) + centralMoment(I, 0, 2) + Math.sqrt(Math.pow((centralMoment(I, 2, 0) - centralMoment(I, 0, 2)), 2) + 4 * (Math.pow(centralMoment(I, 1, 1), 2)))) / (centralMoment(I, 2, 0) + centralMoment(I, 0, 2) - Math.sqrt(Math.pow((centralMoment(I, 2, 0) - centralMoment(I, 0, 2)), 2) + 4 * (Math.pow(centralMoment(I, 1, 1), 2)))));
    }

    /**
     * Run
     * @param ip
     */
    public void run(ImageProcessor ip) {
        double orientationValue = orientation(ip);
        double eccentricityValue = eccentricity(ip);
        IJ.log("Orientation is: ");
        IJ.log(String.valueOf(orientationValue));
        IJ.log("Eccentricity is: ");
        IJ.log(String.valueOf(eccentricityValue));
    }

}
