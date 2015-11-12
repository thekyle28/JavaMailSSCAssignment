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
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.mail.Multipart;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder;

import emailGui.EmailGUI;

/**
 * This class is used to retrieve the emails from an existing inbox. It then creates a GUI using the EmailGUI class.
 * @author Kyle
 */
public class IMAPClient {

	public static void main(String args[]) throws MessagingException,
			IOException {
		new IMAPClient();
	}

	private static IMAPFolder folder;
	private static Store store;
	private static EmailGUI gui;
	private static String multipartContent;
	final static String username = "kylearat@gmail.com";
	static String password = "";

	
	/**
	 * Searches the folders for emails that contain the search text that the user inputs.
	 * @param search the text that the user input to search through the email inbox.
	 */
	public IMAPClient(String search) {
		try {
			getMessages(search.toLowerCase());
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialises the email client by grabbing all of the data required from the server and 
	 * creating the email GUI with that data.
	 * @throws IOException
	 * @throws MessagingException
	 */
	public IMAPClient() throws IOException, MessagingException {
		// TODO Auto-generated method stub
		IMAPClient.folder = null;
		IMAPClient.store = null;

		// Step 1.1: set mail user properties using Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		// Get user password using JPasswordField
		JPasswordField pwd = new JPasswordField(10);
		
		  int action = JOptionPane.showConfirmDialog(null,
		  pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION); if(action < 0) {
		  JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected");
		  System.exit(0); } else password = new String(pwd.getPassword());
		 

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

			ArrayList<String> subjects = new ArrayList<String>();
			ArrayList<Object> contents = new ArrayList<Object>();
			IMAPClient.multipartContent = new String();

			Message messages[] = folder.getMessages();

			getMessageContent(subjects, contents, messages);
			// creates a new gui for the emails to be viewed in.
			IMAPClient.gui = new EmailGUI(subjects, contents);

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

	//this constructor is used to update flags
	/**
	 * This constructor is called when flags of the email messages need to be altered.
	 * @param tag The new flag name. If the tag is UNREAD then it will set the flag of the message to be UNREAD.
	 * @param flagKeyword The keyword that is searched for when setting a new flag.
	 * @param messageIndex The index of the message in the JList in the email GUI.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public IMAPClient(String tag, String flagKeyword, int messageIndex) throws MessagingException, IOException {
		if(tag == "UNREAD")
		setUnreadFlag(messageIndex);
		else
			createFlag(tag, flagKeyword, messageIndex );
	}

	/**
	 * Sets an email's flag so that it is UNREAD
	 * @param messageIndex The index of the message in the JList in the email GUI.
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void setUnreadFlag(int messageIndex) throws MessagingException, IOException {
		if (!folder.isOpen())
			folder.open(Folder.READ_WRITE);
		folder.getMessage(messageIndex+1).setFlag(Flags.Flag.SEEN, false);
		if (folder != null && folder.isOpen()) {
			folder.close(true);
		}
		updateMessageFlags("");
	}

	/**
	 * Retrieves the message content and subject line of all the messages 
	 * in a folder including the message flags
	 * @param subjects The arrayList to store the subjects in, normally an empty 
	 * @param contents
	 * @param messages
	 * @throws MessagingException
	 * @throws IOException
	 */
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

	/**
	 * Extracts the plain text from a multipart message.
	 * @param multipartContent 
	 * @param message
	 * @return 
	 * @throws IOException
	 * @throws MessagingException
	 */
	private static String extractMultipartMessage(String multipartContent,
			Message message) throws IOException, MessagingException {
		Multipart multipart = (Multipart) message.getContent();

		for (int x = 0; x < multipart.getCount(); x++) {
			BodyPart bodyPart = multipart.getBodyPart(x);
			// If the part is a plan text message, then print it
			// out.
			if (bodyPart.getContentType().contains("TEXT/PLAIN")) {
				multipartContent += bodyPart.getContent().toString();
			} else {
				//else print nothing
				multipartContent += "";
			}
		}
		return multipartContent;
	}

	/**
	 * Searches for messages that have the search text in the body or 
	 * subject line of the email.
	 * @param searchText
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void getMessages(String searchText)
			throws MessagingException, IOException {

		try {

			ArrayList<String> subjects = new ArrayList<String>();
			ArrayList<Object> contents = new ArrayList<Object>();

			if (!folder.isOpen())
				folder.open(Folder.READ_WRITE);

			// create a search term using the search text sent earlier
			SearchTerm search = createSearchTerm(searchText);
			Message messages[] = folder.search(search);

			getMessageContent(subjects, contents, messages);
			//Updates the GUI 
			EmailGUI.updateInbox(subjects, contents);

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

	/** Creates the search term to be used in the folder search method.
	 * @param searchText The text that the user 
	 * @return returns the search term
	 */
	private static SearchTerm createSearchTerm(String searchText) {
		SearchTerm search = new SearchTerm() {

			@Override
			public boolean match(Message message) {

				try {
					if (message.getContentType().contains("TEXT/PLAIN")) {
						// check if the search text is in the message,
						// subject,
						// or in the flags
						try {
							if (message.getContent().toString().toLowerCase()
									.contains(searchText)
									|| message.getSubject().toString().toLowerCase()
											.contains(searchText)
									|| message.getFlags().toString().toLowerCase().contains(
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

						if (multipartContent.toString().toLowerCase().contains(searchText)
								|| message.getSubject().toString().toLowerCase().contains(searchText)) {
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
		return search;
	}

	/**
	 * Updates the flags so that they are represented in the email GUI.
	 * @param tag The name of the new flag to be created.
	 * @throws IOException
	 * @throws MessagingException
	 */
	private static void updateMessageFlags(String tag) throws IOException, MessagingException {
		

		// Step 1.1: set mail user properties using Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

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

			ArrayList<String> subjects = new ArrayList<String>();
			ArrayList<Object> contents = new ArrayList<Object>();
			multipartContent = new String();

			Message messages[] = folder.getMessages();

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
				if (mes_flag.contains(tag)) {
				tags += tag.toUpperCase() + " ";
				}

				subjects.add(tags + message.getSubject() + "\n");
			
			}
			// creates a new gui for the emails to be viewed in.
			EmailGUI.updateInbox(subjects, contents);

			

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
	
	/**
	 * This creates a custom flag.
	 * @param tag The name of the new Flag you wish to create.
	 * @param flagKeyword The keyword to search for in emails when setting the tags.
	 * @param messageIndex The index of the message in the folder.
	 * @throws MessagingException
	 * @throws IOException
	 */
	private static void createFlag(String tag, String flagKeyword, int messageIndex) throws MessagingException, IOException {
		if (!folder.isOpen())
			folder.open(Folder.READ_WRITE);
		
		SearchTerm search = createSearchTerm(flagKeyword);
		Flags customTag = new Flags(tag);
		Message messages[] = folder.search(search);
		folder.setFlags(messages, customTag, true);	
		
		updateMessageFlags(tag);
			
	}
}
