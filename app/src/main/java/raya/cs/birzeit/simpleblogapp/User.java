package raya.cs.birzeit.simpleblogapp;

public class User {
    String Type;
    String email;
    String password;
    String userName;
    String userUd;
    String image;

    public User() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public User(String type, String email, String password, String userName, String userUd) {
        Type = type;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.userUd = userUd;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUd() {
        return userUd;
    }

    public void setUserUd(String userUd) {
        this.userUd = userUd;
    }
}
