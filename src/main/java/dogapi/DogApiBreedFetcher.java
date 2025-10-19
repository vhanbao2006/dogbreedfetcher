package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        // Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        // return statement included so that the starter code can compile and run.
        try {
            String url = String.format("https://dog.ceo/api/breed/%s/list", breed);
            String contents = getURLContents(url);

            JSONObject jsonObject = new JSONObject(contents);
            String status = jsonObject.getString("status");
            if (!status.equals("success")) {
                String errorMessage = jsonObject.getString("message");
                throw new BreedNotFoundException(errorMessage);
            }
            JSONArray breeds = jsonObject.getJSONArray("message");
            List<String> subbreeds = new ArrayList<>();
            for (int i = 0; i < breeds.length(); i++) {
                subbreeds.add(breeds.getString(i));
            }
            return subbreeds;

        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }

    }

    private String getURLContents(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
    }
}