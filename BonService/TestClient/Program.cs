using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using TestClient.BonWs;

namespace TestClient
{
    class Program
    {
        static void Main(string[] args)
        {
            var client = new BonServiceClient(new BasicHttpBinding(),
                new EndpointAddress("http://bonwebservice.azurewebsites.net/BonService.svc?wsdl"));
            Restaurants response = client.GetFilteredRestaurants(new Filter
            {
                HasDelivery = true
            });

            foreach (Restaurant restaurant in response.Values)
            {
                Console.WriteLine(restaurant.Name);
            }
            Console.ReadLine();
        }
    }
}
