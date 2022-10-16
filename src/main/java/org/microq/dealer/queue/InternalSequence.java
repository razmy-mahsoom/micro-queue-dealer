package org.microq.dealer.queue;

import lombok.Data;

import java.io.Serializable;
import java.util.PriorityQueue;
import java.util.Queue;

@Data
public class InternalSequence implements Serializable {

    private String internalSequenceName;
    private String path;
    public InternalSequence(String internalSequenceName) {
        this.internalSequenceName = internalSequenceName;
    }

    public InternalSequence(String internalSequenceName, String path) {
        this.internalSequenceName = internalSequenceName;

    }
}
