using RESTRODBACCESS.Helper;
using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TESTRESTRO.Provider
{
    public class CartProvider
    {
        public AddToCartResponseModel addToCart(AddToCartRequestModel addToCartRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Cart cartHelper = new Cart();
                return cartHelper.addToCart(addToCartRequestModel, out errorModel);
            }
            catch (Exception)
            {
                return null;
            }
        }

        public List<GetCartItemsResponseModel> getCartItems(int userId, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Cart cartHelper = new Cart();
                List<GetCartItemsResponseModel> cartItems = cartHelper.getCartItems(userId, out errorModel);
                return cartItems;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public AddToCartResponseModel deleteorModifyCartItems(int cartId,int quantity,bool isDelete, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Cart cartHelper = new Cart();
                return cartHelper.deleteorModifyCartItems(cartId,quantity,isDelete, out errorModel);
            }
            catch (Exception)
            {
                return null;
            }
        }
    }
}