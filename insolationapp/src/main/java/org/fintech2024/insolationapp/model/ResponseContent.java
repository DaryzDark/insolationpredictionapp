package org.fintech2024.insolationapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseContent implements Serializable {
    private String exampleResponseContent;
}
