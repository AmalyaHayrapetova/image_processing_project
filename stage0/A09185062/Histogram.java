import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.*;

public class Histogram implements PlugInFilter {

    //R G B
    double[] red = new double[256];
    double[] green = new double[256];
    double[] blue = new double[256];

    /**
     * Set up
     * @param args
     * @param im
     * @return
     */
    public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }


    public void run(ImageProcessor ip) {
        for (int i = 0; i < ip.getWidth(); i++) {
            for (int j = 0; j < ip.getHeight(); j++) {
                Color color = new Color(ip.getPixel(i, j));
                red[color.getRed()] += 1;
                blue[color.getBlue()] += 1;
                green[color.getGreen()] += 1;
            }
        }

        red[0] = red[0] / ip.getWidth() * ip.getHeight();
        green[0] = green[0] / ip.getWidth() * ip.getHeight();
        blue[0] = blue[0] / ip.getWidth() * ip.getHeight();

        for (int i = 1; i < red.length; i++) {
            red[i] = (red[i] / (ip.getWidth() * ip.getHeight())) + red[i - 1];
            green[i] = (green[i] / (ip.getWidth() * ip.getHeight())) + green[i - 1];
            blue[i] = (blue[i] / (ip.getWidth() * ip.getHeight())) + blue[i - 1];
        }

        IJ.log("Reds counts: ");
        for (int i = 0; i < red.length; i++) {
            IJ.log(Double.toString(red[i]));
        }

        IJ.log("Greens counts: ");
        for (int i = 0; i < green.length; i++) {
            IJ.log(Double.toString(green[i]));
        }

        IJ.log("Blues counts: ");
        for (int i = 0; i < blue.length; i++) {
            IJ.log(Double.toString(blue[i]));
        }
    }
}
