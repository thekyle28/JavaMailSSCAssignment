package javaMailExample;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.util.*;

public class SendSMTPMail {

	private String subject;
	private String messageContent;
	private String[] cc;
	private String to;
	private Object[] attachments;

	/**
	 * @param args
	 */
	public SendSMTPMail(String subject, String messageContent, String to,
			String[] cc, Object[] attachments) {
		this.subject = subject;
		this.messageContent = messageContent;
		this.cc = cc;
		this.to = to;
		this.attachments = attachments;

		String username = "kylearat@gmail.com";
		String password = "Kingdomhearts28";
		String smtphost = "smtp.gmail.com";

		// Step 1: Set all Properties
		// Get system properties
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");

		// Input password
		// JPasswordField pwd = new JPasswordField(10);
		// int action = JOptionPane.showConfirmDialog(null,
		// pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);
		// if(action < 0) {
		// JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected");
		// System.exit(0);
		// }
		// else
		// password = new String(pwd.getPassword());

		// Set Property with username and password for authentication
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		// Step 2: Establish a mail session (java.mail.Session)
		Session session = Session.getDefaultInstance(props);

		try {

			// Step 3: Create a message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			for (int i = 0; i < cc.length; i++) {
				message.addRecipients(Message.RecipientType.CC,
						InternetAddress.parse(cc[i]));
			}
			message.setSubject(subject);
			if (attachments.length == 0) {

				message.setText(messageContent);

				message.saveChanges();

				// Step 4: Send the message by javax.mail.Transport .
				Transport tr = session.getTransport("smtp"); // Get Transport
																// object from
																// session
				tr.connect(smtphost, username, password); // We need to connect
				tr.sendMessage(message, message.getAllRecipients()); // Send
																		// message

				System.out.println("Done");
			}

			else {
				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();

				// Now set the actual message
				messageBodyPart.setText(messageContent);
				
				// Create a multipart message
				Multipart multipart = new MimeMultipart();

				// Set text message part
				multipart.addBodyPart(messageBodyPart);
				
				for (int i = 0; i < attachments.length; i++) {

					// Part two is attachment
					messageBodyPart = new MimeBodyPart();
					String filename = attachments[i].toString();
					DataSource source = new FileDataSource(filename);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename);
					multipart.addBodyPart(messageBodyPart);

					message.setContent(multipart);

				}
				Transport tr = session.getTransport("smtp"); // Get Transport
				// object from
				// session
				tr.connect(smtphost, username, password); // We need to connect
				tr.sendMessage(message, message.getAllRecipients()); // Send
				// message

				System.out.println("Done");
			}

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
