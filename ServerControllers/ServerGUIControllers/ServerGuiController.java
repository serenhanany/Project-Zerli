package ServerGUIControllers;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import AllUsers.ClientStatus;
import ZliServer.ZliServerUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import jdbc.mysqlConnection;
import queries.ImportQuery;
import queries.mainQuery;

/**
 * This class is for the Server Controller. It Controls All buttons that clicked
 * in GUI.
 */

public class ServerGuiController implements Initializable {

	private int IndexOflClients = 0;

	/**
	 * DBUser variable contains the Database User.
	 */
	final public static String DBUser = "root";

	/**
	 * DBPassword variable contains Database Password.
	 */

	final public static String DBPassword = "Ebrahemenb123";

	/**
	 * DBpath variable contains Database Path.
	 */
	final public static String DBPath = "jdbc:mysql://localhost/zli_db?useSSL=false&allowPubliceKeyRetrieval=true&serverTimezone=Asia/Jerusalem";
	/**
	 * ExternalDBpath variable contains Database Path.
	 */
	final public static String ExternalDBPath = "jdbc:mysql://localhost/allusers?serverTimezone=IST";

	/**
	 * DBPort variable contains Database Port that we connect on.
	 */
	final public static String DBPort = "5555";

	/**
	 * mySqlConnection instance to save the SQl connection of the server.
	 */
	public static mysqlConnection connectionToExternalDB;

	/**
	 * Loader variable type of FXMLLoader.
	 */
	public static FXMLLoader loader;

	/**
	 * Button for Connect Button.
	 */
	@FXML
	private Button BtnConnect;

	/**
	 * Button for Disconnect Button.
	 */
	@FXML
	private Button BtnDisconnect;
	/**
	 * Button for import Data From ExternalDB.
	 */
	@FXML
	private Button ImportButton;
	/**
	 * TextField for the Console Area.
	 */
	@FXML
	private TextArea TextAreaConsole;

	/**
	 * TextField for the IP.
	 */
	@FXML
	private TextField TextFieldIP;

	/**
	 * TextField for the Port.
	 */
	@FXML
	private TextField TextfieldPort;

	/**
	 * TextField for Database Path.
	 */
	@FXML
	private TextField TextFieldDBPath;

	/**
	 * TextField for the DataBase User.
	 */
	@FXML
	private TextField TextFieldDBUser;

	/**
	 * TextField for the Password Field.
	 */
	@FXML
	private TextField TextFieldPass;

	/**
	 * Table that shows the Connected and Disconnected Clients.
	 */
	@FXML
	public TableView<ClientStatus> Table;

	/**
	 * Table Column for Host.
	 */
	@FXML
	private TableColumn<ClientStatus, String> HostCol;

	/**
	 * Table Column for IP.
	 */
	@FXML
	private TableColumn<ClientStatus, String> IPCol;

	/**
	 * Table Column for Status of Client (Connected/Disconnected).
	 */
	@FXML
	private TableColumn<ClientStatus, String> StatusCol;

	/**
	 * Observable List to save Clients from ClientStatus Type.
	 */
	public ObservableList<ClientStatus> list = FXCollections.observableArrayList(new ClientStatus(null, null, null));

