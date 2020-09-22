using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TESTRESTRO;

namespace RESTRODBACCESS.Helper
{
    public class Order
    {
        public OrderCartItemResponseModel addOrder(OrderCartItemRequestModel orderCartItemRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            OrderCartItemResponseModel orderCartItemResponseModel = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_orderCartItem, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    #region Command Parameters
                    command.Parameters.AddWithValue("orderBy", orderCartItemRequestModel.orderBy);
                    command.Parameters.AddWithValue("isDiningIn", orderCartItemRequestModel.isDiningIn);
                    command.Parameters.AddWithValue("isCardPayment", orderCartItemRequestModel.isCardPayment);
                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        if (reader.isColumnExists("ErrorCode"))
                        {
                            errorModel = new ErrorModel();
                            errorModel.ErrorCode = reader["ErrorCode"].ToString();
                            errorModel.ErrorMessage = reader["ErrorMessage"].ToString();
                        }
                        else
                        {
                            orderCartItemResponseModel = new OrderCartItemResponseModel();
                            orderCartItemResponseModel.billId = Convert.ToInt32(reader["billId"].ToString());
                            orderCartItemResponseModel.billDate = reader["billDate"].ToString();
                            orderCartItemResponseModel.billAmount = Convert.ToDecimal(reader["billAmount"].ToString());
                        }
                    }
                    command.Dispose();
                    return orderCartItemResponseModel;
                }
            }
            catch (Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return null;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }

        public List<GetOrderHistoryResponseModel> getOrdersHistory(string startDate, string endDate, out ErrorModel errorModel)
        {
            errorModel = null;
            List<GetOrderHistoryResponseModel> list;
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    string query = "Select Orders.orderId, Orders.reservationId, Orders.orderDate, Orders.isDiningIn, OrderHistory.statusId as currentStatusId, OrderStatus.orderStatusTitle, Users.userId, Users.email as reservedBy, count(itemId) as totalItems from Orders"+
                                    " Inner join OrderHistory on OrderHistory.orderId = Orders.orderId"+
                                    " inner join OrderDetails on OrderDetails.orderId = OrderHistory.orderId"+
                                    " inner join OrderStatus on OrderStatus.orderStatusId = OrderHistory.statusId"+
                                    " inner join Reservation on Reservation.reservationId = Orders.reservationId"+
                                    " inner join Users on Reservation.reservedBy = Users.userId"+
                                    " where Orders.orderDate between '"+startDate+"' and '"+endDate+"'"+
                                    "group by Orders.orderId, Orders.reservationId, Orders.orderDate, Orders.isDiningIn, OrderHistory.statusId, OrderStatus.orderStatusTitle, Users.userId, Users.email";
                    SqlCommand sqlCommand = new SqlCommand(query, connection);
                    connection.Open();
                    SqlDataReader reader = sqlCommand.ExecuteReader();
                    list = new List<GetOrderHistoryResponseModel>();
                    while (reader.Read())
                    {
                        GetOrderHistoryResponseModel getOrderHistoryResponse = new GetOrderHistoryResponseModel();
                        getOrderHistoryResponse.orderId = Convert.ToInt32(reader["orderId"].ToString());
                        getOrderHistoryResponse.userId = Convert.ToInt32(reader["userId"].ToString());
                        getOrderHistoryResponse.totalItems = Convert.ToInt32(reader["totalItems"].ToString());
                        getOrderHistoryResponse.currentStatusId = Convert.ToInt32(reader["currentStatusId"].ToString());
                        getOrderHistoryResponse.isDineIn = Convert.ToBoolean(reader["isDiningIn"].ToString());
                        getOrderHistoryResponse.orderDate = reader["orderDate"].ToString();
                        getOrderHistoryResponse.reservationId = Convert.ToInt32(reader["reservationId"].ToString());
                        getOrderHistoryResponse.reservedBy = reader["reservedBy"].ToString();
                        getOrderHistoryResponse.orderStatusTitle = reader["orderStatusTitle"].ToString();
                        list.Add(getOrderHistoryResponse);
                    }
                    sqlCommand.Dispose();
                    return list;
                }
            }
            catch(Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return null;
            }
            finally
            {
                if(connection != null)
                {
                    connection.Close();
                }
            }
        }


        public List<GetOrdersResponseModel> getOrders(GetOrdersRequestModel getOrdersRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            List<GetOrdersResponseModel> orders = null;
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getOrders, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    command.Parameters.AddWithValue("chefOrCashier", getOrdersRequestModel.chefOrCashier);
                    command.Parameters.AddWithValue("customer_id", getOrdersRequestModel.customerId);
                    command.Parameters.AddWithValue("fromDate", getOrdersRequestModel.fromDate);
                    command.Parameters.AddWithValue("toDate", getOrdersRequestModel.toDate);
                    command.Parameters.AddWithValue("email", getOrdersRequestModel.email);
                    command.Parameters.AddWithValue("needUnpaidOnly", getOrdersRequestModel.needUnpaidOnly);
                    connection.Open();
                    orders = new List<GetOrdersResponseModel>();
                    SqlDataReader reader = command.ExecuteReader();
                    GetOrdersResponseModel getOrdersResponseModel = null;
                    int orderIdCheck = 0;
                    while (reader.Read())
                    {
                        MenuItems menuItems = new MenuItems();
                        if(orderIdCheck != Convert.ToInt32(reader["orderId"].ToString()))
                        {
                            if(orderIdCheck != 0)
                            {
                                orders.Add(getOrdersResponseModel);
                            }
                            getOrdersResponseModel = new GetOrdersResponseModel();
                            getOrdersResponseModel.menuItems = new List<MenuItems>();
                            getOrdersResponseModel.orderId = Convert.ToInt32(reader["orderId"].ToString());
                            getOrdersResponseModel.orderDate = reader["orderDate"].ToString();
                            getOrdersResponseModel.isDiningIn = Convert.ToBoolean(reader["isDiningIn"].ToString());
                            getOrdersResponseModel.orderStatusTitle = reader["orderStatusTitle"].ToString();
                            getOrdersResponseModel.billingAmount = Convert.ToDouble(reader["billingAmount"].ToString());
                            getOrdersResponseModel.isCardPayment = Convert.ToBoolean(reader["isCardPayment"].ToString());
                            getOrdersResponseModel.firstName = reader["FirstName"].ToString();
                            getOrdersResponseModel.lastName = reader["lastName"].ToString();
                            getOrdersResponseModel.statusId = Convert.ToInt32(reader["orderStatusId"].ToString());
                            getOrdersResponseModel.isPaid = Convert.ToBoolean(reader["isPaid"].ToString());
                            getOrdersResponseModel.billId = Convert.ToInt32(reader["billId"].ToString());
                            getOrdersResponseModel.GST = Convert.ToDecimal(reader["gst"].ToString());
                            getOrdersResponseModel.PST = Convert.ToDecimal(reader["pst"].ToString());
                            getOrdersResponseModel.totalAfterTax = Convert.ToDecimal(reader["totalAfterTax"].ToString());
                            getOrdersResponseModel.tip = Convert.ToDecimal(reader["tip"].ToString());
                            getOrdersResponseModel.isReadyToPay = Convert.ToBoolean(reader["isReadyToPay"].ToString());
                            int temp = 0;
                            int.TryParse(reader["tableId"].ToString(), out temp);
                            if (temp != 0)
                            {
                                getOrdersResponseModel.tableId = Convert.ToInt32(reader["tableId"].ToString());
                            }
                            orderIdCheck = getOrdersResponseModel.orderId;
                        }
                        menuItems.itemName = reader["itemName"].ToString();
                        menuItems.itemQty = Convert.ToInt32(reader["itemQty"].ToString());
                        menuItems.itemDescription = reader["itemDescription"].ToString();
                        menuItems.itemPrice = Convert.ToDouble(reader["price"].ToString());
                        menuItems.category = reader["categoryTitle"].ToString();
                        getOrdersResponseModel.menuItems.Add(menuItems);
                    }
                    if(getOrdersResponseModel != null)
                    {
                        orders.Add(getOrdersResponseModel);
                    }
                    command.Dispose();
                }
                return orders;
            }
            catch(Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return null;
            }
            finally
            {
                if(connection != null)
                {
                    connection.Close();
                }
            }
        }

        public ChangeOrderStatusResponseModel changeOrderStatus(ChangeOrdersStatusRequestModel changeOrdersStatusRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            ChangeOrderStatusResponseModel changeOrderStatusResponse= new ChangeOrderStatusResponseModel();
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_updateOrderStatus, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    command.Parameters.AddWithValue("orderId", changeOrdersStatusRequestModel.orderId);
                    command.Parameters.AddWithValue("orderStatus", changeOrdersStatusRequestModel.orderStatus);
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        changeOrderStatusResponse.StatusCode = reader["StatusCode"].ToString();
                        changeOrderStatusResponse.StatusMessage = reader["StatusMessage"].ToString();
                    }
                    command.Dispose();
                    return changeOrderStatusResponse;
                }
            }
            catch(Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return null;
            }
            finally
            {
                if(connection != null)
                {
                    connection.Close();
                }
            }
        }

        public ChangeOrderStatusResponseModel changePaymentStatus(ChangePaymentStatusRequestModel changePaymentStatusRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            ChangeOrderStatusResponseModel changeOrderStatusResponse = new ChangeOrderStatusResponseModel();
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_changePaymentStatus, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    command.Parameters.AddWithValue("billId", changePaymentStatusRequest.billId);
                  
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        changeOrderStatusResponse.StatusCode = reader["StatusCode"].ToString();
                        changeOrderStatusResponse.StatusMessage = reader["StatusMessage"].ToString();
                    }
                    command.Dispose();
                    return changeOrderStatusResponse;
                }
            }
            catch (Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return null;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }


        public ChangeOrderStatusResponseModel readyToPay(ReadyForPaymentRequestModel readyForPaymentRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            ChangeOrderStatusResponseModel changeOrderStatusResponse = new ChangeOrderStatusResponseModel();
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_readyToPay, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    command.Parameters.AddWithValue("orderId", readyForPaymentRequest.orderId);
                    command.Parameters.AddWithValue("tip", readyForPaymentRequest.tip);
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        changeOrderStatusResponse.StatusCode = reader["StatusCode"].ToString();
                        changeOrderStatusResponse.StatusMessage = reader["StatusMessage"].ToString();
                    }
                    command.Dispose();
                    return changeOrderStatusResponse;
                }
            }
            catch (Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return null;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }


        public List<GetReadyForPayment> getReadyForPayment(out ErrorModel errorModel)
        {
            errorModel = null;
            List<GetReadyForPayment> readyForPay = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getreadyforPayment, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters


                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    readyForPay = new List<GetReadyForPayment>();
                    while (reader.Read())
                    {
                        if (reader.isColumnExists("ErrorCode"))
                        {
                            errorModel = new ErrorModel();
                            errorModel.ErrorCode = reader["ErrorCode"].ToString();
                            errorModel.ErrorMessage = reader["ErrorMessage"].ToString();
                        }
                        else
                        {
                            GetReadyForPayment getReadyResponse = new GetReadyForPayment();

                            getReadyResponse.billId = Convert.ToInt32(reader["billId"].ToString());
                            getReadyResponse.billingAmount=Convert.ToDouble(reader["billingAmount"].ToString());

                            getReadyResponse.billDate= reader["billDate"].ToString(); 
                            getReadyResponse.orderId= Convert.ToInt32(reader["orderId"].ToString()); 
                            getReadyResponse.isPaid= Convert.ToBoolean(reader["isPaid"].ToString()); 
                            getReadyResponse.isReadyToPay= Convert.ToBoolean(reader["isReadyToPay"].ToString()); 
                            getReadyResponse.icCardPayment= Convert.ToBoolean(reader["isCardPayment"].ToString());
                            getReadyResponse.FirstName = reader["FirstName"].ToString();
                            getReadyResponse.lastName=reader["lastName"].ToString();
                            if(string.IsNullOrEmpty(reader["tableID"].ToString()))
                            {
                                getReadyResponse.tableID = 0;
                            }
                            else
                            {
                                getReadyResponse.tableID= Convert.ToInt32(reader["tableID"].ToString());
                            }
                           // getReadyResponse.tableID= reader["tableID"].ToString().Equals(DBNull.Value) ? 0 : Convert.ToInt32(reader["tableID"].ToString());
                           
                            
                            readyForPay.Add(getReadyResponse);
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return readyForPay;
            }
            catch (Exception exception)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = exception.Message;
                return null;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }
    }


}
