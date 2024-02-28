package queries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import Orders.Branch;
import Orders.DominantColor;
import Orders.FlowerColor;
import Orders.Item;
import Orders.ItemCategory;
import Orders.ItemType;
import RequestsAndResponses.FullMessage;
import RequestsAndResponses.Response;
import Worker.SaleColumn;

/**
 * This class is responsible for handling All Items and products in Catalog
 * @author Ebrahem Enbtawe
 *
 */
public class ItemsAndProductsQuery {

	/**
	 * This method takes all items from catalog in database to display to user
	 * @param messageFromClient
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage CreateCatalogForUser(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		List<Item> itemList = new ArrayList<Item>();

		ResultSet rs = mainQuery.SelectAllFromDB("catalog");
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_CATALOG);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				Item ItemFromDB = convertToItem(rs);
				if (ItemFromDB != null) {
					itemList.add(ItemFromDB);
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(itemList);
		returnMessageToClient.setResponse(Response.CATALOG_FOUND);
		return returnMessageToClient;
	}

	/**
	 * This method takes all items from catalog in database according to price to display to user
	 * @param messageFromClient
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage CreateCatalogAccordingToPrice(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		List<Item> itemList = new ArrayList<Item>();

		String price = messageFromClient.getObject().toString();
		String[] parsedMsgFromClient = ((String) price).split("->");
		int minPrice = Integer.parseInt(parsedMsgFromClient[0]);
		int maxPrice = Integer.parseInt(parsedMsgFromClient[1]);
		String condition = "Price between'" + minPrice + "' AND '" + maxPrice + "'";
		ResultSet rs = mainQuery.getTuple("catalog", condition);
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_CATALOG);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				Item ItemFromDB = convertToItem(rs);
				if (ItemFromDB != null) {
					itemList.add(ItemFromDB);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		returnMessageToClient.setObject(itemList);
		returnMessageToClient.setResponse(Response.CATALOG_FOUND);
		return returnMessageToClient;
	}
	/**
	 * This method takes all items from catalog in database according to Type to display to user
	 * @param messageFromClient
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage CreateCatalogAccordingToType(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		List<Item> itemList = new ArrayList<Item>();
		ItemType type = (ItemType) returnMessageToClient.getObject();
		String condition = "Type='" + type + "'";
		ResultSet rs = mainQuery.getTuple("catalog", condition);
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_CATALOG);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				Item ItemFromDB = convertToItem(rs);
				if (ItemFromDB != null) {
					itemList.add(ItemFromDB);
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(itemList);
		returnMessageToClient.setResponse(Response.CATALOG_FOUND);
		return returnMessageToClient;
	}

	/**
	 * This method takes all items from catalog in database according to Type to display to user
	 * @param messageFromClient
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage CreateCatalogAccordingToColor(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		List<Item> itemList = new ArrayList<Item>();

		FlowerColor color = (FlowerColor) returnMessageToClient.getObject();
		String condition = "Color='" + color + "'";
		ResultSet rs = mainQuery.getTuple("catalog", condition);
		try {
			// If the row doesn't exist in login Table
			if (!rs.isBeforeFirst()) {
				returnMessageToClient.setResponse(Response.NO_CATALOG);
				returnMessageToClient.setObject(null);
				return returnMessageToClient;
			}
			while (rs.next()) {
				Item ItemFromDB = convertToItem(rs);
				if (ItemFromDB != null) {
					itemList.add(ItemFromDB);
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnMessageToClient.setObject(itemList);
		returnMessageToClient.setResponse(Response.CATALOG_FOUND);
		return returnMessageToClient;
	}

	/**
	 * This method takes Item details from database and returns new Item with this details
	 * @param rs
	 * @return Item
	 */
	private static Item convertToItem(ResultSet rs) {
		
		try {
			String itemID = rs.getString(1);
			ItemCategory itemCategory = ItemCategory.valueOf(rs.getString(2));
			FlowerColor flowercolor = FlowerColor.valueOf(rs.getString(3));
			String name = rs.getString(4);
			double price = rs.getDouble(5);
			String picturePath = rs.getString(6);
			String greetingCard = rs.getString(7);
			ItemType itemType = ItemType.valueOf(rs.getString(8));
			DominantColor dominantColor = DominantColor.valueOf(rs.getString(9));
			int amount = rs.getInt(10);

			return new Item(itemID, itemCategory, flowercolor, name, price, picturePath, greetingCard, itemType,
					dominantColor, amount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method Restores amount of items in database
	 * @param messageFromClient
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage RestoreAmountForItem(FullMessage messageFromClient) throws SQLException {

		FullMessage returnMessageToClient = messageFromClient;
		String id = (String) messageFromClient.getObject();
		String condition = "ID='" + id + "'";
		ResultSet rs = mainQuery.getTuple("catalog", condition);
		try {
			while (rs.next()) {
				int amount = rs.getInt(10);
				amount++;
				String condition1 = "Amount" + "=" + amount; /// CHANGEEE EBRAHEM
				String condition2 = "ID=" + id;
				mainQuery.updateTuple("catalog", condition1, condition2);
				returnMessageToClient.setResponse(Response.AMOUNT_RESTORED);

			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// returnMessageToClient.setObject(customerFromDB);
		return returnMessageToClient;
	}

	/**
	 * This method removes any Item in catalog
	 * @param message
	 */
	public static void RemoveItemFromCatalog(FullMessage message) {
		
		String[] parsedMsgFromClient = ((String) message.getObject()).split(" ");
		String ID = parsedMsgFromClient[0];
		String tablename = parsedMsgFromClient[1];
		mainQuery.DeleteRowFromDB("ID='" + ID + "'", tablename);
	}

	/**
	 * This method is to edit an Item in catalog
	 * @param message
	 */
	public static void UpdateItemInCatalog(FullMessage message) {

		String[] parsedMsgFromClient = ((String) message.getObject()).split(" ");
		String ID = parsedMsgFromClient[0];
		double price = Double.parseDouble(parsedMsgFromClient[1]);
		int amount = Integer.parseInt(parsedMsgFromClient[2]);
		mainQuery.updateTuple("catalog", "Price" + "=" + price, "ID=" + ID);
		mainQuery.updateTuple("catalog", "amount" + "=" + amount, "ID=" + ID);

	}

	public static void addItemToDB(FullMessage messageFromClient) throws SQLException, ParseException {
		Item item = (Item) messageFromClient.getObject();
		ItemCategory itemCategory = item.getItemCategory();
		;
		FlowerColor color = item.getColor();
		String name = item.getItemName();
		double price = item.getPrice();
		String path = item.getPicturePath();
		String card = item.getGreetingCard();
		ItemType type = item.getType();
		DominantColor dominant = item.getDominantColor();
		int amount = item.getAmount();

		mainQuery.InsertOneRowIntoCatalogTable(null, itemCategory, color, name, price, path, card, type, dominant,
				amount);

	}

	/**
	 * This method is to change Item prices when there's sales
	 * @param message
	 * @throws SQLException
	 */
	public static void ChangeItemsPrices(FullMessage message) throws SQLException {
		
		ResultSet rs = mainQuery.SelectAllFromDB("catalog");
		String percentOfSaleInString = (String) message.getObject();

		double pricePercentageAfterSale = Double.parseDouble(percentOfSaleInString);
		pricePercentageAfterSale = (100.0 - pricePercentageAfterSale) / 100.0;

		while (rs.next()) {
			double priceBeforeSale = rs.getDouble(5);
			String id = rs.getString(1);
			double priceToDB = priceBeforeSale * pricePercentageAfterSale;
			mainQuery.updateTuple("catalog", "Price='" + priceToDB + "'", "ID=" + id);
		}

		rs.close();
	}

	/**
	 * This method restores the Item prices to original prices (after end sales)
	 * @param message
	 * @throws SQLException
	 */
	public static void ChangeItemsPricesToBeforeSales(FullMessage message) throws SQLException {
		
		ResultSet rs = mainQuery.SelectAllFromDB("catalog");
		String[] parsedMsgFromClient = ((String) message.getObject()).split(",");

		double pricePercentageAfterSale = Double.parseDouble(parsedMsgFromClient[1]);
		pricePercentageAfterSale = (100.0 - pricePercentageAfterSale) / 100.0;

		while (rs.next()) {
			double priceBeforeSale = rs.getDouble(5);
			String id = rs.getString(1);
			double priceToDB = priceBeforeSale / pricePercentageAfterSale;
			mainQuery.updateTuple("catalog", "Price='" + priceToDB + "'", "ID=" + id);
		}

		rs.close();
	}
	
	/**
	 * This method Updates Sale column for worker when starting sales for specific branch
	 * @param message
	 * @throws SQLException
	 */
	public static void UpdateSaleForWorkerForSpecificBranch(FullMessage message) throws SQLException {
		
		String[] parsedMsgFromClient = ((String) message.getObject()).split(" ");
		String percent = parsedMsgFromClient[1];
		String Branch = parsedMsgFromClient[2];
		String condition = "Branch='" + Branch + "'";
		ResultSet rs = mainQuery.getTuple("worker", condition);
		String valueToDB = "2" + "," + percent + "" + "," + Branch;
		while (rs.next()) {

			mainQuery.updateTuple("worker", "Sales='" + valueToDB + "'", condition);
		}

		rs.close();

	}

	/**
	 * This method Updates Sale column for worker when ending sales with specific branch
	 * @param message
	 * @throws SQLException
	 */
	public static void UpdateSaleForWorkerToEndSale(FullMessage message) throws SQLException {
		
		ResultSet rs = null;
		String[] parsedMsgFromClient = ((String) message.getObject()).split(" ");
		String branch = parsedMsgFromClient[0];
		String IsItForBranchSale = parsedMsgFromClient[1];
		String condition = "Branch='" + branch + "'";
		int number = Integer.parseInt(IsItForBranchSale);
		if (number == 2) {
			rs = mainQuery.SelectAllFromDB("worker");
		} else {
			rs = mainQuery.getTuple("worker", condition);
		}

		while (rs.next()) {

			String id = rs.getString(1);
			mainQuery.updateTuple("worker", "Sales" + "=" + "0", "ID=" + id);
		}

		rs.close();

	}

	/**
	 * This mehtod checks if there's Sales in progress
	 * @param message
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage CheckIfSalesAreOn(FullMessage message) throws SQLException {

		String[] parsedMsgFromClient = null;
		FullMessage returnMessageToClient = message;
		String SaleCol = null;
		Branch branch = (Branch) message.getObject();
		String condition = "Branch='" + branch + "'";
		ResultSet rs = mainQuery.getTuple("worker", condition);

		while (rs.next()) {

			SaleCol = rs.getString(9);
			parsedMsgFromClient = SaleCol.split(",");
			if (parsedMsgFromClient[0].equals("1") || parsedMsgFromClient[0].equals("2")) {
				returnMessageToClient.setObject(parsedMsgFromClient);
				return returnMessageToClient;
			}
		}

		rs.close();
		returnMessageToClient.setObject(parsedMsgFromClient);
		return returnMessageToClient;

	}

	/**
	 * This method checks if there's sales when loading catalog page
	 * @param message
	 * @return returnMessageToClient
	 * @throws SQLException
	 */
	public static FullMessage CheckIfSalesAreOnForCatalog(FullMessage message) throws SQLException {
		
		ArrayList<SaleColumn> SaleCols = new ArrayList<>();
		FullMessage returnMessageToClient = message;
		String SaleCol = null;
		String[] SplitMessage = null;
		ResultSet rs = mainQuery.SelectAllFromDB("worker");

		while (rs.next()) {

			SaleCol = rs.getString(9);
			if (SaleCol.equals("0")) {
				SaleCols.add(new SaleColumn("0","0","0"));
			} else {
				SplitMessage = SaleCol.split(",");
				if (SplitMessage.length == 2) {
					SaleCols.add(new SaleColumn(SplitMessage[0], SplitMessage[1], null));
				} else {
					if (SplitMessage.length == 3) {
						SaleCols.add(new SaleColumn(SplitMessage[0], SplitMessage[1], SplitMessage[2]));
					}
				}
			}
		}
		returnMessageToClient.setObject(SaleCols);
		return returnMessageToClient;
	}

	/**
	 * This method takes the percentage from database and returns it to user
	 * @param message
	 * @return
	 * @throws SQLException
	 */
	public static FullMessage GetPercentageOfSales(FullMessage message) throws SQLException {
		
		FullMessage returnMessageToClient = message;
		String SaleCol = null;
		Branch branch = (Branch) message.getObject();
		String condition = "Branch='" + branch + "'";
		ResultSet rs = mainQuery.getTuple("worker", condition);

		while (rs.next()) {

			SaleCol = rs.getString(9);
			if (SaleCol != "0") {
				returnMessageToClient.setObject(SaleCol);
				return returnMessageToClient;

			}
		}

		rs.close();
		returnMessageToClient.setObject(SaleCol);
		return returnMessageToClient;

	}

}
