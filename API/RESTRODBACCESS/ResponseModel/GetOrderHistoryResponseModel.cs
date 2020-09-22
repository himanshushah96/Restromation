using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class GetOrderHistoryResponseModel
    {
        public int orderId { get; set; }
        public int reservationId { get; set; }
        public string orderDate { get; set; }
        public bool isDineIn { get; set; }
        public int currentStatusId { get; set; }
        public string orderStatusTitle { get; set; }
        public int userId { get; set; }
        public string reservedBy { get; set; }
        public int totalItems { get; set; }
    }
}
