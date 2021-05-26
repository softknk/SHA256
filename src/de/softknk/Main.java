package de.softknk;

public class Main {

    public static void main(String[] args) throws InputSizeTooLargeException {
        SHA256 sha = new SHA256();
        System.out.println(sha.hash("abc"));
    }
}
