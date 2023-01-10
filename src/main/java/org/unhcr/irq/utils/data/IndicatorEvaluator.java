/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.unhcr.irq.utils.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 *
 * @author MATAYO
 */
public class IndicatorEvaluator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String attributes = KoboClient.readAttributes();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(attributes);
            System.out.println(actualObj.toPrettyString());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(IndicatorEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
