import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private String inputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "profiles.txt";
    private String outputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "profiles_output.json";

    private int amtOfJiggs;
    private int jiggNum;
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
                       "\n 1) Generatie profiles" +
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
        File f = new File(inputPath);

        if(!f.getParentFile().exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();

                if(choice == 1) {
                    System.out.println("The input folder containing the profiles.txt has been created." +
                            "\nPlease paste the profile details in that file, and run the generator again." +
                            "\nThe folder is located at: C:/TSB Profile Generator/input" +
                            "\nCheck the guide for how to format the input file.");
                } else if(choice == 2) {
                    System.out.println("The input folder has been created." +
                            "\nPlease place your profiles export in that folder, and run the generator again." +
                            "\nThe folder is located at: C:/TSB Profile Generator/input" +
                            "\nMake sure the file is called profiles.json!" +
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
     * Method that reads profile details from profiles.txt file, and puts them in a JSONArray
     */
    public void generateProfiles()
    {
        ArrayList<String[]> cards = new ArrayList<>();

        //Read cards and details from .txt file
        try {
            File cardsFile = new File(inputPath);

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
            //First take user input about jigging
            Scanner input = new Scanner(System.in);
            System.out.print("How many times would you like to jigg your adress for each card?\nInput: ");
            amtOfJiggs = input.nextInt();
            System.out.print("\nAt what number would you like to start jigging? (eg. 1 will start at #001, but 133 will start at #133)\nInput: ");
            jiggNum = input.nextInt();

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

            //Write profiles to .json file. This file will be located in C/TSB Profile Generator/output
            try {
                File f = new File(outputPath);

                f.getParentFile().mkdirs();
                f.createNewFile();

                String json = jsonArray.toString(1);
                Files.write(Paths.get(f.getPath()), json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("\nNumber of profiles created: " + numOfProfiles +
                    "\nThe file containing the profiles can be found at: C:/TSB Profile Generator/output");
        } else {
            System.out.println("Your profiles.txt file is empty! Paste your details in the file and rerun the program");
        }

    }

    /**
     * Retrieves creditcard number, expiry date and cvc from TSB profiles export
     */
    public void retrieveCcDetails()
    {
        HashMap<String, JSONObject> creditCards = new HashMap<>();

        try {
            String path = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "profiles.json";
            String jsonString = new String(Files.readAllBytes(Paths.get(path)));
            JSONArray array = new JSONArray(jsonString);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                creditCards.put(obj.getJSONObject("cc").getString("ccNumber"), obj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: Details wegschrijven naar een .txt bestand

        /* Ideeën:
           - geëxporteerde profiles wegschrijven naar .txt file als vers genereerbare profiles
           - zodat je vaker of minder vaak kan laten jiggen
         */

        for (JSONObject obj : creditCards.values()) {
            System.out.println(obj.getJSONObject("cc").getString("ccNumber") +
                    ";" + obj.getJSONObject("cc").getString("ccExpiry") +
                    ";" + obj.getJSONObject("cc").getString("ccCvc"));
        }

        try {
            String txtOutputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "profiles_output.txt";
            File output = new File(txtOutputPath);
            output.getParentFile().mkdirs();
            output.createNewFile();

            FileWriter fstream = new FileWriter(output);
            BufferedWriter info = new BufferedWriter(fstream);

            for (JSONObject obj : creditCards.values()) {
                info.write(String.format(obj.getJSONObject("cc").getString("ccNumber") +
                        ";" + obj.getJSONObject("cc").getString("ccExpiry") +
                        ";" + obj.getJSONObject("cc").getString("ccCvc") + "%n"));
            }

            info.close();
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

        String jiggerTxt = " #" + String.format("%03d", jiggNum++);

        //Put card details
        card.put("profileName", details[0] + " (J" + (jigg + 1) + ")");
        card.put("phone", details[1]);
        card.put("ccNumber", details[2]);
        card.put("ccExpiry", details[3]);
        card.put("ccCvc", details[4]);

        //Put shipping details
        shipping.put("firstName", details[5]);
        shipping.put("lastName", details[6]);
        shipping.put("address", details[7] + jiggerTxt);
        shipping.put("address2", (details[8] + jiggerTxt).trim());
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
}
