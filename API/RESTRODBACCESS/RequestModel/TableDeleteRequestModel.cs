using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class TableDeleteRequestModel
    {
        public int tableId { get; set; }
        public bool isDelete { get; set; }
    }
}
