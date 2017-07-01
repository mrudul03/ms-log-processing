package com.cts.microservices.processor.exceptions;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.apache.kafka.streams.state.Stores;

import com.cts.microservices.model.ExceptionRecord;
import com.cts.microservices.model.ExceptionRecordSummary;
import com.cts.microservices.serializer.JsonDeserializer;
import com.cts.microservices.serializer.JsonSerializer;


public class ExceptionSummaryStatefulProcessorDriver {
	
	public static void main(String[] args) {

        StreamsConfig streamingConfig = new StreamsConfig(getProperties());

        TopologyBuilder builder = new TopologyBuilder();

        JsonSerializer<ExceptionRecordSummary> expRecordSummarySerializer = new JsonSerializer<>();
        JsonDeserializer<ExceptionRecordSummary> expRecordSummaryDeserializer = new JsonDeserializer<>(ExceptionRecordSummary.class);
        JsonDeserializer<ExceptionRecord> expRecordDeserializer = new JsonDeserializer<>(ExceptionRecord.class);
        JsonSerializer<ExceptionRecord> expRecordJsonSerializer = new JsonSerializer<>();
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();

        Serde<ExceptionRecordSummary> expRecordSummarySerde = Serdes.serdeFrom(expRecordSummarySerializer,expRecordSummaryDeserializer);

        builder.addSource("exceptions-source", stringDeserializer, expRecordDeserializer, "exceptions")
                       .addProcessor("summary", ExceptionSummaryProcessor::new, "exceptions-source")
                       .addStateStore(Stores.create("exception-records").withStringKeys()
                               .withValues(expRecordSummarySerde).inMemory().maxEntries(100).build(),"summary")
                       .addSink("sink", "exceptions-out", stringSerializer,expRecordJsonSerializer,"exceptions-source")
                       .addSink("sink-2", "exception-summary", stringSerializer, expRecordSummarySerializer, "summary");

        System.out.println("Starting ExceptionSummaryStatefulProcessor Example");
        KafkaStreams streaming = new KafkaStreams(builder, streamingConfig);
        streaming.start();
        System.out.println("ExceptionSummaryStatefulProcessor Example now started");

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Sample-Stateful-Processor");
        props.put("group.id", "test-consumer-group");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateful_processor_id");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }

}
