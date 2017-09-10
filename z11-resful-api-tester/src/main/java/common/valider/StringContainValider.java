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

public class StringContainValider extends IsNotNullValider {
    String patten;
    public StringContainValider(String patten) {
        this.patten = patten;
    }
    
    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        
        if (!content.contains(patten)) {
            throw new Exception("StringContainValider not valid, string not contain, patten: " + patten + ", content:" + content);
        }
        
    }
}