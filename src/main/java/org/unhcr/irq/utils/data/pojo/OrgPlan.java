/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.unhcr.irq.utils.data.pojo;

import com.opencsv.bean.CsvBindByName;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author MATAYO
 */
@XmlRootElement
public class OrgPlan extends ActivityInfoRow {

    @XmlElement(name = "uuid")
    @CsvBindByName(column = "UUID", required = true)
    private String uuid;
    @XmlElement(name = "planning_year")
    @CsvBindByName(column = "Planning Year", required = true)
    private Integer planningYear;
    @XmlTransient
    private String partner;
    @XmlTransient
    private String population_type;
    @XmlTransient
    private String country_of_origin;
    @XmlTransient
    private String programme_organization;
    @XmlTransient
    private String donor_organization;
    @XmlTransient
    private String location_type;
    @XmlTransient
    private String location;
    @XmlTransient
    private String sector;
    @XmlTransient
    private String admin_level_1;
    @XmlTransient
    private String admin_level_2;
    @XmlTransient
    private String admin_level_3;

    @XmlElement(name = "partner")
    @CsvBindByName(column = "Partner Code Name", required = true)
    private String partner_name;
    @XmlElement(name = "population_type")
    @CsvBindByName(column = "Population Type Population Type Key", required = true)
    private String population_type_name;

    @XmlElement(name = "coo")
    @CsvBindByName(column = "County of Origin Country Key")
    private String country_of_origin_name;
    @XmlElement(name = "program_organization")
    @CsvBindByName(column = "Program Organization Code Name", required = true)
    private String programme_organization_name;
    @XmlElement(name = "donor_organization")
    @CsvBindByName(column = "Donor Organization Code Name")
    private String donor_organization_name;
    @XmlElement(name = "location_type")
    @CsvBindByName(column = "Location Type", required = true)
    private String location_type_name;
    @XmlElement(name = "location_camp")
    @CsvBindByName(column = "Location")
    private String location_name;
    @XmlTransient
    private String sector_name;
    @XmlElement(name = "admin_level_1")
    @CsvBindByName(column = "Admin Level 1 Code Name")
    private String admin_level_1_name;
    @XmlElement(name = "admin_level_2")
    @CsvBindByName(column = "Admin Level 2 Code Name")
    private String admin_level_2_name;
    @XmlElement(name = "admin_level_3")
    @CsvBindByName(column = "Admin Level 3 Code Name")
    private String admin_level_3_name;
    @XmlTransient
    private List<MonthlyReports> monthlyReports;

    public OrgPlan(Integer planningYear, String partner, String population_type, String country_of_origin, String programme_organization, String donor_organization, String location_type, String location, String admin_level_1, String admin_level_2, String admin_level_3) {

        this.planningYear = planningYear;
        this.partner = partner;
        this.population_type = population_type;
        this.country_of_origin = country_of_origin;
        this.programme_organization = programme_organization;
        this.donor_organization = donor_organization;

        this.location_type = location_type;
        this.location = location;
        this.admin_level_1 = admin_level_1;
        this.admin_level_2 = admin_level_2;
        this.admin_level_3 = admin_level_3;
    }

    public OrgPlan() {
    }

    public OrgPlan(Integer planningYear, String partner, String population_type, String country_of_origin, String programme_organization, String donor_organization, String location_type, String location, String admin_level_1, String admin_level_2, String admin_level_3, String sector, String sector_name) {

        this.planningYear = planningYear;
        this.partner = partner;
        this.population_type = population_type;
        this.country_of_origin = country_of_origin;
        this.programme_organization = programme_organization;
        this.donor_organization = donor_organization;

        this.location_type = location_type;
        this.location = location;
        this.sector_name = sector_name;
        this.sector = sector;
        this.admin_level_1 = admin_level_1;
        this.admin_level_2 = admin_level_2;
        this.admin_level_3 = admin_level_3;

    }

    private String getValue(String string) {
        return string == null ? "" : string;
    }

    public String getUuid() {
        if (location_type.equals("LOCT01")) {
            this.uuid = this.planningYear.toString()
                    + "|"
                    + getValue(this.partner)
                    + "|"
                    + getValue(this.population_type)
                    + "|"
                    + getValue(this.country_of_origin)
                    + "|"
                    + getValue(this.programme_organization)
                    + "|"
                    + getValue(this.donor_organization)
                    + "|"
                    + getValue(this.sector)
                    + "|"
                    + getValue(this.location_type)
                    + "|"
                    + getValue(this.location);
        } else {
            this.uuid = this.planningYear.toString()
                    + "|"
                    + getValue(this.partner)
                    + "|"
                    + getValue(this.population_type)
                    + "|"
                    + getValue(this.country_of_origin)
                    + "|"
                    + getValue(this.programme_organization)
                    + "|"
                    + getValue(this.donor_organization)
                    + "|"
                    + getValue(this.sector)
                    + "|"
                    + getValue(this.location_type)
                    + "|"
                    + getValue(this.admin_level_3);
        }
        return uuid;
    }

    public String getSector() {
        return sector;
    }

