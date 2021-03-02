package nl.sneakersven.tsbprofilegenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHABET_WITH_NUMBERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321";
    private static final String ADDRESS_2_SUFFIX_CHAR = "ABCDE";
    private static final String FIRST_NAME_SUFFIX = "ABCDEFGHKL";

    private final String INPUT_FOLDER = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator;
    private final String INPUT_TXT_NAME = "creditcard_details_input.txt";
    private final String OUTPUT_FOLDER = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator;
    private final String OUTPUT_TXT_NAME = "creditcard_details_output.txt";
    private final String OUTPUT_JSON_NAME = "profiles_output.json";

    private static Random random;

    private int amtOfJiggs;
    private int numOfProfiles = 0;
    private boolean randomPhone;
    private boolean jiggCity;
    private boolean billingSameAsShipping;
    private boolean alreadyJigged;
    private String phoneNumberStart;
    private int phoneNumberLength;
    private int jiggingPattern;

    private final String[] prefixes = { "Appt.", "Appartement", "Floor", "Verdieping", "Suite", "Room", "Kamer" };

    public static void main(String[] args)
    {
        new ProfileGenerator().run();
    }

    /**
     * Starts the program by asking the user for input
     */
    public void run()
    {
        //Initializing global objects
        random = new Random();

        //Welcome user
        System.out.println("\nWELCOME TO THE TSB PROFILE GENERATOR!\n\n" +
                           "Refer to the guide for further information.\n" +
                           "Good luck pooping!\n");

        //Ask for what functionality the user would like to use
        Scanner input = new Scanner(System.in);
        System.out.print("What would you like to do?" +
                       "\n 1) Generate jigged profiles" +
                       "\n 2) Retrieve CC details from TSB export" +
                       "\nInput: ");
        int choice = input.nextInt();

        while (choice < 1 || choice > 2) {
            System.out.print("Input out of bounds, please try again: ");
            choice = input.nextInt();
        }

        setup(choice);
    }

    /**
     * This method ensures that the input directory exists, and runs the desired function.
     * If the input directory does not exist, the user will be notified
     */
    public void setup(int choice)
    {
        File f = new File(INPUT_FOLDER + INPUT_TXT_NAME);

        if(!f.getParentFile().exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();

                if(choice == 1) {
                    System.out.println("\nThe input folder containing the creditcard_details_input.txt file has been created." +
                            "\nPlease type or paste your credit card details in that file, and rerun the generator." +
                            "\n\nThe folder is located at: C:/TSB Profile Generator/input" +
                            "\nCheck the guide on how to format the input file!");
                } else if(choice == 2) {
                    System.out.println("\nThe input folder has been created." +
                            "\nPlease place your TSB profiles export file in that folder, and rerun the generator." +
                            "\nIn that folder, the creditcard_details_input.txt file also has been created." +
                            "\nYou can use that file for generating jigged profiles." +
                            "\n\nThe folder is located at: C:/TSB Profile Generator/input" +
                            "\nCheck the guide for further information!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if(choice == 1) {
                generateProfiles();
            } else if(choice == 2) {
                retrieveCcDetails();
            }
        }

    }

    /**
     * Method that reads profile details from the .txt file, and puts them in a JSONArray
     */
    public void generateProfiles()
    {
        ArrayList<String[]> cards = new ArrayList<>();
        ArrayList<File> txtFiles = getFiles(".txt",new File(INPUT_FOLDER));

        if(txtFiles.size() == 0) {
            System.out.println("\nInput .txt file has not been found! Creating a file now.\nType/paste your credit card details in that file and rerun the program.");
            try {
                File f = new File(INPUT_FOLDER + INPUT_TXT_NAME);
                f.getParentFile().mkdirs();
                f.createNewFile();
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Oops! Something went wrong while creating the credit cards input file...\n");
                e.printStackTrace();
            }
        } else {
            try {
                File cardsFile = txtFiles.get(0);

                Scanner reader = new Scanner(cardsFile);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();

                    cards.add(data.split(";"));
                }
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("\nOops! An error occurred." +
                        "\nNotifying the dev of this error will be appreciated!" +
                        "\nInfo about error:");
                e.printStackTrace();
            }
        }

        //If there are cards available, ask about user input
        if(cards.size() > 0) {
            Scanner input = new Scanner(System.in);
            String[] shippingDetails = new String[16];

            final int LINE_LENGTH = 12;

            System.out.println("\nJIGGING SETTINGS");
            System.out.print("Which of the following jigging patterns for the Address 1 field do you want to use?" +
                    "\n(Note that the 'TSBPG' will be randomized characters, and the the '534' will be a random number)" +
                    "\n 1) TSBPG Streetname 1" +
                    "\n 2) TSBPG Streetname 1 534" +
                    "\n 3) TSBPG Streetname 1 534 TSBPG" +
                    "\n 4) TSBPG Streetname 1 TSBPG" +
                    "\n 5) Streetname 1 534 TSBPG" +
                    "\n 6) Streetname 1 TSBPG" +
                    "\n 7) Randomized mix of all of the patterns above" +
                    "\nInput: ");
            jiggingPattern = Integer.parseInt(input.nextLine());
            while (jiggingPattern < 1 || jiggingPattern > 7) {
                System.out.print("Input out of bounds, please try again: ");
                jiggingPattern = Integer.parseInt(input.nextLine());
            }

            randomPhone = userSaysYes(input, "Do you want to randomize the profile's phone number? (y/n): ");
            if(randomPhone) {
                System.out.print("What should the beginning of each phone number be? (e.g. +316 for NL): ");
                phoneNumberStart = input.nextLine();
                System.out.print("How much numbers should be added to the previously provided beginning of the phone number? (e.g. 8 for NL): ");
                phoneNumberLength = Integer.parseInt(input.nextLine());
            }

            boolean randomNames = userSaysYes(input, "Do you want to randomize the profile's first and last name? (y/n): ");
            jiggCity = userSaysYes(input, "Do you want to jigg random characters to the end of the City field? (y/n): ");
            billingSameAsShipping = userSaysYes(input, "Does the billing address have to be the same as the shipping address? (y/n): ");
            System.out.print("How many times would you like to jigg your adress for each credit card: ");
            amtOfJiggs = Integer.parseInt(input.nextLine());

            //First we ask user for input about address
            System.out.println("\nSHIPPING DETAILS" +
                    "\nIf you want a field to be empty, just type nothing and hit enter." +
                    "\n(If you enter something for address 2, it will not get jigged)");
            if(!randomNames) {
                print("First name: ", LINE_LENGTH);
                shippingDetails[0] = input.nextLine();
                print("Last name: ", LINE_LENGTH);
                shippingDetails[1] = input.nextLine();
            }
            print("Address 1: ", LINE_LENGTH);
            shippingDetails[2] = input.nextLine();
            print("Address 2: ", LINE_LENGTH);
            shippingDetails[3] = input.nextLine();
            print("Zip: ", LINE_LENGTH);
            shippingDetails[4] = input.nextLine();
            print("City: ", LINE_LENGTH);
            shippingDetails[5] = input.nextLine();
            print("Country: ", LINE_LENGTH);
            shippingDetails[6] = input.nextLine();
            print("State: ", LINE_LENGTH);
            shippingDetails[7] = input.nextLine();

            if(billingSameAsShipping) {
                shippingDetails[8] = "true";
            } else {
                System.out.println("\nBILLING DETAILS");
                print("First name: ", LINE_LENGTH);
                shippingDetails[8] = input.nextLine();
                print("Last name: ", LINE_LENGTH);
                shippingDetails[9] = input.nextLine();
                print("Address 1: ", LINE_LENGTH);
                shippingDetails[10] = input.nextLine();
                print("Address 2: ", LINE_LENGTH);
                shippingDetails[11] = input.nextLine();
                print("Zip: ", LINE_LENGTH);
                shippingDetails[12] = input.nextLine();
                print("City: ", LINE_LENGTH);
                shippingDetails[13] = input.nextLine();
                print("Country: ", LINE_LENGTH);
                shippingDetails[14] = input.nextLine();
                print("State: ", LINE_LENGTH);
                shippingDetails[15] = input.nextLine();
            }



            //Create desired amount of profiles for each provided credit card
            JSONArray jsonArray = new JSONArray();
            for (int i=0; i<cards.size(); i++)
            {
                String[] ccDetails = cards.get(i);

                for(int j=0; j<amtOfJiggs; j++)
                {
                    JSONObject c = getProfileObject(ccDetails, shippingDetails, j);
                    jsonArray.put(c);
                    numOfProfiles++;
                }
            }

            //Write profiles to .json file. This file will be located in C:/TSB Profile Generator/output
            try {
                File f = new File(OUTPUT_FOLDER + OUTPUT_JSON_NAME);

                f.getParentFile().mkdirs();
                f.createNewFile();

                String json = jsonArray.toString(4);
                Files.write(Paths.get(f.getPath()), json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("\nNumber of profiles created: " + numOfProfiles +
                    "\nThe file containing the profiles can be found at: C:/TSB Profile Generator/output");
        } else {
            System.out.println("\nOops! Your creditcard_details_input.txt file is empty!" +
                    "\nType/paste your details in the file and rerun the program!" +
                    "\nThe file can be found at: C:/TSB Profile Generator/input");
        }

    }

    /**
     * Retrieves all profiles with unique credit cards from .json file (TSB export).
     * Writes them off to a .txt file that's ready to be used to generate new profiles
     */
    public void retrieveCcDetails()
    {
        HashMap<String, JSONObject> creditCards = new HashMap<>();


        try {
            File f = getFiles(".json", new File(INPUT_FOLDER)).get(0);
            String jsonString = new String(Files.readAllBytes(Paths.get(f.getPath())));
            JSONArray array = new JSONArray(jsonString);

            //Filters all the profiles for profiles with unique credit cards, those profiles get added to a HashMap
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                creditCards.put(obj.getJSONObject("cc").getString("ccNumber"), obj);
            }

            //Write all unique credit cards to .txt file. Ready to be used to generate new profiles
            String txtOutputPath = OUTPUT_FOLDER + OUTPUT_TXT_NAME;
            File output = new File(txtOutputPath);
            output.getParentFile().mkdirs();
            output.createNewFile();

            FileWriter fstream = new FileWriter(output);
            BufferedWriter info = new BufferedWriter(fstream);

            ArrayList<JSONObject> unorderedCCList = new ArrayList<>(creditCards.values());
            ArrayList<JSONObject> orderedCCList;
            ArrayList<JSONObject> numericProfileNames = new ArrayList<>();
            ArrayList<JSONObject> alphabeticalProfileNames = new ArrayList<>();

            //Before sorting, remove the "( J1 )" if present
            for (JSONObject obj : unorderedCCList) {
                String newName = obj.getJSONObject("cc").getString("profileName").split("\\(")[0].trim();
                obj.getJSONObject("cc").put("profileName", newName);
            }

            //Computing to sort the profiles based on the profileName field
            for (JSONObject obj : unorderedCCList) {
                String pnEnd = obj.getJSONObject("cc").getString("profileName").split(" ")[ obj.getJSONObject("cc").getString("profileName").split(" ").length-1 ];
                System.out.println(pnEnd);

                if( pnEnd.chars().allMatch( Character::isDigit ) ) {
                    numericProfileNames.add(obj);
                } else {
                    alphabeticalProfileNames.add(obj);
                }
            }

            //Sorting both arraylists
            numericProfileNames.sort(Comparator.comparing(o -> Integer.parseInt(o.getJSONObject("cc").getString("profileName").split(" ")[o.getJSONObject("cc").getString("profileName").split(" ").length - 1])));
            alphabeticalProfileNames.sort(Comparator.comparing(o -> o.getJSONObject("cc").getString("profileName")));

            //Adding arraylists together
            numericProfileNames.addAll(alphabeticalProfileNames);
            orderedCCList = numericProfileNames;

            //Writing credit cards to .txt file
            int counter = 0;
            for (JSONObject obj : orderedCCList) {
                JSONObject cc = obj.getJSONObject("cc");

                String line = String.format(cc.getString("profileName") +
                            ";" + cc.getString("phone") +
                            ";" + cc.getString("ccNumber") +
                            ";" + cc.getString("ccExpiry") +
                            ";" + cc.getString("ccCvc")  + "%n");

                info.write(line);
                counter++;
            }

            info.close();

            System.out.println("\n" + counter + " profiles written to output file successfully. File is located at C:/TSB Profile Generator/output");

        } catch (IndexOutOfBoundsException | IOException e) {
            if(e instanceof IndexOutOfBoundsException) {
                System.out.println("\nOops! You didn't place a .json file in the input folder..." +
                        "\nPlease place your TSB profiles export in that folder and rerun the generator");
            } else {
                e.printStackTrace();
            }
        }

    }

    /**
     * Creates and returns a JSONObject containing card details and jigged address
     * @param ccDetails Card details
     * @param shippingDetails Shipping details
     * @param jigg Jigg number
     * @return JSONObject containing profile details
     */
    public JSONObject getProfileObject(String [] ccDetails, String[] shippingDetails, int jigg)
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

        //Address 1 jigging:
        switch (jiggingPattern) {
            //Pattern 1: TSBPG Streetname 1
            case 1:
                shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2]);
                break;

            //Pattern 2: TSBPG Streetname 1 534
            case 2:
                shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2] + " " + randomInt(100,999));
                break;

            //Pattern 3: TSBPG Streetname 1 534 TSBPG
            case 3:
                shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2] + " " + randomInt(100,999) + " " + getRandomString(ALPHABET, randomInt(2,4)));
                break;

            //Pattern 4: TSBPG Streetname 1 TSBPG
            case 4:
                shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2] + " " + getRandomString(ALPHABET, randomInt(2,4)));
                break;

            //Pattern 5: Streetname 1 534 TSBPG
            case 5:
                shipping.put("address", shippingDetails[2] + " " + randomInt(100,999) + " " + getRandomString(ALPHABET, randomInt(2,4)));
                break;

            //Pattern 6: Streetname 1 TSBPG
            case 6:
                shipping.put("address", shippingDetails[2] + " " + getRandomString(ALPHABET, randomInt(2,4)));
                break;

            //Random mix
            case 7:
                switch (randomInt(1,6)) {
                    case 1:
                        shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2]);
                        break;

                    //Pattern 2: TSBPG Streetname 1 534
                    case 2:
                        shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2] + " " + randomInt(100,999));
                        break;

                    //Pattern 3: TSBPG Streetname 1 534 TSBPG
                    case 3:
                        shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2] + " " + randomInt(100,999) + " " + getRandomString(ALPHABET, randomInt(2,4)));
                        break;

                    //Pattern 4: TSBPG Streetname 1 TSBPG
                    case 4:
                        shipping.put("address", getRandomString(ALPHABET, randomInt(2,4)) + " " + shippingDetails[2] + " " + getRandomString(ALPHABET, randomInt(2,4)));
                        break;

                    //Pattern 5: Streetname 1 534 TSBPG
                    case 5:
                        shipping.put("address", shippingDetails[2] + " " + randomInt(100,999) + " " + getRandomString(ALPHABET, randomInt(2,4)));
                        break;

                    //Pattern 6: Streetname 1 TSBPG
                    case 6:
                        shipping.put("address", shippingDetails[2] + " " + getRandomString(ALPHABET, randomInt(2,4)));
                        break;
                }
                break;
        }

        //Address fields jigging:
        shipping.put("firstName", (NameGenerator.getFirstName() + " " + getRandomString(FIRST_NAME_SUFFIX,randomInt(0,2))).trim());
        shipping.put("lastName", NameGenerator.getLastName());
        if(shippingDetails[3].equals("")) {
            if (randomInt(0,100) < 75) {
                shipping.put("address2", prefixes[randomInt(0, prefixes.length - 1)] + " " + randomInt(1, 5) + getRandomString(ADDRESS_2_SUFFIX_CHAR, 1));
            } else {
                shipping.put("address2", shippingDetails[3]);
            }
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
     * Method for asking user for a 'y' or 'n' input
     * @param input Scanner for input
     * @param message String to print
     * @return true if 'y', false if 'n'
     */
    private boolean userSaysYes(Scanner input, String message) {
        System.out.print(message);
        String yesOrNo = input.nextLine();

        while ( !yesOrNo.equalsIgnoreCase("y") && !yesOrNo.equalsIgnoreCase("n") ) {
            System.out.print("Unknown input, please type 'y' for yes or 'n' for no: ");
            yesOrNo = input.nextLine();
        }

        if(yesOrNo.equalsIgnoreCase("y")) {
            return true;
        } else {
            return false;
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
     * Gets list of files in a folder that are a certain extension. Source: https://stackoverflow.com/a/26614854
     * @param extension String extension
     * @param folder Folder containing file
     * @return ArrayList of Files with provided extension
     */
    public static ArrayList<File> getFiles(String extension, final File folder)
    {
        extension = extension.toUpperCase();

        final ArrayList<File> files = new ArrayList<File>();
        for (final File file : folder.listFiles())
        {
            if (file.isDirectory())
                files.addAll(getFiles(extension, file));
            else if (file.getName().toUpperCase().endsWith(extension))
                files.add(file);
        }

        return files;
    }

    /**
     * Prints a String of at least the given width, right justified
     * @param line String to print
     * @param width Minimum length of printed String
     */
    private void print(String line, int width) {
        String format = "%" + width + "s";
        System.out.printf(format, line);
    }

    /**
     * Generates random integer between min and max, including min and max.
     * Source: https://www.techiedelight.com/generate-random-integers-specified-range-java/
     * @param min Minimum
     * @param max Maximum
     * @return Random int
     */
    public static int randomInt(int min, int max)
    {
        return random.nextInt(max - min + 1) + min;
    }
}
