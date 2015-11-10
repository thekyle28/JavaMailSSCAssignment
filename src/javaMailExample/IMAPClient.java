package javaMailExample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JPasswordField;
import javax.mail.Multipart;
import javax.mail.search.SearchTerm;

import com.sun.mail.handlers.message_rfc822;
import com.sun.mail.imap.IMAPFolder;

import emailGui.EmailGUI;

public class IMAPClient {

	public static void main(String args[]) throws MessagingException,
			IOException {
		new IMAPClient();
	}

	private static IMAPFolder folder;
	private static Store store;
	private static EmailGUI gui;
	private static String multipartContent;

	public IMAPClient(String search) {
		try {
			getMessages(search);
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IMAPClient() throws IOException, MessagingException {
		// TODO Auto-generated method stub
		this.folder = null;
		this.store = null;

		String username = "kylearat@gmail.com";
		String password = "Kingdomhearts28";

		// Step 1.1: set mail user properties using Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		// Get user password using JPasswordField
		JPasswordField pwd = new JPasswordField(10);
		/*
		 * int action = JOptionPane.showConfirmDialog(null,
		 * pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION); if(action < 0) {
		 * JOptionPane
		 * .showMessageDialog(null,"Cancel, X or escape key selected");
		 * System.exit(0); } else password = new String(pwd.getPassword());
		 */

		// Set Property with username and password for authentication
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		// Step 1.2: Establish a mail session (java.mail.Session)
		Session session = Session.getDefaultInstance(props);

		try {
			// Step 2: Get the Store object from the mail session
			// A store needs to connect to the IMAP server
			store = session.getStore("imaps");
			store.connect("imap.googlemail.com", username, password);

			// Step 3: Choose a folder, in this case, we chose inbox
			folder = (IMAPFolder) store.getFolder("inbox");

			// Step 4: Open the folder
			if (!folder.isOpen())
				folder.open(Folder.READ_WRITE);

			// Step 5: Get messages from the folder
			// Get total number of message
			System.out.println("No of Messages : " + folder.getMessageCount());
			// Get total number of unread message
			System.out.println("No of Unread Messages : "
					+ folder.getUnreadMessageCount());

			int count = 0;
			ArrayList<String> subjects = new ArrayList<String>();
			ArrayList<Object> contents = new ArrayList<Object>();
			this.multipartContent = new String();

			Message messages[] = folder.getMessages();

			getMessageContent(subjects, contents, messages);
			// creates a new gui for the emails to be viewed in.
			this.gui = new EmailGUI(subjects, contents);

			gui.setVisible(true);

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (folder != null && folder.isOpen()) {
				folder.close(true);
			}
			if (store != null) {
				store.close();
			}
		}

	}

	private static void getMessageContent(ArrayList<String> subjects,
			ArrayList<Object> contents, Message[] messages)
			throws MessagingException, IOException {
		for (Message message : messages) {

			// Get subject of each message
			Flags mes_flag = message.getFlags();
			String tags = new String();
			if (mes_flag.contains(Flag.FLAGGED)) {
				tags += "FLAGGED ";
			}
			if (mes_flag.contains(Flag.SEEN)) {
				tags += "READ ";
			} else if (!mes_flag.contains(Flag.SEEN)) {
				tags += "UNREAD ";
			}
			if (mes_flag.contains(Flag.RECENT)) {
				tags += "RECENT ";
			}
			if (mes_flag.contains(Flag.ANSWERED)) {
				tags += "ANSWERED ";
			}

			subjects.add(tags + message.getSubject() + "\n");

			// System.out.println("The " + count + "th message is: " +
			// message.getSubject());
			// System.out.println(message.getContentType());
			if (message.getContentType().contains("TEXT/PLAIN")) {
				contents.add(message.getContent());
			} else {
				multipartContent = "";
				// How to get parts from multiple body parts of MIME message
				multipartContent = extractMultipartMessage(multipartContent,
						message);

				contents.add(multipartContent);
			}

			// System.out.println("Has this message been read?  " +
			// mes_flag.contains(Flag.SEEN));
		}
	}

	private static String extractMultipartMessage(String multipartContent,
			Message message) throws IOException, MessagingException {
		Multipart multipart = (Multipart) message.getContent();

		for (int x = 0; x < multipart.getCount(); x++) {
			BodyPart bodyPart = multipart.getBodyPart(x);
			// If the part is a plan text message, then print it
			// out.
			if (bodyPart.getContentType().contains("TEXT/PLAIN")) {
				multipartContent += bodyPart.getContent().toString();
				// System.out.println(bodyPart.getContentType());
				// System.out.println(bodyPart.getContent().toString());
			} else {
				multipartContent += "";
				// System.out.println(bodyPart.getContentType()
				// .toString());
			}

		}
		return multipartContent;
	}

	private static void getMessages(String searchText)
			throws MessagingException, IOException {

		try {

			ArrayList<String> subjects = new ArrayList<String>();
			ArrayList<Object> contents = new ArrayList<Object>();

			if (!folder.isOpen())
				folder.open(Folder.READ_WRITE);

			// create a search term using the search text sent earlier
			SearchTerm search = new SearchTerm() {

				@Override
				public boolean match(Message message) {
					try {
						if (message.getContentType().contains("TEXT/PLAIN")) {
							// check if the search text is in the message,
							// subject,
							// or in the flags
							try {
								if (message.getContent().toString()
										.contains(searchText)
										|| message.getSubject().toString()
												.contains(searchText)
										|| message.getFlags().contains(
												searchText)) {
									return true;
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						else {
							multipartContent = "";

							multipartContent = extractMultipartMessage(multipartContent, message);

							if (multipartContent.toString().contains(searchText)
									|| message.getSubject().toString()
											.contains(searchText)) {
								return true;
							}

						}
					} catch (MessagingException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return false;
					
				}
			};
			Message messages[] = folder.search(search);

			getMessageContent(subjects, contents, messages);
			// creates a new gui for the emails to be viewed in.
			gui.updateInbox(subjects, contents);

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (folder != null && folder.isOpen()) {
				folder.close(true);
			}
			if (store != null) {
				store.close();
			}
		}

	}

}
