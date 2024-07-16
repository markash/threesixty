package za.co.yellowfire.threesixty.config;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.MongoProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.config.EnableMongoAuditing;
//import org.springframework.data.mongodb.core.convert.MongoConverter;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;

//@Configuration
//@EnableMongoAuditing
//@Import(value = MongoAutoConfiguration.class)
public class MongoConfiguration /*extends AbstractMongoConfiguration*/ {


//    @Autowired
//    private MongoProperties mongoProperties;
    
    //@Bean
    //public ValidatingMongoEventListener validatingMongoEventListener() {
    //    return new ValidatingMongoEventListener(validator());
    //}

    //@Bean
    //public LocalValidatorFactoryBean validator() {
    //    return new LocalValidatorFactoryBean();
    //}
//
//	@Override
//	public String getDatabaseName() {
//		return mongoProperties.getDatabase();
//	}
//
//	@Override
//	@Bean
//	public Mongo mongo() throws Exception {
//		ServerAddress address = new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort());
//
//		MongoClient client;
//		if (!mongoProperties.getHost().equals("127.0.0.1")) {
//			MongoCredential credential =
//					MongoCredential.createCredential(
//							mongoProperties.getUsername(),
//							mongoProperties.getDatabase(),
//							mongoProperties.getPassword());
//			ArrayList<MongoCredential> credentials = new ArrayList<MongoCredential>(1);
//			credentials.add(credential);
//
//			client = new MongoClient(address, credentials);
//		} else {
//			client = new MongoClient(address);
//		}
//
//		return client;
//	}

//	@Bean
//	public GridFsTemplate gridFsTemplate(MongoDatabaseFactory mongoDbFactory, MongoConverter mappingMongoConverter) throws Exception {
//		return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
//	}
}