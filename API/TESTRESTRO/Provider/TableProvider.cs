using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;
using RESTRODBACCESS.Helper;
using RESTRODBACCESS.RequestModel;
using RESTRODBACCESS.ResponseModel;

namespace TESTRESTRO.Provider
{
    public class TableProvider
    {
        public List<GetTableResponseModel> getTables(int tableId, int capacity, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Table tableHelper = new Table();
                List<GetTableResponseModel> tableItems = tableHelper.getTables(tableId, capacity, out errorModel);
                return tableItems;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public ReserveTableResponseModel reserveTable(ReserveTableRequestModel reserveTableRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Table tableHelper = new Table();
                ReserveTableResponseModel model = tableHelper.reserveTable(reserveTableRequestModel, out errorModel);
                return model;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public GetTableResponseModel addTable(AddTableRequestModel addTableRequestModel, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Table tableHelper = new Table();
                return tableHelper.addTable(addTableRequestModel, out errorModel);
            }
            catch (Exception)
            {
                return null;
            }
        }

        public bool isTableReserved(int clientId, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Table tableHelper = new Table();
                bool response = tableHelper.istableReserved(clientId, out errorModel);
                return response;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public GetTableResponseModel deleteorModifyTable(TableDeleteRequestModel tableDeleteRequest, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Table tableHelper = new Table();
                return tableHelper.deleteorModifyTable(tableDeleteRequest, out errorModel);
            }
            catch (Exception)
            {
                return null;
            }
        }
    }
}