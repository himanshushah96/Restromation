using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class CustomerNotificationResponseModel
    {
        public List<OrderStatusChangeNotification> orderChange { get; set; }
        public bool istableAvailable { get; set; }
    }
    public class OrderStatusChangeNotification
    {
        public int orderId { get; set; }
        public int statusId { get; set; }
        public string orderStatusTitle { get; set; }

    }
}
