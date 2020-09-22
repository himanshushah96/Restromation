
using RESTRODBACCESS.Helper;
using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;

namespace TESTRESTRO.Provider
{
    public class UserProvider
    {
        public UserRegisterResponseModel register(UserRegisterRequestModel userRegister, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                User userProvider = new User();
                UserRegisterResponseModel userRegisterResponse = userProvider.register(userRegister.Email, userRegister.Password, userRegister.FirstName, userRegister.lastName, userRegister.Phone, userRegister.UserType, out errorModel);
                return userRegisterResponse;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        public List<UsersResponseModel> getAll(int userType, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                User userHelper = new User();
                List<UsersResponseModel> users = userHelper.getUsers(userType, /*usersRequestModel.email,*/ out errorModel);
                return users;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public EditProfileResponseModel editProfile(EditProfileRequestModel editProfileRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            User user = new User();
            try
            {
                return user.editProfile(editProfileRequestModel, out errorModel);
            }
            catch
            {
                return null;
            }
        }

        public ChangeOrderStatusResponseModel changePassword(ChangePasswordRequestModel changePasswordRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            User user = new User();
            try
            {
                return user.changePassword(changePasswordRequest, out errorModel);
            }
            catch
            {
                return null;
            }
        }
    }
}