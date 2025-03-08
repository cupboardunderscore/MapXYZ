package dev.letsgoaway.mapxyz;

public class MathUtils {
    public static boolean between(float x, float min, float max) {
        return x >= min && x <= max;
    }

    public static boolean between(double x, double min, double max) {
        return x >= min && x <= max;
    }

    public static boolean between(int x, int min, int max) {
        return x >= min && x <= max;
    }

    /**
     * If number is greater than the max, set it to max, and if number is lower than low, set it to low.
     *
     * @param num number to calculate
     * @param min the lowest value the number can be
     * @param max the greatest value the number can be
     * @return - min if num is lower than min <br>
     * - max if num is greater than max <br>
     * - num otherwise
     */
    public static float constrain(float num, float min, float max) {
        if (num > max) {
            num = max;
        }

        if (num < min) {
            num = min;
        }

        return num;
    }
}
