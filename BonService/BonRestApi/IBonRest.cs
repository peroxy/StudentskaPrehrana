using System.Collections.Generic;
using System.ServiceModel;
using System.ServiceModel.Web;
using BonRestApi.BonWs;

namespace BonRestApi
{
    [ServiceContract]
    public interface IBonRest
    {
        [OperationContract]
        [WebGet(
            UriTemplate =
                "Features/{lunch}/{saladBar}/{vegetarian}/{disabled}/{disabledWc}/{pizzas}/{weekends}/{fastFood}/{studentBenefits}/{delivery}",
            ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetRestaurantsByFeatures(string lunch, string saladBar, string vegetarian, string disabled,
            string disabledWc, string pizzas, string weekends, string fastFood, string studentBenefits, string delivery);

        [OperationContract]
        [WebGet(UriTemplate = "InRadius/{x}/{y}/{radius}", ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetRestaurantsInRadius(string x, string y, string radius);

        [OperationContract]
        [WebGet(UriTemplate = "All", ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetAllRestaurants();

        [OperationContract]
        [WebGet(UriTemplate = "Name/{name}", ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetRestaurantsByName(string name);

        [OperationContract]
        [WebGet(UriTemplate = "Address/{address}", ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetRestaurantsByAddress(string address);

        [OperationContract]
        [WebGet(UriTemplate = "Price/{from}/{to}", ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetRestaurantsInPriceRange(string from, string to);

        [OperationContract]
        [WebGet(UriTemplate = "Open", ResponseFormat = WebMessageFormat.Json)]
        Restaurants GetCurrentlyOpened();
    }
}
