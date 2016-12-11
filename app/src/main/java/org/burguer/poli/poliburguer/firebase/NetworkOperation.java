package org.burguer.poli.poliburguer.firebase;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class NetworkOperation extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String ...destinationApp){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization: key=AAAA7Ejacp8:APA91bFqWFLb8cTkhcFwyl7jMUhemI-UZzkNCL3W9Tqb0sHd0D7dDcBE4urHrIL_31Zzrqa7IhM434JTUEzxWnEJz_z-y9kDxDZUHCD9qZ_cXHUI34oPxvXZ2HUzm6cTldSlv3CCLJ-VMnS22KdWzfsU1HWiHdD1CA", "");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            JSONObject notification = new JSONObject();
            JSONObject parent = new JSONObject();

            notification.put("title", "Poli Burguer");
            notification.put("body", "Seu Pedido está pronto!");
            parent.put("notification", notification);
            parent.put("to", destinationApp);


            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(URLEncoder.encode(parent.toString(), "UTF-8"));
            os.close();
            System.out.print("Teste");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
