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
        //POST 로그인
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "Authenticate", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        bool Authenticate(string id, string pwd);
    }

    [DataContract]
    public class LoginCheck //json //xml 형식
    {
        string userid;
        string passwd;

        [DataMember]
        public string Userid
        {
            get { return userid; }
            set { userid = value; }
        }

        public string Passwd
        {
            get { return passwd; }
            set { passwd = value; }
        }
    }
           
}
