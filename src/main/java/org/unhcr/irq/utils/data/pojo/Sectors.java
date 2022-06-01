/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.unhcr.irq.utils.data.pojo;

/**
 *
 * @author MATAYO
 */
public enum Sectors {

    SEC100("SEC100", "SEC100 | General Protection", "C5uui6yl27hkneu9"),
    SEC102("SEC102", "SEC102 | Child Protection", "cmuy1wil304kx534"),
    SEC103("SEC103", "SEC103 | GBV Protection", "c3wvr11l304rd771t"),
    SEC106("SEC106", "SEC106 | Education", "ctvpsbil304vpk72w"),
    SEC201("SEC201", "SEC201 | Health", "c2h0yf5l304yixv3y"),
    SEC203("SEC203", "SEC203 | Water Hygiene and Sanitation (WASH)", "cp146hel30519yk52"),
    SEC204("SEC204", "SEC204 | Shelter and Infrastructure", "crqasbel3053w0g66"),
    SEC205("SEC205", "SEC205 | Food security", "cjagmljl30562817a"),
    SEC206("SEC206", "SEC206 | Basic Needs", "cx0uu0tl3058lsq8f"),
    SEC207("SEC207", "SEC207 | Livelihoods", "ch13prbl305c3419j");

    private final String code;
    private final String name;
    private final String formId;

    private Sectors(String code, String name, String formId) {
        this.code = code;
        this.name = name;
        this.formId = formId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFormId() {
        return formId;
    }

}
