package it.luciferino.trainmonitoringbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GenericResponse extends GenericDTO implements Serializable {

    private static final long serialVersionUID = -4900308896754431666L;

    private GenericDTO data;

}
