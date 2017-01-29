package net.jlxip.aulaxxihacker;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	private JButton btnLogIn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setTitle("Log in");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(337, 408);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(12, 191, 63, 16);
		contentPane.add(lblUsername);
		
		username = new JTextField();
		username.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		username.setBounds(87, 188, 229, 28);
		contentPane.add(username);
		username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(12, 229, 63, 16);
		contentPane.add(lblPassword);
		
		password = new JPasswordField();
		password.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		password.setBounds(87, 223, 229, 28);
		contentPane.add(password);
		
		btnLogIn = new JButton("LOG IN");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				finish();
			}
		});
		btnLogIn.setBounds(12, 290, 305, 70);
		contentPane.add(btnLogIn);
		
		JLabel logo = new JLabel("");
		logo.setIcon(new ImageIcon(Main.class.getResource("/net/jlxip/aulaxxihacker/logo.png")));
		logo.setBounds(81, 13, 166, 162);
		contentPane.add(logo);
	}
	
	public void finish() {
		if(password.getPassword() == new char[]{}) {
			JOptionPane.showMessageDialog(null, "You have to introduce a password.");
			return;
		}
		if(username.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "You have to introduce a username.");
			return;
		}
		
		String loginCookie = Login.login(username.getText(), password.getPassword());
		if(loginCookie == null) {
			btnLogIn.setText("LOG IN");
			JOptionPane.showMessageDialog(null, "Incorrect username or password.");
			return;
		}
		
		new SelectCourse(loginCookie).setVisible(true);
		this.dispose();
	}
}
