import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class GamePanel extends JPanel {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int CELL_SIZE = 40;
    private static final int TIMER_DELAY = 200;
    private int gridWidth=0;
    private int gridHeight=0;    
    private ImageIcon headU = new ImageIcon("resources/HeadUp.png");
    private ImageIcon headD = new ImageIcon("resources/HeadDown.png");
    private ImageIcon headL = new ImageIcon("resources/HeadLeft.png");
    private ImageIcon headR = new ImageIcon("resources/HeadRight.png");
    private Image bodyImage = Toolkit.getDefaultToolkit().getImage("resources/Body.png");

    private ImageIcon DheadU = new ImageIcon("resources/DHeadUp.png");
    private ImageIcon DheadD = new ImageIcon("resources/DHeadDown.png");
    private ImageIcon DheadL = new ImageIcon("resources/DHeadLeft.png");
    private ImageIcon DheadR = new ImageIcon("resources/DHeadRight.png");
    private Image DbodyImage = Toolkit.getDefaultToolkit().getImage("resources/DeadBody.png");

    private Image foodImage = Toolkit.getDefaultToolkit().getImage("resources/Food.png");

    private Snake snake;
    private Timer timer;

    private boolean directionChanged = false;
    private boolean keyPressed = false;
    private static final int MIN_KEY_PRESS_INTERVAL = 100;
    private long lastKeyPressTime = 0;
    private int score = 0;
    private JLabel scoreLabel;
    private boolean easyMode = true; // Chế độ mặc định là dễ
    private JButton startButton;
    private JRadioButton easyModeRadioButton;
    private JRadioButton hardModeRadioButton;
    private ButtonGroup modeButtonGroup;
    private JDialog dialog;
    
    // Phương thức để lấy scoreLabel từ bên ngoài lớp
    public JLabel getScoreLabel() {
        return scoreLabel;
    }
    
// Phương thức để cập nhật điểm trên scoreLabel
    public void updateScoreLabel(int score) {
        scoreLabel.setText("Điểm: " + score);
    }    
    
    public GamePanel(boolean pMode) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.easyMode = pMode;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyReleased(e);
            }
        });
        

        setFocusable(true);
        gridWidth = WIDTH / CELL_SIZE;
        gridHeight = HEIGHT / CELL_SIZE;
        snake = new Snake(WIDTH, HEIGHT,gridWidth,gridHeight, easyMode);

        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snake.move();
                repaint();
            }
        });
        timer.start();

        scoreLabel = new JLabel("Điểm: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.BLACK);
        add(scoreLabel, BorderLayout.NORTH);
    }

    private void handleKeyPressed(KeyEvent e) {
        if (!snake.isGameOver()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastKeyPressTime >= MIN_KEY_PRESS_INTERVAL) {
                if (!keyPressed) {
                    int keyCode = e.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.VK_UP:
                            if (snake.getDy() != 1) {
                                snake.changeDirection(0, -1);
                                directionChanged = true;
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if (snake.getDy() != -1) {
                                snake.changeDirection(0, 1);
                                directionChanged = true;
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            if (snake.getDx() != 1) {
                                snake.changeDirection(-1, 0);
                                directionChanged = true;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (snake.getDx() != -1) {
                                snake.changeDirection(1, 0);
                                directionChanged = true;
                            }
                            break;
                    }
                    keyPressed = true;
                    lastKeyPressTime = currentTime;
                }
            }
        } else {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {
                showRestartDialog();
            }
        }
    }

    private void showRestartDialog() {
               JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                JPanel panel = new JPanel(new GridLayout(1, 2));
                easyModeRadioButton = new JRadioButton("Chế độ dễ");
                easyModeRadioButton.setFont(new Font("Arial", Font.PLAIN, 16));
                easyModeRadioButton.setSelected(true); // Chọn mặc định chế độ dễ
                hardModeRadioButton = new JRadioButton("Chế độ khó");
                hardModeRadioButton.setFont(new Font("Arial", Font.PLAIN, 16));
                easyModeRadioButton.setSelected(easyMode?true:false);
                hardModeRadioButton.setSelected(easyMode?false:true);
                System.out.println("Is easy mode selected: " + easyMode);
                modeButtonGroup = new ButtonGroup();
                modeButtonGroup.add(easyModeRadioButton);
                modeButtonGroup.add(hardModeRadioButton);

                // Thêm KeyListener vào để xử lý các phím mũi tên

                panel.add(easyModeRadioButton);
                panel.add(hardModeRadioButton);
                Action okAction = new AbstractAction() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         dialog.dispose();
                         easyMode = easyModeRadioButton.isSelected() ?  true : false;
                         restartGame();
                     }
                 };
                 KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
                 panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(enterKey, "OK");
                 panel.getActionMap().put("OK", okAction);

                 dialog = new JDialog(parentFrame, "Chọn Chế Độ", true);
                 dialog.setContentPane(panel);
                 dialog.pack();
                 dialog.setLocationRelativeTo(parentFrame);
                 dialog.setVisible(true);
    }    
    
      private void restartGame() {
        snake = new Snake(WIDTH, HEIGHT, gridWidth, gridHeight, easyMode);
        timer.start();
    }  
    
    private void handleKeyReleased(KeyEvent e) {
        if (directionChanged) {
            directionChanged = false;
        }
        keyPressed = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int startX = easyMode ? 0 : CELL_SIZE;
        int startY = easyMode ? 0 : CELL_SIZE;

       
        // Vẽ bảng đồ với kích thước ô CELL_SIZE
        for (int i = startX; i < (startX + gridWidth); i++) {
            for (int j = startY; j < (startY + gridHeight); j++) {
                Color color;
            if (!easyMode && (i == startX || i == startX + gridWidth - 1 || j == startY || j == startY + gridHeight - 1)) {
                    // Tô màu xanh lá đậm cho biên của bản đồ
                    color = Color.decode("#006400");
                } else if ((i + j) % 2 == 0) {
                    color = Color.decode("#CCFF99");
                } else {
                    color = Color.decode("#00CC33");
                }
                g2d.setColor(color);
                g2d.fillRect((i - startX) * CELL_SIZE, (j - startY) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        if (!snake.isGameOver()) {
            for (Point p : snake.getBody()) {
                Image headImage = getHeadImage();
                g2d.drawImage(headImage, snake.getHead().x, snake.getHead().y, null);
                g2d.drawImage(bodyImage, p.x, p.y, this);
            }
            g2d.drawImage(foodImage, snake.getFood().x, snake.getFood().y, this);
            if (snake.isGameFinished(gridWidth, gridHeight) || score == 10) {
                timer.stop();
                showWinMessage(score);       
            }                   
        } 
        else {
            timer.stop();
            for (Point p : snake.getBody()) {
                Image DheadImage = getDHeadImage();
                g2d.drawImage(DbodyImage, p.x, p.y, this);
                g2d.drawImage(DheadImage, snake.getHead().x, snake.getHead().y, null);
            }
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            String gameOverMsg = "Game Over!";
            g2d.drawString(gameOverMsg, (WIDTH - g2d.getFontMetrics().stringWidth(gameOverMsg)) / 2, HEIGHT / 2);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            String scoreMsg = "Điểm: " + score;
            g2d.drawString(scoreMsg, (WIDTH - g2d.getFontMetrics().stringWidth(scoreMsg)) / 2, HEIGHT / 2 + 30);
            g2d.setFont(new Font("Arial", Font.PLAIN, 20));
            String isReplay = "Enter để chơi lại!";
            g2d.drawString(isReplay, (WIDTH - g2d.getFontMetrics().stringWidth(isReplay)) / 2, HEIGHT / 2 + 60);
        }
        // Cập nhật điểm trên scoreLabel
            score = snake.getScore();
            updateScoreLabel(score);
        
    }
    
    private void showWinMessage(int score) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        String message = "Chúc mừng, bạn đã chiến thắng!\nSố điểm của bạn: " + score;
        String[] options = {"Chơi lại", "Thoát"};
        int choice = JOptionPane.showOptionDialog(parentFrame, message, "Chúc Mừng!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == JOptionPane.YES_OPTION) {
            // Chơi lại
            showRestartDialog();
        } else {
            // Thoát game
            System.exit(0);
        }
    }    

    private Image getHeadImage() {
        if (snake.getDx() == 0 && snake.getDy() == -1) {
            return headU.getImage();
        } else if (snake.getDx() == 0 && snake.getDy() == 1) {
            return headD.getImage();
        } else if (snake.getDx() == -1 && snake.getDy() == 0) {
            return headL.getImage();
        } else {
            return headR.getImage();
        }
    }

    private Image getDHeadImage() {
        if (snake.getDx() == 0 && snake.getDy() == -1) {
            return DheadU.getImage();
        } else if (snake.getDx() == 0 && snake.getDy() == 1) {
            return DheadD.getImage();
        } else if (snake.getDx() == -1 && snake.getDy() == 0) {
            return DheadL.getImage();
        } else {
            return DheadR.getImage();
        }
    }
}