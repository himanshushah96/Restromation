using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.ResponseModel
{
    public class GetTableResponseModel
    {
        public int tableId { get; set; }

        public int capacity { get; set; }

        public bool availability { get; set; }
    }
}
