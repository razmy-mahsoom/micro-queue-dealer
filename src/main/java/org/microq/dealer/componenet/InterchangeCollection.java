package org.microq.dealer.componenet;

import lombok.Data;
import org.microq.dealer.queue.InternalInterchange;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Data
public class InterchangeCollection {

    public static Set<InternalInterchange> internalInterchangeSet = new HashSet<>();
}
