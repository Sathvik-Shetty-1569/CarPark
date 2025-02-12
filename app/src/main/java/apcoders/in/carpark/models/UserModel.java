package apcoders.in.carpark.models;

public class UserModel {
    String Userid, UserFullName, PhoneNumber,UserRole, Email;

    public UserModel() {

    }

    public UserModel(String userid, String userFullName, String phoneNumber, String userRole, String email) {
        Userid = userid;
        UserFullName = userFullName;
        PhoneNumber = phoneNumber;
        UserRole = userRole;
        Email = email;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUserFulName() {
        return UserFullName;
    }

    public void setUserFulName(String userFullName) {
        UserFullName = userFullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
