using RESTRODBACCESS.RequestModel;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;


namespace TESTRESTRO.Controllers
{
    public class UsersController : ApiController
    {

        [HttpGet]
        [Route("api/users/{userType:int?}")]
        //[ApiAuthorization]
        public HttpResponseMessage getAll(/*[FromUri]UsersRequestModel usersRequestModel*/int userType=0)
        {
            UserProvider userProvider = new UserProvider();
            ErrorModel errorModel = null;
            /*if (usersRequestModel == null)
            {
                usersRequestModel = new UsersRequestModel();
            }*/
            var users = userProvider.getAll(userType, out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = users;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }


     

        [HttpPost]
        [Route("api/users/add")]

        public HttpResponseMessage Register(UserRegisterRequestModel userRegisterModel)
        {
            ErrorModel errorModel = new ErrorModel();
            APIResponseModel responseModel = new APIResponseModel();
            if (userRegisterModel != null)
            {
                UserProvider userProvider = new UserProvider();
                responseModel.Response = userProvider.register(userRegisterModel, out errorModel);
                responseModel.Error = errorModel;
                return Request.CreateResponse(HttpStatusCode.OK, responseModel);
            }

            responseModel.Error = ErrorCode.BadRequest;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpPost]
        [Route("api/users/editProfile")]
        public HttpResponseMessage editProfile(EditProfileRequestModel editProfileRequestModel)
        {
            ErrorModel errorModel = null;
            UserProvider userProvider = new UserProvider();
            var result = userProvider.editProfile(editProfileRequestModel, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = result;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpPost]
        [Route("api/users/changePassword")]
        public HttpResponseMessage changePassword(ChangePasswordRequestModel changePasswordRequest)
        {
            ErrorModel errorModel = null;
            UserProvider userProvider = new UserProvider();
            var result = userProvider.changePassword(changePasswordRequest, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = result;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }
    }

}
