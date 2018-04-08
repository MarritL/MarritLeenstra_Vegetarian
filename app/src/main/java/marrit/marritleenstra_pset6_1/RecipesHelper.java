package marrit.marritleenstra_pset6_1;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RecipesHelper implements Response.Listener<JSONObject>, Response.ErrorListener{

    public interface Callback {
        void gotRecipes(ArrayList<Recipe> recipesArrayList, Context mContext);
        void gotError(String message);
    }

    // declare variables
    private final Context mContext;
    private Callback mCallback;
    private final String TAG = "RECIPESHELPER";
    private final String APIkey = "358842bcce6b938ba3d887ccba20a6a1";
    private final String APIid = "12ae1b10";
    private ArrayList<String> ingredients = new ArrayList<>();

    // constructor
    RecipesHelper(Context context) {
        mContext = context;
    }

    // request recipes from yummly API
    void getRecipes(Callback activity) {
        mCallback = activity;

        // fill arrayList with ingredients
        getIngredientList();

        // create new queue
        RequestQueue recipesQueue = Volley.newRequestQueue(mContext);

        // get random ingredients
        String ingredient1 = getRandomIngredient();

        // create url
        String mUrl = "http://api.yummly.com/v1/api/recipes?_app_id=" + APIid + "&_app_key="
                + APIkey + "&allowedDiet[]=388^Lacto vegetarian&requirePictures=true&maxResults=" +
                "30&allowedIngredient[]=" + ingredient1;

        // create JSON object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                mUrl, null, this, this);
        recipesQueue.add(jsonObjectRequest);

    }
    // Handle API response
    @Override
    public void onResponse(JSONObject response) {

        System.out.println(TAG + " response: " + response);

        ArrayList<Recipe> recipesArrayList = new ArrayList<>();

        // get the recipes
        try {
            JSONArray matches = response.getJSONArray("matches");
            System.out.println("matches :" + matches);

            for (int i = 0; i < matches.length(); i++) {
                ArrayList<String> imagesArray = new ArrayList<>();
                JSONObject object = matches.getJSONObject(i);
                String name = object.getString("recipeName");
                Double rating = object.getDouble("rating");
                JSONArray images =  object.getJSONArray("smallImageUrls");

                for (int j = 0; j < images.length(); j++) {
                    imagesArray.add(images.getString(j));
                }

                Recipe recipe = new Recipe(name, rating, imagesArray);
                recipesArrayList.add(recipe);
            }
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());

        }

        mCallback.gotRecipes(recipesArrayList, mContext);

    }

    // Handle error
    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.gotError(error.getMessage());
    }

    // method to fill the list of ingredients
        private void getIngredientList() {

        try {
            read(mContext.getAssets().open("ingredients.txt"));
            System.out.println("MAINACTIVITY ingredients: " + ingredients);
        } catch (IOException e) {
            Log.e(TAG, e.getClass().getName());
        }
    }

    // method to read ingredients from assets file
    public void read(InputStream stream) {

        Scanner scanner = new Scanner(stream).useDelimiter(",");

        while (scanner.hasNext()) {
            ingredients.add(scanner.next());
        }

    }

    // get a random ingredient from the list of ingredients
    public String getRandomIngredient() {
        //get three random ingredients from the ingredients list
        // get a random number
        Random rand = new Random();
        int i = rand.nextInt(ingredients.size());
        String ingredient = ingredients.get(i);

        return ingredient;
    }
}
