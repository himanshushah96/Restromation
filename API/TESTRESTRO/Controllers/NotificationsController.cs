using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;
using Ubiety.Dns.Core;

namespace TESTRESTRO.Controllers
{
    public class NotificationsController : ApiController
    {
        [HttpGet]
        [Route("api/notification/customer/{customerId:int}/{capacity:int}")]
        public HttpResponseMessage getCustomerNoticationData(int customerId, int capacity)
        {
            NotificationProvider notificationProvider = new NotificationProvider();
            ErrorModel errorModel = null;
            var data = notificationProvider.getNotificationsDataCustomer(customerId, capacity, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = data;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }
    }
}
