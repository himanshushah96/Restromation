using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TESTRESTRO;

namespace RESTRODBACCESS.Helper
{
    public class Notification
    {
        public CustomerNotificationResponseModel getNotificationsDataCustomer(int customerId, int capacity, out ErrorModel errorModel)
        {
            CustomerNotificationResponseModel customerNotificationResponseModel = null;
            errorModel = null;
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    customerNotificationResponseModel = new CustomerNotificationResponseModel();
                    customerNotificationResponseModel.orderChange = new List<OrderStatusChangeNotification>();
                    SqlCommand command = new SqlCommand(SqlCommands.SP_customerNotification, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    #region parameters
                    command.Parameters.AddWithValue("capacity", capacity);
                    command.Parameters.AddWithValue("customerId", customerId);
                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        customerNotificationResponseModel.istableAvailable = Convert.ToBoolean(reader["isTableAvailable"].ToString());
                        if (reader.isColumnExists("orderId")) {
                            OrderStatusChangeNotification temp = new OrderStatusChangeNotification();
                            temp.orderId = Convert.ToInt32(reader["orderId"].ToString());
                            temp.statusId = Convert.ToInt32(reader["orderStatusId"].ToString());
                            temp.orderStatusTitle = reader["orderStatusTitle"].ToString();
                            customerNotificationResponseModel.orderChange.Add(temp);
                        }
                        
                    }
                    connection.Close();
                    command.Dispose();
                }
               
                return customerNotificationResponseModel;
            }
            catch(Exception e)
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
    }
}
