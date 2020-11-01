﻿using System;
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
        DataSet_VocaNote GetVocaNote(int Page_NO, int Page_SIZE, string userid, string OrderBy);
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

        public string NickName
        {
            get { return nickName; }
            set { nickName = value; }
        }

        public string CrDateNote
        {
            get { return crDateNote; }
            set { crDateNote = value; }
        }

        public string TotalVocaCount
        {
            get { return totalVocaCount; }
            set { totalVocaCount = value; }
        }        
    }
}
