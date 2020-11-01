using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service_VocaNote" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service_VocaNote.svc or Service_VocaNote.svc.cs at the Solution Explorer and start debugging.
    public class Service_VocaNote : IService_VocaNote
    {
        DB_VocaForm DB_VOCAFORM;
        DataTable dataTable;
        public DataSet_VocaNote GetVocaNote(int Page_NO, int Page_SIZE, string userid, string OrderBy)
        {
            DB_VOCAFORM = new DB_VocaForm();

            dataTable = new DataTable();

            dataTable = DB_VOCAFORM.VocaNote(Page_NO, Page_SIZE, userid, OrderBy).Tables[0];

            int i = 0;

            DataSet_VocaNote dataSet_VocaNote = new DataSet_VocaNote();

            foreach (DataRow dr in dataTable.Rows) //0 : VocaNoteName, 1 : NickName, 2 : CrDateNote, 3 : TotalVocaCount
            {
                dataSet_VocaNote.VocaNoteName = dr[0].ToString().TrimEnd();
                dataSet_VocaNote.NickName = dr[1].ToString().TrimEnd();
                dataSet_VocaNote.CrDateNote = dr[2].ToString().TrimEnd();
                dataSet_VocaNote.TotalVocaCount = dr[3].ToString().TrimEnd();
                i++;
            }

            DB_VOCAFORM.Close();
            return dataSet_VocaNote;
        }
    }
}
