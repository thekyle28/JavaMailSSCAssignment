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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.Color;

public class EmailGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public EmailGUI(ArrayList<String> subjects, ArrayList<Object> contents) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 983, 769);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		Object[] contentsArray = contents.toArray();
		JList<String> list = new JList<String>();

		contentPane.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Mail Client");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		contentPane.add(lblNewLabel);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel() {
			//sets the values of the Jlist from the subjects arrayList in the main class. 
			String[] values = subjects.toString().substring(1, (subjects.toString().length()) - 1).split("\n");
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}

		});
		contentPane.add(list);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		JTextArea Preview = new JTextArea();
		scrollPane.setViewportView(Preview);
		Preview.setEditable(false);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				System.out.println(contents.toString());
				System.out.println(list.getSelectedIndex());

				String content = contentsArray[list.getSelectedIndex()].toString();
				System.out.println(list.getSelectedIndex());
				System.out.println(content);
				Preview.setText(contentsArray[list.getSelectedIndex()].toString());
				
			}
		});
		
	}

}
