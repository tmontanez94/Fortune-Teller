/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 *
 * @author tmontanez
 */
public class User {
    private String nameTM;
    private String passwordTM;

    public User(String nameTM, String passwordTM) {
        this.nameTM = nameTM;
        this.passwordTM = passwordTM;
    }
    public String getNameTM() {
        return nameTM;
    }

    public String getPasswordTM() {
        return passwordTM;
    }

    @Override
    public String toString() {
        return nameTM+","+passwordTM;
    }
}
