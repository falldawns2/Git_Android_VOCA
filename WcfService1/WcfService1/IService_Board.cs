using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService_Board" in both code and config file together.
    [ServiceContract]
    public interface IService_Board
    {
        //게시판 글 가져오기
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetBoard", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_Board> GetBoard(int Page_NO, int Page_SIZE);
    }

    [DataContract]
    public class DataSet_Board
    {
        string title;
        string uploadtime;
        int hits;
        int comments;
        string nickname;
        string profileimage;

        //가져오는 테이블 스키마   
        //0 : profileimage, 1 : userid, 2 : title, 3 : contents, 4 : uploadtime, 5 : hits, 6 : commnets, 7 : noti, 8 : filename,  9 : filesize, 10 : nickname, 11 : no

        [DataMember]
        public string Profileimage
        {
            get { return profileimage; }
            set { profileimage = value; }
        }
        [DataMember]
        public string Title
        {
            get { return title; }
            set { title = value; }
        }
        [DataMember]
        public string Uploadtime
        {
            get { return uploadtime; }
            set { uploadtime = value; }
        }
        [DataMember]
        public int Hits
        {
            get { return hits; }
            set { hits = value; }
        }
        [DataMember]
        public int Comments
        {
            get { return comments; }
            set { comments = value; }
        }
        [DataMember]
        public string Nickname
        {
            get { return nickname; }
            set { nickname = value; }
        }

    }
}
