package org.microq.dealer.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.microq.support.auditor.Chaining;
import org.microq.support.auditor.Interchange;
import org.microq.support.auditor.Sequence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DefaultChainConfig {

    @Bean
    @Qualifier("default")
    public Interchange interchange(){
        return new Interchange("defaultInterchange");
    }

    @Bean
    public Sequence sequence(){
        return new Sequence("defaultSequence");
    }

    @Bean
    @Qualifier("default")
    public Chaining chaining(){
        log.info("Initiating Default Interchange,Sequence");
        return Chaining.builder()
                .chain(sequence())
                .to(interchange())
                .withPath("Default-Interchange-to-Default-Sequence")
                .build();
    }
}
