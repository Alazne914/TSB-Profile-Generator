import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {
    public final int amtOfJiggs = 3;
    public int numOfProfiles = 1;

    public static void main(String[] args)
    {
        new Main().run();
    }

    /**
     * Method that reads profile details from profiles.txt file, and puts them in a JSONArray
     */
    private void run()
    {
        ArrayList<String[]> cards = new ArrayList<>();

        //Read cards and details from .txt file
        try {
            File cardsFile = new File("profiles/profiles.txt");
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
                jsonArray.add(c);
            }
        }

        System.out.println("Number of profiles created: " + numOfProfiles + "\n" + jsonArray.toString());

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
        shipping.put("state", null);

        //Put billing details
        billing.put("billingSameAsShipping", true);

        //Put extra details
        profile.put("cc", card);
        profile.put("shipping", shipping);
        profile.put("billing", billing);
        profile.put("isJapaneseAddress", null);
        profile.put("isRussianAddress", null);
        profile.put("isMexicanAddress", null);
        profile.put("isPhilippinesAddress", null);
        profile.put("date", System.currentTimeMillis());

        return profile;
    }

}
