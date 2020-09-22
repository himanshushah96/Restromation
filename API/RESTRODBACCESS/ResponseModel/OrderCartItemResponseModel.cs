using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class OrderCartItemResponseModel
    {
        public int billId { get; set; }
        public decimal billAmount { get; set; }
        public string billDate { get; set; }
    }
}
