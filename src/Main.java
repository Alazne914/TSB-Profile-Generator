import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    private String inputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "input" + File.separator + "profiles.txt";
    private String outputPath = "C:" + File.separator + "TSB Profile Generator" + File.separator + "output" + File.separator + "profiles_output.json";

    private final int amtOfJiggs = 3;
    private int numOfProfiles = 1;

    public static void main(String[] args)
    {
        new Main().setup();
    }

    /**
     * This method ensures that the input directory exists. If it does not, the user will be notified
     */
    private void setup()
    {
        File f = new File(inputPath);

        if(!f.getParentFile().exists()) {
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();

                System.out.println("The profiles.txt file has been created." +
                        "\nPlease paste the profile details in that file, and run the generator again." +
                        "\nThe folder is located at: C/TSB Profile Generator/" +
                        "\nProfiles must be formatted as following:" +
                        "\n\nProfile name;phone number;cc number;cc expiry date;cc cvc;first name;last name;adress 1;adress 2;country;city;zip" +
                        "\n\nExample:" +
                        "\nREVOLUT 1;+3167762212;0000 0000 0000 0000;11 / 25;993;Mals;Kippetje;Zeedijk 61;;Netherlands;Amsterdam;1012AS");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            run();
        }

    }

    /**
     * Method that reads profile details from profiles.txt file, and puts them in a JSONArray
     */
    private void run()
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

        JSONArray jsonArray = new JSONArray();
        for (int i=0; i<cards.size(); i++)
        {
            String[] details = cards.get(i);

            for(int j=0; j<amtOfJiggs; j++)
            {
                JSONObject c = getProfileObject(details, j);
                jsonArray.put(c);
            }
        }

        System.out.println("Number of profiles created: " + (numOfProfiles - 1) + "\n" + jsonArray.toString(2));

        //Write profiles to .json file. This file will be located in C/TSB Profile Generator/output
        try {
            File f = new File(outputPath);

            f.getParentFile().mkdirs();
            f.createNewFile();

            String json = jsonArray.toString(2);
            Files.write(Paths.get(f.getPath()), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns a JSONObject containig card details and jigged adress
     * @param details Card and adress details
     * @param jigg Jigg number
     * @return JSONObject containing profile details
     */
    private JSONObject getProfileObject(String [] details, int jigg)
    {
        JSONObject profile = new JSONObject();
        JSONObject card = new JSONObject();
        JSONObject shipping = new JSONObject();
        JSONObject billing = new JSONObject();

        String jiggNumber = " #" + String.format("%03d", numOfProfiles++);

        //Put card details
        card.put("name", details[0] + " (J" + (jigg + 1) + ")");
        card.put("phone", details[1]);
        card.put("ccNumber", details[2]);
        card.put("ccExpiry", details[3]);
        card.put("ccCvc", details[4]);

        //Put shipping details
        shipping.put("firstName", details[5]);
        shipping.put("lastName", details[6]);
        shipping.put("adress", details[7] + jiggNumber);
        shipping.put("adress2", (details[8] + jiggNumber).trim());
        shipping.put("country", details[9]);
        shipping.put("city", details[10]);
        shipping.put("zip", details[11]);
        shipping.put("state", JSONObject.NULL);

        //Put billing details
        billing.put("billingSameAsShipping", true);

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
