import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakeGame {
    private static StartPanel startPanel; // Đặt StartPanel là một biến toàn cục

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BENA");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);

                startPanel = new StartPanel(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean easyMode = startPanel.isEasyModeSelected(); // Sử dụng biến toàn cục startPanel
                        frame.getContentPane().removeAll();
                        GamePanel gamePanel = new GamePanel(easyMode);
                        frame.add(gamePanel);

                        // Thêm scoreLabel vào khung cửa sổ
                        JLabel scoreLabel = gamePanel.getScoreLabel();
                        frame.add(scoreLabel, BorderLayout.NORTH);

                        frame.pack();
                        gamePanel.requestFocusInWindow();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                    }
                });
                frame.add(startPanel);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}