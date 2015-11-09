package emailGui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.Color;
import javaMailExample.SendSMTPMail;

import net.miginfocom.swing.MigLayout;

public class EmailGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the Email GUI.
	 */
	public EmailGUI(ArrayList<String> subjects, ArrayList<Object> contents) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1265, 1001);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		Object[] contentsArray = contents.toArray();
		contentPane.setLayout(new MigLayout("", "[321px][700px][321px,grow]", "[1000px,grow]"));
		JList<String> list = new JList<String>();
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
		contentPane.add(panel, "cell 2 0,grow");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		
		JButton sendEmail = new JButton("Send Email");
		sendEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				new SendEmail().setVisible(true);
			}
		});
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
		
	}

}
