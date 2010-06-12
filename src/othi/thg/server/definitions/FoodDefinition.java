/**
 * Copyright (c) 2010, The Project OTHI
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *
 *    * Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in 
 *      the documentation and/or other materials provided with the 
 *      distribution.
 *    * Neither the name of the OTHI nor the names of its contributors 
 *      may be used to endorse or promote products derived from this 
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR 
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 */

/*
 * ReadPlaceDefinition.java
 * 
 * Created on Aug 1, 2007, 11:49:13 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package othi.thg.server.definitions;
/**
 *
 * @author DongWon Kim
 */
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.w3c.dom.*;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import othi.thg.server.THGServerDefault;


public class FoodDefinition {
    
    private static final Logger logger = Logger.getLogger(FoodDefinition.class.getName());   
  
    public static void loadConfiguredFoods() throws IOException {
        
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(DefinitionsDefault.getDefinitionsDir() + DefinitionsDefault.FOOD_XML);

            Element element = document.getDocumentElement();
            if (!element.getNodeName().equals("foods")) {
                    throw new IOException("Not a food file");
            }

            NodeList foodlist = element.getElementsByTagName("food");
            for (int i = 0; i < foodlist.getLength(); i++) {
                Node foodNode = foodlist.item(i);
                if (foodNode.getNodeType() == Node.ELEMENT_NODE){  
                   
                    Element foodElement = (Element)foodNode;

                    String name = foodElement.getAttribute("name");     //name
                    int attractionPoint = Integer.parseInt(foodElement.getAttribute("attraction"));     //attractionPoint
                    int reGenTime = Integer.parseInt(foodElement.getAttribute("regentime"));     //by second             
                    String classRef = foodElement.getAttribute("class");//classRef                    
                    String imgFile = foodElement.getAttribute("file");  //fileRef

                    GameFood gameFood = new GameFood(i, name, attractionPoint, reGenTime, classRef, imgFile);
                    
                    DataManager dm = AppContext.getDataManager();
                    dm.setBinding(THGServerDefault.ITEM_FOOD + name, gameFood);                       
                }
            }
        } catch (ParserConfigurationException e) {
            logger.log(Level.SEVERE, "Parsing error", e);        
        } catch ( SAXException e) {
            logger.log(Level.SEVERE, null, e); 
        }
        logger.info("Complete loading Food Definition!");               
    }
 }