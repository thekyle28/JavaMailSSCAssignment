package emailGui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Scrollbar;
import java.awt.ScrollPane;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javaMailExample.SendSMTPMail;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JScrollPane;

public class SendEmail extends JFrame {

	private JPanel contentPane;
	private JTextField to;
	private JPanel panel_1;
	private JLabel lblCc;
	private JTextField cc;
	private JPanel panel_2;
	private JTextArea messageContent;
	private JButton send;
	private JLabel lblSubject;
	private JTextField subject;
	private JPanel panel_3;
	private JScrollPane scrollPane;
	private JButton attachment;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendEmail frame = new SendEmail();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the Send email GUI/frame.
	 */
	public SendEmail() {
		setBounds(100, 100, 1071, 735);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{1043, 0};
		gbl_contentPane.rowHeights = new int[]{105, 573, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.anchor = GridBagConstraints.NORTH;
		gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		contentPane.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel_2.add(panel);
		
		JLabel toLabel = new JLabel("To:");
		panel.add(toLabel);
		
		to = new JTextField();
		to.setToolTipText("To:");
		panel.add(to);
		to.setColumns(30);
		

		panel_1 = new JPanel();
		panel_2.add(panel_1);
		
		lblCc = new JLabel("Cc:");
		panel_1.add(lblCc);
		
		cc = new JTextField();
		cc.setToolTipText("Cc:");
		cc.setColumns(30);
		panel_1.add(cc);
		
		//create an arraylist to store all of the attachment file names
		ArrayList<String> attachmentFileName = new ArrayList<String>();
		System.out.println(attachmentFileName.toArray().toString());
		attachment = new JButton("Attachment");
		attachment.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			            "JPG & GIF Images", "jpg", "gif");
			        chooser.setFileFilter(filter);
			        int returnVal = chooser.showOpenDialog(getParent());
			        if(returnVal == JFileChooser.APPROVE_OPTION) {
			           attachmentFileName.add(chooser.getSelectedFile().getAbsolutePath());
			           
			        	System.out.println("You chose to open this file: " +
			                chooser.getSelectedFile().getAbsolutePath());
			        }
			}
		});
		panel.add(attachment);
		
		send = new JButton("Send");
		send.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					new SendSMTPMail(subject.getText(), messageContent.getText(), to.getText() ,cc.getText().split(";"), attachmentFileName.toArray() );
			}
		});
		panel.add(send);
		
		
		panel_3 = new JPanel();
		panel_2.add(panel_3);
		
		lblSubject = new JLabel("Subject:");
		panel_3.add(lblSubject);
		
		subject = new JTextField();
		panel_3.add(subject);
		subject.setHorizontalAlignment(SwingConstants.LEFT);
		subject.setToolTipText("Subject:");
		subject.setColumns(30);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		messageContent = new JTextArea();
		scrollPane.setViewportView(messageContent);
		
	}

}
