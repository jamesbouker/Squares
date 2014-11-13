
import java.awt.Color;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author jamesbouker
 */
public class Player {
    int x, y, size;
    Color color;
    int playerId;
    boolean dying;
    
    public Player(JSONObject obj) {
        x = obj.getInt("x");
        y = obj.getInt("y");
        size = obj.getInt("size");
        JSONArray arr = obj.getJSONArray("color");
        color = new Color(arr.getInt(0), arr.getInt(1), arr.getInt(2));
        playerId = obj.getInt("playerId");
        dying = obj.getBoolean("dying");
    }
}
