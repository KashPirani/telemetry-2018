
// kafka imports
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
// avro imports
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
// java utils import
import java.util.Properties;

public class producer {

  private final static String TOPIC_BATTERY = "battery";
  private final static String TOPIC_POWERTRAIN = "powertrain";
  private final static String TOPIC_MOTOR = "motor";
  private final static String KEY = "key1";
  private final static String BOOTSTRAP_SERVERS = "localhost:9092, localhost:9093, localhost:9094";

  // creates a Kafka Producer

  private static Producer<String, byte[]> createProducer(){
    //Java.utils.properties defines certain properties to pass to constructor of kafka producer
    Properties props = new Properties();

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(Producer.CLIENT_ID_CONFIG, "KafkaProducer");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
    props.put("schema.registry.url", "http://localhost:8081");

    return new KafkaProducer<>(props);
  }

  public static final String POWERTRAIN_SCHEMA = """{
  	"type":"record",
  	"name":"Motor",
  	"namespace":"AVRO",
  	"fields":[
  		{	"name":"State", "type":["int", "null"] },
  		{	"name":"Temp", "type":["float", "null"] },
  		{	"name":"Cool_Temp_Flow", "type":["float", "null"] },
  		{	"name":"Cool_Temp_In", "type":["float", "null"] },
  		{	"name":"Cool_Temp_Out", "type":["float", "null"] },
  		{	"name":"Encoders", "type":["int", "null"] },
  		{	"name":"Speed", "type":["int", "null"] }
  	]
  }"""

  public static final String MOTOR_SCHEMA = "{"
  + "\"type\":\"record\","
  + "\"name\":\"myrecord\","
  + "\"fields\":["
  + "  { \"name\":\"str1\", \"type\":\"string\" },"
  + "  { \"name\":\"str2\", \"type\":\"string\" },"
  + "  { \"name\":\"int1\", \"type\":\"int\" }"
  + "]}";

  public static final String BATTERY_SCHEMA = "{"
  + "\"type\":\"record\","
  + "\"name\":\"myrecord\","
  + "\"fields\":["
  + "  { \"name\":\"str1\", \"type\":\"string\" },"
  + "  { \"name\":\"str2\", \"type\":\"string\" },"
  + "  { \"name\":\"int1\", \"type\":\"int\" }"
  + "]}";

  private static GenericRecord generateAvroBattery(GenericRecord avroRecord /* PARAMETERS: VALUES TO BE INPUTTED PARSED FROM SERIAL*/){
    avroRecord.put("name", "value"); // ie. ("str1", "hello")
    // FILL AVRO

    return avroRecord;
  }

  private static GenericRecord generateAvroMotor(GenericRecord avroRecord /* PARAMETERS: VALUES TO BE INPUTTED PARSED FROM SERIAL*/){
    avroRecord.put("name", "value"); // ie. ("str1", "hello")
    // FILL AVRO

    return avroRecord;
  }

  private static GenericRecord generateAvroPowertrain(GenericRecord avroRecord /* PARAMETERS: VALUES TO BE INPUTTED PARSED FROM SERIAL*/){
    avroRecord.put("name", "value"); // ie. ("str1", "hello")
    // FILL AVRO
    //avroRecord.put("name", Hasmaap.remove(KEY))

    return avroRecord;
  }

  public static void main(String[] args) throws InterruptedException {
    Schema.Parser parserA = new Schema.Parser(); // instantiate a new schema
    Schema.Parser parserB = new Schema.Parser(); // instantiate a new schema
    Schema.Parser parserC = new Schema.Parser(); // instantiate a new schema

    // define schemas
    Schema schema_battery = parserA.parse(BATTERY_SCHEMA); // USER_SCHEMA is a JSON listed above as a Java string
    Schema schema_powertrain = parserB.parse(POWERTRAIN_SCHEMA);
    Schema schema_motor = parserC.parse(MOTOR_SCHEMA);

    // Injection: object to convert objects back and forth
    Injection<GenericRecord, byte[]> recordInjectionBattery = GenericAvroCodecs.toBinary(schema_battery);
    Injection<GenericRecord, byte[]> recordInjectionMotor = GenericAvroCodecs.toBinary(schema_motor);
    Injection<GenericRecord, byte[]> recordInjectionPowertrain = GenericAvroCodecs.toBinary(schema_powertrain);

    GenericRecord avroRecordBattery = new GenericData.Record(BATTERY_SCHEMA); // option 1? from confluent schema so looks right
    GenericRecord avroRecordPowertrain = new GenericData.Record(POWERTRAIN_SCHEMA);
    GenericRecord avroRecordMotor = new GenericData.Record(MOTOR_SCHEMA);
    //GenericData.Record avroRecord = new GenericData.Record(schema); // option 2?

    while(true){

      // make call to Tyler function to get info for following 3 functions
      // put info into records
      //tylerfunction(); x 3

      generateAvroMotor(avroRecordMotor/*, DATA STORAGE OF VALUES TO INPUT*/);
      generateAvroBattery(avroRecordBattery);
      generateAvroPowertrain(generateAvroMotor);

      byte[] bytesA = recordInjection.apply(avroRecordBattery);
      byte[] bytesB = recordInjection.apply(avroRecordMotor);
      byte[] bytesC = recordInjection.apply(avroRecordPowertrain);

      ProducerRecord<Object, Object> recordBattery = new ProducerRecord<>(TOPIC_BATTERY, KEY, bytesA);
      ProducerRecord<Object, Object> recordMotor = new ProducerRecord<>(TOPIC_MOTOR, KEY, bytesB);
      ProducerRecord<Object, Object> recordPowertrain = new ProducerRecord<>(TOPIC_POWERTRAIN, KEY, bytesC);

      try{
        producer.send(recordBattery);
        producer.send(recordMotor);
        producer.send(recordPowertrain);
      } catch (SerializationException e){
        // do smthn
      }
    } // while true

    producer.flush();
    producer.close();

  } // MAIN

} // CLASS PRODUCER


/* ASYNCHRONOUS RECORD SENDING WITH KAFKA PRODUCER
static void runProducer(final int sendMessageCount) throws InterruptedException {
    final Producer<Long, String> producer = createProducer();
    long time = System.currentTimeMillis();
    final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);

    try {
        for (long index = time; index < time + sendMessageCount; index++) {
            final ProducerRecord<Long, String> record =
                    new ProducerRecord<>(TOPIC, index, "Hello Mom " + index);
            producer.send(record, (metadata, exception) -> {
                long elapsedTime = System.currentTimeMillis() - time;
                if (metadata != null) {
                    System.out.printf("sent record(key=%s value=%s) " +
                                    "meta(partition=%d, offset=%d) time=%d\n",
                            record.key(), record.value(), metadata.partition(),
                            metadata.offset(), elapsedTime);
                } else {
                    exception.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await(25, TimeUnit.SECONDS);
    }finally {
        producer.flush();
        producer.close();
    }
}

*/
