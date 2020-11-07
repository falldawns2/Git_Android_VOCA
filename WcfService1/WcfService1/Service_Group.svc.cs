using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using WcfService1.Class;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service_Group" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service_Group.svc or Service_Group.svc.cs at the Solution Explorer and start debugging.
    public class Service_Group : IService_Group
    {
        DB_Group DBDB_Group = new DB_Group();
        DataTable dataTable = new DataTable();
        List<DataSet_Group> dataSet_Group;

        //단어장
        public List<DataSet_Group> GetGroup(int Page_NO, int Page_SIZE)
        {

            DBDB_Group = new DB_Group();
            dataTable = DBDB_Group.Get_Group(Page_NO, Page_SIZE).Tables[0];
            dataSet_Group = new List<DataSet_Group>();

            //int i = 0; 

            foreach (DataRow dr in dataTable.Rows) 
            {                
                dataSet_Group
                    .Add(new DataSet_Group
                    {
                        GroupName = dr[1].ToString().TrimEnd(),
                        GroupImage = dr[5].ToString().TrimEnd()
                    });
                //i++;
            }

            DBDB_Group.Close();
            return dataSet_Group;
        }
    }
}
