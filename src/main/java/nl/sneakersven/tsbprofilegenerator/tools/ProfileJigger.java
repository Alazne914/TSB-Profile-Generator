package nl.sneakersven.tsbprofilegenerator.tools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class ProfileJigger {

    private String[] prefixes = { "Appt.", "Appartement", "Floor", "Verdieping", "Suite", "Room", "Kamer" };

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ADDRESS_2_SUFFIX_CHAR = "ABCDE";
    private static final String FIRST_NAME_SUFFIX = "ABCDEFGHKL";
    private static final int LOWER_BOUND_ADDRESS_1_RANDOM_NUMBER = 300;
    private static final int UPPER_BOUND_ADDRESS_1_RANDOM_NUMBER = 999;

    private final Random random;

    private ArrayList<String> patterns;

    public ProfileJigger() {
        this.random = new Random();
        patterns = new ArrayList<>();

        //Initializing jigging patterns
//        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2);
//        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
//        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
//        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2);
//        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2);
//        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
//        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);

        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS + JiggSection.RANDOM_NUMBER);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS);
        patterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER);

    }

    /**
     * Creates and returns a JSONObject containing card details and jigged address
     * @param ccDetails Card details
     * @param shippingDetails Shipping details
     * @param jigg Jigg number
     * @return JSONObject containing profile details
     */
    public JSONObject getProfileObject(String [] ccDetails, String[] shippingDetails, int jigg, boolean randomPhone, boolean jiggCity, int phoneNumberLength, String phoneNumberStart, int[] choices)
    {
        JSONObject profile = new JSONObject();
        JSONObject card = new JSONObject();
        JSONObject shipping = new JSONObject();
        JSONObject billing = new JSONObject();

        //Putting card details
        card.put("profileName", ccDetails[0] + " (J" + (jigg + 1) + ")");
        if(randomPhone) {
            double min = Math.pow(10, (phoneNumberLength - 1));
            double max = Math.pow(10, phoneNumberLength) - 1;
            card.put("phone", phoneNumberStart + randomInt((int) min,(int) max));
        } else {
            card.put("phone", ccDetails[1]);
        }
        card.put("ccNumber", ccDetails[2]);
        card.put("ccExpiry", ccDetails[3]);
        card.put("ccCvc", ccDetails[4]);

        //Putting Address details in JSONObject
        //First name
        shipping.put("firstName", (shippingDetails[0].equals("") ?
                (NameGenerator.getFirstName() + " " + getRandomString(FIRST_NAME_SUFFIX,randomInt(0,2))).trim() : shippingDetails[0]));
        //Lastname
        shipping.put("lastName", shippingDetails[1].equals("") ? NameGenerator.getLastName() : shippingDetails[1]);
        //Address 1
        if(choices.length == 1) {
            int choice = choices[0];
            shipping.put("address", (choice==patterns.size()+1 ?
                    jiggAddress1(shippingDetails[2], patterns.get(randomInt( 0,patterns.size()-1 ))) : jiggAddress1(shippingDetails[2], patterns.get( choices[0]-1 ))));
        } else {
            //Randomly choose pattern from list of specific patterns
            int randomChoice = choices[randomInt(0,choices.length-1)];
            if(randomChoice==patterns.size()) randomChoice = randomInt(0,patterns.size()-1);
            shipping.put("address", jiggAddress1(shippingDetails[2], patterns.get(randomChoice-1)));
        }
        //Address 2
        shipping.put("address2", (shippingDetails[3].equals("") ? jiggAddress2() : shippingDetails[3]));
        //Country
        shipping.put("country", shippingDetails[6]);
        //City
        shipping.put("city", (jiggCity ? (shippingDetails[5] + " " + getRandomString(ALPHABET,randomInt(0,2))).trim() : shippingDetails[5]));
        //Zipcode
        shipping.put("zip", shippingDetails[4]);
        //State
        shipping.put("state", (shippingDetails[7].equals("") ? JSONObject.NULL : shippingDetails[7]));

        //Put billing details
        if(shippingDetails[8].equals("true")) {
            billing.put("billingSameAsShipping", true);
        } else {
            billing.put("billingSameAsShipping", "");
            billing.put("firstName", shippingDetails[8]);
            billing.put("lastName", shippingDetails[9]);
            billing.put("address", shippingDetails[10]);
            billing.put("address2", shippingDetails[11]);
            billing.put("country", shippingDetails[14]);
            billing.put("city", shippingDetails[13]);
            billing.put("zip", shippingDetails[12]);
            billing.put("state", (shippingDetails[15].equals("") ? JSONObject.NULL : shippingDetails[15]));
        }

        //Put extra details
        profile.put("cc", card);
        profile.put("shipping", shipping);
        profile.put("billing", billing);
        profile.put("isJapaneseAddress", JSONObject.NULL);
        profile.put("isRussianAddress", JSONObject.NULL);
        profile.put("isMexicanAddress", JSONObject.NULL);
        profile.put("isPhilippinesAddress", JSONObject.NULL);
        profile.put("date", System.currentTimeMillis());

        return profile;
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
                    result += " " + randomInt(LOWER_BOUND_ADDRESS_1_RANDOM_NUMBER, UPPER_BOUND_ADDRESS_1_RANDOM_NUMBER);
                    break;
            }
        }

        return result.trim();
    }

    /**
     * Method to generate a random, jigged Address 2 field. 25% chance of happening
     * @return Jigged address 2 field
     */
    public String jiggAddress2() {
        int random = randomInt(1,100);
        if(random < 76) {
            return prefixes[ randomInt(0, prefixes.length-1) ] + " " + randomInt(1,5) + getRandomString(ADDRESS_2_SUFFIX_CHAR,randomInt(0,1));
        } else {
            return "";
        }
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

    public int amountOfPatterns() {
        return patterns.size();
    }

    public ArrayList<String> getPatterns() {
        return patterns;
    }
}
