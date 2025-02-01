import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame implements ActionListener {
    private JLabel titleLabel;
    private JButton loginBtn, regBtn;
    private JPanel contentPane, panel;

    public MainMenu() {
        super("Creeper Clash");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

       
        contentPane = new JPanel() {
            
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = Toolkit.getDefaultToolkit().getImage("BG.jpg");
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        
        panel = new JPanel(null);
        panel.setOpaque(false);
		
		
		titleLabel = new JLabel("Creeper Clash");
        titleLabel.setBounds(105, 95, 400, 30);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 34));
        panel.add(titleLabel); 


        Color textColor = Color.WHITE;
        Color buttonColor = new Color(0, 0, 0, 250);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 200, 150, 50);
        loginBtn.setForeground(textColor);
        loginBtn.setBackground(buttonColor);
        loginBtn.addActionListener(this);
        panel.add(loginBtn);

        regBtn = new JButton("Register");
        regBtn.setBounds(150, 300, 150, 50);
        regBtn.setForeground(textColor);
        regBtn.setBackground(buttonColor);
        regBtn.addActionListener(this);
        panel.add(regBtn);

        contentPane.add(panel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == loginBtn) {
            new Login().setVisible(true);
            setVisible(false);
        } else if (source == regBtn) {
            new Registration().setVisible(true);
            setVisible(false);
        }
    }
}
