using Org.BouncyCastle.Bcpg;
using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using TESTRESTRO;

namespace RESTRODBACCESS.Helper
{
    public class User
    {
        public UserRegisterResponseModel register(string email, string password, string fname, string lname, string phone, int userTypeId, out ErrorModel errorModel)
        {
            errorModel = null;
            UserRegisterResponseModel userRegisterResponseModel = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_RegisterUser, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters
                    command.Parameters.Add(new SqlParameter("@email", System.Data.SqlDbType.VarChar, 150));
                    command.Parameters["@email"].Value = email;

                    command.Parameters.Add(new SqlParameter("@fname", System.Data.SqlDbType.VarChar, 50));
                    command.Parameters["@fname"].Value = fname;

                    command.Parameters.Add(new SqlParameter("@lname", System.Data.SqlDbType.VarChar, 50));
                    command.Parameters["@lname"].Value = lname;

                    command.Parameters.Add(new SqlParameter("@password", System.Data.SqlDbType.VarChar, 64));
                    command.Parameters["@password"].Value = password;

                    command.Parameters.Add(new SqlParameter("@phone", System.Data.SqlDbType.VarChar, 10));
                    command.Parameters["@phone"].Value = phone;

                    command.Parameters.Add(new SqlParameter("@userTypeId", System.Data.SqlDbType.Int));
                    command.Parameters["@userTypeId"].Value = userTypeId;

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
                            userRegisterResponseModel = new UserRegisterResponseModel();
                            userRegisterResponseModel.Email = reader["email"].ToString();
                            userRegisterResponseModel.UserId = reader["userId"].ToString();
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return userRegisterResponseModel;
            }
            finally
            {
                if (connection != null)
                {
                    connection.Close();
                }
            }
        }


        public List<UsersResponseModel> getUsers(int userType,  /*string email,*/ out ErrorModel errorModel)
        {
            errorModel = null;
            List<UsersResponseModel> users = null;
            SqlConnection connection = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_getUsers, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    #region Query Parameters


                    command.Parameters.Add(new SqlParameter("@userTypeId", System.Data.SqlDbType.Int));
                    command.Parameters["@userTypeId"].Value = userType;
                    
                    #endregion
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    users = new List<UsersResponseModel>();
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
                            UsersResponseModel user = new UsersResponseModel();
                            user.UserId = Convert.ToInt32(reader["Id"].ToString());
                            user.FirstName = reader["FirstName"].ToString();
                            user.LastName = reader["LastName"].ToString();
                            user.Email = reader["Email"].ToString();
                            user.Phone = reader["Phone"].ToString();
                            user.Image = reader["Image"].ToString();
                            user.UserType = Convert.ToInt32(reader["UserType"].ToString());
                            user.Gender = reader["Gender"].ToString();
                            user.UserTypeTitle = reader["UserTypeTitle"].ToString();



                            users.Add(user);
                        }
                    }
                    command.Dispose();
                    connection.Close();
                }

                return users;
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

        public EditProfileResponseModel editProfile(EditProfileRequestModel editProfileRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            EditProfileResponseModel editProfile = null;
            SqlConnection connection = null;
            try
            {
                using(connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand(SqlCommands.SP_editProfile, connection);
                    command.CommandType = System.Data.CommandType.StoredProcedure;
                    #region command Parameters
                    command.Parameters.AddWithValue("userId", editProfileRequestModel.userId);
                    command.Parameters.AddWithValue("firstName", editProfileRequestModel.firstName);
                    command.Parameters.AddWithValue("lastName", editProfileRequestModel.lastName);
                    command.Parameters.AddWithValue("email", editProfileRequestModel.email);
                    command.Parameters.AddWithValue("gender", editProfileRequestModel.gender);
                    command.Parameters.AddWithValue("phone", editProfileRequestModel.phone);
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
                            editProfile = new EditProfileResponseModel();
                            editProfile.firstName = reader["FirstName"].ToString();
                            editProfile.userId = Convert.ToInt32(reader["userId"].ToString());
                            editProfile.gender = reader["gender"].ToString();
                            editProfile.email = reader["email"].ToString();
                            editProfile.phone = reader["phoneNo"].ToString();
                            editProfile.lastName = reader["lastName"].ToString();
                        }
                    }
                }
                return editProfile;
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

        public ChangeOrderStatusResponseModel changePassword(ChangePasswordRequestModel changePasswordRequest, out ErrorModel errorModel)
        {
            SqlConnection connection = null;
            errorModel = null;
            try
            {
                using (connection = new SqlConnection(Database.getConnectionString()))
                {
                    SqlCommand command = new SqlCommand("", connection);
                    command.CommandText = "update users set password = CONVERT(varchar(64), HASHBYTES('SHA2_256', '" + changePasswordRequest.password + "') ,2) where userId = " + changePasswordRequest.userId;
                    connection.Open();
                    int reader = command.ExecuteNonQuery();
                    if (reader != 0)
                    {
                        ChangeOrderStatusResponseModel changeOrderStatusResponseModel = new ChangeOrderStatusResponseModel();
                        changeOrderStatusResponseModel.StatusCode = "1";
                        changeOrderStatusResponseModel.StatusMessage = "Password changed Successfully";
                        return changeOrderStatusResponseModel;
                    }
                    else
                    {
                        ChangeOrderStatusResponseModel changeOrderStatusResponseModel = new ChangeOrderStatusResponseModel();
                        changeOrderStatusResponseModel.StatusCode = "0";
                        changeOrderStatusResponseModel.StatusMessage = "Not Succesfull updated";
                        return changeOrderStatusResponseModel;
                    }
                }
                return null;
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