	/**
	 * This method starts the FXML file and enables mouse Drag.
	 * 
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) throws Exception {
		loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/ServerFXMLFiles/ServerForm.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		scene.setOnMousePressed(pressEvent -> {
			scene.setOnMouseDragged(dragEvent -> {
				primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
				primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
			});
		});
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * This method loads the info into textFields of the GUI.
	 */
	public void loadInfoIntoServerGui() {

		this.TextfieldPort.setText(String.valueOf(DBPort));
		try {
			this.TextFieldIP.setText(InetAddress.getLocalHost().getHostAddress());
			this.TextFieldIP.setDisable(true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.TextFieldDBPath.setText(DBPath);
		this.TextFieldDBUser.setText(DBUser);
		this.TextFieldPass.setText(DBPassword);
	}

	/**
	 * This method Terminates server when Exit Button is clicked.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void ExitButton(ActionEvent event) throws Exception {

		System.exit(0);

	}

	/**
	 * This method Runs the server and connects to Database.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	/* When Clicked will Connect the server */
	public void ConnectBtn(ActionEvent event) throws Exception {

		DisplayMessageToTextAreaConsole(ZliServerUI.runServer(DBPort, DBPath, DBUser,
				TextFieldPass.getText())); /* connect to DB and start listening */
		DisplayMessageToTextAreaConsole("Driver definition Succeeded");
		DisplayMessageToTextAreaConsole("SQL Connection Succeeded");
		SetTextFieldsVisibility(true);

	}

	/**
	 * After clicking on import button , we get all the data from the external db
	 * After making a new connection to the external DB.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void ImportButton(ActionEvent event) throws Exception {
		if (BtnConnect.isDisable() == false) {
			DisplayMessageToTextAreaConsole("You Should Connect First");
		} else {
			connectionToExternalDB = new mysqlConnection();
			if (mysqlConnection.connectToDB(ExternalDBPath, DBUser, TextFieldPass.getText())) {
				mainQuery.setConnectionFromServerToExternalDB(connectionToExternalDB.getConnection());
				ImportQuery.getFromExternalDB();

			}

		}
	}

	/**
	 * This method determines the visibility of TextFields and Buttons.
	 * 
	 * @param Visible
	 */
	public void SetTextFieldsVisibility(boolean Visible) {

		this.BtnConnect.setDisable(Visible);
		this.BtnDisconnect.setDisable(!Visible);

		this.BtnDisconnect.setDisable(!Visible);
		this.TextfieldPort.setDisable(Visible);
		this.TextFieldDBPath.setDisable(Visible);
		this.TextFieldDBPath.setDisable(Visible);
		this.TextFieldPass.setDisable(Visible);
		this.TextFieldDBUser.setDisable(Visible);

	}

	/**
	 * This method Disconnects the server when Disconnect button is clicked.
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void DisconnectBtn(ActionEvent event) throws Exception {

		ZliServerUI.DisconnectServer();
		this.IndexOflClients = 0;
		DisplayMessageToTextAreaConsole("The Server is Disconnected");
		SetTextFieldsVisibility(false);
	}

	/**
	 * This method Run the specified Runnable on the JavaFX Application Thread at
	 * some. unspecified time in the future and print to user the wanted message
	 * 
	 * @param msg
	 */
	public void DisplayMessageToTextAreaConsole(String msg) {
		Platform.runLater(() -> {
			String Message = TextAreaConsole.getText();
			this.TextAreaConsole.setText(Message + "\n" + msg);
		});
	}

	/**
	 * This method returns FXML Loader.
	 * 
	 * @return Loader
	 */
	public static FXMLLoader getLoader() {
		return loader;
	}

	/**
	 * This method initialize the TableView and calls for the LoadInfoIntoServerGui
	 * method.
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		HostCol.setCellValueFactory(new PropertyValueFactory<ClientStatus, String>("Host"));
		IPCol.setCellValueFactory(new PropertyValueFactory<ClientStatus, String>("IP"));
		StatusCol.setCellValueFactory(new PropertyValueFactory<ClientStatus, String>("Status"));
		loadInfoIntoServerGui();
	}

	/**
	 * This method adds new Clients to the List.
	 */
	public void AddNewClientStatus() {

		list.add(new ClientStatus(null, null, null));
	}

	/**
	 * This function for adding a new Client 
	 * and Display it in the table
	 * and change the status to connected
	 * 
	 * @param IP
	 * @param Host
	 * @param Status
	 */
	public void AddNewClient(String IP, String Host, String Status) {

		AddNewClientStatus();
		list.get(IndexOflClients).setHost(Host);
		list.get(IndexOflClients).setIP(IP);
		list.get(IndexOflClients).setStatus(Status);

		Table.setItems(list);
		Table.refresh();
		IndexOflClients++;
	}

	/**
	 * This function for seting the status of the current 
	 * client in the table connected 
	 * 
	 * @param CurrentIPAddress
	 * @return
	 */
	public boolean CheckIfClientIsInListAndSetStatusToConnect(String CurrentIPAddress) {

		for (int i = 0; i < list.size(); i++) {

			ClientStatus ClientInList = list.get(i);

			if (CurrentIPAddress.equals(ClientInList.getIP())) {
				list.get(i).setStatus("Connected");
				Table.refresh();
				return false;
			}

		}

		return true;

	}

	/**
	 * This function for seting the status of the current 
	 * client in the table  disconnected 
	 * @param CurrentClientIP
	 * @param Status
	 */
	public void SetClientStatusToDisconnect(String CurrentClientIP, String Status) {

		for (int i = 0; i < list.size(); i++) {

			ClientStatus ClientInList = list.get(i);

			if (CurrentClientIP.equals(ClientInList.getIP())) {
				list.get(i).setStatus("Disconnected");
				Table.refresh();

			}

		}

	}

}
