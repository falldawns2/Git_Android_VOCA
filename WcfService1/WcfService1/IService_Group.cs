using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService_Group" in both code and config file together.
    [ServiceContract]
    public interface IService_Group
    {
        //POST 단어 뜻 (단어 카드)
        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "GetGroup", BodyStyle = WebMessageBodyStyle.WrappedRequest, RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<DataSet_Group> GetGroup(int Page_NO, int Page_SIZE);
    }

    [DataContract]
    public class DataSet_Group
    {
        string groupName; //그룹명
        string groupImage; //그룹 사진        

        //0 : no, 1 : groupName, 2 : groupcreatedate, 3 : leader, 4 : groupPassword, 5 : groupImage, 6 : groupcontents

        [DataMember]
        public string GroupName
        {
            get { return groupName; }
            set { groupName = value; }
        }
        [DataMember]
        public string GroupImage
        {
            get { return groupImage; }
            set { groupImage = value; }
        }        
    }
}
