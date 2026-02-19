package com.thejatech.whatsapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "submissions")
public class Submission {
    
    @Id
    private String id;
    
    private String name;
    private String phone;
    private String email;
    private String address;
    private String mandal;
    private String district;
    private String state;
    private String pincode;
    private String facingProblems;
    private String couponCode;
    private LocalDateTime timestamp;

    // Constructors
    public Submission() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getMandal() { return mandal; }
    public void setMandal(String mandal) { this.mandal = mandal; }
    
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    
    public String getFacingProblems() { return facingProblems; }
    public void setFacingProblems(String facingProblems) { this.facingProblems = facingProblems; }
    
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Submission{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", couponCode='" + couponCode + '\'' +
                '}';
    }
}
