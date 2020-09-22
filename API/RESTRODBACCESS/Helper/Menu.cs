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
    public class Menu
    {
        public List<MenuItemResponseModel> getMenuItems(int categoryId, out ErrorModel errorModel)
        {
            errorModel = null;
            List<MenuItemResponseModel> menuItems = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getMenuItems, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters

                   
                    
                    command.Parameters.Add(new SqlParameter("@categoryId", System.Data.SqlDbType.Int));
                    command.Parameters["@categoryId"].Value = categoryId;

                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    menuItems = new List<MenuItemResponseModel>();
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
                            MenuItemResponseModel menuItemResponse = new MenuItemResponseModel();
                            menuItemResponse.menuItemId = Convert.ToInt32(reader["ItemId"].ToString());
                            menuItemResponse.menuItemName = reader["ItemName"].ToString();
                            menuItemResponse.menuItemDescription = reader["Description"].ToString();
                            menuItemResponse.price = Convert.ToDouble(reader["Price"].ToString());
                            menuItemResponse.availablequantity = Convert.ToInt32(reader["QuantityAvailable"].ToString());
                            menuItemResponse.itemStatus = Convert.ToBoolean(reader["ItemStatus"].ToString());
                            menuItemResponse.categoryId = Convert.ToInt32(reader["CategoryId"].ToString());
                            menuItemResponse.userId = Convert.ToInt32(reader["CreatedById"].ToString());
                            menuItemResponse.updatedBy = reader["UpdatedBy"].ToString();
                            menuItemResponse.deletedBy = reader["DeletedBy"].ToString();
                            menuItemResponse.createdBy = reader["CreatedBy"].ToString();
                            menuItemResponse.categoryTitle = reader["CategoryTitle"].ToString();
                            menuItemResponse.itemImage = reader["ItemImage"].ToString();

                            menuItems.Add(menuItemResponse);
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return menuItems;
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


        public List<MenuItemResponseModel> getCategory(out ErrorModel errorModel)
        {
            errorModel = null;
            List<MenuItemResponseModel> menuItems = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getCategory, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters


                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    menuItems = new List<MenuItemResponseModel>();
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
                            MenuItemResponseModel menuItemResponse = new MenuItemResponseModel();
                           
                            menuItemResponse.categoryId = Convert.ToInt32(reader["CategoryId"].ToString());
                            
                          
                            menuItemResponse.categoryTitle = reader["CategoryTitle"].ToString();
                        

                            menuItems.Add(menuItemResponse);
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return menuItems;
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

        public MenuItemResponseModel changePrice(MenuItemRequestModel menuItemRequestModel , out ErrorModel errorModel)
        {
            errorModel = null;
            MenuItemResponseModel menuItemResponseModel = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_changePrice, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters

                    command.Parameters.Add(new SqlParameter("@isDelete", System.Data.SqlDbType.Bit));
                    command.Parameters["@isDelete"].Value = menuItemRequestModel.isDelete;

                    command.Parameters.Add(new SqlParameter("@itemId", System.Data.SqlDbType.Int));
                    command.Parameters["@itemId"].Value = menuItemRequestModel.itemId;

                    command.Parameters.Add(new SqlParameter("@price", System.Data.SqlDbType.Decimal,4));
                    command.Parameters["@price"].Value = menuItemRequestModel.price;

                    command.Parameters.Add(new SqlParameter("@updatedBy", System.Data.SqlDbType.Int));
                    command.Parameters["@updatedBy"].Value = menuItemRequestModel.updatedBy;
                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
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
                            menuItemResponseModel = new MenuItemResponseModel();
                            menuItemResponseModel.menuItemId = Convert.ToInt32(reader["menuItemId"].ToString());
                            menuItemResponseModel.menuItemName = reader["menuItemName"].ToString();
                            menuItemResponseModel.price = Convert.ToDouble(reader["price"].ToString());
                            
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return menuItemResponseModel;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }


        public MenuItemResponseModel addMenuItem(int createdBy, string menuItemName, string menuItemDescription, double price, int categoryId, int availableQty,string itemImage,out ErrorModel errorModel)
        {
            errorModel = null;
            MenuItemResponseModel menuItemResponse = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_addMenuItem, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters
                    command.Parameters.Add(new SqlParameter("@itemName", System.Data.SqlDbType.VarChar,30));
                    command.Parameters["@itemName"].Value = menuItemName;

                    command.Parameters.Add(new SqlParameter("@itemDescription", System.Data.SqlDbType.VarChar,150));
                    command.Parameters["@itemDescription"].Value = menuItemDescription;

                    command.Parameters.Add(new SqlParameter("@price", System.Data.SqlDbType.Decimal, 4));
                    command.Parameters["@price"].Value = price;

                    command.Parameters.Add(new SqlParameter("@categoryID", System.Data.SqlDbType.Int));
                    command.Parameters["@categoryID"].Value = categoryId;

                    command.Parameters.Add(new SqlParameter("@availableQty", System.Data.SqlDbType.Int));
                    command.Parameters["@availableQty"].Value = availableQty;

                    command.Parameters.Add(new SqlParameter("@createdBy", System.Data.SqlDbType.Int));
                    command.Parameters["@createdBy"].Value = createdBy;

                    command.Parameters.Add(new SqlParameter("@itemImage", System.Data.SqlDbType.VarChar));
                    command.Parameters["@itemImage"].Value = itemImage;
                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
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
                            menuItemResponse = new MenuItemResponseModel();
                            menuItemResponse.menuItemId = Convert.ToInt32(reader["ItemId"].ToString());
                            menuItemResponse.menuItemName = reader["ItemName"].ToString();
                            menuItemResponse.menuItemDescription = reader["Description"].ToString();
                            menuItemResponse.price = Convert.ToDouble(reader["Price"].ToString());
                            menuItemResponse.availablequantity = Convert.ToInt32(reader["QuantityAvailable"].ToString());
                            menuItemResponse.itemStatus = Convert.ToBoolean(reader["ItemStatus"].ToString());
                            menuItemResponse.categoryId = Convert.ToInt32(reader["CategoryId"].ToString());
                            menuItemResponse.categoryTitle = reader["CategoryTitle"].ToString();
                            menuItemResponse.userId =Convert.ToInt32(reader["CreatedById"].ToString());
                            menuItemResponse.itemImage = reader["itemImage"].ToString();

                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return menuItemResponse;
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
