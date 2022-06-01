/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.unhcr.irq.utils.data.pojo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlList;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 *
 * @author MATAYO
 */
@XmlRootElement
public class ActivityInfoChanges {

    @XmlElement
    @XmlList
    private ArrayList<ActivityInfoChange> changes;

    public ActivityInfoChanges() {
        this.changes = new ArrayList<>();
    }

    public ArrayList<ActivityInfoChange> getChanges() {
        return changes;
    }

    public void setChanges(ArrayList<ActivityInfoChange> changes) {
        this.changes = changes;
    }

}
