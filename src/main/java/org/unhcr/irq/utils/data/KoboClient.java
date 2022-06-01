/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 *       https://kobo.unhcr.org/api/v2/assets/aUq9wTbyRAThtnHu5FcLT9/data/
 */
package org.unhcr.irq.utils.data;

import org.unhcr.irq.utils.data.pojo.MonthlyReports;
import org.unhcr.irq.utils.data.pojo.OrgPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.uri.UriComponent;
import org.unhcr.irq.utils.data.pojo.ActivityInfoChange;
import org.unhcr.irq.utils.data.pojo.ActivityInfoChanges;
import org.unhcr.irq.utils.data.pojo.ActivityInfoRow;
import org.unhcr.irq.utils.data.pojo.FocalPoints;
import org.unhcr.irq.utils.data.pojo.Sectors;

/**
 *
 * @author MATAYO
 */
public class KoboClient {

    private static final Logger logger = Logger.getLogger(KoboClient.class.getName());
    private static File currentSavingFolder;

    public KoboClient() {

    }

    private static File getFolderForSaving() {
        String folderPath = ResourceBundle.getBundle(BUNDLE_PATH).getString("SAVED_FOLDER");
        File f_ = new File(folderPath);
        String name = "DATA " + getCurrentDate();
        File currenFile = new File(f_, name);
        int i = 1;
        while (currenFile.exists() && currenFile.isDirectory()) {
            currenFile = new File(f_, name + "_" + Integer.toString(i));
            i++;
        }
        currenFile.mkdir();

        return currenFile;
    }

