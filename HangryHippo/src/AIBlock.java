import java.awt.Color;
import org.json.*;
/**
 *
 * @author jamesbouker
 */
public class AIBlock {
    int x, y, size;
    int vx, vy;
    Color color;
    
    public AIBlock(JSONObject obj) {
        x = (int) obj.getDouble("x");
        y = (int) obj.getDouble("y");
        vx = 0;
        vy = 0;
        size = obj.getInt("size");
        JSONArray arr = obj.getJSONArray("color");
        color = new Color(arr.getInt(0), arr.getInt(1), arr.getInt(2));
    }
}
