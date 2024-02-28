package queries;

import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Request;
import RequestsAndResponses.Response;
import AllUsers.User;
import AllUsers.ConfirmationStatus;
import AllUsers.Customer;
import AllUsers.BranchManager;
import AllUsers.CEOZli;
import AllUsers.CustomerServiceWorker;
import AllUsers.DeliveryPerson;
import AllUsers.ServiceExpert;
import AllUsers.Worker;
import Orders.Branch;

import java.sql.ResultSet;
import java.sql.SQLException;

import AllUsers.Login;

/**
 * Class description:
 * Controlling the LogInQueries
 * @author Mario Rohanna
 *
 */
public class LogInQuery {

	/**
	 * This function is responsible for the LogIn of user
	 * @param message
	 * @return FullMessage
	 * @throws SQLException
	 */
	public static FullMessage serverLogIn(FullMessage message) throws SQLException {

		ResultSet rs;

		FullMessage new_message = message;
		String username = ((Login) message.getObject()).getUsername();
		String password = ((Login) message.getObject()).getPassword();
		String userId = null;
		String userType = null;
		// search for the user in the DB
		String condition = "Username='" + username + "' AND Password='" + password + "'";
		rs = mainQuery.getTuple("login", condition);
		if (!rs.isBeforeFirst()) {
			new_message.setObject(null);
			new_message.setRequest(Request.POP_UP_ERROR);
			new_message.setResponse(Response.NO_SUCH_USER);
			return new_message;
		}

		if (rs.next()) {
			userId = rs.getString(3);
			userType = rs.getString(4);
		}
		rs.close();

		condition = "ID='" + userId + "'";
		ResultSet userDetails = mainQuery.getTuple(userType, condition);
		new_message = checkType(userDetails, userType, new_message);
		return new_message;

	}

	/**This function is responsible for the Log Out of User
	 * @param message
	 */
	public static void LogOut(FullMessage message) {
		
		if (message.getObject() instanceof Customer)
			mainQuery.updateTuple("customer", "LogInStatus" + "=" + "0",
					"ID=" + ((User) (message.getObject())).getID());
		else if (message.getObject() instanceof CEOZli)
			mainQuery.updateTuple("ceozli", "LogInStatus" + "=" + "0", "ID=" + ((User) (message.getObject())).getID());
		else if (message.getObject() instanceof BranchManager)
			mainQuery.updateTuple("branchmanager", "LogInStatus" + "=" + "0",
					"ID=" + ((User) (message.getObject())).getID());
		else if (message.getObject() instanceof Worker)
			mainQuery.updateTuple("worker", "LogInStatus" + "=" + "0", "ID=" + ((User) (message.getObject())).getID());
		else if (message.getObject() instanceof ServiceExpert)
			mainQuery.updateTuple("servicespecialist", "LogInStatus" + "=" + "0",
					"ID=" + ((User) (message.getObject())).getID());
		else if (message.getObject() instanceof CustomerServiceWorker)
			mainQuery.updateTuple("customerserviceworker", "LogInStatus" + "=" + "0",
					"ID=" + ((User) (message.getObject())).getID());
		else if (message.getObject() instanceof DeliveryPerson)
			mainQuery.updateTuple("deliveryperson", "LogInStatus" + "=" + "0",
					"ID=" + ((User) (message.getObject())).getID());

	}

