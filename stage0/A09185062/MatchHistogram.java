import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;

public class MatchHistogram implements PlugInFilter {
    public void run(ImageProcessor ip) {
        // target image I A (to be modified)
        // reference image I R(169-11.JPG)
        int[] hA = ip.getHistogram(); // get histogram for I A
        int[] hR = {0, 0, 0, 0, 0, 0, 0, 0, 10, 23, 50, 99, 149, 185, 285, 395, 419, 511, 523, 630, 650, 631, 653, 578, 533, 589, 586, 620, 581, 595, 559, 529, 533, 665,
                661, 643, 675, 748, 683, 739, 742, 775, 799, 813, 809, 805, 812, 789, 779, 770, 675, 639, 619, 601, 618, 662, 702, 624, 649, 607, 574, 544, 564, 503, 529
                546, 645, 642, 665, 799, 845, 934, 890, 890, 814, 772, 711, 739, 823, 811, 861, 859, 792, 707, 715, 716, 744, 778, 903, 832, 920, 834, 924, 931
                873, 835, 898, 892, 945, 1043, 1102, 1077, 1110, 1091, 1080, 1050, 1063, 1121, 1060, 1252, 1233, 1379, 1463, 1505, 1459, 1420, 1378, 1353, 1269, 1239, 1249
                1234, 1189, 1081, 1104, 1060, 1023, 1020, 1023, 1106, 1098, 1134, 1187, 1304, 1341, 1527, 1619, 1743, 1952, 2171, 2175, 2179, 2153, 2252, 2430, 2555, 2764
                3038, 3156, 3561, 3883, 4161, 4414, 4844, 5246, 6171, 7103, 7288, 6975, 7492, 8608, 9990, 10675, 10808, 10410, 9854, 9711, 8802, 8162, 7102, 6142, 5027, 3295
                1642, 625, 234, 99, 29, 12, 2, 1, 0, 0, 0, 0, 0};
        int[] fhs = matchHistograms(hA, hR); // mapping function
        ip.applyTable(fhs);
    }


    public int setup(String args, ImagePlus im) {
        return DOES_RGB;
    }

    /**
     * Method for matching the histograms
     *
     * @param hA - histogram h A of the target image I A
     * @param hR - reference histogram h R
     * @return the mapping f hs() to be applied to image I A
     */
    int[] matchHistograms(int[] hA, int[] hR) {
        //
        int K = hA.length;
        double[] PA = Cdf(hA);
        double[] PR = Cdf(hR);
        int[] fhs = new int[K];

        // compute mapping function f hs () :
        for (int a = 0; a < K; a++) {
            int j = K - 1;
            do {
                fhs[a] = j;
                j--;
            } while (j >= 0 && PA[a] <= PR[j]);
        }
        return fhs;
    }

    double[] Cdf(int[] h) {
        int K = h.length;
        int n = 0;

        for (int i = 0; i < K; i++) {
            n += h[i];
        }
        double[] P = new double[K];
        int c = h[0];

        P[0] = (double) c / n;

        for (int i = 1; i < K; i++) {
            c += h[i];
            P[i] = (double) c / n;
        }
        return P;
    }

}