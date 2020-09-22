using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class AddToCartResponseModel
    {
        public string StatusCode { get; set; }
        public string StatusMessage { get; set; }
        public double Total { get; set; }
        public string Quantity { get; set; }
        public double ItemTotal { get; set; }
    }
}
