using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace BonParser
{
    public static class Parser
    {
        public static string BonarApiUrl { get; } = "http://bonar.si/api/restaurants";

        private enum Features
        {
            Vegetarian = 1,
            Disabled = 3,
            Delivery = 5,
            SaladBar = 7,
            DisabledWc = 8,
            StudentBenefits = 9,
            Weekends = 10,
            Lunch = 11,
            Pizzas = 12,
            FastFood = 13
        }

        public static void Begin()
        {
            using (WebClient webClient = new WebClient())
            {
                //Console.WriteLine("Initializing program...");
                var s = new Stopwatch();
                s.Start();
                webClient.Encoding = Encoding.UTF8;
                string json = "";
                try
                {
                    //  Console.WriteLine("Downloading data from Bonar API...");
                    json = webClient.DownloadString(BonarApiUrl);
                    //Console.WriteLine("Successfully downloaded data from Bonar API...");
                }
                catch (Exception)
                {
                    Environment.Exit(-1);
                }

                //Console.WriteLine("Truncating all tables...");
                DbOperations.TruncateAllTables();
                //Console.WriteLine("Begin inserting..");

                ParseRestaurants(s, json);

                //Console.WriteLine($"Finished parsing and inserting into DB in {s.Elapsed.TotalSeconds} seconds.");
            }
        }

        private static void ParseRestaurants(Stopwatch s, string json)
        {
            dynamic data = JsonConvert.DeserializeObject(json);
            int i = 1;
            foreach (dynamic item in (IEnumerable)data)
            {
                string name = item.name;
                string address = item.address;
                string phone = null;
                if (item.telephone.Count > 0)
                {
                    phone = item.telephone[0];
                }
                string priceStr = (string)item.price;
                decimal price = Convert.ToDecimal(priceStr.Split(' ')[0], CultureInfo.GetCultureInfo("de-DE"));
                TimeSpan? weekStart, weekEnd, satStart, satEnd, sunStart, sunEnd;
                ParseRestaurantOpeningTimes(item, out weekStart, out weekEnd, out satStart, out satEnd, out sunStart,
                    out sunEnd);
                string menus = ParseRestaurantMenus(item);

                decimal coordinateX = item.coordinates[0];
                decimal coordinateY = item.coordinates[1];
                bool lunch,
                    saladBar,
                    vegetarian,
                    disabled,
                    disabledWc,
                    pizzas,
                    weekends,
                    fastFood,
                    studentBenefits,
                    delivery;
                ParseRestaurantFeatures(item, out lunch, out saladBar, out vegetarian, out disabled, out disabledWc,
                    out pizzas, out weekends, out fastFood, out studentBenefits, out delivery);
                InsertIntoDatabase(name, address, phone, price, weekStart, weekEnd, satStart, satEnd, sunStart, sunEnd, coordinateX, coordinateY, lunch, saladBar, vegetarian, disabled,
                    disabledWc, pizzas, weekends, fastFood, studentBenefits, delivery, menus);

            }
        }

        private static void InsertIntoDatabase(string name, string address, string phone, decimal price,
            TimeSpan? weekStart, TimeSpan? weekEnd, TimeSpan? satStart, TimeSpan? satEnd, TimeSpan? sunStart,
            TimeSpan? sunEnd, decimal coordinateX,
            decimal coordinateY, bool lunch, bool saladBar, bool vegetarian, bool disabled, bool disabledWc, bool pizzas,
            bool weekends, bool fastFood, bool studentBenefits, bool delivery, string menus)
        {
            int featureId = DbOperations.InsertFeature(lunch, saladBar, vegetarian, disabled, disabledWc, pizzas,
                weekends, fastFood, studentBenefits, delivery);
            int openingId = DbOperations.InsertOpening(weekStart, weekEnd, satStart, satEnd, sunStart, sunEnd);
            DbOperations.InsertRestaurant(name, address, phone, price, coordinateX, coordinateY,
                TimeZoneInfo.ConvertTimeFromUtc(DateTime.UtcNow,
                    TimeZoneInfo.FindSystemTimeZoneById("Central Europe Standard Time")),
                openingId, featureId, menus);
            //DbOperations.InsertMenus(menus, restaurantId);
        }

        private static void ParseRestaurantFeatures(dynamic item, out bool lunch, out bool saladBar, out bool vegetarian,
            out bool disabled, out bool disabledWc, out bool pizzas, out bool weekends, out bool fastFood,
            out bool studentBenefits, out bool delivery)
        {
            var features = new List<int>();
            foreach (dynamic feature in item.features)
            {
                features.Add((int)feature.id);
            }

            lunch = features.Contains((int)Features.Lunch);
            saladBar = features.Contains((int)Features.SaladBar);
            vegetarian = features.Contains((int)Features.Vegetarian);
            disabled = features.Contains((int)Features.Disabled);
            disabledWc = features.Contains((int)Features.DisabledWc);
            pizzas = features.Contains((int)Features.Pizzas);
            weekends = features.Contains((int)Features.Weekends);
            fastFood = features.Contains((int)Features.FastFood);
            studentBenefits = features.Contains((int)Features.StudentBenefits);
            delivery = features.Contains((int)Features.Delivery);
        }

        private static string ParseRestaurantMenus(dynamic item)
        {
            var menus = new StringBuilder("[");
            if ((int)item.menu.Count > 0)
            {
                foreach (dynamic menu in item.menu)
                {
                    switch ((int)menu.Count)
                    {
                        case 1:
                            menus.AppendFormat(@"{{""Dessert"":null,""MainCourse"":""{0}"",""Salad"":null,""Soup"":null}},", 
                                menu[0].Value.Replace(@"""", ""));
                            break;
                        case 2:
                            menus.AppendFormat(@"{{""Dessert"":null,""MainCourse"":""{0}"",""Salad"":null,""Soup"":""{1}""}},", 
                                menu[1].Value.Replace(@"""", ""), menu[0].Value.Replace(@"""", ""));
                            break;
                        case 3:
                            menus.AppendFormat(@"{{""Dessert"":""{2}"",""MainCourse"":""{0}"",""Salad"":""{1}"",""Soup"":null}},", 
                                menu[0].Value.Replace(@"""", ""), menu[1].Value.Replace(@"""", ""), menu[2].Value.Replace(@"""", ""));
                            break;
                        case 4:
                            menus.AppendFormat(@"{{""Dessert"":""{2}"",""MainCourse"":""{0}"",""Salad"":""{1}"",""Soup"":""{3}""}},", 
                                menu[1].Value.Replace(@"""", ""), menu[2].Value.Replace(@"""", ""), menu[3].Value.Replace(@"""", ""),
                                menu[0].Value.Replace(@"""", ""));
                            break;
                    }

                }
            }
            else
            {
                return "[]";
            }
            menus.Remove(menus.Length - 1, 1);
            menus.Append("]");
            return menus.ToString();
        }

        private static void ParseRestaurantOpeningTimes(dynamic item, out TimeSpan? weekStart, out TimeSpan? weekEnd,
            out TimeSpan? satStart, out TimeSpan? satEnd, out TimeSpan? sunStart, out TimeSpan? sunEnd)
        {
            weekStart = null;
            weekEnd = null;
            satStart = null;
            satEnd = null;
            sunStart = null;
            sunEnd = null;
            if (item.opening.week is JArray && (int)item.opening.week.Count > 0)
            {
                weekStart = TimeSpan.Parse((string)item.opening.week[0]);
                weekEnd = TimeSpan.Parse((string)item.opening.week[1]);
            }
            if (item.opening.saturday is JArray && (int)item.opening.saturday.Count > 0)
            {
                satStart = TimeSpan.Parse((string)item.opening.saturday[0]);
                satEnd = TimeSpan.Parse((string)item.opening.saturday[1]);
            }
            if (item.opening.sunday is JArray && (int)item.opening.sunday.Count > 0)
            {
                sunStart = TimeSpan.Parse((string)item.opening.sunday[0]);
                sunEnd = TimeSpan.Parse((string)item.opening.sunday[1]);
            }
        }
    }



}

