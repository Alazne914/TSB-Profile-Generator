import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    private String inputFolder = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input";
    private String outputFolder = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output";
    private String inputFilePath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "profiles_input_text.txt";
    private String outputFilePath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "profiles_output_json.json";

    private int amtOfJiggs;
    private int numOfProfiles = 0;
    private int choice;

    public static void main(String[] args)
    {
        new Main().run();
    }

    public void run()
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Do you want to generate profiles, or retrieve CC details from profiles export?" +
                       "\n 1) Generate profiles" +
                       "\n 2) Retrieve CC details" +
                       "\n Input: ");
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

        //Read cards and details from .txt file
        try {
            File cardsFile = getFiles(".txt",new File(inputFolder)).get(0);

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

        if(cards.size() > 0) {
            //First ask user for input about jigging
            Scanner input = new Scanner(System.in);
            System.out.print("How many times would you like to jigg your adress for each credit card?\nInput: ");
            amtOfJiggs = input.nextInt();

            //Create desired amount of profiles for each provided credit card
            JSONArray jsonArray = new JSONArray();
            for (int i=0; i<cards.size(); i++)
            {
                String[] details = cards.get(i);

                for(int j=0; j<amtOfJiggs; j++)
                {
                    JSONObject c = getProfileObject(details, j);
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
            //String path = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "profiles_input_json.json";
            File f = getFiles(".json", new File(inputFolder)).get(0);
            String jsonString = new String(Files.readAllBytes(Paths.get(f.getPath())));
            JSONArray array = new JSONArray(jsonString);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                creditCards.put(obj.getJSONObject("cc").getString("ccNumber"), obj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Write all unique credit cards to .txt file. Ready to be used to generate new profiles
        try {
            String txtOutputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "profiles_output_text.txt";
            File output = new File(txtOutputPath);
            output.getParentFile().mkdirs();
            output.createNewFile();

            FileWriter fstream = new FileWriter(output);
            BufferedWriter info = new BufferedWriter(fstream);

            int counter = 0;
            for (JSONObject obj : creditCards.values()) {
                JSONObject cc = obj.getJSONObject("cc");
                JSONObject shipping = obj.getJSONObject("shipping");
                JSONObject billing = obj.getJSONObject("billing");

                String[] profileNameSplit = cc.getString("profileName").split(" ");
                profileNameSplit[ profileNameSplit.length - 1 ] = "";
                String profileName = String.join(" ", profileNameSplit);

                String line = String.format(profileName.trim() +
                        ";" + cc.getString("phone") +
                        ";" + cc.getString("ccNumber") +
                        ";" + cc.getString("ccExpiry") +
                        ";" + cc.getString("ccCvc") +
                        ";" + NamesProvider.getFirstName() +
                        ";" + NamesProvider.getLastName() +
                        ";" + shipping.getString("address").split("#")[0].trim() +
                        ";" + shipping.getString("address2").split("#")[0] +
                        ";" + shipping.getString("country") +
                        ";" + shipping.getString("city") +
                        ";" + shipping.getString("zip") +
                        ";" + billing.getBoolean("billingSameAsShipping") + "%n");

                info.write(line);
                counter++;
            }

            info.close();

            System.out.println("\n" + counter + " profiles written to profiles_output_text.txt file successfully. File is located at C:/TSB Profile Generator/output");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns a JSONObject containig card details and jigged address
     * @param details Card and adress details
     * @param jigg Jigg number
     * @return JSONObject containing profile details
     */
    public JSONObject getProfileObject(String [] details, int jigg)
    {
        JSONObject profile = new JSONObject();
        JSONObject card = new JSONObject();
        JSONObject shipping = new JSONObject();
        JSONObject billing = new JSONObject();

        //Generating random info used for jigging
        int length = (int) (Math.random()*4) + 2;
        int position = (int) (Math.random()*2);
        String address1Jig = getRandomString(length);
        String address2Jig = getRandomString(length);

        //Put card details
        card.put("profileName", details[0] + " (J" + (jigg + 1) + ")");
        card.put("phone", details[1]);
        card.put("ccNumber", details[2]);
        card.put("ccExpiry", details[3]);
        card.put("ccCvc", details[4]);

        //Put shipping details
        shipping.put("firstName", details[5]);
        shipping.put("lastName", details[6]);
        if(position == 0) {
            shipping.put("address",  address1Jig + " " + details[7]);
        } else {
            shipping.put("address", details[7] + " " + address1Jig);
        }
        shipping.put("address2", (details[8] + " " + address2Jig).trim());
        shipping.put("country", details[9]);
        shipping.put("city", details[10]);
        shipping.put("zip", details[11]);
        shipping.put("state", JSONObject.NULL);

        //Put billing details
        if(details[12].equals("true")) {
            billing.put("billingSameAsShipping", true);
        } else {
            billing.put("billingSameAsShipping", "");
            billing.put("firstName", details[5]);
            billing.put("lastName", details[6]);
            billing.put("address", details[7]);
            billing.put("address2", details[8]);
            billing.put("country", details[9]);
            billing.put("city", details[10]);
            billing.put("zip", details[11]);
            billing.put("state", JSONObject.NULL);
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
}
