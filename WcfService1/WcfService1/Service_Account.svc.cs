using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using WcfService1.Class;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service_Account" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service_Account.svc or Service_Account.svc.cs at the Solution Explorer and start debugging.
    public class Service_Account : IService_Account
    {
        private string conn = "Data Source = DESKTOP-DS7MIT5\\KTH2019; Initial Catalog=VOCA_KTH; uid=User; pwd=1";
        DB_Session dB_Session;

        public LoginCheck Authenticate(string id, string pwd)
        {
            dB_Session = new DB_Session();

            bool IsAuthenticated = dB_Session.Authenticate(id, pwd);

            //dB_Session.Close();

            if (IsAuthenticated)
            {
                LoginCheck loginCheck = new LoginCheck();
                loginCheck.Check = true;
                return loginCheck; //로그인 일치
            } 
            else
            {
                LoginCheck loginCheck = new LoginCheck();
                loginCheck.Check = false;
                return loginCheck; //불일치
            }
            
        }
    }
}
