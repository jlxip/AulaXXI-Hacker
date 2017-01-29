package net.jlxip.aulaxxihacker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class SelectCourse extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> list;
	
	private String loginCookie = "";
	private ArrayList<List<String>> courses;
	
	private static final String CoursesURL = "https://aulavirtual.murciaeduca.es/my/";
	private static final String CourseBEG = "<h2 class=\"title\"><a title=\"";
	private static final String CourseEND = "\"";

	/**
	 * Create the frame.
	 */
	public SelectCourse(String loginCookie) {
		this.loginCookie = loginCookie;
		
		setResizable(false);
		setTitle("Select course");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 539);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 42, 643, 383);
		contentPane.add(scrollPane);
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		courses = getCourses();
		for(int i=0;i<courses.size();i++) {
			model.addElement(courses.get(i).get(0));
		}
		
		list = new JList<String>(model);
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2) {
					finish();
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		scrollPane.setViewportView(list);
		
		JButton btnSelect = new JButton("ACCESS");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				finish();
			}
		});
		btnSelect.setBounds(12, 438, 643, 53);
		contentPane.add(btnSelect);
		
		JLabel lblLoggedInAs = new JLabel("Logged in as");
		lblLoggedInAs.setBounds(12, 13, 72, 16);
		contentPane.add(lblLoggedInAs);
		
		String Sname = Pattern.compile(Pattern.quote("</span>")).split(Pattern.compile(Pattern.quote("<span class=\"usertext\">")).split(RunRequest.run(CoursesURL, "GET", "", loginCookie)[0])[1])[0];
		JLabel name = new JLabel(Sname);
		name.setBounds(88, 13, 567, 16);
		contentPane.add(name);
	}
	
	private ArrayList<List<String>> getCourses() {
		String page = RunRequest.run(CoursesURL, "GET", "", loginCookie)[0];
		
		ArrayList<List<String>> courses = new ArrayList<List<String>>();
		
		String[] arraybegcourses = Pattern.compile(Pattern.quote(CourseBEG)).split(page);
		for(int i=1;i<arraybegcourses.length;i++) {
			ArrayList<String> course = new ArrayList<String>();
			String[] quotes = Pattern.compile(Pattern.quote(CourseEND)).split(arraybegcourses[i]);
			course.add(quotes[0]);
			course.add(quotes[2]);
			courses.add(course);
		}
		
		return courses;
	}
	
	private static final String CourseID = "?id=";
	
	public void finish() {
		if(list.isSelectionEmpty()) {
			return;
		}
		
		int n = list.getSelectedIndex();
		
		String ID = Pattern.compile(Pattern.quote(CourseID)).split(courses.get(n).get(1))[1];

		new Students(loginCookie, ID).setVisible(true);
		
		dispose();
	}
}
