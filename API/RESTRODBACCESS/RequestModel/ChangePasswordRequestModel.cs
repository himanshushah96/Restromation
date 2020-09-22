using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RESTRODBACCESS.RequestModel
{
    public class ChangePasswordRequestModel
    {
        public string password { get; set; }
        public int userId { get; set; }
    }
}
