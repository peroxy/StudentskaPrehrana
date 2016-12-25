using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.ServiceModel;
using System.Threading;
using BonParser;
using TestClient.BonWs;
using Restaurant = TestClient.BonWs.Restaurant;
using System.Linq;

namespace TestClient
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            try
            {
                var binding = new BasicHttpBinding
                {
                    MaxBufferSize = 20000000,
                    MaxReceivedMessageSize = 20000000,
                    ReceiveTimeout = new TimeSpan(0, 10, 0)
                };
                var client = new BonServiceClient(binding, new EndpointAddress("http://bonwebservice.azurewebsites.net/BonService.svc?wsdl"));
                var s = new Stopwatch();

                var elapsed = new List<long>();
                for (int i = 0; i < 100; i++)
                {
                    s.Restart();
                    Restaurants res = client.GetAllRestaurants();
                    elapsed.Add(s.ElapsedMilliseconds);
                }

                Debug.WriteLine(elapsed.Average());

            }
            catch (Exception e)
            {
                Debug.WriteLine(e.InnerException);
            }
            ////Restaurants res = client.GetRestaurantsInRadius(new Coordinates
            ////{
            ////    X = 46.239597M,
            ////    Y = 14.356079M,  //coordinates of Kranj
            ////    RadiusKm = 250
            ////});
            ////foreach (Restaurant restaurant in res.Values)
            ////{
            ////    Console.WriteLine(restaurant.Name);
            ////}
            ////Console.Read();

        }
    }
}
