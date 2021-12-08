import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class Panel2 extends JPanel
{
   // PROPERTIES
   private final int DEFAULT_WIDTH  = 400;
   private final int DEFAULT_HEIGHT = 400;
   private final Color BACK_COLOR   = Color.WHITE;
   
   private static BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
   
   private static Graphics g;
   static JLabel picLabel;
   
   // CONSTRUCTOR
   public Panel2()
   {
      setBackground( BACK_COLOR );
      setPreferredSize( new Dimension( DEFAULT_WIDTH, DEFAULT_HEIGHT ));
        
      picLabel = new JLabel();
      add(picLabel);
   }
   
   public static void buildImage(int pic[][])
   {
         
      for (int x = 0; x < pic.length; x++) {
         for (int y = 0; y < pic[0].length; y++) {
         
            if(pic[x][y] == 0){
               image.setRGB(x, y, Color.WHITE.getRGB());
            }else{
               image.setRGB(x, y, Color.BLACK.getRGB());
            }                 
         }
      } 
      paintImage();
   }
   
   public static void paintImage() 
   {
      picLabel.setIcon(new ImageIcon(image));
   }
      
}