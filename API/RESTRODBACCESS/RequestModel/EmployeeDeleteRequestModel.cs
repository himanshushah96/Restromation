using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class EmployeeDeleteRequestModel
    {
        public int userId { get; set; }
        public bool isDelete { get; set; }
    }
}
