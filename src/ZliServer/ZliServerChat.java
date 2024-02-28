// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package ZliServer;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import ReadMessage.ReadMessageFromClient;
import RequestsAndResponses.FullMessage;
import ServerGUIControllers.ServerGuiController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import queries.mainQuery;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 */

public class ZliServerChat extends AbstractServer {
	private ConnectionToClient client;
	private ServerGuiController HandleClientStatus = ServerGuiController.getLoader().getController();
	private Connection con;

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port: The port number to connect on.
	 */
	public ZliServerChat(int port) {
		super(port);
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg: The message received from the client.
	 * 
	 * @param client: The connection from which the message originated.
	 */

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof FullMessage) {
			FullMessage recivedMessageFromClient = (FullMessage) msg;
			try {
				sentToSpecificClient(client, ReadMessageFromClient.ReadMessage(recivedMessageFromClient, client));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		ServerGuiController serverGuiController = ServerGuiController.getLoader().getController();
		con=ZliServerUI.connectionToDB.getConnection();
		serverGuiController
				.DisplayMessageToTextAreaConsole("Server listening for connections on port " + getPort() + "\n");
           mainQuery.setConnectionFromServerToDB(con);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	@SuppressWarnings("deprecation")
	protected void serverStopped() {
		HandleClientStatus.list.clear();
		HandleClientStatus.Table.refresh();
		if (client != null)
			client.stop();

		ServerGuiController serverGuiController = ServerGuiController.getLoader().getController();
		serverGuiController.DisplayMessageToTextAreaConsole("Server has stopped listening for connections." + "\n");
	}

	/** Send Message to A specific Client not all */
	public void sentToSpecificClient(ConnectionToClient client, Object msg) {
		try {
			client.sendToClient(msg);
		} catch (Exception ex) {
		}
	}

}
//End of ZliServerChat class
