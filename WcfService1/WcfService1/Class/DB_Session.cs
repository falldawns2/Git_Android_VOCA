using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Web;

namespace WcfService1.Class
{
    public class DB_Session
    {
        //SQL 서버 관련
        //string dataSrc = @"Server=DESKTOP-DS7MIT5\KTH2019; uid=user; pwd=1; database=VOCA_KTH";
        string dataSrc = "Data Source = DESKTOP-DS7MIT5\\KTH2019; Initial Catalog=VOCA_KTH; uid=User; pwd=1";

        SqlConnection myConn;

        public DB_Session()
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

        //로그인
        //사용자 인증 (로그인) -- > 인증 성공 : true, 인증 실패 : false
        public bool Authenticate(string id, string pwd)
        {
            bool isAuthen = false; //초기 리턴 미인증

            string sQuery = "SELECT passwd FROM members WHERE userid = '" + id + "'";

            SqlDataReader myReader = this.ExecuteReader(sQuery);

            //사용자 id 존재여부 확인
            if (myReader.Read())
            {
                //탈퇴 여부 확인 --- > 탈퇴 시 false 반환
                //if (bool.Parse(myReader["status"].Tostring())) return false;

                //암호 일치 여부 확인 -- md5 32문자 128bit 암호화
                MD5 pwdMD5 = new MD5CryptoServiceProvider();
                byte[] hash = pwdMD5.ComputeHash(Encoding.ASCII.GetBytes(pwd));

                StringBuilder stringBuilder = new StringBuilder();

                foreach (byte b in hash)
                    stringBuilder = stringBuilder.AppendFormat("{0:X2}", b);

                if (stringBuilder.ToString() == myReader["passwd"].ToString().TrimEnd())
                    isAuthen = true; //암호 일치 -- > 인증 성공
            }

            //SqlDataReader 객체를 닫음
            myReader.Close();

            //결과를 반환
            return isAuthen;
        }

        //프로필 이미지를 가져옴
        public string GetImage(string uid)
        {
            string profileimage = null;

            string mySql = "SELECT profileimage From members WHERE userid = '" + uid + "'";

            SqlDataReader myReader = this.ExecuteReader(mySql);

            if (myReader.Read())
            {
                profileimage = myReader["profileimage"].ToString().TrimEnd();
            }
            myReader.Close();
            return profileimage;
        }

        //userid를 이용하여, members 테이블에서 nickname을 읽어옴
        //메인 마스터 페이지에서 환영인사 표시하기 위함
        public string GetNickname(string uid)
        {
            string nickname = null; //리턴값 초기화

            string sQuery = "SELECT nickname FROM members WHERE userid = '" + uid + "'";

            SqlDataReader myReader = this.ExecuteReader(sQuery);
            //userid 존재 여부 확인 후, nickname 지정

            if (myReader.Read())
            {
                nickname = myReader["nickname"].ToString().TrimEnd();
            }
            myReader.Close(); // 오류 낫던 부분 실수로 myReader를 close 안했음
            return nickname;
        }
    }
}