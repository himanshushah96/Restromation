using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class GetCartItemsResponseModel
    {

        public int cartId { get; set; }
        public int menuItemId { get; set; }
        public int quantity { get; set; }
        public int addedBy { get; set; }
       
        public string menuItemName { get; set; }
        public string menuItemDescription { get; set; }


        public double price { get; set; }
        
        public bool itemStatus { get; set; }
        
        public string itemImage { get; set; }

        public double checkoutPrice { get; set; }

    }
}
