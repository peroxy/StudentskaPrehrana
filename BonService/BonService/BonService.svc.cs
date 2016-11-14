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

namespace BonService
{
    public class BonService : IBonService
    {
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
            Soup = 13,
            MainCourse = 14,
            Salad = 15,
            Dessert = 16,
            Lunch = 17,
            SaladBar = 18,
            Vegetarian = 19,
            Disabled = 20,
            DisabledWc = 21,
            Pizzas = 22,
            Weekends = 23,
            FastFood = 24,
            StudentBenefits = 25,
            Delivery = 26
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

        public void ParseAllRestaurants()
        {
            Parser.Begin();
        }

        public Restaurants GetFilteredRestaurants(Filter values)
        {
            string sql = GetSqlCommand(values);
            Debug.WriteLine(sql);
            using (
                var conn =
                    new SqlConnection(ConfigurationManager.ConnectionStrings["BonDBConnectionString"].ConnectionString))
            {
                conn.Open();
                var cmd = new SqlCommand(sql, conn) {CommandType = CommandType.Text};
                using (SqlDataReader reader = cmd.ExecuteReader())
                {
                    var restaurants = new Restaurants();
                    while (reader.Read())
                    {
                        restaurants.Values.Add(new Restaurant
                        {
                            Address = reader.GetString((int) Row.Address),
                            CoordinateX = reader.GetDecimal((int) Row.CoordinateX),
                            CoordinateY = reader.GetDecimal((int) Row.CoordinateY),
                            HasDelivery = reader.GetBoolean((int) Row.Delivery),
                            HasDisabledSupport = reader.GetBoolean((int) Row.Disabled),
                            HasDisabledWcSupport = reader.GetBoolean((int) Row.DisabledWc),
                            HasSaladBar = reader.GetBoolean((int) Row.SaladBar),
                            HasStudentBenefits = reader.GetBoolean((int) Row.StudentBenefits),
                            HasVegetarianSupport = reader.GetBoolean((int) Row.Vegetarian),
                            Menu = new Menu
                            {
                                Dessert =
                                    reader.IsDBNull((int) Row.Dessert) ? null : reader.GetString((int) Row.Dessert),
                                MainCourse =
                                    reader.IsDBNull((int) Row.MainCourse)
                                        ? null
                                        : reader.GetString((int) Row.MainCourse),
                                Salad = reader.IsDBNull((int) Row.Salad) ? null : reader.GetString((int) Row.Salad),
                                Soup = reader.IsDBNull((int) Row.Soup) ? null : reader.GetString((int) Row.Soup),
                            },
                            Name = reader.GetString((int) Row.Name),
                            OpenDuringWeekends = reader.GetBoolean((int) Row.Weekends),
                            OpeningTime = new OpeningTime
                            {
                                Week = new Week
                                {
                                    From =
                                        reader.IsDBNull((int) Row.WeekStart)
                                            ? (TimeSpan?) null
                                            : reader.GetTimeSpan((int) Row.WeekStart),
                                    To =
                                        reader.IsDBNull((int) Row.WeekEnd)
                                            ? (TimeSpan?) null
                                            : reader.GetTimeSpan((int) Row.WeekEnd)
                                },
                                Saturday = new Saturday
                                {
                                    From =
                                        reader.IsDBNull((int) Row.SaturdayStart)
                                            ? (TimeSpan?) null
                                            : reader.GetTimeSpan((int) Row.SaturdayStart),
                                    To =
                                        reader.IsDBNull((int) Row.SaturdayEnd)
                                            ? (TimeSpan?) null
                                            : reader.GetTimeSpan((int) Row.SaturdayEnd)
                                },
                                Sunday = new Sunday
                                {
                                    From =
                                        reader.IsDBNull((int) Row.SundayStart)
                                            ? (TimeSpan?) null
                                            : reader.GetTimeSpan((int) Row.SundayStart),
                                    To =
                                        reader.IsDBNull((int) Row.SundayEnd)
                                            ? (TimeSpan?) null
                                            : reader.GetTimeSpan((int) Row.SundayEnd)
                                }
                            },
                            Phone = reader.IsDBNull((int) Row.Phone) ? null : reader.GetString((int) Row.Phone),
                            Price = reader.GetDecimal((int) Row.Price),
                            ServesFastFood = reader.GetBoolean((int) Row.FastFood),
                            ServesLunch = reader.GetBoolean((int) Row.Lunch),
                            ServesPizzas = reader.GetBoolean((int) Row.Pizzas),
                            UpdatedOn = reader.GetDateTime((int) Row.UpdatedOn)
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

            if (filter.Coordinates != null)
            {
                if (filter.Coordinates.X != null)
                {
                    sql.Append(
                        $"R_CoordinateX BETWEEN '{filter.Coordinates.X.From}' AND '{filter.Coordinates.X.To}' AND ");
                }
                if (filter.Coordinates.Y != null)
                {
                    sql.Append(
                        $"R_CoordinateY BETWEEN '{filter.Coordinates.Y.From}' AND '{filter.Coordinates.Y.To}' AND ");
                }
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
    }
}
