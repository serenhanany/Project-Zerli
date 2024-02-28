package queries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import AllUsers.ConfirmationStatus;
import Orders.Branch;
import Orders.DominantColor;
import Orders.FlowerColor;
import Orders.ItemCategory;
import Orders.ItemType;
import Orders.OrderStatus;
import Orders.TypeOfSupply;
import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Response;
import Survey.SurveyAnswers;

/**
 * Class Description: This class is responsible for all the Insert,
 * Update,Remove... Queries This class has Direct connection to the database
 * 
 * @author Obied haddad
 * @author Seren Hananny
 * @author Maisalon Safory
 * @author Ebrahem Enbtawe
 * @author Mario Rohannah
 * @author Shorok Heeb
 *
 */
public class mainQuery {
	/**
	 * The var of the connection to the main db
	 */
	private static Connection con;
	/**
	 * The var of the connection to external db
	 */
	private static Connection externaldbCon;

	/**
	 * Setter of the connection of zli db
	 */
	public static void setConnectionFromServerToDB(Connection connection) {
		con = connection;
	}

	/**
	 * Setter of the connection of external db
	 */
	public static void setConnectionFromServerToExternalDB(Connection connection) {
		externaldbCon = connection;
	}

	/**
	 * This method will be called once to import the data of users management from
	 * the allusers DB.
	 * 
	 * @return rs
	 */
	public static ResultSet getExternalDBData() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM allusers.usermanagement";
		try {
			pstmt = externaldbCon.prepareStatement(query);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * Method for inserting Survey into DB
	 * 
	 * @param answerANDid
	 * @throws ParseException
	 */
	public static void InsertOneRowIntosurveyAnswersTable(SurveyAnswers answerANDid) throws ParseException {

		String query = "INSERT INTO surveyanswers (SurveyID,CustomerID, QuestionNumber,QuestionAnswer) VALUES ("
				+ "?, ?, ?,?)";

		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, answerANDid.getSurveyID());
			pstmt.setString(2, answerANDid.getCustomerID());
			pstmt.setString(3, answerANDid.getQuestionNumber());
			pstmt.setString(4, answerANDid.getQuestionAnswer());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method For Inserting Row In login table
	 * 
	 * @param userName
	 * @param Password
	 * @param userId
	 * @param userType
	 */
	public static void insertOneRowIntoLoginTable(String userName, String Password, String userId, String userType) {
		String query = "INSERT INTO zli_db.login ( Username, Password, UserID, UserType) VALUES( '" + userName + "' , '"
				+ Password + "' , '" + userId + "' , '" + userType + "' )";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	/**
	 * Method For Inserting Row In Customer table
	 * 
	 * @param userName
	 * @param Password
	 * @param userId
	 * @param userType
	 */
	public static void insertOneRowIntoCustomerTable(String id, String FirstName, String LastName, String Email,
			String PhoneNumber, String UserType, String LogInStatus, ConfirmationStatus Status, String CreditCard,
			Double Balance) {
		String query = "INSERT INTO zli_db.customer (ID,FirstName,LastName,Email,PhoneNumber,UserType,LogInStatus,ConfirmationStatus,CreditCard,Balance) VALUES( '"
				+ id + "' , '" + FirstName + "' , '" + LastName + "' , '" + Email + "' , '" + PhoneNumber + "' ,'"
				+ UserType + "' , '" + LogInStatus + "' ,  '" + Status + "' , '" + CreditCard + "' , '" + Balance
				+ "')";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	/**
	 * Method For Inserting Row In BranchManager table
	 * 
	 * @param userName
	 * @param Password
	 * @param userId
	 * @param userType
	 */
	public static void insertOneRowIntoBranchManagerAndDeliveryTable(String tabelname, String id, String FirstName,
			String LastName, String Email, String PhoneNumber, String UserType, String LogInStatus,
			ConfirmationStatus Status, Branch branch) {
		String query = "INSERT INTO zli_db." + tabelname
				+ "(ID,FirstName,LastName,Email,PhoneNumber,UserType,LogInStatus,ConfirmationStatus,Branch) VALUES( '"
				+ id + "' , '" + FirstName + "' , '" + LastName + "' , '" + Email + "' , '" + PhoneNumber + "' ,'"
				+ UserType + "' , '" + LogInStatus + "' ,  '" + Status + "' , '" + branch + "')";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	/**
	 * Method For Inserting Row In CeoZli And ServiceSpecialist And CustomerService
	 * Table
	 * 
	 * @param userName
	 * @param Password
	 * @param userId
	 * @param userType
	 */
	public static void insertOneRowIntoCeoZliAndServiceSpAndCustomerServiceTable(String tabelname, String id,
			String FirstName, String LastName, String Email, String PhoneNumber, String UserType, String LogInStatus,
			ConfirmationStatus Status) {
		String query = "INSERT INTO zli_db." + tabelname
				+ "(ID,FirstName,LastName,Email,PhoneNumber,UserType,LogInStatus,ConfirmationStatus) VALUES( '" + id
				+ "' , '" + FirstName + "' , '" + LastName + "' , '" + Email + "' , '" + PhoneNumber + "' ,'" + UserType
				+ "' , '" + LogInStatus + "' ,  '" + Status + "')";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	/**
	 * Method For Inserting Row In Worker Table
	 * 
	 * @param userName
	 * @param Password
	 * @param userId
	 * @param userType
	 */
	public static void insertOneRowIntoWorkerTable(String id, String FirstName, String LastName, String Email,
			String PhoneNumber, String UserType, String LogInStatus, ConfirmationStatus Status, String Sales,
			Branch branch) {
		String query = "INSERT INTO zli_db.worker (ID,FirstName,LastName,Email,PhoneNumber,UserType,LogInStatus,ConfirmationStatus,Sales,Branch) VALUES( '"
				+ id + "' , '" + FirstName + "' , '" + LastName + "' , '" + Email + "' , '" + PhoneNumber + "' ,'"
				+ UserType + "' , '" + LogInStatus + "' ,  '" + Status + "' , '" + Sales + "','" + branch + "')";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This function Selects a tuple from database with certain condition and
	 * returns it with ResultSet
	 * 
	 * @param tableName
	 * @param condition
	 * @return rs
	 */
	public static ResultSet getTuple(String tableName, String condition) {

		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = con.prepareStatement("SELECT * FROM zli_db." + tableName + " WHERE " + condition);
			rs = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * this function Selects tuple according to MAX and MIN price
	 * 
	 * @param tableName
	 * @param minPrice
	 * @param maxPrice
	 * @return
	 */
	public static ResultSet getTupleAccordingToPrice(String tableName, int minPrice, int maxPrice) {

		PreparedStatement statement = null;
		ResultSet rs = null;
		try {

			statement = con.prepareStatement("SELECT * FROM zli_db." + tableName + "  WHERE " + "Price between'"
					+ minPrice + "' AND '" + maxPrice + "'");

			rs = statement.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * This function selects Column from table in database with certain condition
	 * 
	 * @param tableName
	 * @param columnName
	 * @param condition
	 * @return
	 */
	public static ResultSet getColumnFromTableInDB(String tableName, String columnName, String condition) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT " + columnName + " FROM zli_db." + tableName + "WHERE" + condition;
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * This function updates table column in database with certain condition
	 * 
	 * @param tableName
	 * @param columnSet
	 * @param condition
	 */
	public static void updateTuple(String tableName, String columnSet, String condition) {

		PreparedStatement statement = null;
		try {
			String query = "UPDATE zli_db." + tableName + " SET " + columnSet + " WHERE " + "(" + condition + ")";
			statement = con.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This function updates column without condition
	 * 
	 * @param tableName
	 * @param columnSet
	 */
	public static void updateTupleWithNoCondition(String tableName, String columnSet) {

		PreparedStatement statement = null;
		try {
			String query = "UPDATE zli_db." + tableName + " SET " + columnSet + "";
			statement = con.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This function Selects all from certain table in database
	 * 
	 * @param tableName
	 * @return
	 */
	public static ResultSet SelectAllFromDB(String tableName) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM zli_db." + tableName + "";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}

	/**
	 * this function Inserts PDF into database
	 * 
	 * @param path
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public static void InsertPDF(String pathAndID) throws SQLException, FileNotFoundException {
		String[] parsedMsg = pathAndID.split(" ");
		String query = "INSERT INTO zli_db.summarizedpdf (surveyID) VALUES( '" + parsedMsg[1] + "')";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		}

		String updateSQL = "UPDATE summarizedpdf " + "SET File = ? " + "WHERE surveyID=?";

		PreparedStatement pstmt1 = con.prepareStatement(updateSQL);

		File file = new File(parsedMsg[0]);
		FileInputStream input = new FileInputStream(file);

		pstmt1.setBinaryStream(1, input);
		pstmt1.setString(2, parsedMsg[1]);
		pstmt1.executeUpdate();
		SetDateForPDFTable(parsedMsg[1]);

	}

	/**
	 * This function Sets the date for when inserting PDF file into database
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public static void SetDateForPDFTable(String id) throws SQLException, FileNotFoundException {

		String updateSQL = "UPDATE summarizedpdf " + "SET Date = ? " + "WHERE surveyID=?";
		// String updateSQL = "UPDATE candidates " + "SET resume = ? " + "WHERE id=?";

		PreparedStatement pstmt = con.prepareStatement(updateSQL);

		Timestamp date = new Timestamp(System.currentTimeMillis());
		System.out.println(date);
		pstmt.setTimestamp(1, date);
		pstmt.setString(2, id);
		pstmt.executeUpdate();

	}

	/**
	 * this function Extracts the PDF from database onto Reports folder in PC
	 * 
	 * @param message
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static FullMessage ShowPDF(FullMessage message) throws SQLException, IOException {

		String selectSQL = "SELECT File FROM summarizedpdf WHERE surveyID=?";
		PreparedStatement pstmt = con.prepareStatement(selectSQL);

		pstmt.setString(1, "1000");
		ResultSet rs = pstmt.executeQuery();

		File file = new File("C:\\Reports\\PDF_from_db.pdf");

		if (rs.next()) {

			if (rs.getBlob(1) != null) {

				try (FileOutputStream output = new FileOutputStream(file)) {
					InputStream input = rs.getBinaryStream("File");
					message.setResponse(Response.PDF_FOUND);
					message.setObject(file.getAbsolutePath());
					byte[] buffer = new byte[1024];
					while (input.read(buffer) > 0) {
						output.write(buffer);
					}
				}

			} else {
				message.setResponse(Response.PDF_NOT_FOUND);
			}
			return message;

		}
		message.setResponse(Response.PDF_NOT_FOUND);
		return message;
	}

	/**
	 * This function Inserts new order Into the Order table
	 * 
	 * @param orderNumber
	 * @param customerID
	 * @param branch
	 * @param orderstatus
	 * @param orderDate
	 * @param estimatedDate
	 * @param actualDate
	 * @param supplyType
	 * @param totalPrice
	 * @param deliveryCost
	 * @param customerName
	 * @param deliveryAddress
	 * @param Item
	 * @param PhoneNumber
	 * @param greetingCard
	 * @throws ParseException
	 */
	public static void InsertOneRowIntoOrderTable(String orderNumber, String customerID, Branch branch,
			OrderStatus orderstatus, Timestamp orderDate, Timestamp estimatedDate, Timestamp actualDate,
			TypeOfSupply supplyType, double totalPrice, double deliveryCost, String customerName,
			String deliveryAddress, String Item, String PhoneNumber, String greetingCard) throws ParseException {

		// String estimatedSupplyDateAndTime =
		// DateTimeHandler.convertMySqlDateTimeFormatToString(estimatedSupplyDateTime);
		String query = "INSERT INTO orders (" + " OrderNumber," + " CustomerID," + " Branch," + " OrderStatus,"
				+ " OrderDate," + " EstimatedOrderDate," + " ActualOrderDate," + "SupplyType," + "TotalPrice,"
				+ "DeliveryCost," + "CustomerName," + "DeliveryAddress," + "AllItems," + "PhoneNumber,"
				+ "GreetingCard) VALUES (" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, orderNumber);
			pstmt.setString(2, customerID);
			pstmt.setString(3, String.valueOf(branch));
			pstmt.setString(4, String.valueOf(orderstatus));
			pstmt.setTimestamp(5, null);
			pstmt.setTimestamp(6, estimatedDate);
			pstmt.setTimestamp(7, null);
			pstmt.setString(8, String.valueOf(supplyType));
			pstmt.setString(9, String.valueOf(totalPrice));
			pstmt.setString(10, String.valueOf(deliveryCost));
			pstmt.setString(11, customerName);
			pstmt.setString(12, deliveryAddress);
			pstmt.setString(13, Item);
			pstmt.setString(14, PhoneNumber);
			pstmt.setString(15, greetingCard);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function Inserts New complaint into Complaint table
	 * 
	 * @param complaintid
	 * @param complaintnumber
	 * @param customerid
	 * @param ordernumber
	 * @param date
	 * @param branch
	 * @param text
	 * @throws ParseException
	 */
	public static void InsertOneRowIntoComplaintTable(int complaintid, int complaintnumber, String customerid,
			int ordernumber, Timestamp date, Branch branch, String text) throws ParseException {

		String query = "INSERT INTO complaint (" + " ComplaintID," + " ComplaintNum," + " CustomerID," + " OrderID,"
				+ " Date," + " Branch," + " Text) VALUES (" + "?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, complaintid);
			pstmt.setInt(2, complaintnumber);
			pstmt.setString(3, customerid);
			pstmt.setInt(4, ordernumber);
			pstmt.setTimestamp(5, date);
			pstmt.setString(6, String.valueOf(branch));
			pstmt.setString(7, text);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is to insert new Item for Catalog in database
	 * 
	 * @param itemID
	 * @param itemCategory
	 * @param color
	 * @param itemName
	 * @param price
	 * @param picturePath
	 * @param greetingCard
	 * @param type
	 * @param dominantColor
	 * @param amount
	 * @throws ParseException
	 */
	public static void InsertOneRowIntoCatalogTable(String itemID, ItemCategory itemCategory, FlowerColor color,
			String itemName, double price, String picturePath, String greetingCard, ItemType type,
			DominantColor dominantColor, int amount) throws ParseException {

		String query = "INSERT INTO catalog (" + "ID," + "Category," + " Color," + " ItemName," + " Price,"
				+ " PicturePath," + " GreetingCard," + " Type," + "DominantColor," + "Amount) VALUES ("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, 0);
			pstmt.setString(2, String.valueOf(itemCategory));
			pstmt.setString(3, String.valueOf(color));
			pstmt.setString(4, itemName);
			pstmt.setDouble(5, price);
			pstmt.setString(6, picturePath);
			pstmt.setString(7, greetingCard);
			pstmt.setString(8, String.valueOf(type));
			pstmt.setString(9, String.valueOf(dominantColor));
			pstmt.setInt(10, amount);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is to delete a row from a table in database with specific
	 * condition
	 * 
	 * @param condition
	 * @param tableName
	 */
	public static void DeleteRowFromDB(String condition, String tableName) {

		String query = "DELETE FROM zli_db." + tableName + " WHERE (" + condition + ")";
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function Selects certain Column from table in database
	 * 
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public static ResultSet getColumnFromTableInDB(String tableName, String columnName) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT " + columnName + " FROM zli_db." + tableName;
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

}
