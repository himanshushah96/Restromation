using RESTRODBACCESS.RequestModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Routing;
using TESTRESTRO.Models;
using TESTRESTRO.Provider;

namespace TESTRESTRO.Controllers
{
    public class TableController : ApiController
    {
        [HttpGet]
        [Route("api/table/getTables/{tableId:int?}/{capacity:int?}")]
        public HttpResponseMessage getTables(int tableId, int capacity)
        {
            TableProvider tableProvider = new TableProvider();
            ErrorModel errorModel = null;

            var tableItems = tableProvider.getTables(tableId, capacity, out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = tableItems;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }




        [HttpPost]
        [Route("api/reserveTable/")]
        public  HttpResponseMessage reserveTable(ReserveTableRequestModel model)
        {
            TableProvider tableProvider = new TableProvider();
            ErrorModel errorModel = null;

            var tableItems = tableProvider.reserveTable(model, out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = tableItems;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpPost]
        [Route("api/addTable")]
        public HttpResponseMessage addTables(AddTableRequestModel addTableRequestModel)
        {
            TableProvider tableProvider = new TableProvider();
            ErrorModel errorModel = null;
            var table = tableProvider.addTable(addTableRequestModel, out errorModel);

            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = table;
            aPIResponseModel.Error = errorModel;

            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }

        [HttpGet]
        [Route("api/table/isTableReserved/{clientId:int?}")]
        public HttpResponseMessage isTableReserved(int clientId)
        {
            TableProvider tableProvider = new TableProvider();
            ErrorModel errorModel = null;

            var status = tableProvider.isTableReserved(clientId, out errorModel);
            APIResponseModel responseModel = new APIResponseModel();
            responseModel.Response = status;
            responseModel.Error = errorModel;
            return Request.CreateResponse(HttpStatusCode.OK, responseModel);
        }

        [HttpPost]
        [Route("api/deleteorModifyTable")]
        public HttpResponseMessage deleteorModifyTable(TableDeleteRequestModel tableDeleteRequest)
        {
            TableProvider tableProvider = new TableProvider();
            ErrorModel errorModel = null;
            var deleteorModifyTable = tableProvider.deleteorModifyTable(tableDeleteRequest, out errorModel);

            APIResponseModel aPIResponseModel = new APIResponseModel();
            aPIResponseModel.Response = deleteorModifyTable;
            aPIResponseModel.Error = errorModel;

            return Request.CreateResponse(HttpStatusCode.OK, aPIResponseModel);
        }
    }
}
