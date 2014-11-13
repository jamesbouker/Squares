
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jamesbouker
 */
public class Client {
    
    String url;
    
    public Client(String url) {
        this.url = url;
    }
    
    public String sendPlayerInfo(int playerId, int vx, int vy) {
        try {
            return sendPost("updatePlayer", playerId, vx, vy);
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error";
    }
    
    // HTTP POST request
    private String sendPost(String type, int playerId, int vx, int vy) throws Exception {
        
        //Digital Ocean
        String url2 = "http://" + url + ":8080";
	URL obj = new URL(url2);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
	//add reuqest header
	con.setRequestMethod("POST");
	String urlParameters = "type=" + type + "&playerId=" + playerId + "&vx=" + vx + "&vy=" + vy;
 
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
 
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        String response = "";

        while ((inputLine = in.readLine()) != null)
            response += inputLine;
        in.close();

        //print result
        return response;
    }
}