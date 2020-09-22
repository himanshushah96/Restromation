using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class GetReadyForPayment
    {
        public int billId { get; set; }
        public int orderId{ get; set; }
        public string billDate{ get; set; }
        public double billingAmount{ get; set; }
        public bool icCardPayment{ get; set; }
        public bool   isPaid{ get; set; }
         public bool   isReadyToPay{ get; set; }
        public string FirstName { get; set; }
        public string lastName { get; set; }
        public int tableID { get; set; }
    }
}
