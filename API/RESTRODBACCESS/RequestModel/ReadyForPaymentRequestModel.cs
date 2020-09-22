using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class ReadyForPaymentRequestModel
    {
        public int orderId { get; set; }
        public decimal tip { get; set; }
    }
}
