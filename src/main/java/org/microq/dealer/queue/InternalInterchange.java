package org.microq.dealer.queue;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class InternalInterchange implements Serializable {

    private String internalInterchangeName;
    private Set<InternalSequence> internalSequences = new HashSet<>();

    public InternalInterchange(String internalInterchangeName) {
        this.internalInterchangeName = internalInterchangeName;
    }

}
