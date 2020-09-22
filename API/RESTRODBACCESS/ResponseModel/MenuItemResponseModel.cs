using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class MenuItemResponseModel
    {
        public int categoryId { get; set; }
        public int menuItemId { get; set; }
        public string menuItemName { get; set; }
        public string menuItemDescription { get; set; }


        public double price { get; set; }
        public int availablequantity { get; set; }
        public bool itemStatus { get; set; }
        public string createdBy { get; set; }
        public int userId { get; set; }
        public string updatedBy { get; set; }
        public string deletedBy { get; set; }

        public string categoryTitle { get; set; }

        public string itemImage { get; set; }

    }
}
