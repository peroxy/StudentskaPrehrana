﻿using System;
using System.Diagnostics;
using System.ServiceModel;
using System.Threading;
using TestClient.BonWs;

namespace TestClient
{
    class Program
    {
        static void Main(string[] args)
        {
            var binding = new BasicHttpBinding
            {
                MaxBufferSize = 20000000,
                MaxReceivedMessageSize = 20000000
            };
            var client = new BonServiceClient(binding, new EndpointAddress("http://bonwebservice.azurewebsites.net/BonService.svc?wsdl"));
            Restaurants res = client.GetRestaurantsInRadius(new Coordinates
            {
                X = 46.239597M,
                Y = 14.356079M,  //coordinates of Kranj
                RadiusKm = 2.5M
            });
            foreach (Restaurant restaurant in res.Values)
            {
                Console.WriteLine(restaurant.Name);
            }
            Console.Read();
            //client.ParseAllRestaurants();
        }
    }
}