package io.reflectoring.staticdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@Configuration
@EnableScheduling
class RandomQuotePrinter {

    private static final Logger logger = LoggerFactory.getLogger(RandomQuotePrinter.class);
    private final Random random = new Random();
    private final QuotesProperties quotesProperties;

    RandomQuotePrinter(QuotesProperties quotesProperties) {
        this.quotesProperties = quotesProperties;
    }

    @Scheduled(fixedRate = 5000)
    void printRandomQuote(){
        int index = random.nextInt(quotesProperties.getQuotes().size());
        Quote quote = quotesProperties.getQuotes().get(index);
        logger.info("'{}' - {}", quote.getText(), quote.getAuthor());
    }
}
