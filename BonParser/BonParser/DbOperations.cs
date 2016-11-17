using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BonParser
{
    public static class DbOperations
    {
        public static void TruncateAllTables()
        {
            using (var ctx = new BonDataContext())
            {
                ctx.TruncateAll();
            }
        }

        /// <summary>
        /// Executes BonDB stored procedure that inserts a row into Restaurant table.
        /// </summary>
        public static void InsertRestaurant(string name, string address, string phone, decimal price, decimal coordinateX,
            decimal coordinateY, DateTime updatedOn, int? menuId, int openingId, int featureId)
        {
            using (var ctx = new BonDataContext())
            {
                ctx.InsertRestaurant(name, address, phone, price, coordinateX, coordinateY, updatedOn, menuId, openingId, featureId);
            }
        }

        /// <summary>
        /// Executes BonDB stored procedure that inserts a row into Opening table.
        /// </summary>
        /// <returns>Will return the newly inserted row ID.</returns>
        public static int InsertOpening(TimeSpan? weekStart, TimeSpan? weekEnd, TimeSpan? satStart, TimeSpan? satEnd, TimeSpan? sunStart, TimeSpan? sunEnd)
        {
            using (var ctx = new BonDataContext())
            {
                int? id = 0;
                ctx.InsertOpening(weekStart, weekEnd, satStart, satEnd, sunStart, sunEnd, ref id);
                return id.Value;
            }
        }

        /// <summary>
        /// Executes BonDB stored procedure that inserts a row into Feature table.
        /// </summary>
        /// <returns>Will return the newly inserted row ID.</returns>
        public static int InsertFeature(bool lunch, bool saladBar, bool vegetarian, bool disabled, bool disabledWc,
            bool pizzas, bool weekends, bool fastFood, bool studentBenefits, bool delivery)
        {
            using (var ctx = new BonDataContext())
            {
                int? id = 0;
                ctx.InsertFeature(lunch, saladBar, vegetarian, disabled, disabledWc, pizzas, weekends, fastFood, studentBenefits, delivery, ref id);
                return id.Value;
            }
        }

        /// <summary>
        /// Executes BonDB stored procedure that inserts a row into Menu table.
        /// </summary>
        /// <returns>Will return the newly inserted row ID.</returns>
        public static int InsertMenu(string soup, string mainCourse, string salad, string dessert)
        {
            using (var ctx = new BonDataContext())
            {
                int? id = 0;
                ctx.InsertMenu(soup, mainCourse, salad, dessert, ref id);
                return id.Value;
            }
        }

    }
}
