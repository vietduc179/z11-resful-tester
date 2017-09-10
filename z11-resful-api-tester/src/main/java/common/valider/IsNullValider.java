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
public class IsNullValider extends Valider {

    @Override
    public void valid(String content) throws Exception {
        if (content == null || content.length() == 0) {
            
        } else {
            throw new Exception("IsNullValider not valid, content.length() = " + content.length());
        }
    }
}
