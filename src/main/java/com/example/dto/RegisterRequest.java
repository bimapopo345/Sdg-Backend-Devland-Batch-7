/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Asus
 */
public class RegisterRequest {

    @NotBlank(message = "Name is Required")
    @Size(max = 100, message = "Name Must Be Less Than 100 Characters")
    private String name;

    @NotBlank(message = "Phone Number is Required")
    @Size(min = 10, max = 13, message = "Phone Number Must Be Between 10 and 13 Characters")
    private String phone;

    @NotBlank(message = "Username is Required")
    @Size(max = 50, message = "Username Must Be Less Than 50 Characters")
    private String username;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, message = "Password Must Be At Least 8 Characters")
    private String password;

    public RegisterRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
