package za.co.yellowfire.threesixty.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
}
