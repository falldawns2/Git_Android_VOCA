using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace WcfService1
{
    public class DB_VocaForm
    {
        //SQL 서버 관련
        //string dataSrc = @"Server=DESKTOP-DS7MIT5\KTH2019; uid=user; pwd=1; database=VOCA_KTH";
        string dataSrc = "Data Source = DESKTOP-DS7MIT5\\KTH2019; Initial Catalog=VOCA_KTH; uid=User; pwd=1";

        SqlConnection myConn;

        public DB_VocaForm()
        {
            myConn = new SqlConnection(dataSrc);
            myConn.Open();
        }

        //DB 닫기
        public void Close()
        {
            SqlConnection.ClearAllPools(); //풀 오류 임시방편
            myConn.Close();
        }

        //결과 없는 쿼리문 실행 --> Insert, Update, Delete

        public void ExecuteNonQuery(string sQuery)
        {
            SqlCommand myCmd = new SqlCommand(sQuery, myConn);
            myCmd.ExecuteNonQuery();
        }

        //SqlDataReader 객체 반환 쿼리문 실행 --> Select
        public SqlDataReader ExecuteReader(string sQuery)
        {
            SqlCommand myCmd = new SqlCommand(sQuery, myConn);
            return myCmd.ExecuteReader();
        }

        //DataSet 객체 반환 쿼리문 실행 -- > Select
        public DataSet AdapterFill(string sQuery, string Tablename)
        {
            SqlDataAdapter myAdapter = new SqlDataAdapter(sQuery, myConn);

            DataSet mySet = new DataSet(Tablename);
            myAdapter.Fill(mySet, Tablename);
            return mySet;
        }

        //Winform에만 존재
        //내 단어장 목록을 가져온다. (그룹 제외)
        public DataSet VocaNote(int Page_NO, int Page_SIZE, string userid, string OrderBy)
        {
            //쿼리문 작성            
            string mySql = "DECLARE @PAGE_NO int = " + Page_NO + "" +
                " DECLARE @PAGE_SIZE int = " + Page_SIZE + "" +
                " SELECT VocaNoteName, NickName, CrDateNote, sum(VocaCount) 'TotalVocaCount'" +
                " FROM VocaNote" +
                " WHERE userid = '" + userid + "' and VocaNoteName NOT LIKE '%Group_" + userid + "%'" +
                " GROUP BY Nickname, VocaNoteName, CrDateNote" +
                " ORDER BY " + OrderBy + "" +
                " OFFSET (@PAGE_NO -1)*@PAGE_SIZE ROW" +
                " FETCH NEXT @PAGE_SIZE ROW ONLY";

            //실행 및 결과 반환
            return this.AdapterFill(mySql, "VocaNote");
        }

        //Winform에만 존재
        //단어장의 챕터 목록을 가져온다.
        public DataSet Chapter(int Page_NO, int Page_SIZE, string uid, string VocaNoteName, string OrderBy) // **단어장 챕터 목록을 가져온다** 단어장과 따로 두는 이유 : sum 함수에서 문제 생김.
        {
            //쿼리문 작성
            string mySql = "DECLARE @PAGE_NO int = " + Page_NO + "" +
                " DECLARE @PAGE_SIZE int = " + Page_SIZE + "" +
                " SELECT ChapterName, NickName, CrDateChapter, CrDateNote, VocaCount" +
                " FROM VocaNote" +
                " WHERE userid = '" + uid + "' and VocaNoteName = '" + VocaNoteName + "'" +
                " ORDER BY CrdateChapter DESC" +
                " OFFSET (@PAGE_NO -1)*@PAGE_SIZE ROW" +
                " FETCH NEXT @PAGE_SIZE ROW ONLY";

            //실행 및 결과 반환
            return this.AdapterFill(mySql, "Chapter");
        }
        //단어장 목록을 가져온다
        public DataSet VocaNote(string uid, string OrderBy) // **단어장 목록을 가져온다** 단어장 테이블 이름 : WordBook, 챕터 : VocaNote , 단어 : Voca ,수정 2019-01-28
        {

            //쿼리문 작성
            //string mySql = "SELECT * FROM VocaNote WHERE userid = '" + uid + "' order by CrDateNote desc";
            string mySql = "SELECT VocaNoteName,NickName,CrDateNote,sum(VocaCount) 'TotalVocaCount' FROM VocaNote" +
                " WHERE userid ='" + uid + "' and VocaNoteName NOT LIKE ('%Group_" + uid + "%') GROUP BY NickName,VocaNoteName,CrDateNote ORDER BY  " + OrderBy + "";

            //실행 및 결과 반환
            return this.AdapterFill(mySql, "VocaNote");
        }
        public DataSet Chapter(string uid, string VocaNoteName, string OrderBy) // **단어장 챕터 목록을 가져온다** 단어장과 따로 두는 이유 : sum 함수에서 문제 생김.
        {
            //쿼리문 작성
            string mySql = "SELECT ChapterName,NickName,CrDateChapter,CrDateNote,VocaCount FROM VocaNote WHERE userid ='" + uid + "'" +
                " and VocaNoteName = '" + VocaNoteName + "' ORDER BY " + OrderBy + "";

            //실행 및 결과 반환
            return this.AdapterFill(mySql, "Chapter");
        }
        //단어장 + 챕터 (단어 카드)
        public DataSet Word(string uid, string VocaNoteName, string ChapterName, string OrderBy) // **단어 목록을 가져온다**
        {
            //쿼리문 작성
            string mySql = "SELECT * FROM Voca WHERE userid= '" + uid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "' Order by  " + OrderBy + "";

            //실행 및 결과 반환
            return this.AdapterFill(mySql, "Word");
        }

        //단어
        public DataSet Word(int Page_NO, int Page_SIZE, string uid, string VocaNoteName, string ChapterName, string OrderBy) // **단어 목록을 가져온다**
        {
            //쿼리문 작성
            string mySql = "DECLARE @PAGE_NO int = " + Page_NO + "" +
                " DECLARE @PAGE_SIZE int = " + Page_SIZE + "" +
                " SELECT * " +
                " FROM Voca WHERE userid= '" + uid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "'" +
                " Order by  " + OrderBy + "" +
                " offset (@PAGE_NO - 1) * @PAGE_SIZE ROW" +
                " FETCH NEXT @PAGE_SIZE ROW ONLY";

            //실행 및 결과 반환
            return this.AdapterFill(mySql, "Word");
        }

        //단어장 이름 중복 검사
        public bool VerifyVocaName(string nickname, string vocaname) //닉네임으로 구별한 후 단어장 중복 검사 (닉네임도 중복되지않아 아이디로 구별안했음)
        {
            //결과 반환용 변수
            bool result = true;
            //쿼리문 지정
            string mySql = "SELECT * FROM VocaNote WHERE nickname = '" + nickname + "' and VocaNoteName = '" + vocaname + "'";

            //결과를 구해옴
            SqlDataReader myReader = this.ExecuteReader(mySql);

            if (myReader.Read()) result = false; // 해당 단어장 이미 존재

            //SqlDataReader 객체 소멸
            myReader.Close();

            return result;
        }
        //**단어장을 데이터베이스에 추가한다** 2019-01-28 수정 다시 수정
        public void InsertVocaTable(string VocaTitle, string Userid, string Nickname, string In_Group)
        {
            SqlCommand myCmd = new SqlCommand("procInsertVocaNote", myConn);
            myCmd.CommandType = CommandType.StoredProcedure;

            SqlParameter param;
            param = new SqlParameter("@VocaNoteName", SqlDbType.NVarChar, 50);
            param.Value = VocaTitle;
            myCmd.Parameters.Add(param);

            param = new SqlParameter("@userid", SqlDbType.VarChar, 15);
            param.Value = Userid;
            myCmd.Parameters.Add(param);

            param = new SqlParameter("@nickname", SqlDbType.NChar, 10);
            param.Value = Nickname;
            myCmd.Parameters.Add(param);

            param = new SqlParameter("@Group", SqlDbType.Bit); // group 단어장인지 아닌지 판단, false : 그룹 아님, true : 그룹 단어장.
            param.Value = In_Group;
            myCmd.Parameters.Add(param);

            myCmd.ExecuteNonQuery();
        }
        //단어장명을 변경함.
        public bool VocaNote_Name_Change(string userid, string VocaNoteName, string ChangeVocaNoteName)
        {

            bool result = true;

            string mysql = "UPDATE VocaNote set VocaNoteName = '" + ChangeVocaNoteName + "' where userid = '" + userid + "'" +
                " and VocaNoteName = '" + VocaNoteName + "'" +
                "    UPDATE Voca set VocaNoteName = '" + ChangeVocaNoteName + "' Where userid = '" + userid + "'" +
                " and VocaNoteName = '" + VocaNoteName + "'";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
    }
}