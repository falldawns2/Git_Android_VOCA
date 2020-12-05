using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Runtime.Remoting.Messaging;
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
        List<DataSet_VocaNoteList> dataSet_VocaNoteList;
        List<DataSet_ChapterList> dataSet_ChapterList;
        List<DataSet_Voca_Mean> dataSet_Voca_Mean;
        List<DataSet_Voca> dataSet_Voca;

        static bool isIdDuChecked = false; //단어장 중복 검사

        //단어장
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

            DB_VOCAFORM.Close();
            return dataSet_VocaNote;
        }
        //챕터
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

            DB_VOCAFORM.Close();
            return dataSet_Chapter;
        }
        //단어장 목록 (스피너)
        public List<DataSet_VocaNoteList> GetVocaNoteList(string userid, string OrderBy)
        {
            DB_VOCAFORM = new DB_VocaForm();
            dataTable = DB_VOCAFORM.VocaNote(userid, OrderBy).Tables[0];
            dataSet_VocaNoteList = new List<DataSet_VocaNoteList>();

            foreach (DataRow dr in dataTable.Rows)
            {
                dataSet_VocaNoteList
                    .Add(new DataSet_VocaNoteList
                    {
                        VocaNoteName = dr[0].ToString()
                    });
            }

            DB_VOCAFORM.Close();
            return dataSet_VocaNoteList;
        }
        //챕터 목록 (스피너)
        public List<DataSet_ChapterList> GetChapterList(string userid, string VocaNoteName, string OrderBy)
        {
            DB_VOCAFORM = new DB_VocaForm();
            dataTable = DB_VOCAFORM.Chapter(userid, VocaNoteName, OrderBy).Tables[0];
            dataSet_ChapterList = new List<DataSet_ChapterList>();

            foreach (DataRow dr in dataTable.Rows)
            {
                dataSet_ChapterList
                    .Add(new DataSet_ChapterList
                    {
                        ChapterName = dr[0].ToString()
                    });
            }

            DB_VOCAFORM.Close();
            return dataSet_ChapterList;
        }
        //단어, 뜻
        public List<DataSet_Voca_Mean> GetVocaMean(string userid, string VocaNoteName, string ChapterName, string OrderBy)
        {
            DB_VOCAFORM = new DB_VocaForm();
            dataTable = DB_VOCAFORM.Word(userid, VocaNoteName, ChapterName, OrderBy).Tables[0];
            dataSet_Voca_Mean = new List<DataSet_Voca_Mean>();

            foreach (DataRow dr in dataTable.Rows)
            {
                dataSet_Voca_Mean
                    .Add(new DataSet_Voca_Mean
                    {
                        Voca = dr[5].ToString(),
                        Mean = dr[6].ToString(),
                        Sentence = dr[7].ToString(),
                        Interpretation = dr[8].ToString()
                    });
            }

            DB_VOCAFORM.Close();
            return dataSet_Voca_Mean;
        }      
        //단어
        public List<DataSet_Voca> GetVoca(int Page_NO, int Page_SIZE, string userid, string VocaNoteName, string ChapterName, string OrderBy)
        {
            DB_VOCAFORM = new DB_VocaForm();
            dataTable = DB_VOCAFORM.Word(Page_NO, Page_SIZE, userid, VocaNoteName, ChapterName, OrderBy).Tables[0];
            dataSet_Voca = new List<DataSet_Voca>();

            foreach (DataRow dr in dataTable.Rows)
            {
                dataSet_Voca
                    .Add(new DataSet_Voca
                    {
                        Voca = dr[5].ToString(),
                        Mean = dr[6].ToString(),
                        Sentence = dr[7].ToString(),
                        Interpretation = dr[8].ToString()
                    });
            }

            DB_VOCAFORM.Close();
            return dataSet_Voca;
        }

        //단어장 추가
        public Int_VocaNoteAdd InsertVocaNote(string VocaTitle, string Userid, string Nickname, string In_Group)
        {
            DB_VOCAFORM = new DB_VocaForm();
            Int_VocaNoteAdd int_VocaNoteAdd = new Int_VocaNoteAdd();

            //단어장 이름을 변수에 담아 중복검사 실행
            string VocaNoteName = VocaTitle;

            if (VocaNoteName.Length <= 0)
            {
                DB_VOCAFORM.Close();
                int_VocaNoteAdd.Check = 1; //1 : 한 글자 이상이어야 한다.
                return int_VocaNoteAdd;
            }

            if (isIdDuChecked = DB_VOCAFORM.VerifyVocaName(Nickname,VocaTitle))
            {
                //중복검사 통과 --> 단어장 추가
                DB_VOCAFORM.InsertVocaTable(VocaTitle, Userid, Nickname, In_Group);

                //return 통과                
                DB_VOCAFORM.Close();
                int_VocaNoteAdd.Check = 0; //통과
                return int_VocaNoteAdd;
            }
            else
            {
                //중복검사 실패
                DB_VOCAFORM.Close();
                int_VocaNoteAdd.Check = 2; //이 단어장은 이미 있습니다.                
                return int_VocaNoteAdd;
            }            
        }
        //챕터 추가
        public Int_ChapterAdd InsertChapter(string Userid, string NickName, string ChapterName, string VocaNoteName)
        {
            DB_VOCAFORM = new DB_VocaForm();
            Int_ChapterAdd int_ChapterAdd = new Int_ChapterAdd();

            //챕터 이름을 변수에 담아 중복검사 실행
            
            if (ChapterName.Length <= 0)
            {
                DB_VOCAFORM.Close();
                int_ChapterAdd.Check = 1; //1: 두 글자 이상이어야 한다.
                return int_ChapterAdd;
            }

            if (isIdDuChecked = DB_VOCAFORM.VerifyChapterName(NickName,VocaNoteName,ChapterName))
            {
                //중복검사 통과 --- > 챕터 추가
                //DB_VOCAFORM.NoteCrDateNote(Userid, VocaNoteName)
                //string Date = DB_VOCAFORM.NoteCrDateNote(Userid, VocaNoteName);
                DB_VOCAFORM.InsertChapter(Userid, NickName, ChapterName, VocaNoteName, "1234");

                //return 통과
                DB_VOCAFORM.Close();
                int_ChapterAdd.Check = 0; //통과
                return int_ChapterAdd;
            }
            else
            {
                //중복검사 실패
                DB_VOCAFORM.Close();
                int_ChapterAdd.Check = 2; //이 단어장은 이미 있습니다.
                return int_ChapterAdd;
            }
        }
        public Int_VocaAdd InsertVoca(string Userid, string VocaNoteName, string ChapterName, string Voca, string Mean, string Sentence, string Interpretation)
        {
            DB_VOCAFORM = new DB_VocaForm();
            Int_VocaAdd int_VocaAdd = new Int_VocaAdd();

            //0 : 성공, 1 : 단어 빈칸, 2 : 뜻 빈칸, 3 : 중복 존재

            if (Voca.Length <= 0)
            {
                DB_VOCAFORM.Close();
                int_VocaAdd.Check = 1; //1: 단어 빈칸
                return int_VocaAdd;
            }
            else if (Mean.Length <= 0)
            {
                DB_VOCAFORM.Close();
                int_VocaAdd.Check = 2; //2: 뜻 빈칸
                return int_VocaAdd;
            }

            //단어 이름 중복 검사 하는 이유 : 단어가 중복이다 - > 수정하게 DB에서 불러와서 Update 하도록 한다.
            if (isIdDuChecked = DB_VOCAFORM.SelectVoca(Userid,Voca,VocaNoteName,ChapterName))
            {
                if (Sentence.ToString() == "")
                {
                    //예문이 없다 (= 해석도 없다.)
                    DB_VOCAFORM.InsertVoca(Userid, VocaNoteName, ChapterName, Voca, Mean);
                }
                else if (Interpretation.ToString() == "")
                {
                    //해석 x (하지만 예문 존재)
                    DB_VOCAFORM.InsertVoca(Userid, VocaNoteName, ChapterName, Voca, Mean, Sentence);
                }
                else
                {
                    //단어 중복 x = > 단어 추가
                    DB_VOCAFORM.InsertVoca(Userid, VocaNoteName, ChapterName, Voca, Mean, Sentence, Interpretation);
                }

                //단어 추가 됨 - > 단어 카운트  + 1
                DB_VOCAFORM.UpdateVocaCount(Userid, VocaNoteName, ChapterName); //수정 반영
                //return 통과
                DB_VOCAFORM.Close();
                int_VocaAdd.Check = 0; //통과
                return int_VocaAdd;
            }
            else
            {
                //중복 존재
                //DB에서 값을 가져와, 각각의 위치에 값을 뿌린다. (수정을 위해)
                int_VocaAdd.Check = 3;  //중복 존재

                dataTable = DB_VOCAFORM.GetVoca(Userid, Voca, VocaNoteName, ChapterName).Tables[0];

                DataRow dr = dataTable.Rows[0];

                int_VocaAdd.Update_Voca = dr[5].ToString();
                int_VocaAdd.Update_Mean = dr[6].ToString();
                int_VocaAdd.Update_Sentence = dr[7].ToString();
                int_VocaAdd.Update_Interpretation = dr[8].ToString();

                DB_VOCAFORM.Close();
                return int_VocaAdd;
            }
        }
        //단어 중복으로 인해 (뜻, 예문, 해석) 을 수정하도록 한다.
        public Int_VocaUpdate UpdateVoca(string Userid, string Voca, string Mean, string Sentence, string Interpretation)
        {
            DB_VOCAFORM = new DB_VocaForm();

            Int_VocaUpdate int_VocaUpdate = new Int_VocaUpdate();

            //업데이트
            bool a = DB_VOCAFORM.UpdateVoca(Userid, Voca, Mean, Sentence, Interpretation);
            
            int check = 0; //기본값 성공
            
            //실패하면
            if (!a) check = 1; //실패            

            int_VocaUpdate.Check = check;

            DB_VOCAFORM.Close();

            return int_VocaUpdate;
        }
    }
}
