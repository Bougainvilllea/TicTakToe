package org.example.tictaktoe.Internet;

import java.io.Serializable;
import java.util.HashMap;

public class Update implements Serializable {
    public String codeName;
    public HashMap<String, Object> data;

    public Update(HashMap<String, Object> data, String name) {
        this.codeName = name;
        this.data = data;
    }

}
