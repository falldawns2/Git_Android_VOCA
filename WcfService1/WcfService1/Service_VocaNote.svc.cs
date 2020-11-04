using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Drawing;
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
        DB_VocaForm DB_VOCAFORM = new DB_VocaForm();
        DataTable dataTable = new DataTable();
        List<DataSet_VocaNote> dataSet_VocaNote;
        List<DataSet_Chapter> dataSet_Chapter;

        public List<DataSet_VocaNote> GetVocaNote(int Page_NO, int Page_SIZE, string userid, string OrderBy)
        {

            DB_VOCAFORM = new DB_VocaForm();            
            dataTable = DB_VOCAFORM.VocaNote(Page_NO, Page_SIZE, userid, OrderBy).Tables[0];
            dataSet_VocaNote = new List<DataSet_VocaNote>();

            //int i = 0; 

            foreach (DataRow dr in dataTable.Rows) //0 : VocaNoteName, 1 : NickName, 2 : CrDateNote, 3 : TotalVocaCount
            {
                /*dataSet_VocaNote.DataSet_VocaNote = dr[0].ToString().TrimEnd();
                dataSet_VocaNote.NickName = dr[1].ToString();
                dataSet_VocaNote.CrDateNote = dr[2].ToString();
                dataSet_VocaNote.TotalVocaCount = dr[3].ToString();           */
                dataSet_VocaNote
                    .Add(new DataSet_VocaNote
                    {
                        VocaNoteName = dr[0].ToString().TrimEnd(),
                        //NickName = dr[1].ToString().TrimEnd(),
                        //CrDateNote = dr[2].ToString().TrimEnd(),
                        TotalVocaCount = dr[3].ToString().TrimEnd()
                    });
                //i++;
            }
            return dataSet_VocaNote;
        }
        public List<DataSet_Chapter> GetChapter(int Page_NO, int Page_SIZE, string userid, string VocaNoteName, string OrderBy)
        {
            DB_VOCAFORM = new DB_VocaForm();
            dataTable = DB_VOCAFORM.Chapter(Page_NO, Page_SIZE, userid, VocaNoteName, OrderBy).Tables[0];
            dataSet_Chapter = new List<DataSet_Chapter>();

            foreach (DataRow dr in dataTable.Rows) //0 : ChapterName, 1 : NickName, 2 : CrDateChapter, 3 : CrDateNote, 4 : VocaCount
            {
                dataSet_Chapter
                    .Add(new DataSet_Chapter
                    {
                        ChapterName = dr[0].ToString().TrimEnd(),
                        VocaCount = dr[4].ToString().TrimEnd()
                    });
            }
            return dataSet_Chapter;
        }
    }
}
