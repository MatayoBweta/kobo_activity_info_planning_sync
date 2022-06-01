/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.unhcr.irq.utils.data.pojo;

import com.opencsv.bean.CsvBindByName;
import java.util.Objects;

/**
 *
 * @author MATAYO
 */
public class FocalPoints extends ActivityInfoRow {

    @CsvBindByName(column = "Planning Year", required = true)
    private Integer planningYear;
    @CsvBindByName(column = "Partner", required = true)
    private String partner_name;
    @CsvBindByName(column = "Email", required = true)
    private String email;
    @CsvBindByName(column = "Phone Number", required = true)
    private String phone_number;
    @CsvBindByName(column = "Roles", required = true)
    private String roles;

    public FocalPoints() {
    }

    public FocalPoints(Integer planningYear, String partner_name, String email, String phone_number, String roles) {
        this.planningYear = planningYear;
        this.partner_name = partner_name;
        this.email = email;
        this.phone_number = phone_number;
        this.roles = roles;
    }

    private String getValue(String string) {
        return string == null ? "" : string;
    }

    public Integer getPlanningYear() {
        return planningYear;
    }

    public void setPlanningYear(Integer planningYear) {
        this.planningYear = planningYear;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.email);
        hash = 17 * hash + Objects.hashCode(this.phone_number);
        hash = 17 * hash + Objects.hashCode(this.roles);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FocalPoints other = (FocalPoints) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.phone_number, other.phone_number)) {
            return false;
        }
        return Objects.equals(this.roles, other.roles);
    }

    @Override
    public String toString() {
        return "FocalPoints{" + "email=" + email + ", phone_number=" + phone_number + ", roles=" + roles + '}';
    }

}
