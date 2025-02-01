import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Chicken extends JPanel implements ActionListener, KeyListener {
	private boolean isPaused = false;
    private String characterName;

    interface GameEntity {
        void updateDirection(char direction);
        void reset();
    }
    
    class Block implements GameEntity {
        private int x, y, width, height;
        private Image image;
        private int startX, startY;
        private char direction = 'U'; // U D L R
        private int velocityX = 0;
        private int velocityY = 0;
        private boolean isChasingChicken = false;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public int getWidth() {
            return width;
        }
        public int getHeight() {
            return height;
        }
        public Image getImage() {
            return image;
        }

        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }
        public void setImage(Image image) {
            this.image = image;
        }

        public void updateDirection(char direction) {
            if (isChasingChicken) {
                chaseChicken();
            } else {
                char prevDirection = this.direction;
                this.direction = direction;
                updateVelocity();
                this.x += this.velocityX;
                this.y += this.velocityY;
                for (Block wall : walls) {
                    if (collision(this, wall)) {
                        this.x -= this.velocityX;
                        this.y -= this.velocityY;
                        this.direction = prevDirection;
                        updateVelocity();
                    }
                }
            }
        }

        private void chaseChicken() {
            int dx = chicken.getX() - this.x;
            int dy = chicken.getY() - this.y;

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    this.direction = 'R'; // right
                } else {
                    this.direction = 'L'; // left
                }
            } else {
                if (dy > 0) {
                    this.direction = 'D'; // down
                } else {
                    this.direction = 'U'; // up
                }
            }

            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
        }

        void updateVelocity() {
            switch (this.direction) {
                case 'U' -> {
                    velocityX = 0; velocityY = -tileSize / 4;
                }
                case 'D' -> {
                    velocityX = 0; velocityY = tileSize / 4;
                }
                case 'L' -> {
                    velocityX = -tileSize / 4; velocityY = 0;
                }
                case 'R' -> {
                    velocityX = tileSize / 4; velocityY = 0;
                }
            }
        }

        public void reset() {
            this.x = this.startX;
            this.y = this.startY;
            this.isChasingChicken = false; 
        }
    }

    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int tileSize = 32;
    private final int boardWidth = columnCount * tileSize;
    private final int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image creeper1Image;
    private Image creeper2Image;
    private Image creeper3Image;
    private Image creeper4Image;
    private Image chickUpImage;
    private Image chickDownImage;
    private Image chickLeftImage;
    private Image chickRightImage;
    private Image BackgroundImage;

    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "XOOX X       X XOOX",
            "XXXX X XXrXX X XXXX",
            "X       bpo       X",
            "XXXX X XXXXX X XXXX",
            "XOOX X       X XOOX",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Block> ghosts;
    private Block chicken;

    private final Timer gameLoop;
    private final char[] directions = {'U', 'D', 'L', 'R'};
    private final Random random = new Random();
    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;

    public Chicken() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(new Color(190,244,100));
        addKeyListener(this);
        setFocusable(true);

        characterName = JOptionPane.showInputDialog(this, "Enter your character's name:", "Character Name", JOptionPane.PLAIN_MESSAGE);
        if (characterName == null || characterName.trim().isEmpty()) {
            characterName = "Player";
        }
		
		
		 try {
         loadImages();
         loadMap();
        } catch (IOException e) { 
        System.err.println("I/O Error loading resources: " + e.getMessage());
        e.printStackTrace();
        } catch (NullPointerException e) { 
        System.err.println("Null Pointer Exception: A required resource might be missing.");
        e.printStackTrace();
        } catch (Exception e) { 
        System.err.println("Unexpected error loading resources: " + e.getMessage());
        e.printStackTrace();
        }
		
        ghosts.forEach(ghost -> ghost.updateDirection(directions[random.nextInt(4)]));
        gameLoop = new Timer(50, this); // 20fps
        gameLoop.start();
        showStartDialog();
    }

    private void showStartDialog() {
        int response = JOptionPane.showConfirmDialog(this, "You sure?", "Creeper Clash", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void showEndDialog() {
        int response = JOptionPane.showConfirmDialog(this, "RIP.\nWanna start again?", "Creeper Clash", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        lives = 3;
        score = 0;
        chicken.reset();
        ghosts.forEach(Block::reset);
        gameOver = false;
        showStartDialog();
    }

    private void loadImages() throws Exception {
        try {
            wallImage = new ImageIcon(getClass().getResource("chickwall.png")).getImage();
            creeper1Image = new ImageIcon(getClass().getResource("creeper.png")).getImage();
            creeper2Image = new ImageIcon(getClass().getResource("creeper.png")).getImage();
            creeper3Image = new ImageIcon(getClass().getResource("creeper.png")).getImage();
            creeper4Image = new ImageIcon(getClass().getResource("creeper.png")).getImage();
            chickUpImage = new ImageIcon(getClass().getResource("ChickRight.png")).getImage();
            chickDownImage = new ImageIcon(getClass().getResource("ChickLeft.png")).getImage();
            chickLeftImage = new ImageIcon(getClass().getResource("ChickLeft.png")).getImage();
            chickRightImage = new ImageIcon(getClass().getResource("ChickRight.png")).getImage();
            BackgroundImage = new ImageIcon(getClass().getResource("bg1.jpg")).getImage();

        } catch (Exception e) {
            throw new Exception("Images not found: " + e.getMessage());
        }
    }

    private void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char tileMapChar = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (tileMapChar) {
                    case 'X' -> walls.add(new Block(wallImage, x, y, tileSize, tileSize));
                    case 'b' -> ghosts.add(new Block(creeper1Image, x, y, tileSize, tileSize));
                    case 'o' -> ghosts.add(new Block(creeper2Image, x, y, tileSize, tileSize));
                    case 'p' -> ghosts.add(new Block(creeper3Image, x, y, tileSize, tileSize));
                    case 'r' -> ghosts.add(new Block(creeper4Image, x, y, tileSize, tileSize));
                    case 'P' -> chicken = new Block(chickRightImage, x, y, tileSize, tileSize);
                    case ' ' -> foods.add(new Block(null, x + 14, y + 14, 4, 4));
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(BackgroundImage,0,0,800,800,null);
        g.drawImage(chicken.getImage(), chicken.getX(), chicken.getY(), chicken.getWidth(), chicken.getHeight(), null);
        ghosts.forEach(ghost -> g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null));
        walls.forEach(wall -> g.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight(), null));

        g.setColor(Color.WHITE);
        foods.forEach(food -> g.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight()));

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
        }

        
        g.drawString("Player: " + characterName, tileSize / 2, tileSize / 2 + 20);
    }

    public void actionPerformed(ActionEvent e) {
        if(!isPaused){
            move();
            repaint();
            if (gameOver) {
                gameLoop.stop();
                saveScore("Score.txt");
                showScoreFrame(); 
            }
        }}

    private void move() {
        chicken.setX(chicken.getX() + chicken.velocityX);
        chicken.setY(chicken.getY() + chicken.velocityY);

        for (Block wall : walls) {
            if (collision(chicken, wall)) {
                chicken.setX(chicken.getX() - chicken.velocityX);
                chicken.setY(chicken.getY() - chicken.velocityY);
                break;
            }
        }

        for (Block ghost : ghosts) {
            if (collision(ghost, chicken)) {
                lives -= 1;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            ghost.setX(ghost.getX() + ghost.velocityX);
            ghost.setY(ghost.getY() + ghost.velocityY);
            for (Block wall : walls) {
                if (collision(ghost, wall) || ghost.getX() <= 0 || ghost.getX() + ghost.getWidth() >= boardWidth) {
                    ghost.setX(ghost.getX() - ghost.velocityX);
                    ghost.setY(ghost.getY() - ghost.velocityY);
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(chicken, food)) {
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    private boolean collision(Block a, Block b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    private void resetPositions() {
        chicken.reset();
        chicken.velocityX = 0;
        chicken.velocityY = 0;
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            repaint();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
           
            showEndDialog();
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            chicken.updateDirection('U');
            chicken.setImage(chickUpImage);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            chicken.updateDirection('D');
            chicken.setImage(chickDownImage);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            chicken.updateDirection('L');
            chicken.setImage(chickLeftImage);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            chicken.updateDirection('R');
            chicken.setImage(chickRightImage);
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            isPaused = !isPaused;
        }
    }

    
    public void saveScore(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("Player: " + characterName);
            writer.newLine();
            writer.write("Score: " + score);
            writer.newLine();
            writer.write("----------");
            writer.newLine();
            System.out.println("Score saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void showScoreFrame() {
        JFrame scoreFrame = new JFrame("Game Over");
        scoreFrame.setSize(300, 200);
        scoreFrame.setLocationRelativeTo(null);
        scoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("Player: " + characterName, SwingConstants.CENTER);
        JLabel scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        JButton closeButton = new JButton("Close");

        closeButton.addActionListener(e -> scoreFrame.dispose());

        panel.add(nameLabel);
        panel.add(scoreLabel);
        panel.add(closeButton);

        scoreFrame.add(panel);
        scoreFrame.setVisible(true);
    }
}