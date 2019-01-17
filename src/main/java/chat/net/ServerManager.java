package chat.net;

import chat.models.Notifications;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class ServerManager  {
    private String urlServer;
    //http://localhost:8080/Server_Web_exploded/usr
    public ServerManager(String urlServer){
        this.urlServer=urlServer;
    }

    public void sendPacket(Notifications notifications){
        try {
            HttpURLConnection connection=(HttpURLConnection) (new URL(urlServer).openConnection());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Acceptcharset","en-us");
            connection.setRequestProperty("Accept-Language","en-US,en;q=0.5");
            connection.setRequestProperty("charset","EN-US");
            connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setDoOutput(true);

            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            String serialNotif=serialize(notifications);

            StringBuilder builder=new StringBuilder();
            builder.append(URLEncoder.encode("packet","UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(serialNotif,"UTF-8"));
            writer.write(builder.toString());
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
