using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class OrderCartItemRequestModel
    {
        public int orderBy { get; set; }
        public bool isDiningIn { get; set; }
        public bool isCardPayment { get; set; }
    }
}