	/**
	 * This function is responsible for checking the Type of the user that logged in
	 * @param rs
	 * @param userType
	 * @param message
	 * @return FullMessage
	 */
	public static FullMessage checkType(ResultSet rs, String userType, FullMessage message) {

		switch (userType) {

		case "customer": //if user is customer
			Customer customer = null;
			try {
				if (rs.next()) {
					customer = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)), rs.getString(9), rs.getDouble(10));

					if (ConfirmationStatus.valueOf(rs.getString(8)).equals(ConfirmationStatus.PENDING_APPROVAL)) {
						message.setResponse(Response.NO_SUCH_USER);
						message.setRequest(Request.POP_UP_ERROR);
						return message;
					}
                                 
						
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(customer, message, "customer", userType);
			break;

		case "branchmanager": //if user is branchmanager
			BranchManager branchManager = null;
			try {
				if (rs.next()) {
					branchManager = new BranchManager(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)), rs.getString(9));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(branchManager, message, "branchmanager", userType);
			break;

		case "ceozli": //if user is ceozli
			CEOZli ceo = null;
			try {
				if (rs.next()) {
					ceo = new CEOZli(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(ceo, message, "ceozli", userType);
			break;

		case "customerserviceworker": //if user is customerserviceworker
			CustomerServiceWorker cs_worker = null;
			try {
				if (rs.next()) {
					cs_worker = new CustomerServiceWorker(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)));
				}
				
				if (ConfirmationStatus.valueOf(rs.getString(8)).equals(ConfirmationStatus.PENDING_APPROVAL)) {
					message.setResponse(Response.NO_SUCH_USER);
					message.setRequest(Request.POP_UP_ERROR);
					return message;
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(cs_worker, message, "customerserviceworker", userType);
			break;

		case "servicespecialist": //if user is servicespecialist
			ServiceExpert serviceSpecialist = null;
			try {
				if (rs.next()) {
					serviceSpecialist = new ServiceExpert(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)));
				}
				
				if (ConfirmationStatus.valueOf(rs.getString(8)).equals(ConfirmationStatus.PENDING_APPROVAL)) {
					message.setResponse(Response.NO_SUCH_USER);
					message.setRequest(Request.POP_UP_ERROR);
					return message;
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(serviceSpecialist, message, "servicespecialist", userType);
			break;

		case "worker": //if user is worker
			Worker worker = null;
			try {
				if (rs.next()) {
					worker = new Worker(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)));
					if (ConfirmationStatus.valueOf(rs.getString(8)).equals(ConfirmationStatus.PENDING_APPROVAL)) {
						message.setResponse(Response.NO_SUCH_USER);
						message.setRequest(Request.POP_UP_ERROR);
						return message;
					}
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(worker, message, "worker", userType);// update the message
			break;

		case "deliveryperson": //if user is deliveryperson
			DeliveryPerson deliveryperson = null;
			try {
				if (rs.next()) {
					deliveryperson = new DeliveryPerson(rs.getString(1), rs.getString(2), rs.getString(3),
							rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7),
							ConfirmationStatus.valueOf(rs.getString(8)), Branch.valueOf(rs.getString(9)));
					
					if (ConfirmationStatus.valueOf(rs.getString(8)).equals(ConfirmationStatus.PENDING_APPROVAL)) {
						message.setResponse(Response.NO_SUCH_USER);
						message.setRequest(Request.POP_UP_ERROR);
						return message;
					}
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			message = updateMessage(deliveryperson, message, "deliveryperson", userType);// update the message
			break;

		}
		return message;

	}

	/**
	 * This is the last function in the LogIn Process
	 * Checks if the User is logged in or not
	 * Checks which portal should be opened according to type
	 * @param user
	 * @param message
	 * @param tableName
	 * @param type
	 * @return
	 */
	public static FullMessage updateMessage(User user, FullMessage message, String tableName, String type) {

		if (user.isLogInStatus()) {
			message.setResponse(Response.ALREADY_LOGGED_IN);
			message.setRequest(Request.POP_UP_ERROR);
		} else {

			String condition1 = "LogInStatus" + "=" + "1"; 

			String condition2 = "ID=" + user.getID();
			mainQuery.updateTuple(tableName, condition1, condition2);
			// message.setObject(null); //check meaning
			message.setRequest(Request.OPEN_PORTAL);

			switch (type) {
			case "customer":
				message.setResponse(Response.Create_CUSTOMER_PORTAL);
				message.setObject(user);
				break;
			case "branchmanager":
				message.setResponse(Response.Create_BRANCHMANAGER_PORTAL);
				message.setObject(user);
				break;
			case "ceozli":
				message.setResponse(Response.Create_CEOZLI_PORTAL);
				message.setObject(user);
				break;
			case "customerserviceworker":
				message.setResponse(Response.Create_CUSTOMERSERVICEWORKER_PORTAL);
				message.setObject(user);
				break;
			case "servicespecialist":
				message.setResponse(Response.Create_SERVICESPECIALIST_PORTAL);
				message.setObject(user);
				break;
			case "worker":
				message.setResponse(Response.Create_WORKER_PORTAL);
				message.setObject(user);
				break;
			case "deliveryperson":
				message.setResponse(Response.Create_DELIVERYPERSON_PORTAL);
				message.setObject(user);
				break;
			}
		}
		return message;

	}

}
