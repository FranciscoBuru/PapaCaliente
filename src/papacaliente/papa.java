/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package papacaliente;

import java.io.Serializable;

/**
 *
 * @author sdist
 */
public class papa implements Serializable{
    static int id;
    int tiempo;

    public papa() {
        this.id = id +1;
        this.tiempo = (int) ((Math.random()) * 8) + 1; 
    }

    public static int getId() {
        return id;
    }
    
    public boolean tieneVida(){
        if(tiempo >0){
            tiempo = tiempo -1;
            return true;
        }
        else{
            return false;
        }
        
    }
    
    
}
