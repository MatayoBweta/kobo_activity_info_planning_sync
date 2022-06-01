/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.unhcr.irq.utils.data.pojo;

import com.opencsv.bean.CsvBindByName;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 *
 * @author MATAYO
 */
public class MonthlyReports extends ActivityInfoRow {

    @CsvBindByName(column = "UUID", required = true)
    private String uuid;
    @CsvBindByName(column = "Parent UUID", required = true)
    private String reportUUID;
    @CsvBindByName(column = "Month", required = true)
    private String month;
    private String sector;
    @CsvBindByName(column = "Indicators Indicator Key", required = true)
    private String indicator;
    @CsvBindByName(column = "Indicator Attributes Attribute Key", required = true)
    private String atribute;

    private String indicatorCode;
    private String atributeCode;
    private String sectorCode;

    public MonthlyReports() {
    }

    public MonthlyReports(String uuid, String month, String indicator, String atribute, String sector) {
        this.uuid = uuid;
        this.sector = sector;
        this.month = month;
        this.indicator = indicator;
        this.atribute = atribute;

        for (StringTokenizer stIndicator = new StringTokenizer(indicator, " | "); stIndicator.hasMoreTokens();) {
            String token = stIndicator.nextToken();
            if (token.startsWith("ind") && !token.contains(" ")) {
                this.indicatorCode = token;
                break;
            }
        }
        for (StringTokenizer stAttribute = new StringTokenizer(atribute, " | "); stAttribute.hasMoreTokens();) {
            String token = stAttribute.nextToken();
            if (token.startsWith("3rpa") && !token.contains(" ")) {
                this.atributeCode = token;
                break;
            }
        }
        for (StringTokenizer stSector = new StringTokenizer(sector, " | "); stSector.hasMoreTokens();) {
            String token = stSector.nextToken();
            if (token.startsWith("SEC") && !token.contains(" ")) {
                this.sectorCode = token;
                break;
            }
        }
        this.reportUUID = this.month
                + "|"
                + getValue(this.sectorCode)
                + "|"
                + getValue(this.indicatorCode)
                + "|"
                + getValue(this.atributeCode);
    }

    private String getValue(String string) {
        return string == null ? "" : string;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getReportUUID() {
        return reportUUID;
    }

    public void setReportUUID(String reportUUID) {
        this.reportUUID = reportUUID;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getAtribute() {
        return atribute;
    }

    public void setAtribute(String atribute) {
        this.atribute = atribute;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    @Override
    public String toString() {
        return "MonthlyReports{" + "uuid=" + uuid + ", reportUUID=" + reportUUID + ", month=" + month + ", sector=" + sector + ", indicator=" + indicator + ", atribute=" + atribute + ", indicatorCode=" + indicatorCode + ", atributeCode=" + atributeCode + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.uuid);
        hash = 79 * hash + Objects.hashCode(this.month);
        hash = 79 * hash + Objects.hashCode(this.sector);
        hash = 79 * hash + Objects.hashCode(this.indicator);
        hash = 79 * hash + Objects.hashCode(this.atribute);
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
        final MonthlyReports other = (MonthlyReports) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        if (!Objects.equals(this.month, other.month)) {
            return false;
        }
        if (!Objects.equals(this.sector, other.sector)) {
            return false;
        }
        if (!Objects.equals(this.indicator, other.indicator)) {
            return false;
        }
        return Objects.equals(this.atribute, other.atribute);
    }

}
