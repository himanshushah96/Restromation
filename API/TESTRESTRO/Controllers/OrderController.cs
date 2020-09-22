using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;

namespace TESTRESTRO.Controllers
{
    public class OrderController : ApiController
    {
        [HttpPost]
        [Route("api/orders/addOrders")]
        public HttpResponseMessage addOrders(OrderCartItemRequestModel orderCartItemRequestModel)
        {
            OrderProvider orderProvider = new OrderProvider();
            ErrorModel errorModel = null;
            var order = orderProvider.addOrder(orderCartItemRequestModel, out errorModel);

            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = order;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpGet]
        [Route("api/orders/getOrdersHistory/{startDate}/{endDate}")]
        public HttpResponseMessage getOrdersHistory(string startDate, string endDate)
        {
            OrderProvider orderProvider = new OrderProvider();
            ErrorModel errorModel = null;
            var orders = orderProvider.getOrdersHistory(startDate, endDate, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = orders;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpGet]
        [Route("api/orders/getOrders/{chefOrCashier}/{customerId:int?}/{fromDate}/{toDate}/{email}/{needUnpaidOnly}")]
        public HttpResponseMessage getOrders(bool chefOrCashier,int customerId, string fromDate, string toDate, string email, bool needUnpaidOnly)
        {
            GetOrdersRequestModel requestModel = new GetOrdersRequestModel();
            requestModel.chefOrCashier = chefOrCashier;
            requestModel.customerId = customerId;
            requestModel.fromDate = fromDate;
            requestModel.toDate = toDate;
            requestModel.email = email;
            requestModel.needUnpaidOnly = needUnpaidOnly;
            if (requestModel.email.Equals("0")) requestModel.email = null;
            if (requestModel.fromDate.Equals("0")) requestModel.fromDate = null;
            if (requestModel.toDate.Equals("0")) requestModel.toDate = null;
            if (requestModel.customerId == 0) requestModel.customerId = 0;
            ErrorModel errorModel = null;
            OrderProvider orderProvider = new OrderProvider();
            var orders = orderProvider.getOrders(requestModel, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = orders;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpPost]
        [Route("api/orders/updateOrderStatus")]
        public HttpResponseMessage updateOrderStatus(ChangeOrdersStatusRequestModel changeOrdersStatusRequestModel)
        {
            ErrorModel errorModel = null;
            OrderProvider provider = new OrderProvider();
            var status = provider.updateOrderStatus(changeOrdersStatusRequestModel, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = status;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpPost]
        [Route("api/order/changePaymentStatus")]
        public HttpResponseMessage changePaymentStatus(ChangePaymentStatusRequestModel changePaymentStatusRequest)
        {
            ErrorModel errorModel = null;
            OrderProvider provider = new OrderProvider();
            var status = provider.changePaymentStatus(changePaymentStatusRequest, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = status;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpPost]
        [Route("api/orders/readyToPay")]
        public HttpResponseMessage readyToPay(ReadyForPaymentRequestModel readyForPaymentRequest)
        {
            ErrorModel errorModel = null;
            OrderProvider provider = new OrderProvider();
            var status = provider.readyToPay(readyForPaymentRequest, out errorModel);
            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = status;
            aPIResponseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }


        [HttpGet]
        [Route("api/orders/getReadyForPayment")]
        public HttpResponseMessage getReadyForPayment()
        {
            ErrorModel errorModel = null;
            OrderProvider provider = new OrderProvider();

            var readyForPayment = provider.getReadyForPayment(out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = readyForPayment;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }
    }
}
