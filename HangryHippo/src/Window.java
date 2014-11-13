
import java.awt.*;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author jamesbouker
 */
public class Window extends JFrame {
    
    Canvas canvas;
    
    public Window(String url) {
        super("Hangry Hangry Hippo");
        this.setSize(720, 640);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        canvas = new Canvas(url);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(canvas, BorderLayout.CENTER);
        
        this.add(panel);
        this.setVisible(true);
    }
    
    public void update() {
        canvas.update();
        canvas.repaint();
    }
    
    public static void main(String [] args) {
        String url = "104.236.1.206";
        if(args.length == 1) {
            System.out.println(args[0]);
            url = args[0];
        }
          
        Window window = new Window(url);
        Runner runner = new Runner(window);
    }   
    
    private static class Runner implements Runnable {
        Thread thread;
        Window window;
        
        public Runner(Window window) {
            this.window = window;
            this.thread = new Thread(this);
            thread.start();
        }
        
        @Override
        public void run() {
            long lastTime = new Date().getTime();
            int total = 0;
            int frameCount = 0;
            while(true) {
                try {
                    this.window.update();
                    Thread.sleep(30);
                    
                    long currentTime = new Date().getTime();
                    long delta = currentTime - lastTime;
                    lastTime = currentTime;
                    total += delta;
                    frameCount++;
                    if(total > 1000) {
                        total = total - 1000;
                        System.out.println("FPS: " + frameCount);
                        frameCount = 0;
                    }
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
