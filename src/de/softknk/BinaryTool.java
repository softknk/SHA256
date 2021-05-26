package de.softknk;

public class BinaryTool {

    /**
     * transforms a binary number, so that its size equals limit
     * scale_binary("1101", 8) --> "00001101"
     *
     * @param data  binary number
     * @param limit preferred size (number of bits)
     * @return scaled binary number
     */
    public static String scale_binary(String data, int limit) {
        int needed = limit - data.length();
        StringBuilder scale_builder = new StringBuilder();
        scale_builder.append(data);
        for (int i = 0; i < needed; i++) scale_builder.insert(0, "0");
        return scale_builder.toString();
    }

    /**
     * transforms a binary number, so that its size equals limit
     *
     * @param data  binary number
     * @param limit preferred size (number of bits)
     * @return scaled binary number
     */
    public static String scale_binary(int data, int limit) {
        String binary = Integer.toBinaryString(data);
        int needed = limit - binary.length();
        StringBuilder scale_builder = new StringBuilder();
        scale_builder.append(binary);
        for (int i = 0; i < needed; i++) scale_builder.insert(0, "0");
        return scale_builder.toString();
    }

    /**
     * transforms a hex number, so that its size equals limit
     * scale_hex("4AC6", 6) --> "004AC6"
     *
     * @param data  hex number
     * @param limit preferred size
     * @return scaled hex number
     */
    public static String scale_hex(String data, int limit) {
        int needed = limit - data.length();
        StringBuilder scale_builder = new StringBuilder();
        scale_builder.append(data);
        for (int i = 0; i < needed; i++) scale_builder.insert(0, "0");
        return scale_builder.toString();
    }

    /**
     * fills the given binary number with zeros, so that its size equals n
     *
     * @param data binary number
     * @param n    preferred size (number of bits)
     * @return filled binary number
     */
    public static String fill_binary(String data, int n) {
        StringBuilder builder = new StringBuilder();
        builder.append(data);
        builder.append("1"); // marks the beginning of filling
        for (int i = 1; i < n; i++) builder.append("0");
        return builder.toString();
    }
}
