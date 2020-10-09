using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service1 : IService1
    {
        /*public string GetData(int value)
        {
            return string.Format("You entered: {0}", value);
        }

        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }*/
        //Connection string //
        private string conn = "Data Source =.; Initial Catalog=VOCA_KTH; uid=User; pwd=1";

        public List<Members> GetMembers()
        {
            List<Members> MemberList = new List<Members>();
            SqlConnection connection = new SqlConnection(this.conn);
            connection.Open();
            SqlCommand cmd = new SqlCommand("Select * From Members", connection);
            cmd.CommandType = CommandType.Text;
            SqlDataReader reader = cmd.ExecuteReader();

            while (reader.Read())
            {
                Members members = new Members();
                members.Userid = reader["userid"].ToString();

                MemberList.Add(members);
            }

            return MemberList.ToList();
        }

        public int AddMembers(string userid)
        {
            int status = 0;
            SqlConnection connection = new SqlConnection(this.conn);
            SqlCommand cmd = new SqlCommand();
            try
            {                
                if (connection.State == ConnectionState.Closed)
                    connection.Open();

                cmd = new SqlCommand("Insert into Members (userid, passwd, name, nickname, email, mygroup, profileimage, filesize, joindate, [status], ugrade)" +
                    " values (@userid,'FBADE9E36A3F36D3D676C1B808451DD7','테스트','닉네임','t@t.t','Group_none','SampleImage.PNG',0,'2020-09-06 23:12:00',0,'일반')", connection);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.AddWithValue("@userid", userid);
                cmd.ExecuteReader();
                status = 1;
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                connection.Close();
                cmd.Dispose();
            }
            return status;
        }
    }

    
}
