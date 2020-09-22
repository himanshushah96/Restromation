using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class GetOrdersRequestModel
    {
        public bool chefOrCashier { get; set; }
        public int customerId { get; set; }
        public string fromDate { get; set; }
        public string toDate { get; set; }
        public string email { get; set; }
        public bool needUnpaidOnly { get; set; }
    }
}
