
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

/**
 *
 * @author jamesbouker
 */
public class Canvas extends JPanel {
    boolean up,down,left,right;
    
    float speed;
    DataModel model;
    
    public Canvas(String url) {
        this.model = new DataModel(url);
        this.setOpaque(true);
        this.setFocusable(true);        
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                switch(code) {
                    case 37: left = true; break;
                    case 38: up = true; break;
                    case 39: right = true; break;
                    case 40: down = true; break;
                    case 65: left = true; break;
                    case 87: up = true; break;
                    case 68: right = true; break;
                    case 83: down = true; break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                switch(code) {
                    case 37: left = false; break;
                    case 38: up = false; break;
                    case 39: right = false; break;
                    case 40: down = false; break;
                    case 65: left = false; break;
                    case 87: up = false; break;
                    case 68: right = false; break;
                    case 83: down = false; break;
                }
            }
        });
    }
    
    public void update() {
//        speed = model.player.size * 6;
        int vx = 0, vy = 0;
        if(up)
            vy = -1;
        else if(down)
            vy = 1;
        if(right)
            vx = 1;
        else if(left)
            vx = -1;
        
        model.update(vx, vy);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        
        g.setColor(model.player.color);
        if(model.player.dying == false)
            g.fillRect(model.player.x, model.player.y, model.player.size, model.player.size);
        else if(model.player.size < 2000)
            g.drawRect(model.player.x, model.player.y, model.player.size, model.player.size);
        
        g.setColor(Color.white);
        for (AIBlock block : model.blocks) {
            g.setColor(block.color);
            g.drawRect(block.x, block.y, block.size, block.size);
        }
        
        for(Player player : model.players) {
            g.setColor(player.color);
            if(player.dying == false)
                g.fillRect(player.x, player.y, player.size, player.size);
            else if(player.size < 2000)
                g.drawRect(player.x, player.y, player.size, player.size);
        }
    }
}
