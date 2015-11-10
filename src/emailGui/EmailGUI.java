package emailGui;

import javax.mail.Message;
import javax.mail.search.SearchTerm;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.ListModel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javaMailExample.IMAPClient;

public class EmailGUI extends JFrame {

	private JPanel contentPane;
	private JTextField searchText;
	private static JList<String> list;

	/**
	 * Create the Email GUI.
	 */
	@SuppressWarnings("unchecked")
	public EmailGUI(ArrayList<String> subjects, ArrayList<Object> contents) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1368, 1001);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		Object[] contentsArray = contents.toArray();
		contentPane.setLayout(new MigLayout("", "[321px][700px][321px,grow]", "[1000px,grow]"));
		this.list = new JList<String>();
		list.setModel(new AbstractListModel() {
			//sets the values of the Jlist from the subjects arrayList in the main class. 
			String[] values = subjects.toString().substring(1, (subjects.toString().length()) - 1).split("\n, ");
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}

		});
		contentPane.add(list, "cell 0 0,growx,aligny top");
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 1 0,grow");

		JTextArea Preview = new JTextArea();
		scrollPane.setViewportView(Preview);
		Preview.setEditable(false);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 2 0,growx,aligny top");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		JButton sendEmail = new JButton("New Email");
		sendEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new SendEmail().setVisible(true);
			}
		});
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		

		panel.add(sendEmail);
		
		JButton unread = new JButton("Mark as unread");
		panel.add(unread);
		unread.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				list.setSelectedIndex(list.getSelectedIndex());
				
			}
		});
			
	/*		@Override
			public void actionPerformed(ActionEvent e) {
				String subject = list.getSelectedValue();
					  if (((MouseEvent) e).getClickCount() == 2) {
			                 int index = list.locationToIndex(e.getPoint());
			                 System.out.println("Double clicked on Item " + index);

			                 //??????? CHANGEVALUE(index,"MY NEW VALUE); ????????
			                 d.setElementAt("MY NEW VALUE", index);
			              }

				};);
				
				//System.out.println("you clicked the button");
			});*/
		
		JButton btnNewButton_2 = new JButton("New button");
		panel.add(btnNewButton_2);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {

				String content = contentsArray[list.getSelectedIndex()].toString();
				//System.out.println(content);
				Preview.setText(contentsArray[list.getSelectedIndex()].toString());
				
				
			}
		});
		
		searchText = new JTextField();
		searchText.addActionListener(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSubjects();
			}
		});
		
		searchText.setToolTipText("Search");
		panel_1.add(searchText);
		searchText.setHorizontalAlignment(SwingConstants.LEFT);
		searchText.setColumns(20);
		
		JButton searchButton = new JButton("Search");
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				updateSubjects();
			}
		});
		panel_1.add(searchButton);
		
	}
	
	private void updateSubjects() {
		String search = searchText.getText();
		IMAPClient client = new IMAPClient(search);
	}

	public static void updateInbox(ArrayList<String> subjects, ArrayList<Object> contents){
		list.setModel(new AbstractListModel() {
			//sets the values of the Jlist from the subjects arrayList in the main class. 
			String[] values = subjects.toString().substring(1, (subjects.toString().length()) - 1).split("\n, ");
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}

		});
		
	}
}
