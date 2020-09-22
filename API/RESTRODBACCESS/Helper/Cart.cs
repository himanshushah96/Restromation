using RESTRODBACCESS.RequestModel;
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
    public class Cart
    {
        public AddToCartResponseModel addToCart(AddToCartRequestModel addToCartRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            AddToCartResponseModel addToCartResponseModel = null;
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_addToCart, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    #region Command Parameters
                    command.Parameters.AddWithValue("itemId", addToCartRequestModel.itemId);
                    command.Parameters.AddWithValue("addedBy", addToCartRequestModel.addedby);
                    command.Parameters.AddWithValue("qty", addToCartRequestModel.quantity);
                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        addToCartResponseModel = new AddToCartResponseModel();
                        addToCartResponseModel.StatusCode = reader["StatusCode"].ToString();
                        addToCartResponseModel.StatusMessage = reader["StatusMessage"].ToString();
                    }
                    command.Dispose();
                    return addToCartResponseModel;
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
                if(connection != null)
                {
                    connection.Close();
                }
            }
        }


        public List<GetCartItemsResponseModel> getCartItems(int userId, out ErrorModel errorModel)
        {
            errorModel = null;
            List<GetCartItemsResponseModel> cartItems = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getCartItems, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters
                    command.Parameters.Add(new SqlParameter("@userId",System.Data.SqlDbType.Int));
                    command.Parameters["@userId"].Value = userId;
                    

                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    cartItems = new List<GetCartItemsResponseModel>();
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
                            GetCartItemsResponseModel getCartItemsResponse = new GetCartItemsResponseModel();
                            getCartItemsResponse.cartId = Convert.ToInt32(reader["cartId"].ToString());
                            getCartItemsResponse.menuItemId = Convert.ToInt32(reader["menuItemId"].ToString());
                            getCartItemsResponse.quantity = Convert.ToInt32(reader["quantity"].ToString());
                            getCartItemsResponse.addedBy = Convert.ToInt32(reader["addedBy"].ToString());
                            getCartItemsResponse.menuItemName = reader["itemName"].ToString();
                            getCartItemsResponse.menuItemDescription = reader["itemDescription"].ToString();

                            getCartItemsResponse.price = Convert.ToDouble(reader["Price"].ToString());
                            getCartItemsResponse.itemStatus = Convert.ToBoolean(reader["itemStatusTitle"].ToString());
                            getCartItemsResponse.itemImage = reader["itemImage"].ToString();
                            getCartItemsResponse.checkoutPrice = Convert.ToDouble(reader["TotalPrice"].ToString());

                            cartItems.Add(getCartItemsResponse);
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return cartItems;
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

        public AddToCartResponseModel deleteorModifyCartItems(int cartId,int quantity,bool isDelete, out ErrorModel errorModel)
        {
            errorModel = null;
            AddToCartResponseModel addToCartResponse = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_deleteorModifyCartItems, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Commands Parameters

                    command.Parameters.Add(new SqlParameter("@isDelete", System.Data.SqlDbType.Bit));
                    command.Parameters["@isDelete"].Value =isDelete;

                    command.Parameters.Add(new SqlParameter("@cartId", System.Data.SqlDbType.Int));
                    command.Parameters["@cartId"].Value =cartId;

                    command.Parameters.Add(new SqlParameter("@quantity", System.Data.SqlDbType.Int));
                    command.Parameters["@quantity"].Value = quantity;

                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();

                    addToCartResponse = new AddToCartResponseModel();
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
                            addToCartResponse.StatusCode = reader["StatusCode"].ToString();
                            addToCartResponse.StatusMessage = reader["StatusMessage"].ToString();
                            addToCartResponse.Total = !string.IsNullOrEmpty(reader["Total"].ToString()) ? Convert.ToDouble(reader["Total"].ToString()) : 0;
                            addToCartResponse.Quantity = reader["Quantity"].ToString();
                            addToCartResponse.ItemTotal = !string.IsNullOrEmpty(reader["itemTotal"].ToString()) ? Convert.ToDouble(reader["itemTotal"].ToString()) : 0;

                        }
                    }
                    command.Dispose();
                    return addToCartResponse;
                }
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
