using RESTRODBACCESS.RequestModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;

namespace TESTRESTRO.Controllers
{
    public class CartController : ApiController
    {
        [HttpPost]
        [Route("api/cart/addToCart")]
        public HttpResponseMessage addToCart(AddToCartRequestModel addToCartRequestModel)
        {
            CartProvider cartProvider = new CartProvider();
            ErrorModel errorModel = null;
            var cartStatus = cartProvider.addToCart(addToCartRequestModel, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = cartStatus;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }


        [HttpGet]
        [Route("api/cart/cartItems/{userId:int?}")]
        //[ApiAuthorization]
        public HttpResponseMessage getCartItems(int userId)
        {
            CartProvider cartProvider = new CartProvider();
            ErrorModel errorModel = null;

            var cartItems = cartProvider.getCartItems(userId, out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = cartItems;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpPost]
        [Route("api/cart/deleteorModifyCartItems")]
        public HttpResponseMessage deleteorModifyCartItems(int cartId,int quantity,bool isDelete)
        {
            CartProvider cartProvider = new CartProvider();
            ErrorModel errorModel = null;
            var deleteorModifyCartItems = cartProvider.deleteorModifyCartItems(cartId,quantity,isDelete, out errorModel);

            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = deleteorModifyCartItems;
            aPIResponseModel.Error = errorModel;

            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }
    }
}
