import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

public class Panel extends JPanel
{
   // PROPERTIES
   private final int DEFAULT_WIDTH  = 400;
   private final int DEFAULT_HEIGHT = 400;
   private final Color BACK_COLOR = Color.WHITE;
   private int x1, y1, x2, y2;

   private static int[][] pic;
    
   private static BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
   Graphics2D g2d = image.createGraphics();
   
   static JLabel picLabel = new JLabel(new ImageIcon(image));
   
   private MyMouseHandler handler;
   private static Graphics g;

   // CONSTRUCTOR
   public Panel()
   {
      setBackground(BACK_COLOR);
      setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
      
      handler  = new MyMouseHandler();
    
      g2d.setColor(Color.white);
      
      pic = new int[DEFAULT_WIDTH][DEFAULT_HEIGHT];
      
      for(int i = 0; i < pic.length; i++){
         for(int j = 0; j < pic[0].length; j++){
            pic[i][j] = 0;
         }
      }
     
      this.addMouseListener(handler);
      this.addMouseMotionListener(handler);
   }

   private void setUpDrawingGraphics()
   {
      g = getGraphics();
   }
    
   public static void printArray(int[][] pic)
   {
      drawArray(pic);
      for(int i = 0; i<pic.length; i++)
      {
         for(int j = 0; j<pic[0].length; j++)
         {
            System.out.print(pic[j][i]);
         }
         System.out.println();
      }   
   }
   
   public static void drawArray(int[][] pic){
      for (int x = 0; x < pic.length; x++) {
         for (int y = 0; y < pic[0].length; y++) {
         
            final int color = image.getRGB(x, y);
            if(color == -16777216){
               pic[x][y] = 0;
            }else{
               pic[x][y] = 1;
            }                 
         }
      }
   }
   
   public static int[][] getArray(){
      return pic;
   }
   
   public static BufferedImage getImage(){
      return image;
   }
   
   //post:  returns true if x and y are valid screen coordinates
   private static boolean isValid(double r, double c)
   {
      return (r >= 0 && c >= 0 && r < pic.length && c < pic[0].length);
   }

   // INNER CLASS
   private class MyMouseHandler extends MouseAdapter
   {
      public void mousePressed( MouseEvent e )
      {
         x1 = e.getX();
         y1 = e.getY();
      
         //System.out.println("Mouse is being pressed at X: " + x1 + " Y: " + y1);
      
         setUpDrawingGraphics();
      
         x2=x1;
         y2=y1;
      }
   
      public void mouseDragged( MouseEvent e )
      {
         x1 = e.getX();
         y1 = e.getY();
      
         //System.out.println("Mouse is being dragged at X: " + x1 + " Y: " + y1);  
      
         g.drawLine(x1,y1,x2,y2);
         g2d.drawLine(x1,y1,x2,y2);
         
         x2=x1;
         y2=y1;
        
      }
   }
}