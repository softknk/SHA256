package de.softknk;

import static de.softknk.Operations.shift_r;
import static de.softknk.Operations.rot_r;
import static de.softknk.Operations.xor;
import static de.softknk.Operations.add;

public interface Functions {

    static int sigma_zero(int x) {
        return xor(rot_r(x, 7), rot_r(x, 18), shift_r(x, 3));
    }

    static int sigma_one(int x) {
        return xor(rot_r(x, 17), rot_r(x, 19), shift_r(x, 10));
    }

    static int Sigma_zero(int x) {
        return xor(rot_r(x, 2), rot_r(x, 13), rot_r(x, 22));
    }

    static int Sigma_one(int x) {
        return xor(rot_r(x, 6), rot_r(x, 11), rot_r(x, 25));
    }

    // if x == 1 take y, else take z
    static int choice(int x, int y, int z) {
        return (x & (y & z | y)) | (z & y | (xor(x, y, z) & z));
    }

    // if there are at least two 1's in x, y and z, then 1, otherwise 0
    static int majority(int x, int y, int z) {
        return (x & y) | (y & z) | (x & z);
    }
}
