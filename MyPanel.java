import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;



public class MyPanel extends JPanel implements ActionListener{

    final int PANEL_WIDTH = 500;
    final int PANEL_HEIGHT = 500;
    final int CAR_WIDTH = 41;
    final int CAR_HEIGHT = 53;

    Image car;
    Image car_icon;
    Image route;
    BufferedImage buf_img;
    Timer timer;
    double degrees;
    double xVelocity = 3;
    int yVelocity = 5;
    int route_Y1 = -4500;
    int route_Y2 = -9500;
    int x = 150;
    int y = 50;
    int[][] pixels = new int[500][5000];
    FIS fis;
    FuzzyRuleSet fuzzyRuleSet;




    MyPanel() throws IOException {
        String fileName = "files/fuzzy_velocity.fcl";
        fis = FIS.load(fileName,false);
        fuzzyRuleSet = fis.getFuzzyRuleSet();

        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.BLACK);
        buf_img = ImageIO.read(new File("trasa2.png"));
        route = ImageIO.read(new File("trasa2.png"));
        for( int i = 0; i < 500; i++ )
            for( int j = 0; j < 5000; j++ )
                pixels[i][j] = buf_img.getRGB(i, j);


        car = ImageIO.read(new File("car.png"));
        timer = new Timer(10, this);
        timer.start();
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;


        car_icon = new ImageIcon(car).getImage();
        AffineTransform rotate = AffineTransform.getTranslateInstance(x-CAR_WIDTH/2,400-CAR_HEIGHT/2);
        //System.out.println("degrees " + degrees);
        rotate.rotate(-Math.toRadians((int)degrees), CAR_WIDTH/2, CAR_HEIGHT/2);

        g2D.drawImage(route, 0, route_Y1, null);
        g2D.drawImage(route, 0, route_Y2, null);
        g2D.drawImage(car_icon, rotate, null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int left = -1;
        int right = -1;
        int i=x;
        if ((-route_Y1+400)>=0 && (-route_Y1+400)<5000) {
            while (pixels[i][(-route_Y1 + 400)] == 0)
                i--;
            left = i;
            i = x;
            while (pixels[i][-route_Y1 + 400] == 0)
                i++;
        }
        else{
            while (pixels[i][-route_Y2 + 400] == 0)
                i--;
            left = i;
            i = x;
            while (pixels[i][-route_Y2 + 400] == 0)
                i++;

        }
        right = i;
        fuzzyRuleSet.setVariable("velocity", xVelocity);
        fuzzyRuleSet.setVariable("close_to_left", x-left);
        fuzzyRuleSet.setVariable("close_to_right", right-x);
        fuzzyRuleSet.evaluate();
        xVelocity += fuzzyRuleSet.getVariable("change_velocity").defuzzify();
        route_Y1+=yVelocity;
        route_Y2 +=yVelocity;
        if (route_Y1>500)
            route_Y1 -= 10000;
        if (route_Y2>500)
            route_Y2 -= 10000;
        x += (int)xVelocity;
        degrees = -Math.toDegrees(Math.atan((double)xVelocity/(double)yVelocity));
        repaint();
    }
}
