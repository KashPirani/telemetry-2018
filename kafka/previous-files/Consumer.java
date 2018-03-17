public class Consumer {

	private static Scanner in;
	private static boolean stop = false; 

	public static void main(String[] args) throws Exception{

		if(argv.length != 2){
			System.err.printf("Usage: %s <topicName> <groupID>\n", 
				Consumer.class.getSimpleName()));
			System.exit(-1);
		}

		in = new Scanner(System.in);
		String topicName = argv[0];
		String groupId = argv[1]; 

		ConusmerThread consumerRunnable = new ConsumerThread(topicName, groupId);
		consumerRunnable.start();

		String line = ""; 

		while(!line.equals("exit")) {
			line = in.next();
		}

		consumerRunnable.getKafkaConsumer().wakeup();
		System.out.println("Stopping Consumer ...");
		consumerRunnable.join();
	}

	private static class ConsumerThread extends Thread{
		private String topicName; 
		private String groupId; 
		private KafkaConsumer<String, String> KafkaConsumer;

		public ConusmerThread(String topicName, String groupId){
			this.topicName = topicName;
			this.groupId = groupId;
		}

		public void run(){
			Properties configProperties = new Properties();

			configProperties.put(ConsumerConfig.BOOSTRAP_SERVERS_CONFIG, "localhost:9092");
			configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
			configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");

            //Figure where to start processing messages from
            KafkaConsumer = new KafkaConsumer<String, String>(configProperties);
            KafkaConsumer.subscribe(Arrays.asList(topicName));

            //Start processing messages 

            try {
            	while(true){
            		ConsumerRecords<String, String> records = KafkaConsumer.poll(100);
            		for(ConsumerRecord<String, String> record: records)
            			System.out.println(record.value());
            	}
            } catch(WakeupException ex){
            	System.out.println("Exception caught " + ex.getMessage());
            } finally {
            	kafkaConsumer.close();
            	System.out.println("After closing KafkaConsumer");       
            }
		}
	}

	public KafkaConsumer <String, String> getKafkaConsumer(){
		return this.KafkaConsumer;
	}
}
