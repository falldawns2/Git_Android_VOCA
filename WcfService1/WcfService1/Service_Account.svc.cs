using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Channels;
using System.Text;
using System.Web.Hosting;
using System.Web.UI.WebControls;
using WcfService1.Class;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service_Account" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service_Account.svc or Service_Account.svc.cs at the Solution Explorer and start debugging.
    public class Service_Account : IService_Account
    {
        //private string conn = "Data Source = DESKTOP-DS7MIT5\\KTH2019; Initial Catalog=VOCA_KTH; uid=User; pwd=1";

        DB_Session dB_Session;
        ImageFormat imageFormat;

        public LoginCheck Authenticate(string id, string pwd)
        {
            dB_Session = new DB_Session();

            bool IsAuthenticated = dB_Session.Authenticate(id, pwd);

            dB_Session.Close();

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

        public Image ProfileImage(string id) //base64 형태로 반환
        {
            dB_Session = new DB_Session();

            string base64ProfileImage;

            string path = @"E:\OneDrive\Git_ASP_VOCA\ProfileImage\" + dB_Session.GetImage(id);

            System.Drawing.Image ProfileImage;

            string ImageName = dB_Session.GetImage(id); //이미지 확장자 담기위해
            ImageName = ImageName.Substring(ImageName.LastIndexOf('.') + 1); //확장자만 담음 PNG, JPG 등


            /*ImageFormat imageFormat = 
            (ImageFormat)typeof(ImageFormat).GetProperty(ImageName, BindingFlags.Public | BindingFlags.Static | BindingFlags.IgnoreCase)
                .GetValue(ImageName, null);*/

            imageFormat = new ImageFormat(Guid.NewGuid());
            switch(ImageName.ToLower())
            {
                case "jpg":
                case "jpeg":
                    imageFormat = ImageFormat.Jpeg;
                    break;
                case "png":
                    imageFormat = ImageFormat.Png;
                    break;
            }


            //파일스트림으로 이미지 가져와 Image 객체에 담음.
            using (FileStream fs = new FileStream(path, FileMode.Open))
            {
                ProfileImage = System.Drawing.Image.FromStream(fs);
                fs.Close();
            }

            //메모리스트림으로 이미지를 byte로 변환 
            //base64 인코딩값을 string으로 변환
            using (MemoryStream ms = new MemoryStream())
            {
                // Convert Image to byte[]
                Bitmap bitmap = new Bitmap(ProfileImage);
                //ProfileImage.Save(ms, ImageFormat.Png);
                bitmap.Save(ms, ImageFormat.Png);
                byte[] imageBytes = ms.ToArray();

                base64ProfileImage = Convert.ToBase64String(imageBytes);
            }

            //json 형식으로 담기위해 저장 후 보냄.
            Image base64 = new Image();
            base64.ProfileImage = base64ProfileImage;

            return base64;
        }
    }
}
