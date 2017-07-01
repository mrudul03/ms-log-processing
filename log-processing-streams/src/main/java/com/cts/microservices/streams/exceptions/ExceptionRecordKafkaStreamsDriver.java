package com.cts.microservices.streams.exceptions;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

import com.cts.microservices.model.ExceptionRecord;
import com.cts.microservices.model.ExceptionRecordCollector;
import com.cts.microservices.serializer.JsonDeserializer;
import com.cts.microservices.serializer.JsonSerializer;

public class ExceptionRecordKafkaStreamsDriver {
	
	public static void main(String[] args) {

        StreamsConfig streamingConfig = new StreamsConfig(getProperties());

        JsonSerializer<ExceptionRecordCollector> expRecordsSerializer = new JsonSerializer<>();
        JsonDeserializer<ExceptionRecordCollector> expRecordsDeserializer = new JsonDeserializer<>(ExceptionRecordCollector.class);
        
        JsonDeserializer<ExceptionRecord> expRecordDeserializer = new JsonDeserializer<>(ExceptionRecord.class);
        JsonSerializer<ExceptionRecord> expRecordJsonSerializer = new JsonSerializer<>();
        
        Serde<ExceptionRecord> expRecordSerde = Serdes.serdeFrom(expRecordJsonSerializer,expRecordDeserializer);
        
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();
        
        Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer,stringDeserializer);
        Serde<ExceptionRecordCollector> collectorSerde = Serdes.serdeFrom(expRecordsSerializer,expRecordsDeserializer);
        
        WindowedSerializer<String> windowedSerializer = new WindowedSerializer<>(stringSerializer);
        WindowedDeserializer<String> windowedDeserializer = new WindowedDeserializer<>(stringDeserializer);
        Serde<Windowed<String>> windowedSerde = Serdes.serdeFrom(windowedSerializer,windowedDeserializer);

        KStreamBuilder kStreamBuilder = new KStreamBuilder();
        KStream<String,ExceptionRecord> expRecordsKStream =  kStreamBuilder.stream(stringSerde,expRecordSerde,"exceptions");

        expRecordsKStream.map((k,v)-> new KeyValue<>(v.getName(),v))
                          .through(stringSerde, expRecordSerde,"exceptions-out")
                          .groupBy((k,v) -> k, stringSerde, expRecordSerde)
                          .aggregate(ExceptionRecordCollector::new,
                               (k, v, exceptionRecordCollector) -> exceptionRecordCollector.add(v),
                               TimeWindows.of(10000),
                               collectorSerde, "exception-summaries")
                .to(windowedSerde,collectorSerde,"exception-summary");


        System.out.println("Starting ExceptionRecordStreams Example");
        KafkaStreams kafkaStreams = new KafkaStreams(kStreamBuilder,streamingConfig);
        kafkaStreams.start();
        System.out.println("Now started ExceptionRecordStreams Example");

    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "Exception-Streams-Processor");
        props.put("group.id", "exception-streams");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "exceptions_streams_id");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }

}
