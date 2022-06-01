/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.unhcr.irq.utils.data.pojo;

import cool.graph.cuid.Cuid;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlList;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author MATAYO
 */
@XmlRootElement
public class ActivityInfoChange implements Serializable {

    @XmlElement(name = "formId")
    private String formId;
    @XmlElement(name = "recordId")
    private String recordId;
    @XmlElement(name = "parentRecordId")
    private String parentRecordId;
    @XmlElement(name = "deleted")
    private boolean deleted = false;
    @XmlElement(name = "fields")
    private ActivityInfoRow fields;

    public ActivityInfoChange(String formId, String recordId, String parentRecordId) {
        this.formId = formId;
        this.recordId = recordId;
        if (this.recordId == null) {
            this.recordId = Cuid.createCuid();
        }
        this.parentRecordId = parentRecordId;

    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getParentRecordId() {
        return parentRecordId;
    }

    public void setParentRecordId(String parentRecordId) {
        this.parentRecordId = parentRecordId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ActivityInfoRow getFields() {
        return fields;
    }

    public void setFields(ActivityInfoRow fields) {
        this.fields = fields;
    }

}
