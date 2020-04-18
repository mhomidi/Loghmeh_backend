package domain.FrontEntity;


import domain.entity.User;


public class SingleUserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Double credit;

    public SingleUserDTO(User user) {
        this.username = user.getUsername();
        this.firstName = user.getUserFirstName();
        this.lastName = user.getUserLastName();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.credit = user.getCredit();
    }


    public SingleUserDTO(String username, String firstName, String lastName, String email, String phone, Double credit){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.credit = credit;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
       this.phone = phone;
    }

    public Double getCredit(){
        return  credit;
    }

    public void setCredit(Double credit){
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "SingleUserDTO{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", credit=" + credit +
                '}';
    }
}
