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
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service_Board" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service_Board.svc or Service_Board.svc.cs at the Solution Explorer and start debugging.
    public class Service_Board : IService_Board
    {
        DB_Board DBDBDB_BOARD = new DB_Board();
        DataTable dataTable = new DataTable();

        List<DataSet_Board> dataSet_Board;

        //게시판
        public List<DataSet_Board> GetBoard(int Page_NO, int Page_SIZE)
        {
            DBDBDB_BOARD = new DB_Board();

            dataTable = DBDBDB_BOARD.board_View(Page_NO, Page_SIZE).Tables[0];
            dataSet_Board = new List<DataSet_Board>();

            foreach (DataRow dr in dataTable.Rows)
            {
                dataSet_Board
                    .Add(new DataSet_Board
                    {
                        Profileimage = dr[0].ToString(),
                        Nickname = dr[10].ToString().TrimEnd(),
                        Title = dr[2].ToString(),
                        Uploadtime = dr[4].ToString(),
                        Hits = int.Parse(dr[5].ToString()),
                        Comments = int.Parse(dr[6].ToString())
                    });
            }

            DBDBDB_BOARD.Close();
            return dataSet_Board;
        }
    }
}
