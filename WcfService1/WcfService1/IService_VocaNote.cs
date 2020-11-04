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
}
