using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using TESTRESTRO;

namespace RESTRODBACCESS.Interface
{
    interface IAuth
    {
        UserLoginResponseModel login(UserLoginRequestModel userLoginRequestModel, out ErrorModel errorModel);


        UsersResponseModel isAuthorized(string token, out ErrorModel errorModel);
    }
}
