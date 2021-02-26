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

    private static boolean ASC = true;
    private static boolean DESC = false;

    private String inputFolder = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input";
    private String outputFolder = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output";
    private String inputFilePath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "creditcard_details_input_text.txt";
    private String outputFilePath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "profiles_output_json.json";

    private int amtOfJiggs;
    private int numOfProfiles = 0;
    private int choice;

    public static void main(String[] args)
    {
        new ProfileGenerator().run();
    }

    /**
     * Starts the program by asking the user for input
     */
    public void run()
    {
        System.out.println("- Welcome to the TSB Profile Generator! -\n\n" +
                           "Refer to the guide on how to use the program.\n" +
                           "Good luck pooping!\n");

        Scanner input = new Scanner(System.in);
        System.out.print("Do you want to generate profiles, or retrieve CC details from your profiles export?" +
                       "\n 1) Generate profiles" +
                       "\n 2) Retrieve CC details" +
                       "\nInput: ");
        choice = input.nextInt();

        while (choice < 1 || choice > 2) {
            System.out.print("\nUnkown input, please try again!" +
                             "\n Input: ");
            choice = input.nextInt();
        }

        setup();
    }

    /**
     * This method ensures that the input directory exists, and runs the desired function.
     * If the input directory does not exist, the user will be notified
     */
    public void setup()
    {
        File f = new File(inputFilePath);

        if(!f.getParentFile().exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();

                if(choice == 1) {
                    System.out.println("The input folder containing the profiles_input_text.txt has been created." +
                            "\nPlease paste the profile details in that file, and run the generator again." +
                            "\nThe folder is located at: C:/TSB Profile Generator/input" +
                            "\nCheck the guide for how to format the input file.");
                } else if(choice == 2) {
                    System.out.println("The input folder has been created." +
                            "\nPlease paste your profiles export file in that folder, and run the generator again." +
                            "\nThe folder is located at: C:/TSB Profile Generator/input" +
                            "\nCheck the guide for further information.");
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
        ArrayList<File> txtFiles = getFiles(".txt",new File(inputFolder));

        if(txtFiles.size() == 0) {
            System.out.println("Input .txt file has not been found! Creating a file now. Type/paste your credit card details in that file and rerun the program\n");

            //Write all unique credit cards to .txt file. Ready to be used to generate new profiles
            String txtInputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "creditcard_details_input.txt";
            try {
                File f = new File(txtInputPath);
                f.getParentFile().mkdirs();
                f.createNewFile();
                System.exit(1);
            } catch (IOException e) {
                System.out.println("Oops! Something went wrong while creating the credit cards input file...\n");
                e.printStackTrace();
            }
        } else {
            //Read cards and details from .txt file. Source of sorting: https://stackoverflow.com/a/16751550
            Collections.sort(cards, new Comparator<String[]>() {
                @Override
                public int compare(String[] a1, String[] a2) {
                    if (Integer.valueOf( a1[0].split(" ")[1] ) > Integer.valueOf( a2[0].split(" ")[1] ))
                        return 1;
                    if (Integer.valueOf( a1[0].split(" ")[1] ) < Integer.valueOf( a2[0].split(" ")[1] ))
                        return -1;
                    return 0;
                }
            });

            try {
                File cardsFile = txtFiles.get(0);

                Scanner reader = new Scanner(cardsFile);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();

                    cards.add(data.split(";"));
                }
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        if(cards.size() > 0) {
            Scanner input = new Scanner(System.in);
            String[] shippingdetails = new String[16];

            final int LINE_LENGTH = 12;

            //First we ask user for input about address
            System.out.println("First we need your address details. If you want a field to be empty, just type nothing and hit enter.");
            print("First name: ", LINE_LENGTH);
            shippingdetails[0] = input.nextLine();
            print("Last name: ", LINE_LENGTH);
            shippingdetails[1] = input.nextLine();
            print("Address 1: ", LINE_LENGTH);
            shippingdetails[2] = input.nextLine();
            print("Address 2: ", LINE_LENGTH);
            shippingdetails[3] = input.nextLine();
            print("Zip: ", LINE_LENGTH);
            shippingdetails[4] = input.nextLine();
            print("City: ", LINE_LENGTH);
            shippingdetails[5] = input.nextLine();
            print("Country: ", LINE_LENGTH);
            shippingdetails[6] = input.nextLine();
            print("State: ", LINE_LENGTH);
            shippingdetails[7] = input.nextLine();

            System.out.print("Does the billing address have to be the same as the shipping address? (y/n): ");
            String shippingSameAsBilling = input.nextLine();

            while ( !shippingSameAsBilling.toLowerCase().trim().equals("y") && !shippingSameAsBilling.toLowerCase().trim().equals("n") ) {
                System.out.print("Unknown input, please type 'y' for yes or 'n' for no: ");
                shippingSameAsBilling = input.nextLine();
            }

            if(shippingSameAsBilling.toLowerCase().trim().equals("n")) {
                print("First name: ", LINE_LENGTH);
                shippingdetails[8] = input.nextLine();
                print("Last name: ", LINE_LENGTH);
                shippingdetails[9] = input.nextLine();
                print("Address 1: ", LINE_LENGTH);
                shippingdetails[10] = input.nextLine();
                print("Address 2: ", LINE_LENGTH);
                shippingdetails[11] = input.nextLine();
                print("Zip: ", LINE_LENGTH);
                shippingdetails[12] = input.nextLine();
                print("City: ", LINE_LENGTH);
                shippingdetails[13] = input.nextLine();
                print("Country: ", LINE_LENGTH);
                shippingdetails[14] = input.nextLine();
                print("State: ", LINE_LENGTH);
                shippingdetails[15] = input.nextLine();
            } else {
                shippingdetails[8] = "true";
            }

            //At last, the amount of times each card needs to be jigged
            System.out.print("How many times would you like to jigg your adress for each credit card?\nInput: ");
            amtOfJiggs = input.nextInt();

            //Create desired amount of profiles for each provided credit card
            JSONArray jsonArray = new JSONArray();
            for (int i=0; i<cards.size(); i++)
            {
                String[] ccdetails = cards.get(i);

                for(int j=0; j<amtOfJiggs; j++)
                {
                    JSONObject c = getProfileObject(ccdetails, shippingdetails, j);
                    jsonArray.put(c);
                    numOfProfiles++;
                }
            }

            //Write profiles to .json file. This file will be located in C:/TSB Profile Generator/output
            try {
                File f = new File(outputFilePath);

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
            System.out.println("Your profiles_input_text.txt file is empty! Paste your details in the file and rerun the program");
        }

    }

    /**
     * Retrieves all profiles with unique credit cards from .json file (TSB export).
     * Writes them off to a .txt file that's ready to be used to generate new profiles
     */
    public void retrieveCcDetails()
    {
        HashMap<String, JSONObject> creditCards = new HashMap<>();

        //Filters all the profiles for profiles with unique credit cards, those profiles get added to a HashMap
        try {
            File f = getFiles(".json", new File(inputFolder)).get(0);
            String jsonString = new String(Files.readAllBytes(Paths.get(f.getPath())));
            JSONArray array = new JSONArray(jsonString);

            if(array.length() < 1) {
                System.out.println("No profiles found in ");
            }

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                creditCards.put(obj.getJSONObject("cc").getString("ccNumber"), obj);
            }

            //Write all unique credit cards to .txt file. Ready to be used to generate new profiles
            String txtOutputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "creditcard_details_output_text.txt";
            File output = new File(txtOutputPath);
            output.getParentFile().mkdirs();
            output.createNewFile();

            FileWriter fstream = new FileWriter(output);
            BufferedWriter info = new BufferedWriter(fstream);

            Map<String, JSONObject> sortedProfiles = sortByValue(creditCards, ASC);
            int counter = 0;
            for (JSONObject obj : sortedProfiles.values()) {
                JSONObject cc = obj.getJSONObject("cc");

                String[] profileNameSplit = cc.getString("profileName").split(" ");
                profileNameSplit[ profileNameSplit.length - 1 ] = "";
                String profileName = String.join(" ", profileNameSplit);

                String line = String.format(profileName.trim() +
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
                System.out.println("Oops! You didn't place a .json file in the input folder..." +
                        "\nPlease place your TSB profiles export in that folder and run the program again");
            } else {
                e.printStackTrace();
            }
        }

    }

    /**
     * Creates and returns a JSONObject containig card details and jigged address
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

        //Generating random info used for jigging
        int length = (int) (Math.random()*4) + 2;
        String address1Jig1 = getRandomString((int) (Math.random()*5) + 2);
        String address1Jig2 = getRandomString((int) (Math.random()*5) + 2);
        String cityJig = getRandomString((int) (Math.random()*3) + 2);

        //Putting card details
        card.put("profileName", ccDetails[0] + " (J" + (jigg + 1) + ")");
        card.put("phone", ccDetails[1]);
        card.put("ccNumber", ccDetails[2]);
        card.put("ccExpiry", ccDetails[3]);
        card.put("ccCvc", ccDetails[4]);

        //Put shipping details
        shipping.put("firstName", shippingDetails[0] + " " + getRandomString(2));
        shipping.put("lastName", shippingDetails[1]);
        shipping.put("address", address1Jig2 + " " + shippingDetails[2] + " " + address1Jig1.trim());
        shipping.put("address2", shippingDetails[3]);
        shipping.put("country", shippingDetails[6]);
        shipping.put("city", shippingDetails[5] + " " + cityJig);
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
     * Generates a String with specified length containing random characters
     * @param length Desired length of String
     * @return String with random characters
     */
    private String getRandomString(int length)
    {
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        String result = "";

        for (int i = 0; i < length; i++) {
            int random = (int) (Math.random()*alphabet.length);
            result += alphabet[random];
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
     * Sorts Map by certain value. Will sort by keys if values are same. Source: https://stackoverflow.com/a/13913206
     * @param unsortMap Unsorted Map
     * @param order true for ASC, false for DESC
     * @return Ordered map
     */
    private static Map<String, JSONObject> sortByValue(Map<String, JSONObject> unsortMap, final boolean order)
    {
        List<Map.Entry<String, JSONObject>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? Integer.valueOf( o1.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] ).compareTo(Integer.valueOf( o2.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] )) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : Integer.valueOf( o1.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] ).compareTo(Integer.valueOf( o2.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] )) : Integer.valueOf( o2.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] ).compareTo(Integer.valueOf( o1.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] )) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : Integer.valueOf( o2.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] ).compareTo(Integer.valueOf( o1.getValue().getJSONObject("cc").getString("profileName").split(" ")[1] )));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
}
