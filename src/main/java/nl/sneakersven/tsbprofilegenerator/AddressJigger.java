package nl.sneakersven.tsbprofilegenerator;

import java.util.Random;

public class AddressJigger {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final Random random;

    public AddressJigger() {
        this.random = new Random();
    }

    /**
     * Method that jiggs the Address 1. See full guide for different patterns
     * @param address1 User input Address 1
     * @param pattern Jiggingpattern
     * @return Jigged Address 1 String
     */
    public String jiggAddress1(String address1, String pattern) {
        //Cutting up the address 1
        String[] addressParts = address1.split(" ");
        String streetname = addressParts[0];
        String housenum = "";
        for (int i = 1; i < addressParts.length; i++) {
            housenum += " " + addressParts[1];
        }
        housenum = housenum.trim();

        //Pasting together the parts
        String result = "";
        String[] patterns = pattern.split(";");
        for (int i = 0; i < patterns.length; i++) {
            String currentPattern = patterns[i];

            switch (currentPattern + ";") {
                case JiggSection.ADDRESS1_1:
                    result += " " + streetname;
                    break;
                case JiggSection.ADDRESS1_2:
                    result += " " + housenum;
                    break;
                case JiggSection.RANDOM_CHARS:
                    result += " " + getRandomString(ALPHABET, randomInt(2, 4));
                    break;
                case JiggSection.RANDOM_NUMBER:
                    result += " " + randomInt(300, 999);
                    break;
            }
        }

        return result.trim();
    }

    public String jiggAddress2(String address2) {
        return null;
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
