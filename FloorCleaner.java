import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class FloorCleaner{
    double x;
    double y;

    public FloorCleaner(){
        this.x = 10; //FloorCleaner or "Robot" starts at (10,10)
        this.x = 10;
    }

    public FloorSpot closestSpot(List<FloorSpot> manchas){
        double min = 10000;
        FloorSpot manchaCercana = manchas.get(0);
        double distance;
        //loop through the remaining spots and find the closest one
        for(FloorSpot m : manchas) {
            distance = Math.sqrt((m.x-x)*(m.x-x)+(m.y-y)*(m.y-y));
            if(distance < min) {
                manchaCercana = m; //mancha cercana = "close spot"
                min = distance;
            }
        }
        //assign a new coordinate for the FloorCleaner (that of the closest spot)
        this.x = manchaCercana.x - 15 + manchaCercana.diameter/2;
        this.y = manchaCercana.y - 15 + manchaCercana.diameter/2;
        manchas.remove(manchaCercana);
        return manchaCercana;
    }
}

class FloorSpot {
    double x;
    double y;
    double diameter;

    public FloorSpot(double x, double y, double d){
        this.x = x;
        this.y = y;
        this.diameter = d;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawPanel draw = new DrawPanel();
        frame.getContentPane().add(draw);
        frame.setSize(500, 520); // window size
        frame.setVisible(true);
        Random r = new Random();

        //create FloorSpot objects
        for (int i = 1; i <= 15; i++) {
            double x = r.nextDouble() * 490;
            double y = r.nextDouble() * 490;
            double d = r.nextDouble() * 10 + 10;
            FloorSpot mancha = new FloorSpot(x, y, d); //mancha is Spanish for "spot"
            draw.addMancha(mancha);
        }

        //redraws the figure by calling paintComponent(g)
        for(int i = 0; i < 14; i++){
            draw.repaint();
            pause(400);
        }
    }

    public void draw(Graphics g){
        g.setColor(Color.red);
        g.fillOval((int)x,(int)y,(int)diameter,(int)diameter);
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            System.out.println("Uh oh");
        }
    }
}

class DrawPanel extends JPanel {

    List<FloorSpot> manchas = new ArrayList<>();
    FloorCleaner limpiador = new FloorCleaner();

    public void addMancha(FloorSpot m) {
        manchas.add(m);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 1; i <= 5; i++)
            g.drawLine(100 * i, 0, 100 * i, 500);
        for (int i = 1; i <= 5; i++)
            g.drawLine(0, 100 * i, 500, 100 * i);

        int x = 0; int y = 0;
        for(int j = 1; j <= 36; j++) {
            Polygon p = new Polygon();
            for (int i = 0; i < 4; i++)
                p.addPoint((int) (x + 10 * Math.cos(i * Math.PI / 2)), (int) (y + 10 * Math.sin(i * Math.PI / 2)));
            g.drawPolygon(p);
            g.fillPolygon(p);
            x += 100;
            if(j % 6 == 0){
                x = 0;
                y += 100;
            }
        }
        //draw the remaining spots
        g.setColor(Color.red);
        for (FloorSpot m : manchas) {
                m.draw(g);
        }
        limpiador.closestSpot(manchas);
        g.setColor(Color.blue);
        g.fillOval((int) limpiador.x, (int) limpiador.y, 30, 30); //size of cleaner is 30
    }


}