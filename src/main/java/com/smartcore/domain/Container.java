package com.smartcore.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Container implements Serializable {

    private static final long serialVersionUID = 8824479091050005672L;

    public String name;
    public Integer count;
    public  Integer bytes;


}
