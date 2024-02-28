package queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import AllUsers.ConfirmationStatus;
import AllUsers.Customer;
import AllUsers.User;
import AllUsers.Users;
import Orders.Branch;
import Report.customer;
import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Response;
import customerService.Complaint;

public class UserQuery {

	public static FullMessage GetUserStatus(FullMessage messageFromClient) throws SQLException {
		FullMessage returnMessageToClient = messageFromClient;
		ConfirmationStatus status = null;
		User user = (User) messageFromClient.getObject();
		String id = user.getID();
		String condition = "ID='" + id + "'";
		ResultSet rs = mainQuery.getTuple("customer", condition);
		try {
			// If the row doesn't exist in login Table
			if (rs.next()) {
				status = ConfirmationStatus.valueOf(rs.getString(8));
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(status);
		return returnMessageToClient;
	}

	public static FullMessage GetCustomerFromDB(FullMessage messageFromClient) throws SQLException {
		FullMessage returnMessageToClient = messageFromClient;
		List<customer> CustomerList = new ArrayList<customer>();

		ResultSet rs = mainQuery.SelectAllFromDB("customer");
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_CUSTOMER);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				customer CustomerFromDB = convertTocustomer(rs);
				if (CustomerFromDB != null) {
					CustomerList.add(CustomerFromDB);
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(CustomerList);
		returnMessageToClient.setResponse(Response.CUSTOMER_FOUND);
		return returnMessageToClient;

	}

	public static FullMessage UpdateCustomerStatus(FullMessage messageFromClient) throws SQLException {
		FullMessage returnedMessage = messageFromClient;
		String condition1;
		customer MsgFromClient = ((customer) returnedMessage.getObject());
		String id = MsgFromClient.getCustomerID();
		if (MsgFromClient.getStatus().equals(ConfirmationStatus.FROZEN)) {
			condition1 = "ConfirmationStatus='FROZEN'";
		} else {
			condition1 = "ConfirmationStatus='CONFIRMED'";
		}
		String condition2 = "ID=" + id;
		mainQuery.updateTuple("customer", condition1, condition2);
		returnedMessage.setResponse(Response.EDIT_SUCCEED);
		return returnedMessage;
	}

	public static FullMessage GetUsersFromDB(FullMessage messageFromClient) throws SQLException {
		String UserType = (String) messageFromClient.getObject();
		FullMessage returnMessageToClient = messageFromClient;
		List<Users> UsersList = new ArrayList<Users>();

		ResultSet rs = mainQuery.SelectAllFromDB(UserType);
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_CUSTOMER);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				Users UserFromDB = convertToUsers(rs);
				if (UserFromDB != null) {
					UsersList.add(UserFromDB);
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(UsersList);
		returnMessageToClient.setResponse(Response.USER_FOUND);
		return returnMessageToClient;

	}

	public static FullMessage ChangeUsersFromDB(FullMessage messageFromClient) throws SQLException {
		FullMessage returnedMessage = messageFromClient;
		Users user = null;
		Users MsgFromClient = (Users) returnedMessage.getObject();
		String condition1 = "UserType='" + MsgFromClient.getUserType() + "'";
		String condition2 = "ID=" + MsgFromClient.getUserID();
		String TableName = MsgFromClient.Email;

		FullMessage NewMsg = manageTheUsers(TableName, condition1, condition2, returnedMessage);// Update and get the
																								// user
		user = (Users) NewMsg.getObject();
		String NewType = MsgFromClient.getUserType();
		// insert
		try {
			InserToNewTable(NewType, user);
		} catch (SQLException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mainQuery.DeleteRowFromDB(condition2, TableName);
		condition2 = "UserID=" + MsgFromClient.getUserID();
		mainQuery.updateTuple("login", condition1, condition2);// update to new type
		messageFromClient.setResponse(Response.USER_UPDATED);
		return messageFromClient;

	}

	public static void InserToNewTable(String NewTable, Users user) throws SQLException, ParseException {

		switch (NewTable) {
		case "worker":
			mainQuery.insertOneRowIntoWorkerTable(user.getUserID(), user.getFirstName(), user.getLastName(),
					user.getEmail(), user.getPhoneNumber(), user.getUserType(), user.isLogInStatus(),
					user.getConfirmationstatus(), "0",Branch.BeautifulBlossoms);

			break;
		case "servicespecialist":
			mainQuery.insertOneRowIntoCeoZliAndServiceSpAndCustomerServiceTable("servicespecialist", user.getUserID(),
					user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getUserType(),
					user.isLogInStatus(), user.getConfirmationstatus());
			break;
		case "deliveryperson":
			mainQuery.insertOneRowIntoBranchManagerAndDeliveryTable("deliveryperson", user.getUserID(),
					user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), user.getUserType(),
					user.isLogInStatus(), user.getConfirmationstatus(), Branch.BeautifulBlossoms);
			break;
		case "customerserviceworker":
			mainQuery.insertOneRowIntoCeoZliAndServiceSpAndCustomerServiceTable("customerserviceworker",
					user.getUserID(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(),
					user.getUserType(), user.isLogInStatus(), user.getConfirmationstatus());
			break;
		case "customer":
			mainQuery.insertOneRowIntoCustomerTable(user.getUserID(), user.getFirstName(), user.getLastName(),
					user.getEmail(), user.getPhoneNumber(), user.getUserType(), user.isLogInStatus(),
					user.getConfirmationstatus(), "401", (double) 1000);
			break;
		}

	}

	public static FullMessage manageTheUsers(String TableName, String condition1, String condition2,
			FullMessage messageFromClient) throws SQLException {
		Users user = null;
		mainQuery.updateTuple(TableName, condition1, condition2);// update to new type

		ResultSet rs = mainQuery.getTuple(TableName, condition2);// get the user from the table

		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				messageFromClient.setResponse(Response.NO_REPORT);
				messageFromClient.setObject(null);
				return messageFromClient;
			}

			while (rs.next()) {
				user = convertToUser(rs);

			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		messageFromClient.setObject(user);
		return messageFromClient;
	}

	private static Users convertToUser(ResultSet rs) {
		Users user = null;
		try {
			String id = rs.getString(1);

			String firstname = rs.getString(2);
			String lastname = rs.getString(3);
			String email = rs.getString(4);
			String PhoneNumber = rs.getString(5);
			String Type = rs.getString(6);
			String isLogin = rs.getString(7);
			ConfirmationStatus confirmation = ConfirmationStatus.valueOf(rs.getString(8));
			return new Users(id, firstname, lastname, email, PhoneNumber, Type, isLogin, confirmation);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static Users convertToUsers(ResultSet rs) {
		try {
			String id = rs.getString(1);
			String firstname = rs.getString(2);
			String lastname = rs.getString(3);
			String email = rs.getString(4);
			String Type = rs.getString(6);
			return new Users(id, firstname, lastname, email, Type);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static FullMessage GetUserBalanceAndCreditCard(FullMessage messageFromClient) throws SQLException {
		FullMessage returnMessageToClient = messageFromClient;
		User user = (User) messageFromClient.getObject();
		Customer customerFromDB = null;
		String id = user.getID();
		String condition = "ID='" + id + "'";
		ResultSet rs = mainQuery.getTuple("customer", condition);
		try {
			// If the row doesn't exist in login Table
			while (rs.next()) {
				customerFromDB = convertToCustomer(rs);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(customerFromDB);
		returnMessageToClient.setResponse(Response.CUSTOMER_FOUND);
		return returnMessageToClient;
	}

	private static customer convertTocustomer(ResultSet rs) {
		try {
			String id = rs.getString(1);
			String firstname = rs.getString(2);
			String lastname = rs.getString(3);
			String email = rs.getString(4);
			ConfirmationStatus confirmation = ConfirmationStatus.valueOf(rs.getString(8));
			return new customer(id, firstname, lastname, email, confirmation);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static Customer convertToCustomer(ResultSet rs) {
		try {
			String id = rs.getString(1);
			String firstname = rs.getString(2);
			String lastname = rs.getString(3);
			String email = rs.getString(4);
			String phoneNumber = rs.getString(5);
			String type = rs.getString(6);
			boolean LogStatus = rs.getBoolean(7);
			ConfirmationStatus confirmation = ConfirmationStatus.valueOf(rs.getString(8));
			String creditCard = rs.getString(9);
			double balance = rs.getDouble(10);

			return new Customer(id, firstname, lastname, email, phoneNumber, type, LogStatus, confirmation, creditCard,
					balance);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static FullMessage UpdateCustomerBalance(FullMessage message) {
		FullMessage returnedMessage = message;
		String[] parsedMsgFromClient = ((String) returnedMessage.getObject()).split(" ");
		double newbalance = Double.parseDouble(parsedMsgFromClient[0]);
		String id = parsedMsgFromClient[1];
		String condition1 = "Balance" + "=" + newbalance; /// CHANGEEE EBRAHEM
		String condition2 = "ID=" + id;
		mainQuery.updateTuple("customer", condition1, condition2);
		// message.setObject(null); //check meaning
		returnedMessage.setResponse(Response.NO_ORDER_FOUND);

		return returnedMessage;

	}

	public static FullMessage restoreTheOldBalanceBeforePurchasing(FullMessage messageFromClient) throws SQLException {
		ArrayList<String> list = (ArrayList<String>) messageFromClient.getObject();
		FullMessage returnMessageToClient = messageFromClient;
		String OrderNumber = list.get(0);
		String CustomerId = list.get(1);
		double Balance = 0.0;

		ResultSet rs = mainQuery.getTuple("customer", "ID='" + CustomerId + "'");
		if (rs.next()) {
			Balance = rs.getDouble(10);
		}
		rs.close();

		double TotalPrice = 0.0;
		double BalanceBeforePurchasing = 0.0;
		ResultSet rs1 = mainQuery.getTuple("orders", "OrderNumber='" + OrderNumber + "'");
		if (rs1.next()) {
			TotalPrice = rs1.getDouble(9);
		}
		rs1.close();
		BalanceBeforePurchasing = TotalPrice + Balance;
		mainQuery.updateTuple("customer", "Balance='" + BalanceBeforePurchasing + "'", "ID='" + CustomerId + "'");
		returnMessageToClient.setResponse(Response.UPDATE_UNAPPROVED_ORDER_BALANCE_SUCCEEDED);
		return returnMessageToClient;

	}

	public static FullMessage restoreOldBalanceAfterComplaint(FullMessage messageFromClient) throws SQLException {
		FullMessage returnMessageToClient = messageFromClient;
		Complaint comp = (Complaint) messageFromClient.getObject();

		String customerId = comp.getCustomerId();
		int complaintNumber = comp.getComplaintNum();
		int ordernum = comp.getOrderNumber();
		Timestamp time = comp.getComplaintDate();
		Double balance = 0.0;
		Double totalprice = 0.0;
		Double priceAfterRefund = 0.0;
		ResultSet rs = mainQuery.getTuple("orders", "OrderNumber='" + ordernum + "'");
		if (rs.next())
			totalprice = rs.getDouble(9);
		else
			return returnMessageToClient;

		rs.close();
		
		ResultSet rs1 = mainQuery.getTuple("customer", "ID='" + customerId + "'");
		if (rs1.next()) 
			balance = rs1.getDouble(10);
		else
			return returnMessageToClient;
                             
		rs1.close();

		priceAfterRefund = balance + totalprice;
		mainQuery.updateTuple("customer", "Balance='" + priceAfterRefund + "'", "ID='" + customerId + "'");
		mainQuery.DeleteRowFromDB("Date ='" + time + "'", "complaint");
		returnMessageToClient.setResponse(Response.UPDATE_BALANCE_AFTER_COMPLAINT_SUCCEEDED);
		returnMessageToClient.setObject(totalprice);
		return returnMessageToClient;

	}

	public static FullMessage GetBranchFromWorker(FullMessage message) throws SQLException {

		FullMessage returnMessageToClient = message;
		String id = ((String) message.getObject());
		String condition = "ID='" + id + "'";
		ResultSet rs = mainQuery.getTuple("worker", condition);
		Branch branch = null;

		if (rs.next()) {

			branch = Branch.valueOf(rs.getString(10));
		}
		returnMessageToClient.setObject(branch);
		return returnMessageToClient;

	}
	
	public static FullMessage GetUserDetails(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		User user = (User) messageFromClient.getObject();
		Customer customerFromDB = null;
		String id = user.getID();
		String condition = "ID='" + id + "'";
		ResultSet rs = mainQuery.getTuple("customer", condition);
		try {
			// If the row doesn't exist in login Table
			while (rs.next()) {
				customerFromDB = convertToCustomer(rs);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(customerFromDB);
		returnMessageToClient.setResponse(Response.CUSTOMER_FOUND);
		return returnMessageToClient;
	}
	public static void UpdateSaleForWorker(FullMessage message) {

		String[] parsedMsgFromClient = ((String) message.getObject()).split(" ");
		String Id = parsedMsgFromClient[0];
		String percent = parsedMsgFromClient[1];
		String valueToDB = "1" + "," + percent;
		mainQuery.updateTuple("worker", "Sales='" + valueToDB + "'", Id);

	}
	
	/**
	 * This method returns new Customer so we can get his email
	 * @param messageFromClient
	 * @return Customer
	 * @throws SQLException
	 */
	public static FullMessage GetUserEmail(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		String id = (String) messageFromClient.getObject();
		Customer customerFromDB = null;
		String condition = "ID='" + id + "'";
		ResultSet rs = mainQuery.getTuple("customer", condition);
		try {
			// If the row doesn't exist in login Table
			while (rs.next()) {
				customerFromDB = convertToCustomer(rs);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(customerFromDB);
		returnMessageToClient.setResponse(Response.CUSTOMER_FOUND);
		return returnMessageToClient;
	}
}
