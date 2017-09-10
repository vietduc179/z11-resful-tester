/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

/**
 *
 * @author vietduc
 */

public class StringLengthValider extends IsNotNullValider {
    String compareType;
    String length;

    public StringLengthValider(String length) {
        this(length, CompareType.EQ);
    }

    public StringLengthValider(String length, String compareType) {
        this.length = length;
        this.compareType = compareType;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        new NumberCompareValider(length, compareType).valid(""+content.length());
    }
}