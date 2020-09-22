using RESTRODBACCESS.Helper;
using RESTRODBACCESS.ResponseModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TESTRESTRO.Provider
{
    public class NotificationProvider
    {
        public CustomerNotificationResponseModel getNotificationsDataCustomer(int customerId, int capacity, out ErrorModel errorModel)
        {
            errorModel = null;
            try
            {
                Notification notification = new Notification();
                return notification.getNotificationsDataCustomer(customerId, capacity, out errorModel);
            }
            catch
            {
                return null;
            }
        }
    }
}