using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TESTRESTRO;

namespace RESTRODBACCESS.Interface
{
    interface IMenu
    {

        List<MenuItemResponseModel> getMenuItems(int categoryId, out ErrorModel errorModel);

        MenuItemResponseModel changePrice(int itemId, double price, int updatedBy, out ErrorModel errorModel);

        MenuItemResponseModel addMenuItem(string menuItemName, string menuItemDescription, double price, int categoryId, int availableQty, bool itemStatus, int createdBy,out ErrorModel errorModel);
    }
}
