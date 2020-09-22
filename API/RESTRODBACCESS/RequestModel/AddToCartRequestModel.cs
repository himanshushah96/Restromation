using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class AddToCartRequestModel
    {
        public int itemId { get; set; }
        public int quantity { get; set; }
        public int addedby { get; set; }
    }
}
