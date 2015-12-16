using System.Runtime.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "credentials")]
    public class AuthCredentials
    {
        [DataMember(Name = "username")]
        public string Username { get; set; }

        [DataMember(Name = "password")]
        public string Password { get; set; }
    }

    [DataContract(Name = "session")]
    public class AuthSession
    {
        [DataMember(Name = "id")]
        public string Id { get; set; }

        [DataMember(Name = "user_id")]
        public string UserId { get; set; }

        [DataMember(Name = "auth_timestamp")]
        public long Authenticated { get; set; }

        [DataMember(Name = "custom_params")]
        public APIKeys Keys { get; set; }
    }

    [DataContract(Name = "custom_params")]
    public class APIKeys
    {
        [DataMember(Name = "key")]
        public string Key { get; set; }

        [DataMember(Name = "secret")]
        public string Secret { get; set; }
    }
}

