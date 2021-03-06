/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jala.efeeder.payment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jala.efeeder.api.command.Command;
import org.jala.efeeder.api.command.CommandUnit;
import org.jala.efeeder.api.command.In;
import org.jala.efeeder.api.command.Out;
import org.jala.efeeder.api.command.impl.DefaultOut;
import org.jala.efeeder.foodmeeting.FoodMeeting;
import org.jala.efeeder.foodmeeting.FoodMeetingManager;
import org.jala.efeeder.order.Order;
import org.jala.efeeder.order.OrderManager;
import org.jala.efeeder.user.Buyer;
import org.jala.efeeder.user.BuyerManager;
import org.jala.efeeder.user.User;

/**
 *
 * @author alexander_castro
 */
@Command
public class PaymentCommand implements CommandUnit {

	@Override
	public Out execute(In parameters) throws Exception {
		Out out = new DefaultOut();
		Connection connection = parameters.getConnection();
		BuyerManager buyerManager = new BuyerManager(connection);
		String idFoodMeeting = parameters.getParameter("id_food_meeting");
		int idUser = parameters.getUser().getId();
		List<Order> orders = getOrders(connection, idFoodMeeting);
		Buyer buyer = buyerManager.getBuyerByFoodMeetingId(Integer.valueOf(idFoodMeeting));
		User user = parameters.getUser();

		int foodMeetingId = Integer.parseInt(parameters.getParameter("id_food_meeting"));
		int buyerId = buyer != null ? buyer.getUserId() : 0;

		String isBuyer = (buyerId == user.getId()) ? "block" : "none";
		List<PaymentItem> itemList = getExtraItems(foodMeetingId, connection);

		double itemTotalPrice = getTotalExternalItemPrice(itemList);
		double partialByOrder = itemTotalPrice > 0 ? itemTotalPrice / orders.size() : 0;

		FoodMeeting foodMeeting = getFoodMeeting(connection, idFoodMeeting);
		out.addResult("foodMeeting", foodMeeting);
		out.addResult("items", itemList);
		out.addResult("estate", isBuyer);
		out.addResult("id_food_meeting", foodMeetingId);
		out.addResult("total_item_price", itemTotalPrice);
		out.addResult("partialByOrder", partialByOrder);
		out.addResult("myUser", user);
		out.addResult("buyer", buyer);
		out.addResult("orders", orders);

		if (buyer == null) {
			return out.redirect("wheeldecide/wheel.jsp");
		} else {
			return out.forward("payment/payment.jsp");
		}
	}

	private FoodMeeting getFoodMeeting(Connection connection, String idFoodMeeting) throws SQLException {
		FoodMeetingManager foodMeetingManager = new FoodMeetingManager(connection);
		return foodMeetingManager.getFoodMeetingById(Integer.parseInt(idFoodMeeting));
	}

	private List<PaymentItem> getExtraItems(int foodMeetingId, Connection connection) throws Exception {
		List<PaymentItem> resultItemsList = new ArrayList<>();
		ItemPaymentManager itemManager = new ItemPaymentManager(connection);

		try {
			resultItemsList = itemManager.getPaymentItemByFoodMeeting(foodMeetingId);
		} catch (SQLException ex) {
			Logger.getLogger(PaymentCommand.class.getName()).log(Level.SEVERE, null, ex);
			throw new Exception("Throw a problem when we wont work with payments table : " + ex.getMessage());
		}

		return resultItemsList;
	}

	private double getTotalExternalItemPrice(List<PaymentItem> itemList) {
		double resp = 0;

		for (PaymentItem paymentItem : itemList) {
			resp += paymentItem.getPrice();
		}

		return resp;
	}

	private List<Order> getOrders(Connection connection, String idFoodMeeting) throws SQLException {
		OrderManager orderManager = new OrderManager(connection);
		return orderManager.getOrdersWithUserByFoodMeeting(Integer.parseInt(idFoodMeeting));
	}

	private Order extractMyOrder(int idUser, List<Order> orders) {
		Order myOrder = null;

		for (Order order : orders) {
			if (order.getIdUser() == idUser) {
				myOrder = order;
				break;
			}
		}

		orders.remove(myOrder);
		return myOrder;
	}

}
