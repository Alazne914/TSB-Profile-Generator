package nl.sneakersven.tsbprofilegenerator;

import java.util.Random;

public class JiggingPattern {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random;

    private String jiggedAddress;
    private String address1;

    public JiggingPattern(String address1, String pattern) {
        this.address1 = address1;
        this.random = new Random();

        //Cutting up the address 1
        String[] addressParts = address1.split(" ");
        String streetname = addressParts[0];
        String streetnum = "";
        for (int i = 1; i < addressParts.length; i++) {
            streetnum += " " + addressParts[1];
        }

        //Pasting together the parts
        String result = "";
        String[] patterns = pattern.split(";");
        for (int i = 0; i < patterns.length; i++) {
            switch (patterns[i]) {
                case Jigg.ADDRESS1_1:
                    result += " " + streetname;
                    break;
                case Jigg.ADDRESS1_2:
                    result += " " + streetnum;
                    break;
                case Jigg.RANDOM_CHARS:
                    result += " " + getRandomString(ALPHABET, randomInt(2,4));
                    break;
                case Jigg.RANDOM_NUMBER:
                    result += " " + randomInt(100,999);
                    break;
            }
        }

        this.jiggedAddress = result.trim();
    }

    /**
     * Generates a String with specified length containing random characters
     * @param length Desired length of String
     * @return String with random characters
     */
    private String getRandomString(String chars, int length)
    {
        char[] charArray = chars.toCharArray();
        String result = "";

        for (int i = 0; i < length; i++) {
            int random = (int) (Math.random()*charArray.length);
            result += charArray[random];
        }

        return result;
    }

    /**
     * Generates random integer between min and max, including min and max.
     * Source: https://www.techiedelight.com/generate-random-integers-specified-range-java/
     * @param min Minimum
     * @param max Maximum
     * @return Random int
     */
    public int randomInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
    }

}
