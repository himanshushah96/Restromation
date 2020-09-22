namespace RESTRODBACCESS.ResponseModel
{
    public class UserLoginResponseModel
    {
        public string Email { get; set; }
        public string UserId { get; set; }
        public string firstName { get; set; }
        public string lastName { get; set; }
        public string Token { get; set; }
        public int UserTypeId { get; set; }
        public string TokenCreatedDate { get; set; }
        public string ExpireDate { get; set; }

        public string gender { get; set; }
        public string phoneNo { get; set; }

    //    public string UserType { get; set; }
    }
}
