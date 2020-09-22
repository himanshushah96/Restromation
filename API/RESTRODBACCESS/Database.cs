using System.Configuration;

namespace RESTRODBACCESS
{
    class Database
    {
        private Database() { }

        public static string getConnectionString()
        {
            return ConfigurationManager.ConnectionStrings["RestaurantAutomation"].ToString();
        }
    }
}
