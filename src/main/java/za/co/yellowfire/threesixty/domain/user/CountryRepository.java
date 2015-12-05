package za.co.yellowfire.threesixty.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<Country, String> {
}
