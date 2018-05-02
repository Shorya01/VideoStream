import java.awt.*;  

import java.awt.image.BufferedImage;  
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import javax.imageio.ImageIO;  
import javax.swing.*;  
import org.opencv.core.Core;  
import org.opencv.core.Mat;  
import org.opencv.core.MatOfByte;  
import org.opencv.core.MatOfRect;  
import org.opencv.core.Point;  
import org.opencv.core.Rect;  
import org.opencv.core.Scalar;  
import org.opencv.core.Size;  
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.videoio.VideoCapture;   
import org.opencv.imgproc.Imgproc;  
import org.opencv.objdetect.CascadeClassifier;  

class FacePanel extends JPanel{  
     private static final long serialVersionUID = 1L;  
     private BufferedImage image;  
     // Create a constructor method  
     public FacePanel(){  
          super();   
     }  
     /*  
      * Converts/writes a Mat into a BufferedImage.  
      *   
      * @param matrix Mat of type CV_8UC3 or CV_8UC1  
      * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
      */       
     public boolean matToBufferedImage(Mat matrix) {  
          MatOfByte mb=new MatOfByte();  
          Imgcodecs.imencode(".jpg", matrix, mb);
          try {  
               this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
          } catch (IOException e) {  
               e.printStackTrace();  
               return false; // Error  
          }  
       return true; // Successful  
     }  
     public void paintComponent(Graphics g){  
          super.paintComponent(g);   
          if (this.image==null) return;         
           g.drawImage(this.image,10,10,this.image.getWidth(),this.image.getHeight(), null);
     }
        
}  

public class Main {  
    
	public static void main(String arg[]) throws InterruptedException{  
      // Load the native library.  
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      System.loadLibrary("opencv_ffmpeg320_64");
      //or ...     System.loadLibrary("opencv_java244");       

      //make the JFrame
      JFrame frame = new JFrame("VideoStream Capture - Face detection");  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     
    //  FaceDetector faceDetector=new FaceDetector();  
      FacePanel facePanel = new FacePanel();  
      frame.setSize(400,400); //give the frame some arbitrary size 
      frame.setBackground(Color.BLUE);
      frame.add(facePanel,BorderLayout.CENTER);       
      frame.setVisible(true);       
      
      //Open and Read from the video stream  
       Mat img_matrix=new Mat();  
       String ipaddr="http://67.175.82.147/axis-cgi/mjpg/video.cgi?resolution=320x240";
       VideoCapture VideoStream =new VideoCapture(ipaddr);   
   
        if( VideoStream.isOpened())  
          {  
          // Thread.sleep(500); /// This one-time delay allows the VideoStream to initialize itself  
           while( true )  
           {  
        	 VideoStream.read(img_matrix);  
             if( !img_matrix.empty() )  
              {   
            	  Thread.sleep(0); /// This delay eases the computational load .. with little performance leakage
                  // frame.setSize(img_matrix.width()+40,img_matrix.height()+60);  
              
                  //Display the image  
                   facePanel.matToBufferedImage(img_matrix);  
                   facePanel.repaint();   
              }  
              else  
              {   
                   System.out.println(" --(!) No captured frame from VideoStream !");   
                   break;   
              }  
             }  
            }
        else{
        	System.out.println("File Not Opened");
        }
           VideoStream.release(); //release the VideoStream
 
      } //end main 
	
}
