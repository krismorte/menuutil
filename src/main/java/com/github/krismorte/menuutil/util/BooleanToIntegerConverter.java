/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.krismorte.menuutil.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author c007329
 */
@Converter
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean value) {
        if (value == null) {
            return null;
        } else {
            return value ? 1 : 0;
        }
    }

    @Override
    public Boolean convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        } else if (value == 1) {
            return true;
        } else if (value == 0) {
            return false;
        } else {
            throw new IllegalStateException("Invalid boolean character: " + value);
        }
    }

}
