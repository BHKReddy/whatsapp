package com.thejatech.whatsapp.model;

public class FormSubmission {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String mandal;
    private String district;
    private String state;
    private String pincode;
    private String facingProblems;

    // Getters
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getMandal() { return mandal; }
    public String getDistrict() { return district; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
    public String getFacingProblems() { return facingProblems; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setMandal(String mandal) { this.mandal = mandal; }
    public void setDistrict(String district) { this.district = district; }
    public void setState(String state) { this.state = state; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setFacingProblems(String facingProblems) { this.facingProblems = facingProblems; }

    @Override
    public String toString() {
        return "FormSubmission{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", mandal='" + mandal + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", pincode='" + pincode + '\'' +
                ", facingProblems='" + facingProblems + '\'' +
                '}';
    }
}
