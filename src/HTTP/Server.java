package HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Server {
    public static void main(String[] args) throws IOException, JSONException {
        HttpURLConnection connection= (HttpURLConnection) new URL("https://jsonplaceholder.typicode.com/users").openConnection();

        connection.setRequestMethod("GET");
        InputStream input = connection.getInputStream();
        int c;
        String datos="";
        while ((c=input.read())!=-1){
            datos+=((char) c);
        }

        JSONArray jsonData= new JSONArray(datos);

        for (int i = 0; i <jsonData.length() ; i++) {
            JSONObject objetoJson= jsonData.getJSONObject(i);
            System.out.println(objetoJson.getInt("id"));
        }



    }
}
