using RESTRODBACCESS.RequestModel;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;

namespace TESTRESTRO.Controllers
{
    public class MenuController : ApiController
    {
        [HttpGet]
        [Route("api/menu/{categoryId:int?}")]
        //[ApiAuthorization]
        public HttpResponseMessage getAllMenuItems(int categoryId = 0)
        {
            MenuProvider menuProvider = new MenuProvider();
            ErrorModel errorModel = null;
            
            var menuItems = menuProvider.getAllMenuItems(categoryId, out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = menuItems;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpPost]
        [Route("api/menu/changePrice")]

        public HttpResponseMessage changePrice(MenuItemRequestModel menuItemRequestModel)
        {
            ErrorModel errorModel = new ErrorModel();
            APIResponseModel responseModel = new APIResponseModel();
            if (menuItemRequestModel != null)
            {
                MenuProvider menuProvider = new MenuProvider();
                responseModel.Response = menuProvider.changePrice(menuItemRequestModel, out errorModel);
                responseModel.Error = errorModel;
                return Request.CreateResponse(HttpStatusCode.OK, responseModel);
            }

            responseModel.Error = ErrorCode.BadRequest;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpPost]
        [Route("api/menu/addMenuItem")]

        public HttpResponseMessage addMenuItem(AddMenuItemRequestModel addMenuItemRequestModel)
        {
            ErrorModel errorModel = new ErrorModel();
            APIResponseModel responseModel = new APIResponseModel();
            if (addMenuItemRequestModel != null)
            {
                MenuProvider menuProvider = new MenuProvider();
                responseModel.Response = menuProvider.addMenuItem(addMenuItemRequestModel, out errorModel);
                responseModel.Error = errorModel;
                return Request.CreateResponse(HttpStatusCode.OK, responseModel);
            }

            responseModel.Error = ErrorCode.BadRequest;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpGet]
        [Route("api/menu/getCategory")]
        public HttpResponseMessage getCategory()
        {
            MenuProvider menuProvider = new MenuProvider();
            ErrorModel errorModel = null;

            var menuItems = menuProvider.getCategory(out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = menuItems;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        




    }
}
