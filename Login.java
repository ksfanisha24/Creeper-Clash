import java.lang.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


//Login class
public class Login extends JFrame implements ActionListener
{
	JLabel emailLabel, passLabel, regLabel, titleLabel;
	JTextField emailTF;
	JPasswordField passPF;
	JButton loginBtn, regBtn, backBtn;
	JPanel contentPane, panel;
	
	public Login()
	{
		super("Login");
		this.setSize(450, 550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		 
        contentPane = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = Toolkit.getDefaultToolkit().getImage("BG2.jpg"); // Replace with your image path
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Scale image to fit panel
            }
        };
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        panel = new JPanel();
        panel.setOpaque(false); 
        panel.setLayout(null);
		
		Color textColor = Color.WHITE; 
        Color buttonColor = new Color(0, 0, 0, 250);
		
		titleLabel = new JLabel("Creeper Clash");
		titleLabel.setBounds(140, 40, 400, 30);
		Font currentFont = titleLabel.getFont();
		titleLabel.setFont(currentFont.deriveFont(Font.BOLD, 24));
		titleLabel.setFont(currentFont.deriveFont(currentFont.getStyle(), 24));
		panel.add(titleLabel);
		
		emailLabel = new JLabel("Email : ");
		emailLabel.setBounds(70, 115, 160, 30);
		panel.add(emailLabel);
		
		emailTF = new JTextField();
		emailTF.setBounds(170, 115, 200, 30);
		panel.add(emailTF);
		
		
		
		passLabel = new JLabel("Password : ");
		passLabel.setBounds(70, 165, 160, 30);
		panel.add(passLabel);
		
		passPF = new JPasswordField();
		passPF.setBounds(170, 165, 200, 30);
		panel.add(passPF);
		
		
		
		loginBtn = new JButton("Login");
		loginBtn.setBounds(180, 225, 80, 30);
		loginBtn.setForeground(textColor);
		loginBtn.setBackground(buttonColor);
		loginBtn.addActionListener(this);
		panel.add(loginBtn);
		
		
		
		regLabel = new JLabel("Do not have an account? ");
		regLabel.setBounds(50, 300, 160, 30);
		panel.add(regLabel);
		
		regBtn = new JButton("Register");
		regBtn.setBounds(200, 300, 160, 30);
		regBtn.setForeground(textColor);
		regBtn.setBackground(buttonColor);
		regBtn.addActionListener(this);
		panel.add(regBtn);
		
		backBtn = new JButton("Back");
		backBtn.setBounds(50, 450, 80, 30);
		backBtn.setForeground(textColor);
		backBtn.setBackground(buttonColor);
		backBtn.addActionListener(this);
		panel.add(backBtn);
		
		contentPane.add(panel);
		this.setLocationRelativeTo(null);
	}
	
	
	public void actionPerformed(ActionEvent ae) {
    String command = ae.getActionCommand();

    if (loginBtn.getText().equals(command)) {
        login();
    } else if (regBtn.getText().equals(command)) {
        Registration reg = new Registration();
        reg.setVisible(true);
        this.setVisible(false);
    } else if (backBtn.getText().equals(command)) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.setVisible(true);
        this.setVisible(false);
    }
}
	
	
	
	private void login() {
    String email = emailTF.getText();
    String password = new String(passPF.getPassword());
    User user = null;

    try {
        File file = new File("Userdata.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "No user registered yet");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        boolean loggedIn = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts[1].equals(email) && parts[2].equals(password)) {
                loggedIn = true;
                user = new User(parts[0], parts[1], parts[2]);
                break;
            }
        }
        reader.close();

        if (loggedIn) {
    try {
      App.launchGame(); 
    } catch (Exception e) {
      
      e.printStackTrace();
    }
  } else {
    JOptionPane.showMessageDialog(this, "Invalid email or password");
  }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}	
}
//Registration class
class Registration extends JFrame implements ActionListener
{
	JLabel userLabel, passLabel, emailLabel, titleLabel;
	JTextField userTF, emailTF;
	JPasswordField passPF;
	JButton regBtn, clearBtn, backBtn;
	JPanel contentPane, panel;
	
	
	
	public Registration()
	{
		super("Registration");
		this.setSize(450, 550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        contentPane = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = Toolkit.getDefaultToolkit().getImage("BG2.jpg"); // Replace with your image path
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Scale image to fit panel
            }
        };
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(null);
		
		Color textColor = Color.WHITE; 
        Color buttonColor = new Color(0, 0, 0, 250);
		
