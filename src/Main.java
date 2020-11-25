import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    public final int amtOfJiggs = 5;
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
                jsonArray.put(c);
            }
        }

        System.out.println("Number of profiles created: " + numOfProfiles + "\n" + jsonArray.toString(1));

        String outputFilename = "output/generated_profiles.json";
        try {
            File file = new File(outputFilename);
            FileWriter filew = new FileWriter(outputFilename);

            //jsonArray.writeJSONString(jsonArray,filew);
            filew.close();

        } catch (IOException | JSONException e) {
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
        JSONObject c = new JSONObject();

        String jiggNumber = " #" + String.format("%03d", numOfProfiles++);

        c.put("name", details[0] + " (J" + (jigg + 1) + ")");
        c.put("phone", details[1]);
        c.put("number", details[2]);
        c.put("expire", details[3]);
        c.put("cvv", details[4]);
        c.put("firstname", details[5]);
        c.put("lastname", details[6]);
        c.put("adress1", details[7] + jiggNumber);
        c.put("adress2", details[8] + jiggNumber);
        c.put("country", details[9]);
        c.put("city", details[10]);
        c.put("zip", details[11]);
        c.put("billingSame", details[12]);

        return c;
    }
}
