package org.microq.dealer.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.microq.support.auditor.Chaining;
import org.microq.support.auditor.Interchange;
import org.microq.support.auditor.Sequence;
import org.microq.support.bootstrap.DefaultInterchange;
import org.microq.support.bootstrap.DefaultSequence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DefaultChainConfig {

    @Bean
    @Qualifier("default")
    public Interchange interchange(){
        return new DefaultInterchange();
    }

    @Bean
    @Qualifier("default")
    public Sequence sequence(){
        return new DefaultSequence();
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
