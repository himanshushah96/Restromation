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
    public class Table
    {
        public List<GetTableResponseModel> getTables(int tableId, int capacity, out ErrorModel errorModel)
        {
            errorModel = null;
            List<GetTableResponseModel> tableItems = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getTables, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters
                    command.Parameters.Add(new SqlParameter("@tableId", System.Data.SqlDbType.Int));
                    command.Parameters["@tableId"].Value = tableId;
                    command.Parameters.AddWithValue("capacity", capacity);

                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    tableItems = new List<GetTableResponseModel>();
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
                            GetTableResponseModel getTableResponseModel = new GetTableResponseModel();
                            getTableResponseModel.tableId = Convert.ToInt32(reader["tableId"].ToString());
                            getTableResponseModel.capacity = Convert.ToInt32(reader["capacity"].ToString());
                            getTableResponseModel.availability = reader.GetBoolean(reader.GetOrdinal("availability"));
                          
                            tableItems.Add(getTableResponseModel);
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return tableItems;
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



        public ReserveTableResponseModel reserveTable(ReserveTableRequestModel reserveTableRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            ReserveTableResponseModel response = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_reserveTable, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters



                    command.Parameters.AddWithValue("@tableId", reserveTableRequestModel.tableId);

                    command.Parameters.AddWithValue("@reservationDate",  reserveTableRequestModel.reservationDate);

                    command.Parameters.AddWithValue("@reservedBy", reserveTableRequestModel.reservedBy);

                    command.Parameters.AddWithValue("@numberOfPeople", reserveTableRequestModel.numberOfPeople);

                    command.Parameters.AddWithValue("@startTime", reserveTableRequestModel.startTime);

                    command.Parameters.AddWithValue("endTime", reserveTableRequestModel.endTime);

                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    response = new ReserveTableResponseModel();
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
                            response.StatusCode = reader["StatusCode"].ToString();
                            response.StatusMessage = reader["StatusMessage"].ToString();
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return response;
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

        public GetTableResponseModel addTable(AddTableRequestModel addTableRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            GetTableResponseModel getTableResponseModel = null;
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_addTable, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Command Parameters
                    command.Parameters.AddWithValue("capacity", addTableRequestModel.capacity);
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
                            getTableResponseModel = new GetTableResponseModel();
                            getTableResponseModel.tableId = Convert.ToInt32(reader["tableId"]);
                            getTableResponseModel.capacity = Convert.ToInt32(reader["capacity"]);
                            getTableResponseModel.availability = Convert.ToBoolean(reader["availability"]);
                        }
                        command.Dispose();
                        return getTableResponseModel;
                    }
                    return null;
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
                if(connection != null)
                {
                    connection.Close();
                }
            }
        }

        public bool istableReserved(int clientId, out ErrorModel errorModel)
        {
            errorModel = null;
            bool isReserved = false;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand("", connection);
                    command.CommandType = System.Data.CommandType.Text;
                    command.CommandText = "Select Top 1 * from Reservation where reservedBy = " + clientId + " and (endTime = '' or endTime is null) and (numberOfPeople is not null  or numberOfPeople != 0)";
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        isReserved = true;
                    }
                    command.Dispose();
                    return isReserved;
                }
            }
            catch (Exception e)
            {
                errorModel = new ErrorModel();
                errorModel.ErrorMessage = e.Message;
                return false;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }

        public GetTableResponseModel deleteorModifyTable(TableDeleteRequestModel tableDeleteRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            GetTableResponseModel getTableResponse = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_deleteorModifyTable, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Commands Parameters

                    command.Parameters.Add(new SqlParameter("@isDelete", System.Data.SqlDbType.Bit));
                    command.Parameters["@isDelete"].Value = tableDeleteRequest.isDelete;

                    command.Parameters.Add(new SqlParameter("@tableId", System.Data.SqlDbType.Int));
                    command.Parameters["@tableId"].Value = tableDeleteRequest.tableId;

                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();

                    getTableResponse = new GetTableResponseModel();
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
                            getTableResponse.tableId = Convert.ToInt32(reader["TableId"].ToString());
                            getTableResponse.capacity = Convert.ToInt32(reader["Capacity"].ToString());
                            getTableResponse.availability = Convert.ToBoolean(reader["Table Active"].ToString());


                        }
                    }
                    command.Dispose();
                    return getTableResponse;
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
