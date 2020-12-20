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
        //챕터 이름 중복 검사
        public bool VerifyChapterName(string nickname, string vocaname, string chaptername) //닉네임으로 구별한 후 단어장 중복 검사 (닉네임도 중복되지않아 아이디로 구별안했음)
        {
            //결과 반환용 변수
            bool result = true;
            //쿼리문 지정
            string mySql = "SELECT ChapterName FROM VocaNote WHERE nickname = '" + nickname + "' and VocaNoteName = '" + vocaname + "' and ChapterName = '" + chaptername + "'";

            //결과를 구해옴
            SqlDataReader myReader = this.ExecuteReader(mySql);

            if (myReader.Read()) result = false; // 해당 챕터 이미 존재

            //SqlDataReader 객체 소멸
            myReader.Close();

            return result;
        }
        //단어장에 null이 존재하지 않으므로 챕터를 새로운 레코드 추가해서 집어넣음.
        public bool InsertChapter(string userid, string nickname, string chaptername, string VocaNoteName, string firstCrDateNote)
        {

            bool result = true;

            string mysql = "INSERT INTO VocaNote" +
                " Values(3, '" + userid + "', '" + nickname + "','" + VocaNoteName + "','" + chaptername + "',0," +
                " (SELECT TOP 1 CrDateNote FROM VocaNote WHERE userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "')" + ",getdate(),'false')";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
        //단어장 생성날짜 저장하기위해 사용
        public string NoteCrDateNote(string uid, string VocaNoteName)
        {
            string CrDateNote = null;

            string mySql = "select VocaNoteName, CONVERT(date,CrDateNote) as CrDateNote from VocaNote" +
                " where userid = '" + uid + "' and VocaNoteName = '" + VocaNoteName + "'";

            SqlDataReader myReader = this.ExecuteReader(mySql);

            if (myReader.Read())
            {
                CrDateNote = myReader["CrDateNote"].ToString();
            }
            myReader.Close();
            return CrDateNote;
        }
        //단어 중복 확인을 위해
        public bool SelectVoca(string userid, string Voca, string VocaNoteName, string ChapterName)
        {
            bool result = true;

            //입력한 단어가 데이터베이스에 존재하는지 확인함.
            string mysql = "SELECT * FROM Voca WHERE userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "' and Voca = '" + Voca + "'";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false;//단어가 존재함.

            myReader.Close();
            return result;
        }
        public bool InsertVoca(string userid, string VocaNoteName, string ChapterName, string Voca, string Mean)
        {
            //단어가 존재하지 않아 단어를 추가한다. 
            //예문이 존재하지 않음, 해석도 당연히 존재하지 않음.
            bool result = true;

            string mysql = "INSERT INTO Voca" +
                " Values(4, '" + userid + "', '" + VocaNoteName + "','" + ChapterName + "','" + Voca + "','" + Mean + "',NULL,NULL,0,GETDATE())";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
        public bool InsertVoca(string userid, string VocaNoteName, string ChapterName, string Voca, string Mean, string Sentence)
        {
            //단어가 존재하지 않아 단어를 추가한다.
            //예문은 존재하지만 해석은 존재하지 않음.
            bool result = true;

            string mysql = "INSERT INTO Voca" +
                " Values(4, '" + userid + "', '" + VocaNoteName + "','" + ChapterName + "','" + Voca + "','" + Mean + "','" + Sentence + "',NULL,0,GETDATE())";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
        public bool InsertVoca(string userid, string VocaNoteName, string ChapterName, string Voca, string Mean, string Sentence, string Interpretation)
        {
            //단어가 존재하지 않아 단어를 추가한다.
            bool result = true;

            string mysql = "INSERT INTO Voca" +
                " Values(4, '" + userid + "', '" + VocaNoteName + "','" + ChapterName + "','" + Voca + "','" + Mean + "','" + Sentence + "','" + Interpretation + "',0,GETDATE())";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
        //단어 수를 수정하여 데이터베이스에 넣는다.
        public bool UpdateVocaCount(string userid, string VocaNoteName, string ChapterName)
        {
            bool result = true;

            string mysql = "UPDATE VocaNote SET VocaCount = (SELECT SUM(VocaCount + 1) VocaCount FROM VocaNote WHERE " +
                " userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "') " +
                " WHERE userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "'  and ChapterName = '" + ChapterName + "'";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false;

            myReader.Close();
            return result;
        }
        //단어가 존재하므로 수정하기 위해 데이터베이스에서 정보를 끌어옴
        public DataSet GetVoca(string userid, string Voca, string VocaNoteName, string ChapterName)
        {            

            string mysql = "SELECT * FROM Voca WHERE userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "' and Voca = '" + Voca + "'";

            return this.AdapterFill(mysql, "Voca");
        }
        public bool UpdateVoca(string userid, string Voca, string CMean, string CSentence, string CInterpretation)
        {
            //단어가 존재하므로 5가지를 수정한다. (단어,뜻,예문,해석,수정날짜)
            bool result = true;

            string mysql = "UPDATE Voca SET Mean = '" + CMean + "' WHERE userid = '" + userid + "' and Voca = '" + Voca + "'" +
                "  UPDATE Voca SET Sentence = '" + CSentence + "' WHERE userid = '" + userid + "' and Voca = '" + Voca + "'" +
                "  UPDATE Voca SET Interpretation = '" + CInterpretation + "' WHERE userid = '" + userid + "' and Voca = '" + Voca + "'" +
                "  UPDATE Voca SET Createdate = GETDATE() WHERE userid = '" + userid + "' and  Voca = '" + Voca + "'";
            //"  UPDATE Voca SET Voca = '" + CVoca + "' WHERE userid = '" + userid + "' and Voca ='" + Voca + "'"; 단어가 중복되서 수정하러왓는데 단어를 수정할리 없다.

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false;

            myReader.Close();
            return result;
        }

        //일반 단어장
        public bool DeleteVocaNote(string userid, string VocaNoteName)
        {

            bool result = true;

            string mysql = "delete From VocaNote where userid ='" + userid + "' and VocaNoteName = '" + VocaNoteName + "'" +
                "  DELETE FROM Voca Where userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "'";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
        //챕터 삭제
        public bool DeleteChapter(string userid, string VocaNoteName, string ChapterName)
        {

            bool result = true;

            string mysql = "delete From VocaNote where userid ='" + userid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "'";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }

        //단어 삭제
        public bool DeleteVoca(string userid, string VocaNoteName, string ChapterName, string voca)
        {

            bool result = true;

            string mysql = "delete From Voca where userid ='" + userid + "' and VocaNoteName = '" + VocaNoteName + "'" +
                "  and ChapterName = '" + ChapterName + "' and Voca = '" + voca + "'";

            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false; //해당 단어장 이미 존재

            myReader.Close();
            return result; // 존재 하지 않을때 true 보냄.
        }
        //챕터내 존재하는 단어 수를 가져온다.
        public int GetVocaCount(string uid, string VocaNoteName, string ChapterName)
        {
            int result = 0;
            string mySql = "SELECT VocaCount FROM VocaNote WHERE userid = '" + uid + "' and VocaNoteName = '" + VocaNoteName + "' and ChapterName = '" + ChapterName + "'";
            SqlDataReader myReader = this.ExecuteReader(mySql);

            if (myReader.Read())
            {
                result = int.Parse(myReader[0].ToString()); //VocaCount의 문자열을 숫자로 바꿈.
            }
            myReader.Close();

            return result;
        }
        //단어 수를 수정하여 데이터베이스에 넣는다.
        public bool UpdateVocaCount(string userid, string VocaNoteName, string ChapterName, string VocaCount)
        {
            bool result = true;

            string mysql = "UPDATE VocaNote SET VocaCount = " + VocaCount + " WHERE userid = '" + userid + "' and VocaNoteName = '" + VocaNoteName + "'  and ChapterName = '" + ChapterName + "'";
            SqlDataReader myReader = this.ExecuteReader(mysql);

            if (myReader.Read()) result = false;

            myReader.Close();
            return result;
        }
    }
}