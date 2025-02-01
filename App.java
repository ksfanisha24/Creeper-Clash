import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JFrame;

public class App {

  private App() 
  throws Exception { 
    int rowCount = 21;
    int columnCount = 19;
    int tileSize = 32;
    int boardWidth = columnCount * tileSize;
    int boardHeight = rowCount * tileSize;

    JFrame frame = new JFrame("Creeper Clash");
    frame.setSize(boardWidth, boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Chicken chicky = new Chicken();
    frame.add(chicky);
    frame.pack();
    chicky.requestFocus();
    frame.setVisible(true);

    // Clip variable outside the Thread block
     Clip constructorClip = AudioSystem.getClip();

    new Thread(() -> {
	try {
    File file = new File("Start.wav");
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
    Clip clip = AudioSystem.getClip();
    clip.open(audioStream);
    clip.loop(Clip.LOOP_CONTINUOUSLY);
	} catch (Exception e) {
    e.printStackTrace();
	}
	}).start();
  }
	 public static void launchGame() 
	 throws Exception { 
    new App(); 
  }
}