    public String getSector_name() {
        return sector_name;
    }

    public Integer getPlanningYear() {
        return planningYear;
    }

    public void setPlanningYear(Integer planningYear) {
        this.planningYear = planningYear;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPopulation_type() {
        return population_type;
    }

    public void setPopulation_type(String population_type) {
        this.population_type = population_type;
    }

    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public void setCountry_of_origin(String country_of_origin) {
        this.country_of_origin = country_of_origin;
    }

    public String getProgramme_organization() {
        return programme_organization;
    }

    public void setProgramme_organization(String programme_organization) {
        this.programme_organization = programme_organization;
    }

    public String getDonor_organization() {
        return donor_organization;
    }

    public void setDonor_organization(String donor_organization) {
        this.donor_organization = donor_organization;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdmin_level_1() {
        return admin_level_1;
    }

    public void setAdmin_level_1(String admin_level_1) {
        this.admin_level_1 = admin_level_1;
    }

    public String getAdmin_level_2() {
        return admin_level_2;
    }

    public void setAdmin_level_2(String admin_level_2) {
        this.admin_level_2 = admin_level_2;
    }

    public String getAdmin_level_3() {
        return admin_level_3;
    }

    public void setAdmin_level_3(String admin_level_3) {
        this.admin_level_3 = admin_level_3;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public void setSector_name(String sector_name) {
        this.sector_name = sector_name;
    }

    public List<MonthlyReports> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(List<MonthlyReports> monthlyReports) {
        this.monthlyReports = monthlyReports;
    }

    public String getPartner_name() {
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        this.partner_name = partner_name;
    }

    public String getPopulation_type_name() {
        return population_type_name;
    }

    public void setPopulation_type_name(String population_type_name) {
        this.population_type_name = population_type_name;
    }

    public String getCountry_of_origin_name() {
        return country_of_origin_name;
    }

    public void setCountry_of_origin_name(String country_of_origin_name) {
        this.country_of_origin_name = country_of_origin_name;
    }

    public String getProgramme_organization_name() {
        return programme_organization_name;
    }

    public void setProgramme_organization_name(String programme_organization_name) {
        this.programme_organization_name = programme_organization_name;
    }

    public String getDonor_organization_name() {
        return donor_organization_name;
    }

    public void setDonor_organization_name(String donor_organization_name) {
        this.donor_organization_name = donor_organization_name;
    }

    public String getLocation_type_name() {
        return location_type_name;
    }

    public void setLocation_type_name(String location_type_name) {
        this.location_type_name = location_type_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getAdmin_level_1_name() {
        return admin_level_1_name;
    }

    public void setAdmin_level_1_name(String admin_level_1_name) {
        this.admin_level_1_name = admin_level_1_name;
    }

    public String getAdmin_level_2_name() {
        return admin_level_2_name;
    }

    public void setAdmin_level_2_name(String admin_level_2_name) {
        this.admin_level_2_name = admin_level_2_name;
    }

    public String getAdmin_level_3_name() {
        return admin_level_3_name;
    }

    public void setAdmin_level_3_name(String admin_level_3_name) {
        this.admin_level_3_name = admin_level_3_name;
    }

    @Override
    public String toString() {
        return "OrgPlan{" + "uuid=" + getUuid() + ", planningYear=" + planningYear + ", partner_name=" + partner_name + ", population_type_name=" + population_type_name + ", country_of_origin_name=" + country_of_origin_name + ", programme_organization_name=" + programme_organization_name + ", donor_organization_name=" + donor_organization_name + ", location_type_name=" + location_type_name + ", location_name=" + location_name + ", admin_level_1_name=" + admin_level_1_name + ", admin_level_2_name=" + admin_level_2_name + ", admin_level_3_name=" + admin_level_3_name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.planningYear);
        hash = 97 * hash + Objects.hashCode(this.partner);
        hash = 97 * hash + Objects.hashCode(this.population_type);
        hash = 97 * hash + Objects.hashCode(this.country_of_origin);
        hash = 97 * hash + Objects.hashCode(this.programme_organization);
        hash = 97 * hash + Objects.hashCode(this.donor_organization);
        hash = 97 * hash + Objects.hashCode(this.location_type);
        hash = 97 * hash + Objects.hashCode(this.location);
        hash = 97 * hash + Objects.hashCode(this.admin_level_3);
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
        final OrgPlan other = (OrgPlan) obj;
        if (!Objects.equals(this.partner, other.partner)) {
            return false;
        }
        if (!Objects.equals(this.population_type, other.population_type)) {
            return false;
        }
        if (!Objects.equals(this.country_of_origin, other.country_of_origin)) {
            return false;
        }
        if (!Objects.equals(this.programme_organization, other.programme_organization)) {
            return false;
        }
        if (!Objects.equals(this.donor_organization, other.donor_organization)) {
            return false;
        }
        if (!Objects.equals(this.location_type, other.location_type)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.admin_level_3, other.admin_level_3)) {
            return false;
        }
        return Objects.equals(this.planningYear, other.planningYear);
    }

}
