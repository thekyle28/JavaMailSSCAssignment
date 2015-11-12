package javaMailExample;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.util.*;

/**
 * @author Kyle
 *
 */
public class SendSMTPMail {

	private String subject;
	private String messageContent;
	private String[] cc;
	private String to;
	private Object[] attachments;


	/**
	 * This class sends emails.
	 * @param subject The subject line for the message
	 * @param messageContent The content of the message to be sent
	 * @param to The address of the recipient of the email
	 * @param cc The address of the other recipients who are cc'd
	 * @param attachments The attachments to go with the email
	 */
	public SendSMTPMail(String subject, String messageContent, String to,
			String[] cc, Object[] attachments) {
		this.subject = subject;
		this.messageContent = messageContent;
		this.cc = cc;
		this.to = to;
		this.attachments = attachments;

		String smtphost = "smtp.gmail.com";

		// Step 1: Set all Properties
		// Get system properties
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");



		// Set Property with username and password for authentication
		props.setProperty("mail.user", IMAPClient.username);
		props.setProperty("mail.password", IMAPClient.password);

		// Step 2: Establish a mail session (java.mail.Session)
		Session session = Session.getDefaultInstance(props);

		try {

			// Step 3: Create a message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(IMAPClient.username));
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
				tr.connect(smtphost, IMAPClient.username, IMAPClient.password); // We need to connect
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
				tr.connect(smtphost, IMAPClient.username, IMAPClient.password); // We need to connect
				tr.sendMessage(message, message.getAllRecipients()); // Send
				// message
				// the message was successfully sent.
				System.out.println("Done");
				
			}

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
