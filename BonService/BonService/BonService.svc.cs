using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Diagnostics;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using BonParser;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace BonService
{
    public class BonService : IBonService
    {
        private static int fail = 0;
        private enum Row
        {
            Name = 0,
            Address = 1,
            Phone = 2,
            Price = 3,
            CoordinateX = 4,
            CoordinateY = 5,
            UpdatedOn = 6,
            WeekStart = 7,
            WeekEnd = 8,
            SaturdayStart = 9,
            SaturdayEnd = 10,
            SundayStart = 11,
            SundayEnd = 12,
            Lunch = 13,
            SaladBar = 14,
            Vegetarian = 15,
            Disabled = 16,
            DisabledWc = 17,
            Pizzas = 18,
            Weekends = 19,
            FastFood = 20,
            StudentBenefits = 21,
            Delivery = 22,
            RestaurantId = 23,
            Menu = 24
        }

        public Restaurants GetAllRestaurants()
        {
            return GetFilteredRestaurants(new Filter());
        }

        public Restaurants GetCurrentlyOpenRestaurants(DateTime now)
        {
            var ts = new TimeSpan(now.Hour, now.Minute, now.Second);
            switch (now.DayOfWeek)
            {
                case DayOfWeek.Saturday:
                    return GetFilteredRestaurants(new Filter
                    {
                        OpeningTime = new OpeningTime
                        {
                            Saturday = new Saturday
                            {
                                From = ts,
                                To = ts
                            }
                        }
                    });
                case DayOfWeek.Sunday:
                    return GetFilteredRestaurants(new Filter
                    {
                        OpeningTime = new OpeningTime
                        {
                            Sunday = new Sunday
                            {
                                From = ts,
                                To = ts
                            }
                        }
                    });
                default:
                    return GetFilteredRestaurants(new Filter
                    {
                        OpeningTime = new OpeningTime
                        {
                            Week = new Week
                            {
                                From = ts,
                                To = ts
                            }
                        }
                    });
            }
        }

        public Restaurants GetRestaurantsInRadius(Coordinates value)
        {
            using (var ctx = new BonDataContext())
            {
                List<fn_GetRestaurantsInRadiusResult> inRadiusResults =
                    (from i in ctx.fn_GetRestaurantsInRadius(value.X, value.Y, value.RadiusKm) select i).ToList();
                var allRestaurants = new Restaurants();
                FillRestaurants(inRadiusResults, allRestaurants);
                return allRestaurants;
            }
        }

        //private static List<Menu> GetRestaurantMenus()
        //{
        //    using (var ctx = new BonDataContext())
        //    {
        //        return ctx.Menus.ToList();//Select(x => new MenuActual
        //        //{
        //        //    MainCourse = x.M_MainCourse,
        //        //    Dessert = x.M_Dessert,
        //        //    Salad = x.M_Salad,
        //        //    Soup = x.M_Soup
        //        //}).ToList();
        //    }
        //}

        private static void FillRestaurants(List<fn_GetRestaurantsInRadiusResult> inRadiusResults,
            Restaurants allRestaurants)
        {
            //List<Menu> menus = GetRestaurantMenus();
            allRestaurants.Values.AddRange(inRadiusResults.Select(i => new Restaurant
            {
                Address = i.R_Address,
                CoordinateX = i.R_CoordinateX,
                CoordinateY = i.R_CoordinateY,
                HasDelivery = i.F_Delivery,
                HasDisabledSupport = i.F_Disabled,
                HasDisabledWcSupport = i.F_DisabledWC,
                HasSaladBar = i.F_SaladBar,
                HasStudentBenefits = i.F_StudentBenefits,
                HasVegetarianSupport = i.F_Vegetarian,
                Menu = MenuToList(i.R_Menu),
                Name = i.R_Name,
                OpenDuringWeekends = i.F_Weekends,
                OpeningTime = new OpeningTime
                {
                    Week = new Week
                    {
                        From = i.O_WeekStart,
                        To = i.O_WeekEnd
                    },
                    Saturday = new Saturday
                    {
                        From = i.O_SaturdayStart,
                        To = i.O_SaturdayEnd
                    },
                    Sunday = new Sunday
                    {
                        From = i.O_SundayStart,
                        To = i.O_SundayEnd
                    }
                },
                Phone = i.R_Phone,
                Price = i.R_Price.Value,
                ServesFastFood = i.F_FastFood,
                ServesLunch = i.F_Lunch,
                ServesPizzas = i.F_Pizzas,
                UpdatedOn = i.R_UpdatedOn
            }));
        }

        public void ParseAllRestaurants()
        {
            Parser.Begin();
        }

        public Restaurants GetFilteredRestaurants(Filter values)
        {
            string sql = GetSqlCommand(values);
            using (var conn = new SqlConnection(ConfigurationManager.ConnectionStrings["BonDBConnectionString"].ConnectionString))
            {
                conn.Open();
                var cmd = new SqlCommand(sql, conn) { CommandType = CommandType.Text };
                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    var restaurants = new Restaurants();
                    //List<Menu> menus = GetRestaurantMenus();
                    while (reader.Read())
                    {
                        restaurants.Values.Add(new Restaurant
                        {
                            Address = reader.GetString((int)Row.Address),
                            CoordinateX = reader.GetDecimal((int)Row.CoordinateX),
                            CoordinateY = reader.GetDecimal((int)Row.CoordinateY),
                            HasDelivery = reader.GetBoolean((int)Row.Delivery),
                            HasDisabledSupport = reader.GetBoolean((int)Row.Disabled),
                            HasDisabledWcSupport = reader.GetBoolean((int)Row.DisabledWc),
                            HasSaladBar = reader.GetBoolean((int)Row.SaladBar),
                            HasStudentBenefits = reader.GetBoolean((int)Row.StudentBenefits),
                            HasVegetarianSupport = reader.GetBoolean((int)Row.Vegetarian),
                            Menu = MenuToList(reader.GetString((int)Row.Menu)),
                            //menus.Where(x => x.M_R_ID == reader.GetInt32((int) Row.RestaurantId))
                            //    .Select(x => new MenuActual
                            //    {
                            //        MainCourse = x.M_MainCourse,
                            //        Dessert = x.M_Dessert,
                            //        Salad = x.M_Salad,
                            //        Soup = x.M_Soup
                            //    }).ToList(),
                            Name = reader.GetString((int)Row.Name),
                            OpenDuringWeekends = reader.GetBoolean((int)Row.Weekends),
                            OpeningTime = new OpeningTime
                            {
                                Week = new Week
                                {
                                    From =
                                        reader.IsDBNull((int)Row.WeekStart)
                                            ? (TimeSpan?)null
                                            : reader.GetTimeSpan((int)Row.WeekStart),
                                    To =
                                        reader.IsDBNull((int)Row.WeekEnd)
                                            ? (TimeSpan?)null
                                            : reader.GetTimeSpan((int)Row.WeekEnd)
                                },
                                Saturday = new Saturday
                                {
                                    From =
                                        reader.IsDBNull((int)Row.SaturdayStart)
                                            ? (TimeSpan?)null
                                            : reader.GetTimeSpan((int)Row.SaturdayStart),
                                    To =
                                        reader.IsDBNull((int)Row.SaturdayEnd)
                                            ? (TimeSpan?)null
                                            : reader.GetTimeSpan((int)Row.SaturdayEnd)
                                },
                                Sunday = new Sunday
                                {
                                    From =
                                        reader.IsDBNull((int)Row.SundayStart)
                                            ? (TimeSpan?)null
                                            : reader.GetTimeSpan((int)Row.SundayStart),
                                    To =
                                        reader.IsDBNull((int)Row.SundayEnd)
                                            ? (TimeSpan?)null
                                            : reader.GetTimeSpan((int)Row.SundayEnd)
                                }
                            },
                            Phone = reader.IsDBNull((int)Row.Phone) ? null : reader.GetString((int)Row.Phone),
                            Price = reader.GetDecimal((int)Row.Price),
                            ServesFastFood = reader.GetBoolean((int)Row.FastFood),
                            ServesLunch = reader.GetBoolean((int)Row.Lunch),
                            ServesPizzas = reader.GetBoolean((int)Row.Pizzas),
                            UpdatedOn = reader.GetDateTime((int)Row.UpdatedOn)
                        });
                    }
                    return restaurants;
                }
            }
        }

        private static string GetSqlCommand(Filter filter)
        {
            var sql = new StringBuilder("SELECT * FROM V_RESTAURANT WHERE ");

            if (filter.Address != null)
            {
                sql.Append($"R_Address LIKE '%{filter.Address}%' AND ");
            }

            if (filter.Name != null)
            {
                sql.Append($"R_Name LIKE '%{filter.Name}%' AND ");
            }

            if (filter.Price != null)
            {
                sql.Append($"R_Price BETWEEN '{filter.Price.From}' AND '{filter.Price.To}' AND ");
            }

            if (filter.OpeningTime != null)
            {
                if (filter.OpeningTime.Week != null)
                {
                    sql.Append(
                        $"O_WeekStart <= '{filter.OpeningTime.Week.From}' AND O_WeekEnd >= '{filter.OpeningTime.Week.To}' AND ");
                }
                if (filter.OpeningTime.Saturday != null)
                {
                    sql.Append(
                        $"O_SaturdayStart <= '{filter.OpeningTime.Saturday.From}' AND O_SaturdayEnd >= '{filter.OpeningTime.Saturday.To}' AND ");
                }
                if (filter.OpeningTime.Sunday != null)
                {
                    sql.Append(
                        $"O_SundayStart <= '{filter.OpeningTime.Sunday.From}' AND O_SundayEnd >= '{filter.OpeningTime.Sunday.To}' AND ");
                }
            }

            if (filter.HasDelivery)
            {
                sql.Append("F_Delivery = 1 AND ");
            }
            if (filter.HasDisabledSupport)
            {
                sql.Append("F_Disabled = 1 AND ");
            }
            if (filter.HasDisabledWcSupport)
            {
                sql.Append("F_DisabledWC = 1 AND ");
            }
            if (filter.ServesFastFood)
            {
                sql.Append("F_FastFood = 1 AND ");
            }
            if (filter.ServesLunch)
            {
                sql.Append("F_Lunch = 1 AND ");
            }
            if (filter.ServesPizzas)
            {
                sql.Append("F_Pizzas = 1 AND ");
            }
            if (filter.HasSaladBar)
            {
                sql.Append("F_SaladBar = 1 AND ");
            }
            if (filter.HasStudentBenefits)
            {
                sql.Append("F_StudentBenefits = 1 AND ");
            }
            if (filter.HasVegetarianSupport)
            {
                sql.Append("F_Vegetarian = 1 AND ");
            }
            if (filter.OpenDuringWeekends)
            {
                sql.Append("F_Weekends = 1 AND ");
            }

            sql.Remove(sql.Length - 5, 5);
            return sql.ToString();
        }

        private static List<Menu> MenuToList(string menus)
        {
            try
            {
                return menus == "[]" ? new List<Menu>() : JsonConvert.DeserializeObject<List<Menu>>(menus);
            }
            catch (Exception)
            {
                Debug.WriteLine(++fail);
                return new List<Menu>();
            }
        }
    }




}
