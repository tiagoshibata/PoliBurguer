package org.burguer.poli.poliburguer.firebase;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetworkOperation extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... destinationApp){
        HttpURLConnection connection = null;

        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "key=AAAA7Ejacp8:APA91bFqWFLb8cTkhcFwyl7jMUhemI-UZzkNCL3W9Tqb0sHd0D7dDcBE4urHrIL_31Zzrqa7IhM434JTUEzxWnEJz_z-y9kDxDZUHCD9qZ_cXHUI34oPxvXZ2HUzm6cTldSlv3CCLJ-VMnS22KdWzfsU1HWiHdD1CA");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            JSONObject notification = new JSONObject();
            JSONObject parent = new JSONObject();

            notification.put("title", "Poli Burguer");
            notification.put("body", "Seu Pedido está pronto!");
            parent.put("notification", notification);
            parent.put("to", destinationApp[0]);

            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(parent.toString());
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            Log.i("PoliBurguer", "FCM response: " + response.toString());
            Log.i("PoliBurguer", "FCM Status: " + connection.getResponseCode());
            assert(connection.getResponseCode() == 200);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
}
