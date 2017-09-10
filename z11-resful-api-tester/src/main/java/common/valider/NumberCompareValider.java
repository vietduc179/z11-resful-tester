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
public class NumberCompareValider extends IsNotNullValider {
    String compareType;
    int compareValue;
    
    public NumberCompareValider(String compareValue) {
        this(compareValue, CompareType.EQ);
    }
    
    public NumberCompareValider(String compareValue, String compareType) {
        this.compareValue = Integer.parseInt(compareValue);
        this.compareType = compareType;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        boolean isValid = false;
        int value = Integer.parseInt(content);
        if (compareType.equals(CompareType.EQ)) {
            isValid = value == compareValue;
        } else if (compareType.equals(CompareType.GTE)) {
            isValid = value >= compareValue;
        } else if (compareType.equals(CompareType.GT)) {
            isValid = value > compareValue;
        } else if (compareType.equals(CompareType.LTE)) {
            isValid = value <= compareValue;
        } else if (compareType.equals(CompareType.LT)) {
            isValid = value < compareValue;
        } else if (compareType.equals(CompareType.NE)) {
            isValid = value != compareValue;
        } else {
            throw new Exception("NumberCompareValider, not support compare type:" + compareType);
        }

        if (!isValid) {
            throw new Exception("NumberCompareValider not valid, value: " + value + ", compare type:" + compareType);
        }
    }
}
