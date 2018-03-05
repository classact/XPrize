package classact.com.clever_little_monkey.utils;

import java.util.List;
import java.util.Random;

/**
 * Created by hyunchanjeong on 2017/05/19.
 */

public class RandomExcluding {

    private static Random rnd;

    private RandomExcluding() {}

    public static List<Integer> nextInt(List<Integer> rndValues, int min, int exclude, int max, int n) {
        // Singleton instance
        if (rnd == null) {
            rnd = new Random();
        }

        // base case
        if (rndValues.size() == n) {
            return rndValues;
        }
        // Get range
        int range = max - min;

        // Get random value
        int rndValue = rnd.nextInt(range) + min;

        if (!(rndValue == exclude || rndValues.contains(rndValue)) && rndValue >= min && rndValue < max ) {
            rndValues.add(rndValue);
        }
        return nextInt(rndValues, min, exclude, max, n);
    }

    public static int nextInt(int min, int exclude, int max) {
        if (rnd == null) {
            rnd = new Random();
        }

        int range = max - min;
        int rndValue = rnd.nextInt(range) + min;

        if (rndValue >= min && rndValue != exclude && rndValue < max) {
            return rndValue;
        } else {
            return nextInt(min, exclude, max);
        }
    }
}
