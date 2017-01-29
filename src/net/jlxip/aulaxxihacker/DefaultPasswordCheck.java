package net.jlxip.aulaxxihacker;

public class DefaultPasswordCheck {
	private static final String DEFAULT_PASSWORD = "A12345678";
	
	public static void run(Students parent) {
		int[] selectedRows = parent.table.getSelectedRows();
		for(int i=0;i<selectedRows.length;i++) {
			String ID = parent.students.get(selectedRows[i]).get(2);
			String CURRENT_PASSWORD = parent.students.get(selectedRows[i]).get(3);
			
			if(!CURRENT_PASSWORD.equals("(Unknown)")) continue;
			
			String result = Login.login(ID, DEFAULT_PASSWORD.toCharArray());
			if(result == null) continue;
			
			final String HTMLTEXT = "<html><b>"+DEFAULT_PASSWORD+"</b></html>";
			parent.students.get(selectedRows[i]).set(3, HTMLTEXT);
		}
		
		parent.updateTable(0);
	}
}
