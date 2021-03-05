package nl.sneakersven.tsbprofilegenerator.tools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class ProfileJigger {

    private final String[] prefixes = { "Appt.", "Appartement", "Floor", "Verdieping", "Suite", "Room", "Kamer" };
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ADDRESS_2_SUFFIX_CHAR = "ABCDE";
    private static final String FIRST_NAME_SUFFIX = "ABCDEFGHKL";

    private final Random random;

    private ArrayList<String> jiggingPatterns = new ArrayList<>();

    public ProfileJigger() {
        this.random = new Random();

        //Initializing jigging patterns
//        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2); //1
//        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2); //2
//        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER); //3
//        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.RANDOM_NUMBER); //4
//        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_NUMBER + JiggSection.RANDOM_CHARS); //5
//        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS); //6
//        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_2 + JiggSection.ADDRESS1_2); //7
//        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_2 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS); //8
//        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2); //9
//        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS); //10
//        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER); //11
//        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER + JiggSection.RANDOM_CHARS); //12
//        jiggingPatterns.add(JiggSection.ADDRESS1_1+ JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS); //13
//        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2); //14
//        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER); //15
//        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS); //16

        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2); //1
        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER); //2
        jiggingPatterns.add(JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS); //3
        jiggingPatterns.add(JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2); //4
        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2); //5
        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_NUMBER); //6
        jiggingPatterns.add(JiggSection.RANDOM_NUMBER + JiggSection.ADDRESS1_1 + JiggSection.RANDOM_CHARS + JiggSection.ADDRESS1_2 + JiggSection.RANDOM_CHARS); //7

    }

    /**
     * Creates and returns a JSONObject containing card details and jigged address
     * @param ccDetails Card details
     * @param shippingDetails Shipping details
     * @param jigg Jigg number
     * @return JSONObject containing profile details
     */
    public JSONObject getProfileObject(String [] ccDetails, String[] shippingDetails, int jigg, boolean randomPhone, boolean jiggCity, int phoneNumberLength, String phoneNumberStart, String jiggingPatternChoice)
    {
        JSONObject profile = new JSONObject();
        JSONObject card = new JSONObject();
        JSONObject shipping = new JSONObject();
        JSONObject billing = new JSONObject();

        if(jiggingPatternChoice.contains(",")) {
            String[] choices = jiggingPatternChoice.split(",");
            int[] intChoices = new int[choices.length];

            for (int i = 0; i < choices.length; i++) {
                intChoices[i] = Integer.parseInt(choices[i]);
            }
        }

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

        //TODO: ArrayList maken met jiggingpatterns

        if(!jiggingPatternChoice.contains(",") && Integer.parseInt(jiggingPatternChoice) == jiggingPatterns.size()+1) {
            shipping.put("address", jiggAddress1(shippingDetails[2], jiggingPatterns.get( randomInt(0,jiggingPatterns.size()-1) )));
        } else {
            shipping.put("address", jiggAddress1(shippingDetails[2], jiggingPatterns.get( Integer.parseInt(jiggingPatternChoice)-1 )));
        }

        //Address fields jigging:
        shipping.put("firstName", (NameGenerator.getFirstName() + " " + getRandomString(FIRST_NAME_SUFFIX,randomInt(0,2))).trim());
        shipping.put("lastName", NameGenerator.getLastName());
        if(shippingDetails[3].equals("")) {
            shipping.put("address2", jiggAddress2());
        } else {
            shipping.put("address2", shippingDetails[3]);
        }
        shipping.put("country", shippingDetails[6]);
        if(jiggCity) {
            shipping.put("city", (shippingDetails[5] + " " + getRandomString(ALPHABET, randomInt(0, 2))).trim());
        } else {
            shipping.put("city", (shippingDetails[5]));
        }
        shipping.put("zip", shippingDetails[4]);
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
                    result += " " + randomInt(300, 999);
                    break;
            }
        }

        return result.trim();
    }

    public String jiggAddress2() {
        int random = randomInt(1,100);
        if(random < 51) {
            return prefixes[ randomInt(0, prefixes.length) ] + " " + randomInt(1,5) + getRandomString(ADDRESS_2_SUFFIX_CHAR,randomInt(0,1));
        }
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

    public int amountOfPatterns() {
        return jiggingPatterns.size();
    }

}
