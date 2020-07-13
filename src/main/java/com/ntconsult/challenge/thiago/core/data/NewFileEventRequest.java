package com.ntconsult.challenge.thiago.core.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewFileEventRequest implements Serializable {
    private String fileName;
}
