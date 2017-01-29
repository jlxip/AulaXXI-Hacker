package net.jlxip.aulaxxihacker;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Students extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String loginCookie;
	private String CourseID;
	protected ArrayList<List<String>> students;
	
	private static final String URL = "https://aulavirtual.murciaeduca.es/user/index.php";
	private static final String BEGSTUDENT = "<td class=\"content cell c1\" style=\"\">";
	private static final String ENDSTUDENT = "</td>";
	private static final String BEGUSERNAME = "<div class=\"username\">";
	private static final String ENDDIV = "</div>";
	private static final String BEGID = "mailto:";
	private static final String ENDID = "@";
	private static final String BEGLAST = "Último acceso:";
	
	private static final String BEGPWD = "<b>";
	private static final String ENDPWD = "</b>";
	
	protected JTable table;
	private JMenuBar menuBar;
	private JMenu mnPrueba;
	private JRadioButtonMenuItem rdbtnmntmAll;
	private JRadioButtonMenuItem rdbtnmntmStudentsWithKnown;
	private JRadioButtonMenuItem rdbtnmntmStudentsWithUnknown;

	/**
	 * Create the frame.
	 */
	public Students(String loginCookie, String CourseID) {
		setTitle("Students");
		this.loginCookie = loginCookie;
		this.CourseID = CourseID;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 403);
		setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnPrueba = new JMenu("Show");
		menuBar.add(mnPrueba);
		
		ButtonGroup show = new ButtonGroup();
		
		rdbtnmntmAll = new JRadioButtonMenuItem("All students");
		rdbtnmntmAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateTable(0);
			}
		});
		rdbtnmntmAll.setSelected(true);
		show.add(rdbtnmntmAll);
		mnPrueba.add(rdbtnmntmAll);
		
		rdbtnmntmStudentsWithUnknown = new JRadioButtonMenuItem("Students with unknown password");
		rdbtnmntmStudentsWithUnknown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTable(1);
			}
		});
		show.add(rdbtnmntmStudentsWithUnknown);
		mnPrueba.add(rdbtnmntmStudentsWithUnknown);
		
		rdbtnmntmStudentsWithKnown = new JRadioButtonMenuItem("Students with known password");
		rdbtnmntmStudentsWithKnown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTable(2);
			}
		});
		show.add(rdbtnmntmStudentsWithKnown);
		mnPrueba.add(rdbtnmntmStudentsWithKnown);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		students = getStudents();
		
		class StudentsTableModel extends DefaultTableModel {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}
		StudentsTableModel model = new StudentsTableModel();
		model.addColumn("Name");
		model.addColumn("Last access");
		model.addColumn("ID");
		model.addColumn("Password");
		table = new JTable(model);
		updateTable(0);
		
		Students me = this;
		
		JPopupMenu popup = new JPopupMenu("Menu");
		JMenuItem MIcopy = new JMenuItem("Copy");
		MIcopy.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String data = (String)table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
				if(Pattern.compile(Pattern.quote(BEGPWD)).split(data).length > 1) {
					String begPWD = Pattern.compile(Pattern.quote(BEGPWD)).split(data)[1];
					data = Pattern.compile(Pattern.quote(ENDPWD)).split(begPWD)[0];
				}
				
				Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection ss = new StringSelection(data);
				cb.setContents(ss, ss);
				
				popup.setVisible(false);
			}
		});
		popup.add(MIcopy);
		popup.add(new JSeparator());
		JMenuItem MIdefault = new JMenuItem("Default password check");
		MIdefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popup.setVisible(false);
				int confirm = JOptionPane.showConfirmDialog(null, "Confirm the action: default password check");
				if(confirm == 0) {
					DefaultPasswordCheck.run(me);
				}
			}
		});
		popup.add(MIdefault);
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getButton() == MouseEvent.BUTTON3 && table.getSelectedRowCount() > 0) {
					popup.setLocation(arg0.getLocationOnScreen());
					popup.setVisible(true);
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
		scrollPane.setViewportView(table);
	}
	
	private ArrayList<List<String>> getStudents() {
		ArrayList<List<String>> students = new ArrayList<List<String>>();
		
		String query = "roleid=5&id="+CourseID+"&perpage=5000&search=&mode=1";
		String page = RunRequest.run(URL, "GET", query, loginCookie)[0];
		
		String[] rawstudents = Pattern.compile(Pattern.quote(BEGSTUDENT)).split(page);
		for(int i=1;i<rawstudents.length;i++) {
			String endstudent = Pattern.compile(Pattern.quote(ENDSTUDENT)).split(rawstudents[i])[0];
			
			String begusername = Pattern.compile(Pattern.quote(BEGUSERNAME)).split(endstudent)[1];
			String username = Pattern.compile(Pattern.quote(ENDDIV)).split(begusername)[0];
			
			String begID = Pattern.compile(Pattern.quote(BEGID)).split(endstudent)[1];
			String ID = Pattern.compile(Pattern.quote(ENDID)).split(begID)[0];
			
			String beglast = Pattern.compile(Pattern.quote(BEGLAST)).split(endstudent)[1];
			String last = Pattern.compile(Pattern.quote(ENDDIV)).split(beglast)[0];
			last = last.replace("&nbsp;", "");
			
			ArrayList<String> student = new ArrayList<String>();
			student.add(username);
			student.add(last);
			student.add(ID);
			student.add("(Unknown)");
			
			students.add(student);
		}
		
		return students;
	}
	
	protected void updateTable(int option) {
		/*
		 * OPTIONS
		 * 0: ALL
		 * 1: DEFAULT
		 * 2: NOT DEFAULT
		 * */
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		
		// CLEAN ROWS
		final int rowCount = model.getRowCount();
		for(int i=rowCount-1;i>=0;i--) {
			model.removeRow(i);
		}
		
		for(int i=0;i<students.size();i++) {
			Boolean valid = false;
			if(option == 0) valid = true;
			if(option == 1 && students.get(i).get(3).equals("(Unknown)")) valid = true;
			if(option == 2 && !students.get(i).get(3).equals("(Unknown)")) valid = true;
			
			if(valid) model.addRow(new Object[]{students.get(i).get(0), students.get(i).get(1), students.get(i).get(2), students.get(i).get(3)});
		}
		
		table.setModel(model);
	}
}
