import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartPanel extends JPanel {
    private JButton startButton;
    private JRadioButton easyModeRadioButton;
    private JRadioButton hardModeRadioButton;
    private ButtonGroup modeButtonGroup;

    public StartPanel(ActionListener listener) {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Bé Na Săn Mồi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        startButton = new JButton("<html>Bắt đầu<br>(Enter)</html>");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.addActionListener(listener);
        buttonPanel.add(startButton);

        easyModeRadioButton = new JRadioButton("Chế độ dễ");
        easyModeRadioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        easyModeRadioButton.setSelected(true); // Chọn mặc định chế độ dễ
        hardModeRadioButton = new JRadioButton("Chế độ khó");
        hardModeRadioButton.setFont(new Font("Arial", Font.PLAIN, 16));

        modeButtonGroup = new ButtonGroup();
        modeButtonGroup.add(easyModeRadioButton);
        modeButtonGroup.add(hardModeRadioButton);

        JPanel modePanel = new JPanel(new GridLayout(2, 1));
        modePanel.add(easyModeRadioButton);
        modePanel.add(hardModeRadioButton);

        buttonPanel.add(modePanel);
        add(buttonPanel, BorderLayout.CENTER);

        // Thêm KeyListener vào StartPanel để xử lý các phím mũi tên
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startButton.doClick(); // Giả lập một cú nhấp vào nút startButton
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    hardModeRadioButton.setSelected(true); // Chọn chế độ khó khi nhấn phím mũi tên xuống
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    easyModeRadioButton.setSelected(true); // Chọn chế độ dễ khi nhấn phím mũi tên lên
                }
            }
        });

        // Làm cho bảng điều khiển có thể nhận được sự tập trung để nhận sự kiện phím
        setFocusable(true);
    }

    public boolean isEasyModeSelected() {
        return easyModeRadioButton.isSelected();
    }
}