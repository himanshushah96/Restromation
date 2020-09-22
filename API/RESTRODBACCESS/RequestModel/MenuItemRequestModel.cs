using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class MenuItemRequestModel
    {
       
        public bool isDelete { get; set; }
        public int itemId { get; set; }

        public double price { get; set; }

        public int updatedBy { get; set; }
    }
}
