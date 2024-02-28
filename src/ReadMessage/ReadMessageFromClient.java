package ReadMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Request;
import RequestsAndResponses.Response;
import ServerGUIControllers.ServerGuiController;
import ocsf.server.ConnectionToClient;
import queries.ComplaintsQuery;
import queries.ItemsAndProductsQuery;
import queries.LogInQuery;
import queries.OrderQuery;
import queries.ReportQuery;
import queries.SurveyQuery;
import queries.UserQuery;
import queries.mainQuery;

/**
 * Class description:
 * This Is A Class Which Is A Wrapper For Handling
 * All Messages From The Client.
 * 
 *@author Obied haddad
 *@author maisalon safory
 *@author Ebrahem enbtawe
 *@author shorok heib
 *@author mario rohana
 *@author seren hanany
 */
public class ReadMessageFromClient {

	public static ConnectionToClient CurrentClient;

	public static ServerGuiController HandleClientStatus = ServerGuiController.getLoader().getController();

	/**
	 * This is a function which analyzes all the messages from the client and than
	 * does logic accordingly.
	 * @param message
	 * @param client
	 * @return
	 * @throws SQLException
	 */
	public static FullMessage ReadMessage(Object message, ConnectionToClient client) throws SQLException {
		CurrentClient = client;

		/**
		 * try { mainQuery.ShowPDF(); } catch (SQLException | IOException e2) { // TODO
		 * Auto-generated catch block e2.printStackTrace(); }
		 */

		if (!(message instanceof FullMessage)) {

			return null;
		}

		else {

			FullMessage messageFromClient = (FullMessage) message;
			Request requestFromClient = messageFromClient.getRequest();
			/**
			 * Switch Case For Getting Message From Client Side.
			 */
			switch (requestFromClient) {
			case Connect:
				/**
				 * The ip is confirmed
				 */
				boolean AddClient = HandleClientStatus
						.CheckIfClientIsInListAndSetStatusToConnect(CurrentClient.getInetAddress().getHostAddress());

				if (AddClient) {

					HandleClientStatus.AddNewClient(CurrentClient.getInetAddress().getHostAddress(),
							CurrentClient.getInetAddress().getCanonicalHostName(), "Connected");

					messageFromClient.setResponse(Response.Succeed);
				}
			default:
				break;

			case Disconnect:
				/**
				 * The Client Disconnect
				 */
				HandleClientStatus.SetClientStatusToDisconnect(CurrentClient.getInetAddress().getHostAddress(),
						"Disconnected");

				messageFromClient.setResponse(Response.Succeed);
				break;

			case LOGIN:
				 /**
				 * User LogIn
			     */
				try {

					messageFromClient = LogInQuery.serverLogIn(messageFromClient);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case LOGOUT:
				 /**
				 * User LogOut
			     */
				LogInQuery.LogOut(messageFromClient);
				messageFromClient.setResponse(Response.Succeed);
				break;

			case GET_ITEMS_FROM_DB:
				 /**
				  * We Get Items From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CreateCatalogForUser(messageFromClient);
				break;

			case GET_ORDER_FROM_DB:
				 /**
				  * We Get Orders From DB
				  */
				messageFromClient = OrderQuery.AcceptOrder(messageFromClient);
				break;

			case GET_ORDER_FROM_DB_FOR_CANCELORDER:
				 /**
				  * We Get Orders From DB For Customer Cancel Order
				  */
				messageFromClient = OrderQuery.CancelOrder(messageFromClient);
				break;

			case GET_ORDER_FROM_DB_FOR_MANAGER_CANCEL_ORDER:
				 /**
				  * We Get Orders From DB For Branch Manager Approve Cancel Order
				  */
				messageFromClient = OrderQuery.ManagerCancelOrder(messageFromClient);
				break;

			case GET_ORDER_FROM_DB_FOR_CUSTOMER:
				 /**
				  * We get Orders From DB For Customer 
				  */
				messageFromClient = OrderQuery.GetOrderForCustomer(messageFromClient);
				break;

			case MANAGE_ORDER_FINISHED:
				 /**
				  * Update The Order Status
				  */
				messageFromClient = OrderQuery.updateOrdersStatusOnDb(messageFromClient);
				break;

			case UPDATE_BALANCE_ORDER_UNAPPROVED:
				 /**
				  * Update The Order Balance After Un_Approved
				  */
				messageFromClient = UserQuery.restoreTheOldBalanceBeforePurchasing(messageFromClient);
				break;

			case GET_THE_SUBRACTED_DATE_TIME:
				 /**
				  * We Get The Difference Betwen The Dates
				  */
				messageFromClient = OrderQuery.getTheDiffrenceTimeBetweenDates(messageFromClient);
				break;

			case CANCEL_ORDER_FINISHED:
				 /**
				  * Update The Order Status
				  */
				messageFromClient = OrderQuery.updateOrdersStatusOnDb(messageFromClient);
				break;
			case COMPLETED_ORDER_FINISHED:
				 /**
				  * Update The Order Status
				  */
				messageFromClient = OrderQuery.updateOrdersStatusOnDb(messageFromClient);
				break;

			case GET_SURVEY_FROM_DB:
				 /**
				  * We Get Survey From DB
				  */
				messageFromClient = SurveyQuery.GetSurveyFromDB(messageFromClient);
				break;

			case SET_SURVEY_ANSWER:
				 /**
				  * We Set Survey In DB
				  */
				messageFromClient = SurveyQuery.SetAnswersToDB(messageFromClient);
				break;

			case GET_CUSTOMER_DETAILS:
				 /**
				  * We Get Balance And CreditCard Customer From DB
				  */
				messageFromClient = UserQuery.GetUserDetails(messageFromClient);
				break;

			case CHECK_IF_CUSTOMER_FIRST_ORDER:
				 /**
				  * We Get Last Order ID From DB
				  */
				messageFromClient = OrderQuery.CheckIfFirstOrder(messageFromClient);
				break;

			case GET_LAST_COMPLAINT_NUMBER:
				 /**
				  * We Get Complaint From DB
				  */
				messageFromClient = ComplaintsQuery.CheckIfFirstComplaint(messageFromClient);
				break;

			case INSERT_ORDER_TO_DB:
				 /**
				  * We Set Order In DB
				  */
				try {

					messageFromClient = OrderQuery.addOrderToDb(messageFromClient);
				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case UPDATE_CUSTOMER_BALANCE:
				 /**
				  * We Set Customer Balance In DB
				  */
				messageFromClient = UserQuery.UpdateCustomerBalance(messageFromClient);
				break;

			case CHECK_AMOUNT:
				 /**
				  * We Update Item Amount In DB
				  */
				messageFromClient = OrderQuery.GetAmountAndUpdate(messageFromClient);
				break;

			case GET_ITEMS_ACCORDING_TO_COLOR:
				 /**
				  * We Get Items According To Color From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CreateCatalogAccordingToColor(messageFromClient);
				break;

			case GET_ITEMS_ACCORDING_TO_TYPE:
				 /**
				  * We Get Items According To Type From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CreateCatalogAccordingToType(messageFromClient);
				break;
			case GET_ITEMS_ACCORDING_TO_PRICE:
				 /**
				  * We Get Items According To Price From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CreateCatalogAccordingToPrice(messageFromClient);
				break;

			case RESTORE_AMOUNT_FOR_ITEM:
				 /**
				  * We Update The Amount In DB
				  */
				messageFromClient = ItemsAndProductsQuery.RestoreAmountForItem(messageFromClient);
				break;
			case GET_ALL_ITEMS_FOR_WORKER:
				 /**
				  * We Get Items Worker From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CreateCatalogForUser(messageFromClient);
				break;

			case REMOVE_ITEM_FROM_CATALOG:
				 /**
				  * We Remove The Item From The Catalog 
				  */
				ItemsAndProductsQuery.RemoveItemFromCatalog(messageFromClient);
				break;

			case UPDATE_ITEM_IN_CATALOG:
				 /**
				  * We Update The Item In Catalog
				  */
				ItemsAndProductsQuery.UpdateItemInCatalog(messageFromClient);
				break;
			case ADD_ITEM_TO_DB:
				 /**
				  * We Set Item In DB
				  */
				try {
					ItemsAndProductsQuery.addItemToDB(messageFromClient);
				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case UPDATE_WORKER_SALE:
				 /**
				  * We Update Worker Sale In DB
				  */
				UserQuery.UpdateSaleForWorker(messageFromClient);
				break;
			case UPDATE_WORKER_SALE_FOR_SPECIFIC_BRANCH:
				 /**
				  * We Update Worker Sale In DB For Specific Branch
				  */
				ItemsAndProductsQuery.UpdateSaleForWorkerForSpecificBranch(messageFromClient);
				break;

			case UPDATE_WORKER_AFTER_END_SALE:
				 /**
				  * We Update Worker Sale In DB
				  */
				ItemsAndProductsQuery.UpdateSaleForWorkerToEndSale(messageFromClient);
				break;

			case UPDATE_PRICES_AFTER_SALES:
				 /**
				  * We Update Price For Items In DB
				  */
				ItemsAndProductsQuery.ChangeItemsPrices(messageFromClient);
				break;

			case CHECK_IF_CATALOG_EXIST_TO_START_SALES:
				 /**
				  * We Check If There Items In DB
				  */
				messageFromClient = ItemsAndProductsQuery.CreateCatalogForUser(messageFromClient);
				break;

			case GET_THE_SALE_PERCENTAGE_FROM_WORKER:
				 /**
				  * We Get Sale Percentage For Items From DB
				  */
				messageFromClient = ItemsAndProductsQuery.GetPercentageOfSales(messageFromClient);
				break;
			case UPDATE_PRICES_AFTER_END_SALES:
				 /**
				  * We Update Price For Items In DB
				  */
				ItemsAndProductsQuery.ChangeItemsPricesToBeforeSales(messageFromClient);
				break;
			case CHECK_IF_SALES_ARE_ON:				
				/**
				  * We Get Sales From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CheckIfSalesAreOn(messageFromClient);
				break;

			case CHECK_IF_SALES_ARE_ON_FOR_CATALOG:
				/**
				  * We Get Sales From DB
				  */
				messageFromClient = ItemsAndProductsQuery.CheckIfSalesAreOnForCatalog(messageFromClient);
				break;

			case CHECK_REPORT_FROM_DB:
				/**
				  * We Get Reports From DB
				  */
				messageFromClient = ReportQuery.GetReportFromDB(messageFromClient);
				break;
			case CHECK_REPORT_FROM_DB_FOR_CEO:
				/**
				  * We Get Reports For CEO From DB
				  */
				messageFromClient = ReportQuery.GetReportFromDB(messageFromClient);
				break;

			case GET_MANAGER_ID:
				/**
				  * We Get Manager ID From DB
				  */
				messageFromClient = ReportQuery.GetManagerID(messageFromClient);
				break;
			case GET_REPORT_FROM_DB:
				/**
				  * We Get Income Reports For Manager From DB
				  */
				messageFromClient = ReportQuery.GetIncomeReportForManager(messageFromClient);
				break;
			case GET_REPORT_FROM_DB_FOR_CEO:
				/**
				  * We Get Income Reports For CEO From DB
				  */
				messageFromClient = ReportQuery.GetIncomeReportForManager(messageFromClient);
				break;
			case GET_ORDER_REPORT_FROM_DB:
				/**
				  * We Get Order Reports From DB
				  */
				messageFromClient = ReportQuery.GetOrderReport(messageFromClient);
				break;
			case GET_NUM_OF_COMPLAINT:
				/**
				  * We Get Number Of Complaint From DB
				  */
				messageFromClient = ReportQuery.GetNumOfComplaintByQuarterly(messageFromClient);
				break;

			case GET_REPORT_FOR_TWO_QUARTERLY:
				/**
				  * We Get Reports From DB
				  */
				messageFromClient = ReportQuery.GetReportForTwoQuarterly(messageFromClient);
				break;
			case GET_REPORT_FOR_TWO_BRANCHES:
				/**
				  * We Get Reports For Two Branchs From DB
				  */
				messageFromClient = ReportQuery.GetReportForTwoBranches(messageFromClient);
				break;
			case GET_ORDER_FROM_DB_FOR_DELIVERY:
				/**
				  * We Get Orders For Deleivery From DB
				  */
				messageFromClient = OrderQuery.DeliveryOrder(messageFromClient);
				break;
			case GET_CUSTOMER_FROM_DB:
				/**
				  * We Get Customers From DB
				  */
				messageFromClient = UserQuery.GetCustomerFromDB(messageFromClient);
				break;
			case UPDATE_STATUS_CUSTOMER:
				/**
				  * Update Customer Status In DB
				  */
				messageFromClient = UserQuery.UpdateCustomerStatus(messageFromClient);
				break;
			case GET_USER_STATUS:
				/**
				  * We Get User Status From DB
				  */
				messageFromClient = UserQuery.GetUserStatus(messageFromClient);
				break;

			case GET_COMPLAINT_FROM_DB:
				/**
				  * We Get Complaint From DB
				  */
				messageFromClient = ComplaintsQuery.showComplaintsForUser(messageFromClient);
				break;
			case GET_NUM_OF_COMPLAINT_FOR_CEO:
				/**
				  * We Get Number Of Complaint From DB
				  */
				messageFromClient = ReportQuery.GetNumOfComplaintByQuarterly(messageFromClient);
				break;
			case GET_ORDER_REPORT_FROM_DB_FOR_CEO:
				/**
				  * We Get Order Report From DB
				  */
				messageFromClient = ReportQuery.GetOrderReport(messageFromClient);
				break;
			case UPDATE_BALANCE_AFTER_COMPLAINT:
				/**
				  * We Update Balance In DB
				  */
				messageFromClient = UserQuery.restoreOldBalanceAfterComplaint(messageFromClient);
				break;

			case GET_USERS_FROM_DB:
				/**
				  * We Get Users From DB
				  */
				messageFromClient = UserQuery.GetUsersFromDB(messageFromClient);
				break;

			case GET_WORKER_BRANCH:
				/**
				  * We Get Worker Branch From DB
				  */
				messageFromClient = UserQuery.GetBranchFromWorker(messageFromClient);
				break;

			case UPDATE_TYPE_USER:
				/**
				  * We Update Users Type From DB
				  */
				messageFromClient = UserQuery.ChangeUsersFromDB(messageFromClient);
				break;

			case ADD_COMPLAINT_FROM_USER_TO_DB:
				/**
				  * We Set Complaint In DB
				  */
				try {
					ComplaintsQuery.AddComplaintToDB(messageFromClient);
				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case GET_USERS_FROM_DB_FOR_WORKER:
				/**
				  * We Get Users From DB
				  */
				messageFromClient = UserQuery.GetUsersFromDB(messageFromClient);
				break;
			case UPLOAD_PDF_TO_SYSTEM:
				/**
				  * We Set PDF 
				  */
				try {
					mainQuery.InsertPDF((String) messageFromClient.getObject());
				} catch (FileNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case EXTRACT_PDF_FROM_DB:
				/**
				  * We get PDF From DB
				  */
				try {
					messageFromClient = mainQuery.ShowPDF(messageFromClient);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case GET_SURVEYID:
				/**
				  * We get Survey From DB
				  */
				messageFromClient = SurveyQuery.GetSurveyFromDB(messageFromClient);
				break;
				
			case GET_CUSTOMER_EMAIL:
				/**
				 * We get Email From DB
				 */
				messageFromClient = UserQuery.GetUserEmail(messageFromClient);
				break;
				
			case GET_SURVEY_ID_FROM_DATABASE:
				/**
				 * We get Survey ID from DB
				 */
				messageFromClient=SurveyQuery.GetSurveyFromDB(messageFromClient);
				break;

			}

			return messageFromClient;

		}

	}
	
	

}