    private static void saveNewFile(String name, List<? extends ActivityInfoRow> infoChanges) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        if (currentSavingFolder != null) {
            logger.info(currentSavingFolder.getAbsolutePath());
            File f = new File(currentSavingFolder, name.replace(" | ", "_").replace(" ", "_"));
            logger.info(f.getAbsolutePath());
            try ( Writer writer = new FileWriter(f)) {
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
                List<? extends ActivityInfoRow> removeDuplicates = infoChanges.stream()
                        .distinct()
                        .collect(Collectors.toList());
                beanToCsv.write(removeDuplicates);
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            logger.info("Starting");
            readAttributes = readAttributes();
            logger.info(readAttributes);
            readOrganizations = readOrgs();
            readAdminLevels = readAdmniLevels();
            readLocations = readLocations();
            logger.info(readLocations);
            readCountries = readCountries();
            readPopulationTypes = readPopulationTypes();
            List<OrgPlan> global_orgPlans = new ArrayList<>();
            List<MonthlyReports> global_Monthly_Report = new ArrayList<>();
            List<FocalPoints> global_FocalPoints = new ArrayList<>();
            String url = ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_URL");
            do {
                logger.info(url);
                String readEntities = getKoboData(url);
                logger.log(Level.INFO, "After {0}", url);
                ObjectMapper mapper_ = new ObjectMapper();
                JsonNode n = mapper_.readTree(readAttributes);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualObj = mapper.readTree(readEntities);
                logger.info(actualObj.toPrettyString());
                JsonNode results = actualObj.get("results");
                JsonNode next = actualObj.get("next");
                JsonNode count = actualObj.get("count");
                Map<String, Collection<? extends ActivityInfoRow>> resultsManaged = manageResults(results);
                global_orgPlans.addAll((Collection<? extends OrgPlan>) resultsManaged.get(PLANNING));
                global_Monthly_Report.addAll((Collection<? extends MonthlyReports>) resultsManaged.get(MONTHLY_REPORT));
                global_FocalPoints.addAll((Collection<? extends FocalPoints>) resultsManaged.get(FOCAL_POINT));
                url = next.asText();
                if ("null".equals(url)) {
                    url = null;
                }
            } while (url != null);

            Map<String, List<OrgPlan>> map = global_orgPlans.stream()
                    .collect(Collectors.groupingBy(d -> d.getSector_name()));
            if (!global_orgPlans.isEmpty() || !global_Monthly_Report.isEmpty() || !global_FocalPoints.isEmpty()) {
                currentSavingFolder = getFolderForSaving();
            }
            for (Map.Entry<String, List<OrgPlan>> entry : map.entrySet()) {
                String key = entry.getKey();
                List<OrgPlan> value = entry.getValue();
                String name = "PLANNING_" + key + "_" + getCurrentDate() + ".csv";
                saveNewFile(name.toUpperCase(), value);
            }
            Map<String, List<MonthlyReports>> mapMonthlyReport = global_Monthly_Report.stream()
                    .collect(Collectors.groupingBy(MonthlyReports::getSector));
            for (Map.Entry<String, List<MonthlyReports>> entry : mapMonthlyReport.entrySet()) {
                String key = entry.getKey();
                List<MonthlyReports> value = entry.getValue();
                String name = "MONTHLY_REPORT_" + key + "_" + getCurrentDate() + ".csv";
                saveNewFile(name.toUpperCase(), value);
            }
            if (!global_FocalPoints.isEmpty()) {
                String name = "Focal_Points" + "_" + getCurrentDate() + ".csv";
                saveNewFile(name.toUpperCase(), global_FocalPoints);
            }

        } catch (JsonProcessingException ex) {
            Logger.getLogger(KoboClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException ex) {
            Logger.getLogger(KoboClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Map<String, Collection<? extends ActivityInfoRow>> manageResults(JsonNode results) {
        Map<String, Collection<? extends ActivityInfoRow>> mainResults = new HashedMap<>();
        List<OrgPlan> global_orgPlans = new ArrayList<>();
        List<MonthlyReports> global_Monthly_Report = new ArrayList<>();
        List<FocalPoints> global_FocalPoints = new ArrayList<>();
        if (results.isArray()) {
            for (JsonNode result : results) {
                JsonNode _validation_status = result.get("_validation_status");
                String status = null;
                if (_validation_status.has("uid")) {
                    status = _validation_status.get("uid").asText();
                    logger.info(status);
                }
                if (status == null || status.equals("-")) {
                    String _id = result.get("_id").asText();
                    Integer planningYear = result.get("section_partner/planning_year").asInt();
                    String partner_organization = result.get("section_partner/partner").asText();
                    JsonNode details = result.get("section_partner/section_implementation");
                    JsonNode contacts = result.get("section_partner/section_contact");
                    if (contacts.isArray()) {
                        for (JsonNode contact : contacts) {
                            String mail_address = getStringValueFromJson(contact, "section_partner/section_contact/mail_address");
                            String phone_number = getStringValueFromJson(contact, "section_partner/section_contact/phone_number");
                            String data_collection_role = getStringValueFromJson(contact, "section_partner/section_contact/data_collection_role");
                            global_FocalPoints.add(new FocalPoints(planningYear, getAILabel(readOrganizations, partner_organization, "$[?(@['Code'] == '", "')]['Code Name']"), mail_address, phone_number, data_collection_role));
                        }
                    }

                    if (details.isArray()) {
                        for (JsonNode detail : details) {
                            String programme_organization = getStringValueFromJson(detail, "section_partner/section_implementation/programme_organization");
                            String donor_organization = getStringValueFromJson(detail, "section_partner/section_implementation/donor_organization");
                            String population_type = getStringValueFromJson(detail, "section_partner/section_implementation/population_type", ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"));
                            String county_of_origin = getStringValueFromJson(detail, "section_partner/section_implementation/county_of_origin");
                            String location_type = getStringValueFromJson(detail, "section_partner/section_implementation/location_type", ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"));
                            String location = getStringValueFromJson(detail, "section_partner/section_implementation/camp_covered");
                            String admin_level_1 = getStringValueFromJson(detail, "section_partner/section_implementation/admin_level_1");
                            String admin_level_2 = getStringValueFromJson(detail, "section_partner/section_implementation/admin_level_2");
                            String admin_level_3 = getStringValueFromJson(detail, "section_partner/section_implementation/admin_level_3", ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"));
                            logger.info(location);
                            JsonNode indicator_group = detail.get("section_partner/section_implementation/indicator_group");
                            JsonNode indicator_3rp_hc_group = detail.get("section_partner/section_implementation/indicator_3rp_hc_group");
                            JsonNode indicator_compass_ref_group = detail.get("section_partner/section_implementation/indicator_compass_ref_group");
                            JsonNode indicator_compass_hc_group = detail.get("section_partner/section_implementation/indicator_compass_hc_group");
                            JsonNode indicator_compass_idps_group = detail.get("section_partner/section_implementation/indicator_compass_idps_group");
                            JsonNode indicator_compass_ridps_group = detail.get("section_partner/section_implementation/indicator_compass_ridps_group");

                            List<OrgPlan> orgPlans = new ArrayList<>();

                            if (!location_type.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"))) {
                                logger.info(location_type);
                                for (StringTokenizer stLocation_Type = new StringTokenizer(location_type, " "); stLocation_Type.hasMoreTokens();) {
                                    String tokenLocationType = stLocation_Type.nextToken();

                                    if (tokenLocationType.equals("LOCT01") && !location.isBlank()) {
                                        for (StringTokenizer stLocation = new StringTokenizer(location, " "); stLocation.hasMoreTokens();) {
                                            String tokenLocation = stLocation.nextToken();
                                            if (!tokenLocation.equals("NA")) {
                                                if (population_type.equals("PT01") || population_type.equals("PT04")) {
                                                    for (StringTokenizer stCountry_Of_Origin = new StringTokenizer(county_of_origin, " "); stCountry_Of_Origin.hasMoreTokens();) {
                                                        String tokenCountry_Of_Origin = stCountry_Of_Origin.nextToken();
                                                        OrgPlan op = new OrgPlan(planningYear, partner_organization, population_type, tokenCountry_Of_Origin, programme_organization, donor_organization, tokenLocationType, tokenLocation, null, null, null);
                                                        orgPlans.add(op);
                                                    }
                                                } else if (!population_type.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"))) {
                                                    OrgPlan op = new OrgPlan(planningYear, partner_organization, population_type, ResourceBundle.getBundle(BUNDLE_PATH).getString("COUNTRY_CODE"), programme_organization, donor_organization, tokenLocationType, tokenLocation, null, null, null);
                                                    orgPlans.add(op);
                                                }
                                            }
                                        }
                                    } else if (tokenLocationType.equals("LOCT02") && !admin_level_3.isBlank()) {
                                        for (StringTokenizer stAdmin_Level_3 = new StringTokenizer(admin_level_3, " "); stAdmin_Level_3.hasMoreTokens();) {
                                            String tokenAdmin_Level_3 = stAdmin_Level_3.nextToken();
                                            if (population_type.equals("PT01")) {
                                                for (StringTokenizer stCountry_Of_Origin = new StringTokenizer(county_of_origin, " "); stCountry_Of_Origin.hasMoreTokens();) {
                                                    String tokenCountry_Of_Origin = stCountry_Of_Origin.nextToken();
                                                    OrgPlan op = new OrgPlan(planningYear, partner_organization, population_type, tokenCountry_Of_Origin, programme_organization, donor_organization, tokenLocationType, ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"), null, null, tokenAdmin_Level_3);
                                                    orgPlans.add(op);
                                                }
                                            } else if (!population_type.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"))) {
                                                OrgPlan op = new OrgPlan(planningYear, partner_organization, population_type, ResourceBundle.getBundle(BUNDLE_PATH).getString("COUNTRY_CODE"), programme_organization, donor_organization, tokenLocationType, ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"), null, null, tokenAdmin_Level_3);
                                                orgPlans.add(op);
                                            }
                                        }
                                    }
                                }
                            } else if (!admin_level_3.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"))) {
                                for (StringTokenizer stAdmin_Level_3 = new StringTokenizer(admin_level_3, " "); stAdmin_Level_3.hasMoreTokens();) {
                                    String tokenAdmin_Level_3 = stAdmin_Level_3.nextToken();
                                    OrgPlan op = new OrgPlan(planningYear, partner_organization, population_type, ResourceBundle.getBundle(BUNDLE_PATH).getString("COUNTRY_CODE"), programme_organization, donor_organization, "LOCT02", ResourceBundle.getBundle(BUNDLE_PATH).getString("NA"), null, null, tokenAdmin_Level_3);
                                    orgPlans.add(op);
                                }
                            }

                            List<OrgPlan> toAdd = new ArrayList<>();
                            List<OrgPlan> toRemove = new ArrayList<>();

                            for (OrgPlan orgPlan : orgPlans) {

                                orgPlan = enrichThePlan(orgPlan);
                                logger.info(orgPlan.toString());
                                List<MonthlyReports> monthlyReports = new ArrayList<>();
                                switch (orgPlan.getPopulation_type()) {
                                    case "PT01" -> {
                                        if (indicator_group != null && indicator_group.isArray()) {
                                            monthlyReports = generateReporting(orgPlan.getUuid(), "indicator", monthlyReports, readAttributes, indicator_group, planningYear);
                                        }
                                        if (programme_organization.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("UNHCR_CODE"))) {
                                            if (indicator_compass_ref_group != null && indicator_compass_ref_group.isArray()) {
                                                monthlyReports = generateReporting(orgPlan.getUuid(), "indicator_compass_ref", monthlyReports, readAttributes, indicator_compass_ref_group, planningYear);
                                            }

                                        }
                                    }
                                    case "PT02" -> {
                                        if (programme_organization.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("UNHCR_CODE"))) {
                                            if (indicator_compass_idps_group != null && indicator_compass_idps_group.isArray()) {
                                                monthlyReports = generateReporting(orgPlan.getUuid(), "indicator_compass_idps", monthlyReports, readAttributes, indicator_compass_idps_group, planningYear);
                                            }

                                        }
                                    }
                                    case "PT03" -> {
                                        if (programme_organization.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("UNHCR_CODE"))) {
                                            if (indicator_compass_ridps_group != null && indicator_compass_ridps_group.isArray()) {
                                                monthlyReports = generateReporting(orgPlan.getUuid(), "indicator_compass_ridps", monthlyReports, readAttributes, indicator_compass_ridps_group, planningYear);
                                            }
                                        }
                                    }
                                    case "PT04" -> {
                                        if (indicator_3rp_hc_group != null && indicator_3rp_hc_group.isArray()) {
                                            monthlyReports = generateReporting(orgPlan.getUuid(), "indicator_3rp_hc", monthlyReports, readAttributes, indicator_3rp_hc_group, planningYear);
                                        }
                                        if (programme_organization.equals(ResourceBundle.getBundle(BUNDLE_PATH).getString("UNHCR_CODE"))) {
                                            if (indicator_compass_hc_group != null && indicator_compass_hc_group.isArray()) {
                                                monthlyReports = generateReporting(orgPlan.getUuid(), "indicator_compass_hc", monthlyReports, readAttributes, indicator_compass_hc_group, planningYear);
                                            }
                                        }
                                    }
                                }
                                orgPlan.setMonthlyReports(monthlyReports);
                            }

                            for (OrgPlan orgPlan : orgPlans) {
                                Map<String, String> sectorValues = new HashMap<>();
                                for (MonthlyReports monthlyReport : orgPlan.getMonthlyReports()) {
                                    sectorValues.put(monthlyReport.getSectorCode(), monthlyReport.getSector());
                                }
                                if (sectorValues.size() > 1) {
                                    toRemove.add(orgPlan);
                                    for (String sectorValue : sectorValues.keySet()) {
                                        OrgPlan orgPlan_new = new OrgPlan(orgPlan.getPlanningYear(), orgPlan.getPartner(), orgPlan.getPopulation_type(), orgPlan.getCountry_of_origin(), orgPlan.getProgramme_organization(), orgPlan.getDonor_organization(), orgPlan.getLocation_type(), orgPlan.getLocation(), orgPlan.getAdmin_level_1(), orgPlan.getAdmin_level_2(), orgPlan.getAdmin_level_3(), sectorValue, sectorValues.get(sectorValue));
                                        orgPlan_new = copyThePlan(orgPlan_new, orgPlan);
                                        List<MonthlyReports> mrss = orgPlan.getMonthlyReports().stream().filter(c -> c.getSectorCode().equals(orgPlan.getSector())).collect(Collectors.toList());
                                        mrss.forEach(c -> c.setUuid(orgPlan.getUuid()));
                                        orgPlan_new.setMonthlyReports(mrss);
                                        toAdd.add(orgPlan_new);
                                    }
                                } else if (sectorValues.size() == 1) {
                                    for (Map.Entry<String, String> entry : sectorValues.entrySet()) {
                                        String key = entry.getKey();
                                        String val = entry.getValue();
                                        orgPlan.setSector(key);
                                        orgPlan.setSector_name(val);
                                        List<MonthlyReports> mrss = orgPlan.getMonthlyReports().stream().filter(c -> c.getSectorCode().equals(orgPlan.getSector())).collect(Collectors.toList());
                                        mrss.forEach(c -> c.setUuid(orgPlan.getUuid()));
                                        orgPlan.setMonthlyReports(mrss);

                                    }
                                }
                            }
                            orgPlans.removeAll(toRemove);
                            orgPlans.addAll(toAdd);
                            for (OrgPlan orgPlan : orgPlans) {
                                logger.info(orgPlan.toString());
                                global_Monthly_Report.addAll(orgPlan.getMonthlyReports());
                                for (MonthlyReports monthlyReport : orgPlan.getMonthlyReports()) {
                                    logger.log(Level.FINE, "-- {0}", monthlyReport.toString());
                                }
                            }
                            orgPlans = orgPlans.stream().filter(c -> !c.getMonthlyReports().isEmpty()).distinct().toList();
                            global_orgPlans.addAll(orgPlans);
                        }
                    }
                    acceptKoboData(ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_URL_PART"), _id);
                }
            }
        }
        mainResults.put(PLANNING, global_orgPlans);
        mainResults.put(MONTHLY_REPORT, global_Monthly_Report);
        mainResults.put(FOCAL_POINT, global_FocalPoints);
        return mainResults;
    }
    private static final String FOCAL_POINT = "FOCAL_POINT";
    private static final String MONTHLY_REPORT = "MONTHLY_REPORT";
    private static final String PLANNING = "PLANNING";

    private static OrgPlan enrichThePlan(OrgPlan orgPlan) {
        orgPlan.setPartner_name(getAILabel(readOrganizations, orgPlan.getPartner(), "$[?(@['Code'] == '", "')]['Code Name']"));
        orgPlan.setPopulation_type_name(getAILabel(readPopulationTypes, orgPlan.getPopulation_type(), "$[?(@['Population Type Code'] == '", "')]['Population Type Key']"));
        orgPlan.setCountry_of_origin_name(getAILabel(readCountries, orgPlan.getCountry_of_origin(), "$[?(@['Country code'] == '", "')]['Country Key']"));
        orgPlan.setProgramme_organization_name(getAILabel(readOrganizations, orgPlan.getProgramme_organization(), "$[?(@['Code'] == '", "')]['Code Name']"));
        orgPlan.setDonor_organization_name(getAILabel(readOrganizations, orgPlan.getDonor_organization(), "$[?(@['Code'] == '", "')]['Code Name']"));
        if (orgPlan.getLocation_type().equals("LOCT01")) {
            orgPlan.setLocation_name(getAILabel(readLocations, orgPlan.getLocation(), "$[?(@['PCode'] == '", "')]['Name']"));
            orgPlan.setAdmin_level_1_name(getAILabel(readLocations, orgPlan.getLocation(), "$[?(@['PCode'] == '", "')]['UNHCR Admin Level 1 Code Name']"));
            orgPlan.setAdmin_level_2_name(getAILabel(readLocations, orgPlan.getLocation(), "$[?(@['PCode'] == '", "')]['UNHCR Admin Level 2 Code Name']"));
            orgPlan.setAdmin_level_3_name(getAILabel(readLocations, orgPlan.getLocation(), "$[?(@['PCode'] == '", "')]['UNHCR Admin Level 3 Code Name']"));
            orgPlan.setLocation_type_name("Camp");
        } else {
            orgPlan.setLocation_name(getAILabel(readLocations, "NA001", "$[?(@['PCode'] == '", "')]['Name']"));
            orgPlan.setAdmin_level_3_name(getAILabel(readAdminLevels, orgPlan.getAdmin_level_3(), "$[?(@['PCode'] == '", "')]['Code Name']"));
            String codeAdminLevel2 = getAILabel(readAdminLevels, orgPlan.getAdmin_level_3(), "$[?(@['PCode'] == '", "')]['Parent Admin Level']");
            orgPlan.setAdmin_level_2(codeAdminLevel2);
            orgPlan.setAdmin_level_2_name(getAILabel(readAdminLevels, codeAdminLevel2, "$[?(@['PCode'] == '", "')]['Code Name']"));
            String codeAdminLevel1 = getAILabel(readAdminLevels, orgPlan.getAdmin_level_2(), "$[?(@['PCode'] == '", "')]['Parent Admin Level']");
            orgPlan.setAdmin_level_1(codeAdminLevel1);
            orgPlan.setAdmin_level_1_name(getAILabel(readAdminLevels, codeAdminLevel1, "$[?(@['PCode'] == '", "')]['Code Name']"));
            orgPlan.setLocation_type_name("Out of Camp");

        }
        return orgPlan;
    }

    private static OrgPlan copyThePlan(OrgPlan orgPlan, OrgPlan oldOrgPlan) {
        orgPlan.setPartner_name(oldOrgPlan.getPartner_name());
        orgPlan.setPopulation_type_name(oldOrgPlan.getPopulation_type_name());
        orgPlan.setCountry_of_origin_name(oldOrgPlan.getCountry_of_origin_name());
        orgPlan.setProgramme_organization_name(oldOrgPlan.getProgramme_organization_name());
        orgPlan.setDonor_organization_name(oldOrgPlan.getDonor_organization_name());
        orgPlan.setLocation_name(oldOrgPlan.getLocation_name());
        orgPlan.setAdmin_level_1_name(oldOrgPlan.getAdmin_level_1_name());
        orgPlan.setAdmin_level_2_name(oldOrgPlan.getAdmin_level_2_name());
        orgPlan.setAdmin_level_3_name(oldOrgPlan.getAdmin_level_3_name());
        orgPlan.setLocation_type_name(oldOrgPlan.getLocation_type_name());
        return orgPlan;
    }

    private static String readAttributes() {
        if (readAttributes == null) {
            readAttributes = getActivityInfoData(ResourceBundle.getBundle(BUNDLE_PATH).getString("ATTRIBUTES_URL"));
        }
        return readAttributes;
    }
    private static String readAttributes;

    private static String readPopulationTypes() {
        if (readPopulationTypes == null) {
            readPopulationTypes = getActivityInfoData(ResourceBundle.getBundle(BUNDLE_PATH).getString("POPULATION_TYPE"));
        }
        return readPopulationTypes;
    }
    private static String readPopulationTypes;

    private static String readCountries() {
        if (readCountries == null) {
            readCountries = getActivityInfoData(ResourceBundle.getBundle(BUNDLE_PATH).getString("COUNTRY_URL"));
        }
        return readCountries;
    }
    private static String readCountries;

    private static String readLocations() {
        if (readLocations == null) {
            readLocations = getActivityInfoData(ResourceBundle.getBundle(BUNDLE_PATH).getString("LOCATION_URL"));
        }
        return readLocations;
    }
    private static String readLocations;

    private static String readAdmniLevels() {
        if (readAdminLevels == null) {
            readAdminLevels = getActivityInfoData(ResourceBundle.getBundle(BUNDLE_PATH).getString("ADMIN_LEVEL_URL"));
        }
        return readAdminLevels;
    }
    private static String readAdminLevels;

    private static String readOrgs() {
        if (readOrganizations == null) {
            readOrganizations = getActivityInfoData(ResourceBundle.getBundle(BUNDLE_PATH).getString("ORGANIZATION_URL"));
        }
        return readOrganizations;
    }
    private static String readOrganizations;

    private static String getAILabel(String jsonData, String path, String pre, String post) {
        List<String> u = JsonPath.read(jsonData, pre + path + post);
        if (!u.isEmpty()) {
            return u.get(0);
        }
        return "";
    }

    private static List<MonthlyReports> generateReporting(String uuid, String indicator_label, List<MonthlyReports> mrs, String attributes, JsonNode indicator_group, Integer planningYear) {

        if (indicator_group != null && indicator_group.isArray()) {
            for (JsonNode ind_details : indicator_group) {
                String indicator_name = getStringValueFromJson(ind_details, "section_partner/section_implementation/" + indicator_label + "_group/" + indicator_label + "_name");
                List<String> attributesText = JsonPath.read(attributes, "$[?(@['Indicator Key'] == '" + StringEscapeUtils.escapeEcmaScript(indicator_name) + "')]['Attribute Key']");
                String sector = getAILabel(attributes, StringEscapeUtils.escapeEcmaScript(indicator_name), "$[?(@['Indicator Key'] == '", "')]['Sector Code Name Code Name']");
                for (String att : attributesText) {
                    for (int i = 0; i < 12; i++) {
                        String reportingMonth = getMonthOfExecution(planningYear, i);
                        MonthlyReports mr = new MonthlyReports(uuid, reportingMonth, indicator_name, att, sector);
                        mrs.add(mr);
                    }
                }
            }
        }
        return mrs;
    }

    private static String getMonthOfExecution(Integer planningYear, int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(planningYear, i, 1);
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        cal.set(planningYear, i, lastDay);
        Date date = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
        final String format = format1.format(date);
        return format;
    }

    private static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        final String format = format1.format(date);
        return format;
    }

    private static String getStringValueFromJson(JsonNode detail, String key) {
        final JsonNode get = detail.get(key);
        String value = "";
        if (get != null) {
            value = get.asText();
        }
        return value;
    }

    private static String getStringValueFromJson(JsonNode detail, String key, String defaultValue) {
        final JsonNode get = detail.get(key);
        String value = defaultValue;
        if (get != null) {
            value = get.asText(defaultValue);
        }
        return value;
    }

    private static Integer getIntValueFromJson(JsonNode detail, String key) {
        final JsonNode get = detail.get(key);
        Integer value = null;
        if (get != null) {
            value = get.asInt();
        }
        return value;
    }

    private static Integer getIntValueFromJson(JsonNode detail, String key, Integer defaultValue) {
        final JsonNode get = detail.get(key);
        Integer value = defaultValue;
        if (get != null) {
            value = get.asInt(defaultValue);
        }
        return value;
    }

    // chops a list into non-view sublists of length L
    static <T> List<List<T>> chopped(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    private static void uploadToActivityInfo(List<OrgPlan> orgPlans) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_USERNAME"), ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_PASSWORD"));
        Client client = ClientBuilder.newClient();
        final WebTarget target = client.target(ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_URL"));
        target.register(feature);
        List<List<OrgPlan>> ops = chopped(orgPlans, 10);

        for (List<OrgPlan> op : ops) {
            ActivityInfoChanges changes = new ActivityInfoChanges();
            for (OrgPlan orgPlan : op) {
                ActivityInfoChange change = new ActivityInfoChange(Sectors.valueOf(orgPlan.getSector()).getFormId(), null, null);
                change.setFields(orgPlan);
                changes.getChanges().add(change);
            }
            target
                    .request("application/json")
                    .post(Entity.entity(changes, "application/json"));
        }
    }

    private static String getKoboData(String url) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_USERNAME"), ResourceBundle.getBundle(BUNDLE_PATH).getString("KOBO_PASSWORD"));
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        String json = "{\"_validation_status\":{}}";
        target = target.register(feature);
        target = target.queryParam("query",
                UriComponent.encode(json, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED));
        logger.info(target.getUri().toString());
        Response res = target
                .request().get();
        final String readEntity = res.readEntity(String.class);
        return readEntity;
    }

    private static String getActivityInfoData(String path) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(ResourceBundle.getBundle(BUNDLE_PATH).getString("ACTIVITY_INFO_USERNAME"), ResourceBundle.getBundle(BUNDLE_PATH).getString("ACTIVITY_INFO_TOKEN"));
        Client client = ClientBuilder.newClient();
        final WebTarget target = client.target(path);
        target.register(feature);
        Response res = target
                .request().get();
        final String readEntity = res.readEntity(String.class);
        return readEntity;
    }

    private static String acceptKoboData(String path, String uid) {
        String real_path = path + uid + "/validation_status/";
        try (final CloseableHttpClient httpclient = HttpClients.custom()
                .build()) {
            final HttpPatch httpPatch = new HttpPatch(real_path);
            String json = "{\"validation_status.uid\":\"validation_status_approved\"}";
            StringEntity entity = new StringEntity(json);
            httpPatch.setEntity(entity);
            httpPatch.setHeader("Accept", "application/json");
            httpPatch.setHeader("Content-type", "application/json");
            httpPatch.setHeader("Authorization", "Token ad44a337a17d31337253f0f2ed0cbdac218d8d20");
            logger.log(Level.INFO, "Executing request {0} {1}", new Object[]{httpPatch.getMethod(), httpPatch.getUri()});
            try (final CloseableHttpResponse response = httpclient.execute(httpPatch)) {
                logger.log(Level.INFO, "----------------------------------------");
                logger.log(Level.INFO, "{0} {1}", new Object[]{response.getCode(), response.getReasonPhrase()});
                logger.log(Level.INFO, EntityUtils.toString(response.getEntity()));
            }
        } catch (URISyntaxException | IOException | ParseException ex) {
            Logger.getLogger(KoboClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private static final String BUNDLE_PATH = "org/unhcr/irq/utils/data/Bundle";

}
