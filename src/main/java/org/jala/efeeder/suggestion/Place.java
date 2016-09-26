/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jala.efeeder.suggestion;

import java.sql.Date;
import lombok.Data;

/**
 *
 * @author alexander_castro
 */
@Data
public class Place {
    private int id;
    private String name;
    private String description;
    private String phone;
    private String direction;
    private String image_link;

    public Place(int id, String name, String description, String phone, String direction, String image_link) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.direction = direction;
        this.image_link = image_link;
    }
}
