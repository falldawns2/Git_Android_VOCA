using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService_Account" in both code and config file together.
    [ServiceContract]
    public interface IService_Account
    {
        //POST 로그인 (계정 확인)
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "Authenticate", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        LoginCheck Authenticate(string id, string pwd); //인수 명칭 = 가져올 json 형태 이름

        //POST 프로필 이미지 
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetProfileImage", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        Image ProfileImage(string id); //base64 -- 수정 이미지 링크를 통해 받음.

        //POST 계정 닉네임
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetNickName", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        GetNickName NickName(string id);

    }

    [DataContract]
    public class LoginCheck //json //xml
    {
        bool check; //결과값을 반환한다.

        [DataMember]
        public bool Check //반환할 json 형태 이름
        {
            get { return check; }
            set { check = value; }
        }
    }

    public class Image
    {
        string profileImage; //프로필이미지

        [DataMember] //base64 형태 //수정 2020.11.01/ 00:59. - > 서비스 폴더 프로필 이미지 경로를 가져옴.
        public string ProfileImage
        {
            get { return profileImage; }
            set { profileImage = value; }
        }
    }

    public class GetNickName
    {
        string nickName; //닉네임

        [DataMember]
        public string NickName
        {
            get { return nickName; }
            set { nickName = value; }
        }
    }          
}
