using RESTRODBACCESS.Helper;
using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TESTRESTRO.Provider
{
    public class MenuProvider
    {
        public List<MenuItemResponseModel> getAllMenuItems(int categoryId, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Menu menuHelper = new Menu();
                List<MenuItemResponseModel> menuItems = menuHelper.getMenuItems(categoryId, out errorModel);
                return menuItems;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public List<MenuItemResponseModel> getCategory( out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Menu menuHelper = new Menu();
                List<MenuItemResponseModel> menuItems = menuHelper.getCategory( out errorModel);
                return menuItems;
            }
            catch (Exception)
            {
                return null;
            }
        }



        public MenuItemResponseModel changePrice(MenuItemRequestModel menuItemRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Menu menuProvider = new Menu();
                MenuItemResponseModel menuItemResponse = menuProvider.changePrice(menuItemRequest,  out errorModel);
                return menuItemResponse;
            }
            catch (Exception e)
            {
                return null;
            }
        }


        public MenuItemResponseModel addMenuItem(AddMenuItemRequestModel addMenuItemRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Menu menuProvider = new Menu();
                MenuItemResponseModel menuItemResponse = menuProvider.addMenuItem(addMenuItemRequest.createdBy, addMenuItemRequest.menuItemName, addMenuItemRequest.menuItemDescription, addMenuItemRequest.price, addMenuItemRequest.categoryId, addMenuItemRequest.availablequantity,addMenuItemRequest.itemImage, out errorModel);
                return menuItemResponse;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }


}