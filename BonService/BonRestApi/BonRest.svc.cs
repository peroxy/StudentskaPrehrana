using System;
using System.Collections.Generic;
using System.Globalization;
using System.ServiceModel;
using BonRestApi.BonWs;

namespace BonRestApi
{
    public class BonRest : IBonRest
    {
        public Restaurants GetRestaurantsByFeatures(string lunch, string saladBar, string vegetarian, string disabled,
            string disabledWc, string pizzas, string weekends, string fastFood, string studentBenefits, string delivery)
        {
            BonServiceClient client = InitClient();
            return client.GetFilteredRestaurants(new Filter
            {
                ServesLunch = lunch == "1",
                HasSaladBar = saladBar == "1",
                HasVegetarianSupport = vegetarian == "1",
                HasDisabledSupport = disabled == "1",
                HasDisabledWcSupport = disabledWc == "1",
                ServesPizzas = pizzas == "1",
                OpenDuringWeekends = weekends == "1",
                ServesFastFood = fastFood == "1",
                HasStudentBenefits = studentBenefits == "1",
                HasDelivery = delivery == "1",
            });
        }

        public Restaurants GetRestaurantsInRadius(string x, string y, string radius)
        {
            BonServiceClient client = InitClient();
            return client.GetRestaurantsInRadius(new Coordinates
            {
                X = Convert.ToDecimal(x, CultureInfo.InvariantCulture),
                Y = Convert.ToDecimal(y, CultureInfo.InvariantCulture),
                RadiusKm = Convert.ToDecimal(radius, CultureInfo.InvariantCulture)
            });
        }

        public Restaurants GetAllRestaurants()
        {
            BonServiceClient client = InitClient();
            return client.GetAllRestaurants();
        }

        public Restaurants GetRestaurantsByName(string name)
        {
            BonServiceClient client = InitClient();
            return client.GetFilteredRestaurants(new Filter
            {
                Name = name
            });
        }

        public Restaurants GetRestaurantsByAddress(string address)
        {
            BonServiceClient client = InitClient();
            return client.GetFilteredRestaurants(new Filter
            {
                Name = address
            });
        }

        public Restaurants GetRestaurantsInPriceRange(string from, string to)
        {
            BonServiceClient client = InitClient();
            return client.GetFilteredRestaurants(new Filter
            {
                Price = new Price
                {
                    From = Convert.ToDecimal(from, CultureInfo.InvariantCulture),
                    To = Convert.ToDecimal(to, CultureInfo.InvariantCulture),
                }
            });
        }

        public Restaurants GetCurrentlyOpened()
        {
            BonServiceClient client = InitClient();
            return
                client.GetCurrentlyOpenRestaurants(TimeZoneInfo.ConvertTimeFromUtc(DateTime.UtcNow,
                    TimeZoneInfo.FindSystemTimeZoneById("Central Europe Standard Time")));
        }

        private static BonServiceClient InitClient()
        {
            var binding = new BasicHttpBinding
            {
                MaxBufferSize = 20000000,
                MaxReceivedMessageSize = 20000000
            };
            var client = new BonServiceClient(binding,
                new EndpointAddress("http://bonwebservice.azurewebsites.net/BonService.svc?wsdl"));
            return client;
        }
    }
}
