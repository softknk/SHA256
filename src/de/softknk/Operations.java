package de.softknk;

public interface Operations {

    // (binary right shift) : shift_r(01101101, 3) --> 00001101
    static int shift_r(int x, int d) {
        return x >>> d;
    }

    // (binary right rotation) : rot_r(01101101, 3) --> 10101101
    static int rot_r(int x, int d) {
        return (x >>> d) | (x << -d);
    }

    /**
     * computes the xor of all input values
     *
     * @param x input values
     * @return xor value
     */
    static int xor(int... x) {
        int r = x[0];
        for (int i = 1; i < x.length; i++) r ^= x[i];
        return r;
    }

    /**
     * binary addition, but result size is limited by rbs
     *
     * @param rbs result bit size limit
     * @param x   input values
     * @return sum of input values, but limited through rbs
     */
    static int add(int rbs, int... x) {
        int r = 0;
        for (int t : x) r += t;
        return (int) (r % Math.pow(2, rbs));
    }

    /**
     * binary addition, but result size is limited by rbs
     *
     * @param rbs result bit size limit
     * @param x   input values
     * @return sum of input values, but limited through rbs
     */
    static String add(int rbs, String... x) {
        int[] values = new int[x.length];
        for (int i = 0; i < x.length; i++) values[i] = (int) Long.parseLong(x[i], 2);
        int r = add(rbs, values);
        return Integer.toBinaryString(r);
    }
}
