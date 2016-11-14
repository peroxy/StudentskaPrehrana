using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace BonService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IBonService
    {
        [OperationContract]
        Restaurants GetAllRestaurants();

        [OperationContract]
        Restaurants GetFilteredRestaurants(Filter values);

        [OperationContract]
        Restaurants GetCurrentlyOpenRestaurants(DateTime now);

        [OperationContract]
        void ParseAllRestaurants();
    }

    [DataContract]
    public class Restaurants
    {
        [DataMember]
        public List<Restaurant> Values { get; set; } = new List<Restaurant>();
    }

    [DataContract]
    public class Restaurant
    {
        [DataMember]
        public string Name { get; set; } 

        [DataMember]
        public string Address { get; set; }

        [DataMember]
        public string Phone { get; set; }

        [DataMember]
        public decimal Price { get; set; }

        [DataMember]
        public decimal CoordinateX { get; set; }

        [DataMember]
        public decimal CoordinateY { get; set; }

        [DataMember]
        public OpeningTime OpeningTime { get; set; }

        [DataMember]
        public DateTime UpdatedOn { get; set; }

        [DataMember]
        public Menu Menu { get; set; }

        [DataMember]
        public bool ServesLunch { get; set; }

        [DataMember]
        public bool HasSaladBar { get; set; }

        [DataMember]
        public bool HasVegetarianSupport { get; set; }

        [DataMember]
        public bool HasDisabledSupport { get; set; }

        [DataMember]
        public bool HasDisabledWcSupport { get; set; }

        [DataMember]
        public bool ServesPizzas { get; set; }

        [DataMember]
        public bool OpenDuringWeekends { get; set; }

        [DataMember]
        public bool ServesFastFood { get; set; }

        [DataMember]
        public bool HasStudentBenefits { get; set; }

        [DataMember]
        public bool HasDelivery { get; set; }
    }

    [DataContract]
    public class Filter
    {
        [DataMember]
        public string Name { get; set; }

        [DataMember]
        public string Address { get; set; }

        [DataMember]
        public Price Price { get; set; }

        [DataMember]
        public Coordinates Coordinates{ get; set; }

        [DataMember]
        public OpeningTime OpeningTime { get; set; }

        [DataMember]
        public bool ServesLunch { get; set; }

        [DataMember]
        public bool HasSaladBar { get; set; }

        [DataMember]
        public bool HasVegetarianSupport { get; set; }

        [DataMember]
        public bool HasDisabledSupport { get; set; }

        [DataMember]
        public bool HasDisabledWcSupport { get; set; }

        [DataMember]
        public bool ServesPizzas { get; set; }

        [DataMember]
        public bool OpenDuringWeekends { get; set; }

        [DataMember]
        public bool ServesFastFood { get; set; }

        [DataMember]
        public bool HasStudentBenefits { get; set; }

        [DataMember]
        public bool HasDelivery { get; set; }
    }

    [DataContract]
    public class Coordinates
    {
        [DataMember]
        public Coordinate X { get; set; }

        [DataMember]
        public Coordinate Y { get; set; }
    }

    [DataContract]
    public class Coordinate
    {
        [DataMember]
        public decimal From { get; set; }

        [DataMember]
        public decimal To { get; set; }
    }

    [DataContract]
    public class Price
    {
        [DataMember]
        public decimal From { get; set; }

        [DataMember]
        public decimal To { get; set; }
    }

    [DataContract]
    public class OpeningTime
    {
        [DataMember]
        public Week Week { get; set; }

        [DataMember]
        public Saturday Saturday { get; set; }

        [DataMember]
        public Sunday Sunday { get; set; }
    }

    [DataContract]
    public class Week
    {
        [DataMember]
        public TimeSpan? From { get; set; }

        [DataMember]
        public TimeSpan? To { get; set; }
    }

    [DataContract]
    public class Saturday
    {
        [DataMember]
        public TimeSpan? From { get; set; }

        [DataMember]
        public TimeSpan? To { get; set; }
    }

    [DataContract]
    public class Sunday
    {
        [DataMember]
        public TimeSpan? From { get; set; }

        [DataMember]
        public TimeSpan? To { get; set; }
    }

    [DataContract]
    public class Menu
    {
        [DataMember]
        public string Soup { get; set; }

        [DataMember]
        public string MainCourse { get; set; }

        [DataMember]
        public string Salad { get; set; }

        [DataMember]
        public string Dessert { get; set; }

    }
}
