﻿using System;
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
    }
}