using RESTRODBACCESS.Helper;
using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;

namespace TESTRESTRO.Provider
{
    public class AuthProvider
    {


        public UserLoginResponseModel login(UserLoginRequestModel userLoginRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Auth authHelper = new Auth();
                UserLoginResponseModel loginResponseModel = authHelper.login(userLoginRequestModel, out errorModel);

                return loginResponseModel;
            }
            catch (Exception)
            {
                return null;
            }
        }
    }
}