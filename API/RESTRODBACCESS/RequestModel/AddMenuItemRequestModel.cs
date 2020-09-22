using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class AddMenuItemRequestModel
    {

        public int categoryId { get; set; } 
        public string menuItemName { get; set; }
        public string menuItemDescription { get; set; }


        public double price { get; set; }
        public int availablequantity { get; set; }
        public bool itemStatus { get; set; }
        public int createdBy { get; set; }

        public string itemImage { get; set; }
        
    }
}
