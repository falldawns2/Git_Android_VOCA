using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService_VocaNote" in both code and config file together.
    [ServiceContract]
    public interface IService_VocaNote
    {
        //POST 단어장 
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetVocaNote", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_VocaNote> GetVocaNote(int Page_NO, int Page_SIZE, string userid, string OrderBy);

        //POST 챕터
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetChapter", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_Chapter> GetChapter(int Page_NO, int Page_SIZE, string userid,string VocaNoteName, string OrderBy);

        //POST 스피너 추가용  (단어장 목록을 가져온다)
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetVocaNoteList", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_VocaNoteList> GetVocaNoteList(string userid, string OrderBy);

        //POST 스피너 추가용  (챕터 목록을 가져온다)
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetChapterList", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_ChapterList> GetChapterList(string userid, string VocaNoteName, string OrderBy);
        
        //POST 단어 뜻 (단어 카드)
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetVocaMean", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_Voca_Mean> GetVocaMean(string userid, string VocaNoteName, string ChapterName, string OrderBy);

        //POST 단어
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetVoca", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_Voca> GetVoca(int Page_NO, int Page_SIZE, string userid, string VocaNoteName, string ChapterName, string OrderBy);

        //단어장 추가
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "InsertVocaNote", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Int_VocaNoteAdd InsertVocaNote(string VocaTitle, string Userid, string Nickname, string In_Group);

        //챕터 추가
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "InsertChapter", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Int_ChapterAdd InsertChapter(string Userid, string NickName, string ChapterName, string VocaNoteName);

        //단어 추가
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "InsertVoca", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Int_VocaAdd InsertVoca(string Userid, string VocaNoteName, string ChapterName, string Voca, string Mean, string Sentence, string Interpretation);

        //단어 수정
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "UpdateVoca", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Int_VocaUpdate UpdateVoca(string Userid, string Voca, string Mean, string Sentence, string Interpretation);

        //단어장 삭제
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "DeleteVocaNote", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Int_VocaNoteDelete DeleteVocaNote(string userid, string VocaNoteName);

        //챕터 삭제
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "DeleteChapter", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Int_ChapterDelete DeleteChapter(string userid, string VocaNoteName, string ChapterName);
    }

    [DataContract]
    public class DataSet_VocaNote
    {
        string vocaNote; //단어장
        string nickName;
        string crDateNote;
        string totalVocaCount;

        //VocaNote 가져오는 테이블 스키마   
        //0 : VocaNoteName, 1 : NickName, 2 : CrDateNote, 3 : TotalVocaCount

        [DataMember]
        public string VocaNoteName
        {
            get { return vocaNote; }
            set { vocaNote = value; }
        }
        /*[DataMember]
        public string NickName
        {
            get { return nickName; }
            set { nickName = value; }
        }
        [DataMember]
        public string CrDateNote
        {
            get { return crDateNote; }
            set { crDateNote = value; }
        }*/
        [DataMember]
        public string TotalVocaCount
        {
            get { return totalVocaCount; }
            set { totalVocaCount = value; }
        }        
    }
    public class DataSet_Chapter
    {
        string chapterName; //챕터 이름
        string vocaCount; //챕터 당 단어 수

        //Chapter 테이블 스키마
        //0 : ChapterName, 1 : NickName, 2 : CrDateChapter, 3 : CrDateNote, 4 : VocaCount

        [DataMember]
        public string ChapterName
        {
            get { return chapterName; }
            set { chapterName = value; }
        }

        [DataMember]
        public string VocaCount
        {
            get { return vocaCount; }
            set { vocaCount = value; }
        }
    }
    public class DataSet_VocaNoteList
    {
        // 0 : VocaNoteName, 1 : NickName, 2 : CrDateNote, 3 : TotalVocaCount

        string vocaNoteName;
        
        [DataMember]
        public string VocaNoteName
        {
            get { return vocaNoteName;}
            set { vocaNoteName = value; }
        }
    }
    public class DataSet_ChapterList
    {
        // 0 : ChapterName, 1 : NickName, 2 : CrDateChapter, 3 : CrDateNote, 4 : VocaCount

        string chapterName;

        [DataMember]
        public string ChapterName
        {
            get { return chapterName; }
            set { chapterName = value; }
        }
    }
    public class DataSet_Voca_Mean
    {
        // 0 : no, 1 : bbsid, 2 : userid, 3 : VocaNoteName, 4 : ChapterName, 5 : Voca, 6 : Mean

        string voca;
        string mean;
        string sentence;
        string interpretation;

        [DataMember]
        public string Voca
        {
            get { return voca; }
            set { voca = value; }
        }
        [DataMember]
        public string Mean
        {
            get { return mean; }
            set { mean = value; }
        }
        [DataMember]
        public string Sentence
        {
            get { return sentence; }
            set { sentence = value; }
        }
        [DataMember]
        public string Interpretation
        {
            get { return interpretation; }
            set { interpretation = value; }
        }
    }
    public class DataSet_Voca
    {
        // 0 : no, 1 : bbsid, 2 : userid, 3 : VocaNoteName, 4 : ChapterName, 5 : Voca, 6: Mean, 7 : Sentence, 8 : Interpretation, 9 : Interpretaion, 10 : Complete, 11 : CreateDate

        string voca;
        string mean;
        string sentence;
        string interpretation;

        [DataMember]
        public string Voca
        {
            get { return voca; }
            set { voca = value; }
        }
        [DataMember]
        public string Mean
        {
            get { return mean; }
            set { mean = value; }
        }
        [DataMember]
        public string Sentence
        {
            get { return sentence; }
            set { sentence = value; }
        }
        [DataMember]
        public string Interpretation
        {
            get { return interpretation; }
            set { interpretation = value; }
        }
    }
    public class Int_VocaNoteAdd
    {
        //

        int check; //결과값 반환
        //0: 성공, 1: 한 글자, 2: 실패

        [DataMember]
        public int Check
        {
            get { return check; }
            set { check = value; }
        }
    }
    public class Int_ChapterAdd
    {
        //

        int check; //결과값 반환
        //0: 성공, 1: 한 글자, 2: 실패

        [DataMember]
        public int Check
        {
            get { return check; }
            set { check = value; }
        }
    }
    public class Int_VocaAdd
    {
        int check;
        //0 : 성공, 1 : 단어 빈칸, 2 : 뜻 빈칸, 3 : 중복 존재

        //만약 중복 존재하면 4가지 다 보내야함
        string voca;
        string mean;
        string sentence;
        string interpretation;

        [DataMember]
        public int Check
        {
            get { return check; }
            set { check = value; }
        }
        [DataMember]
        public String Update_Voca
        {
            get { return voca; }
            set { voca = value; }
        }
        [DataMember]
        public String Update_Mean
        {
            get { return mean; }
            set { mean = value; }
        }
        [DataMember]
        public String Update_Sentence
        {
            get { return sentence; }
            set { sentence = value; }
        }
        [DataMember]
        public String Update_Interpretation
        {
            get { return interpretation; }
            set { interpretation = value; }
        }
    }
    public class Int_VocaUpdate
    {
        int check;
        //0 : 성공, 1 : 에러

        [DataMember]
        public int Check
        {
            get { return check; }
            set { check = value; }
        }      
    }
    public class Int_VocaNoteDelete
    {
        int check;
        //0 : 성공, 1 : 에러

        [DataMember]
        public int Check
        {
            get { return check; }
            set { check = value; }
        }
    }
    public class Int_ChapterDelete
    {
        int check;

        //0 : 성공, 1 : 에러

        [DataMember]
        public int Check
        {
            get { return check; }
            set { check = value; }
        }
    }
}
