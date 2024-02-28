package queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import Orders.Branch;
import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Response;
import customerService.Complaint;

/**
 * Class description:
 * Controlling the Complaints Queries
 * @author Ebrahem Enbtawe
 * @author Mario Rohanna
 *
 */
public class ComplaintsQuery {


	
	/**
	 * This function checks if the complaint exists, then returns it to client
	 * @param messageFromClient
	 * @return FullMessage
	 * @throws SQLException
	 */
	public static FullMessage showComplaintsForUser(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		List<Complaint> complaintList = new ArrayList<Complaint>();
		ResultSet rs = mainQuery.SelectAllFromDB("complaint");
		try {

			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_COMPLAINTS);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				Complaint complaintFromDB = convertToComplaint(rs);
				if (complaintFromDB != null) {
					complaintList.add(complaintFromDB);

				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(complaintList);
		returnMessageToClient.setResponse(Response.COMPLAINTS_FOUND);


		return returnMessageToClient;
	}

	/**
	 * This function takes the details of complaints from database and makes new object of Complaint
	 * @param rs
	 * @return Complaint
	 */
	private static Complaint convertToComplaint(ResultSet rs) {
		try {
			int complaintID = rs.getInt(1);
			int complaintnum = rs.getInt(2);
			String customerID = rs.getString(3);
			int ordernum = rs.getInt(4);
			Timestamp complaintdate = rs.getTimestamp(5);
			Branch branchname = Branch.valueOf(rs.getString(6));
			String text = rs.getString(7);

			return new Complaint(complaintID, complaintnum, customerID, ordernum, complaintdate, branchname, text, 0.0);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This function adds new complaint to Database
	 * @param messageFromClient
	 * @return FullMessage
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static FullMessage AddComplaintToDB(FullMessage messageFromClient) throws SQLException, ParseException {

		Complaint complaint = (Complaint) messageFromClient.getObject();
		FullMessage returnMessageToClient = messageFromClient;
		int complaintid = complaint.getComplaintID();
		int complaintnumber = complaint.getComplaintNum();
		String customerid = complaint.getCustomerId();
		int ordernumber = complaint.getOrderNumber();
		Timestamp date = complaint.getComplaintDate();
		Branch branch = complaint.getBranchName();
		String text = complaint.getText();

		mainQuery.InsertOneRowIntoComplaintTable(complaintid, complaintnumber, customerid, ordernumber, date, branch,
				text);

		return returnMessageToClient;

	}
	
	/**
	 * this function checks if there is complaints in Database
	 * @param messageFromClient
	 * @return FullMessage
	 * @throws SQLException
	 */
	public static FullMessage CheckIfFirstComplaint(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		ResultSet rs = mainQuery.SelectAllFromDB("complaint");
		int c = 0;
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_COMPLAINT_FOUND);
				return returnMessageToClient;
			}

			while (rs.next()) {
				c++;
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(c);
		returnMessageToClient.setResponse(Response.NOT_FIRST_COMPLAINT);
		return returnMessageToClient;
	}
}
