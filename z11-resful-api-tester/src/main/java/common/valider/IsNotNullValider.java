/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.valider;

/**
 *
 * @author danny
 */
public class IsNotNullValider extends Valider {

    @Override
    public void valid(String content) throws Exception {
        if (content == null) { 
            throw new Exception("IsNotNullValider not valid, content is null!");
        }
        if (content.length() <= 0) {
            throw new Exception("IsNotNullValider not valid, content.length() = 0");
        }
    }
}
