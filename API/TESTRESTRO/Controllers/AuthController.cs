using RESTRODBACCESS.RequestModel;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;

namespace TESTRESTRO.Controllers
{
    public class AuthController : ApiController
    {
        [HttpPost]
        [Route("api/auth/login")]
        public HttpResponseMessage login(UserLoginRequestModel userLoginRequestModel)
        {
            AuthProvider authProvider = new AuthProvider();
            ErrorModel errorModel = new ErrorModel();

            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = authProvider.login(userLoginRequestModel, out errorModel);
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }
    }
}
