package emailGui;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;

import javaMailExample.IMAPClient;

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JLabel;

/**
 * @author Kyle
 *
 */
public class EmailGUI extends JFrame {

	private JPanel contentPane;
	private JTextField searchText;
	private static JList<String> list;
	private JTextField flagName;
	private JTextField flagKeywords;

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
		unread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					IMAPClient client = new IMAPClient("UNREAD", list.getSelectedValue(), list.getSelectedIndex());
					
				} catch (IOException | MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(unread);
		
			
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
		
		JButton newFlag = new JButton("Create a Flag");
		
		newFlag.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					IMAPClient client = new IMAPClient(flagName.getText(), flagKeywords.getText(), list.getSelectedIndex());
				} catch (MessagingException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		
		JLabel lblNewLabel_1 = new JLabel("Flag Name");
		panel_4.add(lblNewLabel_1);
		
		flagName = new JTextField();
		panel_4.add(flagName);
		flagName.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		
		JLabel lblNewLabel = new JLabel("Keywords");
		panel_3.add(lblNewLabel);
		
		flagKeywords = new JTextField();
		panel_3.add(flagKeywords);
		flagKeywords.setColumns(10);
		panel.add(newFlag);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(!list.isSelectionEmpty()){
				String content = contentsArray[list.getSelectedIndex()].toString();
				//System.out.println(content);
				Preview.setText(contentsArray[list.getSelectedIndex()].toString());
				}
				
				
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
	
	
	/**
	 * This updates the JList so that it displays the content that the user has searched for.
	 */
	private void updateSubjects() {
		String search = searchText.getText();
		IMAPClient client = new IMAPClient(search);
	}

	/**
	 * @param subjects The subjects to be used to update the inbox on the GUI
	 * @param contents The content to be displayed when the subjects are clicked on
	 * 		  in the JList
	 */
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
