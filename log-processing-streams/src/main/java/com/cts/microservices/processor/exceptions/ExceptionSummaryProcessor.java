package com.cts.microservices.processor.exceptions;

import java.util.Objects;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import com.cts.microservices.model.ExceptionRecord;
import com.cts.microservices.model.ExceptionRecordSummary;

public class ExceptionSummaryProcessor extends AbstractProcessor<String, ExceptionRecord> {
	
	private KeyValueStore<String, ExceptionRecordSummary> summaryStore;
    private ProcessorContext context;
    
    public void process(String key, ExceptionRecord exceptionRecord) {
        String currentSymbol = exceptionRecord.getName();
        ExceptionRecordSummary transactionSummary = summaryStore.get(currentSymbol);
        if (transactionSummary == null) {
            transactionSummary = ExceptionRecordSummary.fromTransaction(exceptionRecord);
        } else {
            transactionSummary.update(exceptionRecord);
        }
        summaryStore.put(currentSymbol, transactionSummary);

        this.context.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        this.context.schedule(10000);
        summaryStore = (KeyValueStore<String, ExceptionRecordSummary>) this.context.getStateStore("exception-records");
        Objects.requireNonNull(summaryStore, "State store can't be null");

    }

    @Override
    public void punctuate(long streamTime) {
        KeyValueIterator<String, ExceptionRecordSummary> it = summaryStore.all();
        long currentTime = System.currentTimeMillis();
        while (it.hasNext()) {
            ExceptionRecordSummary summary = it.next().value;
            if (summary.updatedWithinLastMillis(currentTime, 11000)) {
                this.context.forward(summary.name, summary);
            }
        }
    }

}