		titleLabel = new JLabel("Creeper Clash");
		titleLabel.setBounds(140, 40, 400, 30);
		Font currentFont = titleLabel.getFont();
		titleLabel.setFont(currentFont.deriveFont(Font.BOLD, 24)); 
        panel.add(titleLabel);
		
		userLabel = new JLabel("Username : ");
		userLabel.setBounds(70, 115, 160, 30);
		panel.add(userLabel);
		
		userTF = new JTextField();
		userTF.setBounds(170, 115, 200, 30);
		panel.add(userTF);
		
		
		
		emailLabel = new JLabel("Email : ");
		emailLabel.setBounds(70, 165, 160, 30);
		panel.add(emailLabel);
		
		emailTF = new JTextField();
		emailTF.setBounds(170, 165, 200, 30);
		panel.add(emailTF);
		
		
		
		passLabel = new JLabel("Password : ");
		passLabel.setBounds(70, 215, 100, 30);
		panel.add(passLabel);
		
		passPF = new JPasswordField();
		passPF.setBounds(170, 215, 200, 30);
		panel.add(passPF);
		
		
		
		regBtn = new JButton("Register");
		regBtn.setBounds(220, 280, 100, 30);
		regBtn.setForeground(textColor);
		regBtn.setBackground(buttonColor);
		regBtn.addActionListener(this);
		panel.add(regBtn);
		
		clearBtn = new JButton("Clear");
		clearBtn.setBounds(125, 280, 80, 30);
		clearBtn.setForeground(textColor);
		clearBtn.setBackground(buttonColor);
		clearBtn.addActionListener(this);
		panel.add(clearBtn);
		
		backBtn = new JButton("Back");
		backBtn.setBounds(50, 450, 80, 30);
		backBtn.setForeground(textColor);
		backBtn.setBackground(buttonColor);
		backBtn.addActionListener(this);
		panel.add(backBtn);
		
		contentPane.add(panel);
		this.setLocationRelativeTo(null);
	}
	
	
	public void actionPerformed(ActionEvent ae) {
    String command = ae.getActionCommand();

    if (regBtn.getText().equals(command)) {
        register();
    } else if (clearBtn.getText().equals(command)) {
        clearFields();
    } else if (backBtn.getText().equals(command)) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.setVisible(true);
        this.setVisible(false);
    }
}
	
	
	
	private void register()
	{
		String name = userTF.getText();
		String email = emailTF.getText();
		String password = new String(passPF.getPassword());
		
		if(name.isEmpty() || email.isEmpty() || password.isEmpty())
		{
			JOptionPane.showMessageDialog(this, "Please fill all the fields");
			return;
		}
		
		if(isEmailExists(email))
		{
			JOptionPane.showMessageDialog(this, "This email is already taken");
			return;
		}
		
		User newUser = new User(name, email, password);
		
		try
		{
			FileWriter writer = new FileWriter("Userdata.txt", true);
			writer.write(newUser.getName() + "," + newUser.getEmail() + "," + newUser.getPassword() + "\n");
			writer.close();
			JOptionPane.showMessageDialog(this, "Registration Successful");
			
			Login log = new Login();
			log.setVisible(true);
			this.setVisible(false);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	
	private boolean isEmailExists(String email)
	{
		try
		{
			File file = new File("Userdata.txt");
			if(!file.exists())
			{
				return false;
			}
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = reader.readLine()) != null)
			{
				String[] parts = line.split(",");
				if(parts[1].equals(email))
				{
					reader.close();
					return true;
				}
			}
			reader.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	
	
	private void clearFields()
	{
		userTF.setText("");
		emailTF.setText("");
		passPF.setText("");
	}
}

//Dashboard class
class Dashboard extends JFrame implements ActionListener
{
	JLabel label;
	JButton logoutBtn;
	JPanel panel;
	
	
	
	public Dashboard(User user)
	{
		super("Dashboard");
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(null);
		
		
		
		label = new JLabel("Dashboard " +user.getName());
		label.setBounds(150, 50, 300, 30);
		panel.add(label);
		
		
	
		logoutBtn = new JButton("Logout");
		logoutBtn.setBounds(450, 50, 100, 30);
		logoutBtn.addActionListener(this);
		panel.add(logoutBtn);
		
		
		
		this.add(panel);
		this.setLocationRelativeTo(null);
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		String command = ae.getActionCommand();
		
		if(logoutBtn.getText().equals(command))
		{
			Login log = new Login();
			log.setVisible(true);
			this.setVisible(false);
		}
	}
} 