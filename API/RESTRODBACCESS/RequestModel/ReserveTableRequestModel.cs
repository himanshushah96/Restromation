using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class ReserveTableRequestModel
    {
        public int tableId { get; set; }
        public string reservationDate { get; set; }
        public int reservedBy { get; set; }
        public int numberOfPeople { get; set; }
        public string startTime { get; set; }
        public string endTime { get; set; }
    }
}
