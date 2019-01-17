package chat.net;

import chat.models.Notifications;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ServerManager  {
    private String urlServer;
    //http://localhost:8080/Server_Web_exploded/usr
    public ServerManager(String urlServer){
        this.urlServer=urlServer;
    }

    public void sendPacket(Notifications notifications){
        try {
            URL url=new URL(urlServer);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Acceptcharset", "en-us");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            conn.setRequestProperty("charset", "EN-US");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setDoOutput(true);

            System.out.println(url);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            //define argument
            HashMap<String , String> params = new HashMap<>();
            params.put("packet", serialize(notifications));
            //format it
            StringBuilder result = new StringBuilder();
            for(Map.Entry<String, String> entry : params.entrySet()){
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            wr.write(result.toString());
            wr.flush();
            wr.close();
            System.out.println(conn.getResponseCode());
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String serialize(Object obj) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so = null;
        try {
            so = new ObjectOutputStream(bo);
            so.writeObject(obj);
            so.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.getEncoder().encode(bo.toByteArray()));
    }
}
