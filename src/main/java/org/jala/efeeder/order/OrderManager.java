package org.jala.efeeder.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jala.efeeder.user.User;
import org.jala.efeeder.user.UserManager;

/**
 *
 * @author amir_aranibar
 *
 */
public class OrderManager {

    private static final String SELECT_ORDER = "SELECT id_food_meeting, id_user, order_name, Cost, payment FROM orders";
    private static final String USERS_QUERY = "SELECT id, name, last_name, email FROM user WHERE id=%d";
    private static final String MY_ORDER_QUERY = SELECT_ORDER + " WHERE id_food_meeting=? AND id_user=?;";
    private static final String ORDERS_BY_FOOD_MEETING_QUERY = SELECT_ORDER + " WHERE id_food_meeting=?;";
    private static final String INSERT_ORDER = "INSERT INTO orders(order_name, cost, id_food_meeting, id_user) VALUES(?, ?, ?, ?);";
    private static final String UPDATE_ORDER = "UPDATE orders SET order_name=?, cost=? WHERE id_food_meeting=? AND id_user=?;";
    private static final String UPDATE_PAYMENT = "UPDATE orders SET payment=? WHERE id_food_meeting=? AND id_user=?;";

    private final Connection connection;

    public OrderManager(Connection connection) {
        this.connection = connection;
    }

    public List<Order> getOrdersWithUserByFoodMeeting(int idFoodMeeting) throws SQLException {
        List<Order> orders = getOrdersByFoodMeeting(idFoodMeeting);
        List<Integer> idUsers = new ArrayList<>();

        orders.stream().forEach((order) -> {
            idUsers.add(order.getIdUser());
        });

        joinUserToOrder(orders, idUsers);

        return orders;
    }

    public List<Order> getOrdersByFoodMeeting(int idFoodMeeting) throws SQLException {
        List<Order> orders = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(ORDERS_BY_FOOD_MEETING_QUERY);
        preparedStatement.setInt(1, idFoodMeeting);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int idUser = resultSet.getInt(2);
            int userId = resultSet.getInt(2);
            PreparedStatement preparedUserStatement = connection.prepareStatement(String.format(USERS_QUERY, userId));
            ResultSet userResultSet = preparedUserStatement.executeQuery();
            User user = null;
            if (userResultSet.next()) {
                user = new User(userResultSet.getInt(1), userResultSet.getString(4), userResultSet.getString(2), userResultSet.getString(2));
            }
            Order order = new Order(resultSet.getInt(1), idUser, resultSet.getString(3), resultSet.getDouble(4), resultSet.getDouble(5));
            if (user != null) {
                order.setUser(user);
            }
            orders.add(order);
        }

        return orders;
    }

    public Order getMyOrder(int idUser, int idFoodMeeting) throws SQLException {
        Order myOrder = null;
        PreparedStatement preparedStatement = connection.prepareStatement(MY_ORDER_QUERY);
        preparedStatement.setInt(1, idFoodMeeting);
        preparedStatement.setInt(2, idUser);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            myOrder = new Order(idFoodMeeting, idUser, resultSet.getString(3), resultSet.getDouble(4), resultSet.getDouble(5));
        }

        return myOrder;
    }

    public void insertOrder(int idFoodMeeting, int idUser, String details, double cost) throws SQLException {
        executeUpdateOrder(idFoodMeeting, idUser, details, cost, INSERT_ORDER);
    }

    public void updateOrder(int idFoodMeeting, int idUser, String details, double cost) throws SQLException {
        executeUpdateOrder(idFoodMeeting, idUser, details, cost, UPDATE_ORDER);
    }
    
    public void updatePayment(int idFoodMeeting, int idUser, double payment) throws SQLException {
        executeUpdatePayment(idFoodMeeting, idUser, payment, UPDATE_PAYMENT);
    }

    private void executeUpdateOrder(int idFoodMeeting, int idUser, String details, double cost, String query)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, details);
        preparedStatement.setDouble(2, cost);
        preparedStatement.setInt(3, idFoodMeeting);
        preparedStatement.setInt(4, idUser);

        preparedStatement.executeUpdate();
    }

    private void executeUpdatePayment(int idFoodMeeting, int idUser, double payment, String query)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setDouble(1, payment);
        preparedStatement.setInt(2, idFoodMeeting);
        preparedStatement.setInt(3, idUser);

        preparedStatement.executeUpdate();
    }
    
    private void joinUserToOrder(List<Order> orders, List<Integer> idUsers) throws SQLException {
        UserManager userManager = new UserManager(connection);
        List<User> users = userManager.getUsersById(idUsers);

        orders.stream().forEach((order) -> {
            Optional<User> result = users.stream()
                    .filter(user -> order.getIdUser() == user.getId()).findFirst();
            if (result.isPresent()) {
                order.setUser(result.get());
                users.remove(result.get());
            }
        });
    }
}
