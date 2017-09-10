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
public class IsNumberValider extends IsNotNullValider {

    @Override
    public void valid(String content) throws Exception {
        super.valid(content);
        System.out.println("IsNumberValider Content:" + content);
        Integer.parseInt(content);

    }
    
    public static void main(String[] args) throws Exception {
        IsNumberValider v = new IsNumberValider();
        v.valid("421");
    }
}
