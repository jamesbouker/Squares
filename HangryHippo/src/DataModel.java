import java.util.Iterator;
import java.util.Vector;
import org.json.*;
/**
 *
 * @author jamesbouker
 */
public class DataModel {
    AIBlock [] blocks = new AIBlock[50];
    Player player;
    Client client;
    Vector<Player> players = new Vector<>();
    
    public DataModel(String url) {
        client = new Client(url);
        String response = client.sendPlayerInfo(-1, -1, -1);
        JSONObject obj = new JSONObject(response);
        JSONArray blockArr = obj.getJSONArray("blocks");
        for(int i=0; i<blockArr.length(); i++)
            blocks[i] = new AIBlock(blockArr.getJSONObject(i));
        int playerId = obj.getInt("playerId");
        player = new Player(obj.getJSONObject("players").getJSONObject(playerId+""));
        
        JSONObject playerObj = obj.getJSONObject("players");
        Iterator<String> playersIterator = playerObj.keys();
        while (playersIterator.hasNext()){
            String p = playersIterator.next();
            int pId = Integer.parseInt(p);
            if(pId != player.playerId && p != null) {
                JSONObject ply = playerObj.getJSONObject(p);
                players.add(new Player(ply));
            }
        }
    }
    
    public void update(int vx, int vy) {
        String response = client.sendPlayerInfo(player.playerId, vx, vy);
        JSONObject obj = new JSONObject(response);
        JSONArray blockArr = obj.getJSONArray("blocks");
        for(int i=0; i<blockArr.length(); i++)
            blocks[i] = new AIBlock(blockArr.getJSONObject(i));
        int playerId = obj.getInt("playerId");
        player = new Player(obj.getJSONObject("players").getJSONObject(playerId+""));
        String pl = obj.getJSONObject("players").getJSONObject(playerId+"").toString();
        System.out.println(pl);
        JSONObject playerObj = obj.getJSONObject("players");
        Iterator<String> playersIterator = playerObj.keys();
        players.removeAllElements();
        while (playersIterator.hasNext()){
            String p = playersIterator.next();
            int pId = Integer.parseInt(p);
            if(pId != player.playerId && p != null) {
                JSONObject ply = playerObj.getJSONObject(p);
                players.add(new Player(ply));
            }
        }
    }
}
