using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class ChangeOrdersStatusRequestModel
    {
        public int orderId { get; set; }
        public int orderStatus { get; set; }
    }
}
