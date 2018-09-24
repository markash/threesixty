package za.co.yellowfire.threesixty.domain.user;

import com.github.markash.ui.security.CurrentUserProvider;
import com.mongodb.DBRef;
import com.vaadin.server.VaadinSession;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import za.co.yellowfire.threesixty.RequestResult;
import za.co.yellowfire.threesixty.Response;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.InvalidUserException;
import za.co.yellowfire.threesixty.domain.mail.MailingEvent;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.organization.IdentityService;
import za.co.yellowfire.threesixty.domain.organization.IdentityType;
import za.co.yellowfire.threesixty.domain.user.notification.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

@Service
public class UserService /*implements UserDetailsService*/ {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private CountryRepository countryRepository;
	private JobProfileRepository jobProfileRepository;
	private PositionRepository positionRepository;
	private UserNotificationRepository userNotificationRepository;
	private IdentityService identityService;
	
	private UserConfiguration userConfiguration;
	
	@Autowired
	private MongoTemplate template;
	
	private final GridFsClient client;
    private CurrentUserProvider<User> currentUserProvider;

	@Autowired
	public UserService(
			final UserRepository userRepository, 
			final RoleRepository roleRepository,
			final CountryRepository countryRepository,
			final JobProfileRepository jobProfileRepository,
			final PositionRepository positionRepository,
			final UserNotificationRepository userNotificationRepository,
			final UserConfiguration userConfiguration,
			final IdentityService identityService,
			final GridFsClient client,
            final CurrentUserProvider<User> currentUserProvider) {
		
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.countryRepository = countryRepository;
		this.jobProfileRepository = jobProfileRepository;
		this.positionRepository = positionRepository;
		this.userNotificationRepository = userNotificationRepository;
		this.identityService = identityService;
		this.client = client;
		this.currentUserProvider = currentUserProvider;
	}

	@PostConstruct
	public void init() {
		/* Ensure that the administrator role is configured */
		Role admin = roleRepository.findOne(Role.ROLE_ADMIN);
		if (admin == null) {
			admin = new Role(Role.ROLE_ADMIN);
			admin = roleRepository.save(admin);
		}
		
		Role user = roleRepository.findOne(Role.ROLE_USER);
		if (user == null) {
			user = new Role(Role.ROLE_USER);
			user = roleRepository.save(user);
		}
		
		/* Ensure that the administrator user is configured */
		User administrator = userRepository.findOne(User.USER_ADMIN);
		if (administrator == null) {
			administrator = new User(User.USER_ADMIN, User.USER_ADMIN_PASSWORD, admin);
			administrator.setFirstName("System");
			administrator.setLastName("Administrator");
			administrator.setEmail("admin@localhost");
			administrator.setCreatedDate(DateTime.now());
			administrator.setPasswordChangeRequired(false);
			administrator = userRepository.save(administrator);
		} else {
			administrator.setPasswordChangeRequired(false);
			administrator.setLastModifiedDate(DateTime.now());
			administrator = userRepository.save(administrator);
		}
		
		/* Sample job profiles */
		for(Position position : samplePositions) {
			Position persisted = positionRepository.findOne(position.getId());
			if (persisted == null) {
				JobProfile profile = jobProfileRepository.findOne(position.getJobProfile().getId());
				if (profile == null) {
					position.setJobProfile(jobProfileRepository.save(position.getJobProfile()));
				}
				positionRepository.save(position);
			}
		}
		
		/* Sample organisation */
		if (identityService.count() == 0) {
//			Identity root = new Identity("Identity");
//			root.child(
//					new Identity("Division 1").child(
//							new Identity("Department A").child(
//									new Identity("Team X"),
//									new Identity("Team Y")),
//							new Identity("Department B")).child(
//									new Identity("Team S"),
//									new Identity("Team T")),
//					new Identity("Division 2")).child(
//							new Identity("Department A").child(
//									new Identity("Team X"),
//									new Identity("Team Y"),
//							new Identity("Department B")).child(
//									new Identity("Team S"),
//									new Identity("Team T")
//							)
//					);

			Identity root = new Identity("Identity", IdentityType.Organization).child(
					new Identity("Division 1", IdentityType.Division).child(
							new Identity("Department A", IdentityType.Department).child(
									new Identity("Team X", IdentityType.Team),
									new Identity("Team Y", IdentityType.Team),
									new Identity("Team Z", IdentityType.Team)),
							new Identity("Department B", IdentityType.Department)),
					new Identity("Division 2", IdentityType.Division).child(
							new Identity("Department X", IdentityType.Department),
							new Identity("Department Z", IdentityType.Department).child(
									new Identity("Team 1", IdentityType.Team),
									new Identity("Team 2", IdentityType.Team),
									new Identity("Team 3", IdentityType.Team)))
					);

			identityService.persist(root);
		}
		
		/* Ensure the countries are defined */
		countryRepository.save(Arrays.asList(countries));
		
		/*Notify that the server is running */
		notify(
				administrator,
				administrator,
				NotificationCategory.System,
				NotificationAction.Started.name(),
				"Server started.");
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public Response<User> authenticate(final String userName, final String password) {
		User user = userRepository.findOne(userName);
		if (user == null) { return new Response<>(RequestResult.UNAUTHORIZED.setDescription("The user is invalid")); }
		if (!user.getPassword().equals(password)) { return new Response<>(RequestResult.UNAUTHORIZED.setDescription("The user is invalid")); }
		
		user.retrievePictureSilently(client);
		return new Response<>(RequestResult.OK, user);
	}
	
	public Response<User> changePassword(final String userName, final String oldPassword, final String newPassword) {
		User user = userRepository.findOne(userName);
		if (user == null) { return new Response<>(RequestResult.UNAUTHORIZED.setDescription("The user is invalid")); }
		if (!user.getPassword().equals(oldPassword)) { return new Response<>(RequestResult.UNAUTHORIZED.setDescription("The user is invalid")); }
		
		user.setPassword(newPassword);
		user.setPasswordChangeRequired(false);
		user.setLastModifiedBy(user);
		user.setLastModifiedDate(DateTime.now());
		
		user = userRepository.save(user);
		user.retrievePictureSilently(client);
		
		return new Response<>(RequestResult.OK, user);
	}
	
	public void resetPassword(User user, String changedBy) {
		user.setPasswordChangeRequired(true);
		user.setLastModifiedBy(user);
		user.setLastModifiedDate(DateTime.now());
		user = userRepository.save(user);
	}
	
	public User findUser(final String id) {
		User user =  userRepository.findOne(id);
		if (user != null) {
			try {
				user.retrievePicture(client);
			} catch (IOException e) {
				LOG.warn("Unable to load the profile picture for user " + id, e);
			}
		}
		return user;
	}

	public List<User> findUsers() {
		return userRepository.findByActive(true);
	}

    /**
     * Finds all the users except the user
     * @param user The user to exclude from the list
     * @return The list of users
     */
	public List<User> findUsersExcept(final User user) {
		return userRepository.findByIdNot(user.getId());
	}

    /**
     * Finds all the users except the current session user
     * @return The list of users
     */
	public List<User> findUsersExceptCurrent() {

	    Optional<User> principal = this.currentUserProvider.get();
	    if (principal.isPresent()) {
            return userRepository.findByIdNot(principal.get().getId());
        }

        LOG.warn("No current user when calling findUsersExceptCurrent which might be an error");
		return findUsers();
	}

	public User save(final User user) throws IOException {
        Objects.requireNonNull(user, "The user to save is required");

        this.currentUserProvider.get().ifPresent(user::auditChangedBy);
		user.storePicture(client);

		return userRepository.save(user);
	}
	
	
	
	public void delete(final User user, final User changedBy) {
		user.setActive(false);
		user.auditChangedBy(changedBy);
		userRepository.delete(user);
	}
	
	public List<String> findSalutations() {
		return new ArrayList<String>(Arrays.asList(salutations));
	}
	
	public List<Country> findCountries() {
		return countryRepository.findAll(new Sort("commonName"));
	}
	
	public List<Role> findRoles() {
		return roleRepository.findAll(new Sort("id"));
	}
	
	public List<String> findGenders() {
		return new ArrayList<String>(Arrays.asList(genders));
	}
	
	public List<Position> findPositions() {
		return positionRepository.findAll(new Sort("id"));
	}
	
	public List<Identity> findDepartments() {
		return this.identityService.retrieve(IdentityType.Department, true);
	}
	
	public List<User> findUsersForDepartment(final Identity identity) {
		return this.userRepository.findByDepartment(identity.getId());
	}
	
	public int getUnreadNotificationsCount(@NotNull final User user) {
		if (user == null) { throw new InvalidUserException("User was null"); }
		
		List<UserNotification> notifications = this.userNotificationRepository.findUnread(user.getId());
		return  (notifications != null) ? notifications.size() : 0;
	}
	
	public List<UserNotification> findNotifications(@NotNull final User user, int limit) {
		if (user == null) { throw new InvalidUserException("User was null"); }
		
		Sort sort = new Sort(Direction.DESC, "time");
		List<UserNotification> notifications = this.userNotificationRepository.findNotifications(user.getId(), limit, sort);
		if (notifications != null && limit > 0 && notifications.size() > limit) {
			return notifications.subList(0, limit - 1);
		}
		return notifications;
	}
	
	public List<UserNotification> findNotifications(@NotNull final User user) {
		return findNotifications(user, 10);
	}
	
	public void clearNotifications(@NotNull final User user) {
		List<UserNotification> notifications = findNotifications(user, 1000);
		for(UserNotification notification : notifications) {
			notification.setActive(false);
		}
		userNotificationRepository.save(notifications);
	}
	
	public List<NotificationSummary> getNotificationSummaries(@NotNull User user) {
		
		Criteria isActive = Criteria.where("active").is(true);
		Criteria isForUser = Criteria.where("user").is(new DBRef("user", user.getId()));
		
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(isForUser.andOperator(isActive)),
				Aggregation.group("category").count().as("count"),
				Aggregation.project("count").and("category").previousOperation(),
				Aggregation.sort(Sort.Direction.DESC, "category")
				);
		
		return template.aggregate(
						aggregation, 
						UserNotification.class, 
						NotificationSummary.class).getMappedResults();
	}

	@Async
	@EventListener
	public void mailingKeyInvalid(
			final MailingEvent event) {

		notify(
				findUser("admin"),
				getCurrentUser(),
				NotificationCategory.System,
				event.getAction(),
				event.getMessage());
	}

	public void notify(
			final User to,
			final String message) {

		notify(to, null, NotificationCategory.System, null, message);
	}
	
	public void notify(
			final User to,
			final User from,
			final NotificationCategory category,
			final String action,
			final String message) {

		this.userNotificationRepository.save(
				UserNotification
						.to(to)
						.from(from)
						.at(DateTime.now())
						.category(category)
						.action(action)
						.content(message));
	}
	
	public User getCurrentUser() {
		User user = VaadinSession.getCurrent().getAttribute(User.class);
		if (user == null) {
			user = (User) ((SecurityContext) VaadinSession.getCurrent().getAttribute("org.vaadin.spring.security.internal.springSecurityContext")).getAuthentication().getPrincipal();
		}
		return user;
	}
	
	private static String[] genders = {
			"Male",
			"Female",
			"Other"
	};
	
	private static String[] salutations = {
			"Mr",
			"Mrs",
			"Ms",
			"Miss",
			"Dr",
			"Prof",
			"Rev",
			"Mx",
			"Other"
	};
	
	private static Country[] countries = {
			new Country (1,"Afghanistan","Islamic State of Afghanistan","Independent State",null,null,"Kabul","AFN","Afghani","+93","AF","AFG","004",".af"),
			new Country (2,"Albania","Republic of Albania","Independent State",null,null,"Tirana","ALL","Lek","+355","AL","ALB","008",".al"),
			new Country (3,"Algeria","People's Democratic Republic of Algeria","Independent State",null,null,"Algiers","DZD","Dinar","+213","DZ","DZA","012",".dz"),
			new Country (4,"Andorra","Principality of Andorra","Independent State",null,null,"Andorra la Vella","EUR","Euro","+376","AD","AND","020",".ad"),
			new Country (5,"Angola","Republic of Angola","Independent State",null,null,"Luanda","AOA","Kwanza","+244","AO","AGO","024",".ao"),
			new Country (6,"Antigua and Barbuda",null,"Independent State",null,null,"Saint John's","XCD","Dollar","+1-268","AG","ATG","028",".ag"),
			new Country (7,"Argentina","Argentine Republic","Independent State",null,null,"Buenos Aires","ARS","Peso","+54","AR","ARG","032",".ar"),
			new Country (8,"Armenia","Republic of Armenia","Independent State",null,null,"Yerevan","AMD","Dram","+374","AM","ARM","051",".am"),
			new Country (9,"Australia","Commonwealth of Australia","Independent State",null,null,"Canberra","AUD","Dollar","+61","AU","AUS","036",".au"),
			new Country (10,"Austria","Republic of Austria","Independent State",null,null,"Vienna","EUR","Euro","+43","AT","AUT","040",".at"),
			new Country (11,"Azerbaijan","Republic of Azerbaijan","Independent State",null,null,"Baku","AZN","Manat","+994","AZ","AZE","031",".az"),
			new Country (12,"Bahamas, The","Commonwealth of The Bahamas","Independent State",null,null,"Nassau","BSD","Dollar","+1-242","BS","BHS","044",".bs"),
			new Country (13,"Bahrain","Kingdom of Bahrain","Independent State",null,null,"Manama","BHD","Dinar","+973","BH","BHR","048",".bh"),
			new Country (14,"Bangladesh","People's Republic of Bangladesh","Independent State",null,null,"Dhaka","BDT","Taka","+880","BD","BGD","050",".bd"),
			new Country (15,"Barbados",null,"Independent State",null,null,"Bridgetown","BBD","Dollar","+1-246","BB","BRB","052",".bb"),
			new Country (16,"Belarus","Republic of Belarus","Independent State",null,null,"Minsk","BYR","Ruble","+375","BY","BLR","112",".by"),
			new Country (17,"Belgium","Kingdom of Belgium","Independent State",null,null,"Brussels","EUR","Euro","+32","BE","BEL","056",".be"),
			new Country (18,"Belize",null,"Independent State",null,null,"Belmopan","BZD","Dollar","+501","BZ","BLZ","084",".bz"),
			new Country (19,"Benin","Republic of Benin","Independent State",null,null,"Porto-Novo","XOF","Franc","+229","BJ","BEN","204",".bj"),
			new Country (20,"Bhutan","Kingdom of Bhutan","Independent State",null,null,"Thimphu","BTN","Ngultrum","+975","BT","BTN","064",".bt"),
			new Country (21,"Bolivia","Republic of Bolivia","Independent State",null,null,"La Paz (administrative/legislative) and Sucre (judical)","BOB","Boliviano","+591","BO","BOL","068",".bo"),
			new Country (22,"Bosnia and Herzegovina",null,"Independent State",null,null,"Sarajevo","BAM","Marka","+387","BA","BIH","070",".ba"),
			new Country (23,"Botswana","Republic of Botswana","Independent State",null,null,"Gaborone","BWP","Pula","+267","BW","BWA","072",".bw"),
			new Country (24,"Brazil","Federative Republic of Brazil","Independent State",null,null,"Brasilia","BRL","Real","+55","BR","BRA","076",".br"),
			new Country (25,"Brunei","Negara Brunei Darussalam","Independent State",null,null,"Bandar Seri Begawan","BND","Dollar","+673","BN","BRN","096",".bn"),
			new Country (26,"Bulgaria","Republic of Bulgaria","Independent State",null,null,"Sofia","BGN","Lev","+359","BG","BGR","100",".bg"),
			new Country (27,"Burkina Faso",null,"Independent State",null,null,"Ouagadougou","XOF","Franc","+226","BF","BFA","854",".bf"),
			new Country (28,"Burundi","Republic of Burundi","Independent State",null,null,"Bujumbura","BIF","Franc","+257","BI","BDI","108",".bi"),
			new Country (29,"Cambodia","Kingdom of Cambodia","Independent State",null,null,"Phnom Penh","KHR","Riels","+855","KH","KHM","116",".kh"),
			new Country (30,"Cameroon","Republic of Cameroon","Independent State",null,null,"Yaounde","XAF","Franc","+237","CM","CMR","120",".cm"),
			new Country (31,"Canada",null,"Independent State",null,null,"Ottawa","CAD","Dollar","+1","CA","CAN","124",".ca"),
			new Country (32,"Cape Verde","Republic of Cape Verde","Independent State",null,null,"Praia","CVE","Escudo","+238","CV","CPV","132",".cv"),
			new Country (33,"Central African Republic",null,"Independent State",null,null,"Bangui","XAF","Franc","+236","CF","CAF","140",".cf"),
			new Country (34,"Chad","Republic of Chad","Independent State",null,null,"N'Djamena","XAF","Franc","+235","TD","TCD","148",".td"),
			new Country (35,"Chile","Republic of Chile","Independent State",null,null,"Santiago (administrative/judical) and Valparaiso (legislative)","CLP","Peso","+56","CL","CHL","152",".cl"),
			new Country (36,"China, People's Republic of","People's Republic of China","Independent State",null,null,"Beijing","CNY","Yuan Renminbi","+86","CN","CHN","156",".cn"),
			new Country (37,"Colombia","Republic of Colombia","Independent State",null,null,"Bogota","COP","Peso","+57","CO","COL","170",".co"),
			new Country (38,"Comoros","Union of Comoros","Independent State",null,null,"Moroni","KMF","Franc","+269","KM","COM","174",".km"),
			new Country (39,"Congo, Democratic Republic of the (Congo – Kinshasa)","Democratic Republic of the Congo","Independent State",null,null,"Kinshasa","CDF","Franc","+243","CD","COD","180",".cd"),
			new Country (40,"Congo, Republic of the (Congo – Brazzaville)","Republic of the Congo","Independent State",null,null,"Brazzaville","XAF","Franc","+242","CG","COG","178",".cg"),
			new Country (41,"Costa Rica","Republic of Costa Rica","Independent State",null,null,"San Jose","CRC","Colon","+506","CR","CRI","188",".cr"),
			new Country (42,"Cote d'Ivoire (Ivory Coast)","Republic of Cote d'Ivoire","Independent State",null,null,"Yamoussoukro","XOF","Franc","+225","CI","CIV","384",".ci"),
			new Country (43,"Croatia","Republic of Croatia","Independent State",null,null,"Zagreb","HRK","Kuna","+385","HR","HRV","191",".hr"),
			new Country (44,"Cuba","Republic of Cuba","Independent State",null,null,"Havana","CUP","Peso","+53","CU","CUB","192",".cu"),
			new Country (45,"Cyprus","Republic of Cyprus","Independent State",null,null,"Nicosia","CYP","Pound","+357","CY","CYP","196",".cy"),
			new Country (46,"Czech Republic",null,"Independent State",null,null,"Prague","CZK","Koruna","+420","CZ","CZE","203",".cz"),
			new Country (47,"Denmark","Kingdom of Denmark","Independent State",null,null,"Copenhagen","DKK","Krone","+45","DK","DNK","208",".dk"),
			new Country (48,"Djibouti","Republic of Djibouti","Independent State",null,null,"Djibouti","DJF","Franc","+253","DJ","DJI","262",".dj"),
			new Country (49,"Dominica","Commonwealth of Dominica","Independent State",null,null,"Roseau","XCD","Dollar","+1-767","DM","DMA","212",".dm"),
			new Country (50,"Dominican Republic",null,"Independent State",null,null,"Santo Domingo","DOP","Peso","+1-809 and 1-829","DO","DOM","214",".do"),
			new Country (51,"Ecuador","Republic of Ecuador","Independent State",null,null,"Quito","USD","Dollar","+593","EC","ECU","218",".ec"),
			new Country (52,"Egypt","Arab Republic of Egypt","Independent State",null,null,"Cairo","EGP","Pound","+20","EG","EGY","818",".eg"),
			new Country (53,"El Salvador","Republic of El Salvador","Independent State",null,null,"San Salvador","USD","Dollar","+503","SV","SLV","222",".sv"),
			new Country (54,"Equatorial Guinea","Republic of Equatorial Guinea","Independent State",null,null,"Malabo","XAF","Franc","+240","GQ","GNQ","226",".gq"),
			new Country (55,"Eritrea","State of Eritrea","Independent State",null,null,"Asmara","ERN","Nakfa","+291","ER","ERI","232",".er"),
			new Country (56,"Estonia","Republic of Estonia","Independent State",null,null,"Tallinn","EEK","Kroon","+372","EE","EST","233",".ee"),
			new Country (57,"Ethiopia","Federal Democratic Republic of Ethiopia","Independent State",null,null,"Addis Ababa","ETB","Birr","+251","ET","ETH","231",".et"),
			new Country (58,"Fiji","Republic of the Fiji Islands","Independent State",null,null,"Suva","FJD","Dollar","+679","FJ","FJI","242",".fj"),
			new Country (59,"Finland","Republic of Finland","Independent State",null,null,"Helsinki","EUR","Euro","+358","FI","FIN","246",".fi"),
			new Country (60,"France","French Republic","Independent State",null,null,"Paris","EUR","Euro","+33","FR","FRA","250",".fr"),
			new Country (61,"Gabon","Gabonese Republic","Independent State",null,null,"Libreville","XAF","Franc","+241","GA","GAB","266",".ga"),
			new Country (62,"Gambia, The","Republic of The Gambia","Independent State",null,null,"Banjul","GMD","Dalasi","+220","GM","GMB","270",".gm"),
			new Country (63,"Georgia","Republic of Georgia","Independent State",null,null,"Tbilisi","GEL","Lari","+995","GE","GEO","268",".ge"),
			new Country (64,"Germany","Federal Republic of Germany","Independent State",null,null,"Berlin","EUR","Euro","+49","DE","DEU","276",".de"),
			new Country (65,"Ghana","Republic of Ghana","Independent State",null,null,"Accra","GHS","Cedi","+233","GH","GHA","288",".gh"),
			new Country (66,"Greece","Hellenic Republic","Independent State",null,null,"Athens","EUR","Euro","+30","GR","GRC","300",".gr"),
			new Country (67,"Grenada",null,"Independent State",null,null,"Saint George's","XCD","Dollar","+1-473","GD","GRD","308",".gd"),
			new Country (68,"Guatemala","Republic of Guatemala","Independent State",null,null,"Guatemala","GTQ","Quetzal","+502","GT","GTM","320",".gt"),
			new Country (69,"Guinea","Republic of Guinea","Independent State",null,null,"Conakry","GNF","Franc","+224","GN","GIN","324",".gn"),
			new Country (70,"Guinea-Bissau","Republic of Guinea-Bissau","Independent State",null,null,"Bissau","XOF","Franc","+245","GW","GNB","624",".gw"),
			new Country (71,"Guyana","Co-operative Republic of Guyana","Independent State",null,null,"Georgetown","GYD","Dollar","+592","GY","GUY","328",".gy"),
			new Country (72,"Haiti","Republic of Haiti","Independent State",null,null,"Port-au-Prince","HTG","Gourde","+509","HT","HTI","332",".ht"),
			new Country (73,"Honduras","Republic of Honduras","Independent State",null,null,"Tegucigalpa","HNL","Lempira","+504","HN","HND","340",".hn"),
			new Country (74,"Hungary","Republic of Hungary","Independent State",null,null,"Budapest","HUF","Forint","+36","HU","HUN","348",".hu"),
			new Country (75,"Iceland","Republic of Iceland","Independent State",null,null,"Reykjavik","ISK","Krona","+354","IS","ISL","352",".is"),
			new Country (76,"India","Republic of India","Independent State",null,null,"New Delhi","INR","Rupee","+91","IN","IND","356",".in"),
			new Country (77,"Indonesia","Republic of Indonesia","Independent State",null,null,"Jakarta","IDR","Rupiah","+62","ID","IDN","360",".id"),
			new Country (78,"Iran","Islamic Republic of Iran","Independent State",null,null,"Tehran","IRR","Rial","+98","IR","IRN","364",".ir"),
			new Country (79,"Iraq","Republic of Iraq","Independent State",null,null,"Baghdad","IQD","Dinar","+964","IQ","IRQ","368",".iq"),
			new Country (80,"Ireland",null,"Independent State",null,null,"Dublin","EUR","Euro","+353","IE","IRL","372",".ie"),
			new Country (81,"Israel","State of Israel","Independent State",null,null,"Jerusalem","ILS","Shekel","+972","IL","ISR","376",".il"),
			new Country (82,"Italy","Italian Republic","Independent State",null,null,"Rome","EUR","Euro","+39","IT","ITA","380",".it"),
			new Country (83,"Jamaica",null,"Independent State",null,null,"Kingston","JMD","Dollar","+1-876","JM","JAM","388",".jm"),
			new Country (84,"Japan",null,"Independent State",null,null,"Tokyo","JPY","Yen","+81","JP","JPN","392",".jp"),
			new Country (85,"Jordan","Hashemite Kingdom of Jordan","Independent State",null,null,"Amman","JOD","Dinar","+962","JO","JOR","400",".jo"),
			new Country (86,"Kazakhstan","Republic of Kazakhstan","Independent State",null,null,"Astana","KZT","Tenge","+7","KZ","KAZ","398",".kz"),
			new Country (87,"Kenya","Republic of Kenya","Independent State",null,null,"Nairobi","KES","Shilling","+254","KE","KEN","404",".ke"),
			new Country (88,"Kiribati","Republic of Kiribati","Independent State",null,null,"Tarawa","AUD","Dollar","+686","KI","KIR","296",".ki"),
			new Country (89,"Korea, Democratic People's Republic of (North Korea)","Democratic People's Republic of Korea","Independent State",null,null,"Pyongyang","KPW","Won","+850","KP","PRK","408",".kp"),
			new Country (90,"Korea, Republic of  (South Korea)","Republic of Korea","Independent State",null,null,"Seoul","KRW","Won","+82","KR","KOR","410",".kr"),
			new Country (91,"Kuwait","State of Kuwait","Independent State",null,null,"Kuwait","KWD","Dinar","+965","KW","KWT","414",".kw"),
			new Country (92,"Kyrgyzstan","Kyrgyz Republic","Independent State",null,null,"Bishkek","KGS","Som","+996","KG","KGZ","417",".kg"),
			new Country (93,"Laos","Lao People's Democratic Republic","Independent State",null,null,"Vientiane","LAK","Kip","+856","LA","LAO","418",".la"),
			new Country (94,"Latvia","Republic of Latvia","Independent State",null,null,"Riga","LVL","Lat","+371","LV","LVA","428",".lv"),
			new Country (95,"Lebanon","Lebanese Republic","Independent State",null,null,"Beirut","LBP","Pound","+961","LB","LBN","422",".lb"),
			new Country (96,"Lesotho","Kingdom of Lesotho","Independent State",null,null,"Maseru","LSL","Loti","+266","LS","LSO","426",".ls"),
			new Country (97,"Liberia","Republic of Liberia","Independent State",null,null,"Monrovia","LRD","Dollar","+231","LR","LBR","430",".lr"),
			new Country (98,"Libya","Great Socialist People's Libyan Arab Jamahiriya","Independent State",null,null,"Tripoli","LYD","Dinar","+218","LY","LBY","434",".ly"),
			new Country (99,"Liechtenstein","Principality of Liechtenstein","Independent State",null,null,"Vaduz","CHF","Franc","+423","LI","LIE","438",".li"),
			new Country (100,"Lithuania","Republic of Lithuania","Independent State",null,null,"Vilnius","LTL","Litas","+370","LT","LTU","440",".lt"),
			new Country (101,"Luxembourg","Grand Duchy of Luxembourg","Independent State",null,null,"Luxembourg","EUR","Euro","+352","LU","LUX","442",".lu"),
			new Country (102,"Macedonia","Republic of Macedonia","Independent State",null,null,"Skopje","MKD","Denar","+389","MK","MKD","807",".mk"),
			new Country (103,"Madagascar","Republic of Madagascar","Independent State",null,null,"Antananarivo","MGA","Ariary","+261","MG","MDG","450",".mg"),
			new Country (104,"Malawi","Republic of Malawi","Independent State",null,null,"Lilongwe","MWK","Kwacha","+265","MW","MWI","454",".mw"),
			new Country (105,"Malaysia",null,"Independent State",null,null,"Kuala Lumpur (legislative/judical) and Putrajaya (administrative)","MYR","Ringgit","+60","MY","MYS","458",".my"),
			new Country (106,"Maldives","Republic of Maldives","Independent State",null,null,"Male","MVR","Rufiyaa","+960","MV","MDV","462",".mv"),
			new Country (107,"Mali","Republic of Mali","Independent State",null,null,"Bamako","XOF","Franc","+223","ML","MLI","466",".ml"),
			new Country (108,"Malta","Republic of Malta","Independent State",null,null,"Valletta","MTL","Lira","+356","MT","MLT","470",".mt"),
			new Country (109,"Marshall Islands","Republic of the Marshall Islands","Independent State",null,null,"Majuro","USD","Dollar","+692","MH","MHL","584",".mh"),
			new Country (110,"Mauritania","Islamic Republic of Mauritania","Independent State",null,null,"Nouakchott","MRO","Ouguiya","+222","MR","MRT","478",".mr"),
			new Country (111,"Mauritius","Republic of Mauritius","Independent State",null,null,"Port Louis","MUR","Rupee","+230","MU","MUS","480",".mu"),
			new Country (112,"Mexico","United Mexican States","Independent State",null,null,"Mexico","MXN","Peso","+52","MX","MEX","484",".mx"),
			new Country (113,"Micronesia","Federated States of Micronesia","Independent State",null,null,"Palikir","USD","Dollar","+691","FM","FSM","583",".fm"),
			new Country (114,"Moldova","Republic of Moldova","Independent State",null,null,"Chisinau","MDL","Leu","+373","MD","MDA","498",".md"),
			new Country (115,"Monaco","Principality of Monaco","Independent State",null,null,"Monaco","EUR","Euro","+377","MC","MCO","492",".mc"),
			new Country (116,"Mongolia",null,"Independent State",null,null,"Ulaanbaatar","MNT","Tugrik","+976","MN","MNG","496",".mn"),
			new Country (117,"Montenegro","Republic of Montenegro","Independent State",null,null,"Podgorica","EUR","Euro","+382","ME","MNE","499",".me and .yu"),
			new Country (118,"Morocco","Kingdom of Morocco","Independent State",null,null,"Rabat","MAD","Dirham","+212","MA","MAR","504",".ma"),
			new Country (119,"Mozambique","Republic of Mozambique","Independent State",null,null,"Maputo","MZM","Meticail","+258","MZ","MOZ","508",".mz"),
			new Country (120,"Myanmar (Burma)","Union of Myanmar","Independent State",null,null,"Naypyidaw","MMK","Kyat","+95","MM","MMR","104",".mm"),
			new Country (121,"Namibia","Republic of Namibia","Independent State",null,null,"Windhoek","NAD","Dollar","+264","NA","NAM","516",".na"),
			new Country (122,"Nauru","Republic of Nauru","Independent State",null,null,"Yaren","AUD","Dollar","+674","NR","NRU","520",".nr"),
			new Country (123,"Nepal",null,"Independent State",null,null,"Kathmandu","NPR","Rupee","+977","NP","NPL","524",".np"),
			new Country (124,"Netherlands","Kingdom of the Netherlands","Independent State",null,null,"Amsterdam (administrative) and The Hague (legislative/judical)","EUR","Euro","+31","NL","NLD","528",".nl"),
			new Country (125,"New Zealand",null,"Independent State",null,null,"Wellington","NZD","Dollar","+64","NZ","NZL","554",".nz"),
			new Country (126,"Nicaragua","Republic of Nicaragua","Independent State",null,null,"Managua","NIO","Cordoba","+505","NI","NIC","558",".ni"),
			new Country (127,"Niger","Republic of Niger","Independent State",null,null,"Niamey","XOF","Franc","+227","NE","NER","562",".ne"),
			new Country (128,"Nigeria","Federal Republic of Nigeria","Independent State",null,null,"Abuja","NGN","Naira","+234","NG","NGA","566",".ng"),
			new Country (129,"Norway","Kingdom of Norway","Independent State",null,null,"Oslo","NOK","Krone","+47","NO","NOR","578",".no"),
			new Country (130,"Oman","Sultanate of Oman","Independent State",null,null,"Muscat","OMR","Rial","+968","OM","OMN","512",".om"),
			new Country (131,"Pakistan","Islamic Republic of Pakistan","Independent State",null,null,"Islamabad","PKR","Rupee","+92","PK","PAK","586",".pk"),
			new Country (132,"Palau","Republic of Palau","Independent State",null,null,"Melekeok","USD","Dollar","+680","PW","PLW","585",".pw"),
			new Country (133,"Panama","Republic of Panama","Independent State",null,null,"Panama","PAB","Balboa","+507","PA","PAN","591",".pa"),
			new Country (134,"Papua New Guinea","Independent State of Papua New Guinea","Independent State",null,null,"Port Moresby","PGK","Kina","+675","PG","PNG","598",".pg"),
			new Country (135,"Paraguay","Republic of Paraguay","Independent State",null,null,"Asuncion","PYG","Guarani","+595","PY","PRY","600",".py"),
			new Country (136,"Peru","Republic of Peru","Independent State",null,null,"Lima","PEN","Sol","+51","PE","PER","604",".pe"),
			new Country (137,"Philippines","Republic of the Philippines","Independent State",null,null,"Manila","PHP","Peso","+63","PH","PHL","608",".ph"),
			new Country (138,"Poland","Republic of Poland","Independent State",null,null,"Warsaw","PLN","Zloty","+48","PL","POL","616",".pl"),
			new Country (139,"Portugal","Portuguese Republic","Independent State",null,null,"Lisbon","EUR","Euro","+351","PT","PRT","620",".pt"),
			new Country (140,"Qatar","State of Qatar","Independent State",null,null,"Doha","QAR","Rial","+974","QA","QAT","634",".qa"),
			new Country (141,"Romania",null,"Independent State",null,null,"Bucharest","RON","Leu","+40","RO","ROU","642",".ro"),
			new Country (142,"Russia","Russian Federation","Independent State",null,null,"Moscow","RUB","Ruble","+7","RU","RUS","643",".ru and .su"),
			new Country (143,"Rwanda","Republic of Rwanda","Independent State",null,null,"Kigali","RWF","Franc","+250","RW","RWA","646",".rw"),
			new Country (144,"Saint Kitts and Nevis","Federation of Saint Kitts and Nevis","Independent State",null,null,"Basseterre","XCD","Dollar","+1-869","KN","KNA","659",".kn"),
			new Country (145,"Saint Lucia",null,"Independent State",null,null,"Castries","XCD","Dollar","+1-758","LC","LCA","662",".lc"),
			new Country (146,"Saint Vincent and the Grenadines",null,"Independent State",null,null,"Kingstown","XCD","Dollar","+1-784","VC","VCT","670",".vc"),
			new Country (147,"Samoa","Independent State of Samoa","Independent State",null,null,"Apia","WST","Tala","+685","WS","WSM","882",".ws"),
			new Country (148,"San Marino","Republic of San Marino","Independent State",null,null,"San Marino","EUR","Euro","+378","SM","SMR","674",".sm"),
			new Country (149,"Sao Tome and Principe","Democratic Republic of Sao Tome and Principe","Independent State",null,null,"Sao Tome","STD","Dobra","+239","ST","STP","678",".st"),
			new Country (150,"Saudi Arabia","Kingdom of Saudi Arabia","Independent State",null,null,"Riyadh","SAR","Rial","+966","SA","SAU","682",".sa"),
			new Country (151,"Senegal","Republic of Senegal","Independent State",null,null,"Dakar","XOF","Franc","+221","SN","SEN","686",".sn"),
			new Country (152,"Serbia","Republic of Serbia","Independent State",null,null,"Belgrade","RSD","Dinar","+381","RS","SRB","688",".rs and .yu"),
			new Country (153,"Seychelles","Republic of Seychelles","Independent State",null,null,"Victoria","SCR","Rupee","+248","SC","SYC","690",".sc"),
			new Country (154,"Sierra Leone","Republic of Sierra Leone","Independent State",null,null,"Freetown","SLL","Leone","+232","SL","SLE","694",".sl"),
			new Country (155,"Singapore","Republic of Singapore","Independent State",null,null,"Singapore","SGD","Dollar","+65","SG","SGP","702",".sg"),
			new Country (156,"Slovakia","Slovak Republic","Independent State",null,null,"Bratislava","SKK","Koruna","+421","SK","SVK","703",".sk"),
			new Country (157,"Slovenia","Republic of Slovenia","Independent State",null,null,"Ljubljana","EUR","Euro","+386","SI","SVN","705",".si"),
			new Country (158,"Solomon Islands",null,"Independent State",null,null,"Honiara","SBD","Dollar","+677","SB","SLB","090",".sb"),
			new Country (159,"Somalia",null,"Independent State",null,null,"Mogadishu","SOS","Shilling","+252","SO","SOM","706",".so"),
			new Country (160,"South Africa","Republic of South Africa","Independent State",null,null,"Pretoria (administrative), Cape Town (legislative), and Bloemfontein (judical)","ZAR","Rand","+27","ZA","ZAF","710",".za"),
			new Country (161,"Spain","Kingdom of Spain","Independent State",null,null,"Madrid","EUR","Euro","+34","ES","ESP","724",".es"),
			new Country (162,"Sri Lanka","Democratic Socialist Republic of Sri Lanka","Independent State",null,null,"Colombo (administrative/judical) and Sri Jayewardenepura Kotte (legislative)","LKR","Rupee","+94","LK","LKA","144",".lk"),
			new Country (163,"Sudan","Republic of the Sudan","Independent State",null,null,"Khartoum","SDG","Pound","+249","SD","SDN","736",".sd"),
			new Country (164,"Suriname","Republic of Suriname","Independent State",null,null,"Paramaribo","SRD","Dollar","+597","SR","SUR","740",".sr"),
			new Country (165,"Swaziland","Kingdom of Swaziland","Independent State",null,null,"Mbabane (administrative) and Lobamba (legislative)","SZL","Lilangeni","+268","SZ","SWZ","748",".sz"),
			new Country (166,"Sweden","Kingdom of Sweden","Independent State",null,null,"Stockholm","SEK","Kronoa","+46","SE","SWE","752",".se"),
			new Country (167,"Switzerland","Swiss Confederation","Independent State",null,null,"Bern","CHF","Franc","+41","CH","CHE","756",".ch"),
			new Country (168,"Syria","Syrian Arab Republic","Independent State",null,null,"Damascus","SYP","Pound","+963","SY","SYR","760",".sy"),
			new Country (169,"Tajikistan","Republic of Tajikistan","Independent State",null,null,"Dushanbe","TJS","Somoni","+992","TJ","TJK","762",".tj"),
			new Country (170,"Tanzania","United Republic of Tanzania","Independent State",null,null,"Dar es Salaam (administrative/judical) and Dodoma (legislative)","TZS","Shilling","+255","TZ","TZA","834",".tz"),
			new Country (171,"Thailand","Kingdom of Thailand","Independent State",null,null,"Bangkok","THB","Baht","+66","TH","THA","764",".th"),
			new Country (172,"Timor-Leste (East Timor)","Democratic Republic of Timor-Leste","Independent State",null,null,"Dili","USD","Dollar","+670","TL","TLS","626",".tp and .tl"),
			new Country (173,"Togo","Togolese Republic","Independent State",null,null,"Lome","XOF","Franc","+228","TG","TGO","768",".tg"),
			new Country (174,"Tonga","Kingdom of Tonga","Independent State",null,null,"Nuku'alofa","TOP","Pa'anga","+676","TO","TON","776",".to"),
			new Country (175,"Trinidad and Tobago","Republic of Trinidad and Tobago","Independent State",null,null,"Port-of-Spain","TTD","Dollar","+1-868","TT","TTO","780",".tt"),
			new Country (176,"Tunisia","Tunisian Republic","Independent State",null,null,"Tunis","TND","Dinar","+216","TN","TUN","788",".tn"),
			new Country (177,"Turkey","Republic of Turkey","Independent State",null,null,"Ankara","TRY","Lira","+90","TR","TUR","792",".tr"),
			new Country (178,"Turkmenistan",null,"Independent State",null,null,"Ashgabat","TMM","Manat","+993","TM","TKM","795",".tm"),
			new Country (179,"Tuvalu",null,"Independent State",null,null,"Funafuti","AUD","Dollar","+688","TV","TUV","798",".tv"),
			new Country (180,"Uganda","Republic of Uganda","Independent State",null,null,"Kampala","UGX","Shilling","+256","UG","UGA","800",".ug"),
			new Country (181,"Ukraine",null,"Independent State",null,null,"Kiev","UAH","Hryvnia","+380","UA","UKR","804",".ua"),
			new Country (182,"United Arab Emirates","United Arab Emirates","Independent State",null,null,"Abu Dhabi","AED","Dirham","+971","AE","ARE","784",".ae"),
			new Country (183,"United Kingdom","United Kingdom of Great Britain and Northern Ireland","Independent State",null,null,"London","GBP","Pound","+44","GB","GBR","826",".uk"),
			new Country (184,"United States","United States of America","Independent State",null,null,"Washington","USD","Dollar","+1","US","USA","840",".us"),
			new Country (185,"Uruguay","Oriental Republic of Uruguay","Independent State",null,null,"Montevideo","UYU","Peso","+598","UY","URY","858",".uy"),
			new Country (186,"Uzbekistan","Republic of Uzbekistan","Independent State",null,null,"Tashkent","UZS","Som","+998","UZ","UZB","860",".uz"),
			new Country (187,"Vanuatu","Republic of Vanuatu","Independent State",null,null,"Port-Vila","VUV","Vatu","+678","VU","VUT","548",".vu"),
			new Country (188,"Vatican City","State of the Vatican City","Independent State",null,null,"Vatican City","EUR","Euro","+379","VA","VAT","336",".va"),
			new Country (189,"Venezuela","Bolivarian Republic of Venezuela","Independent State",null,null,"Caracas","VEB","Bolivar","+58","VE","VEN","862",".ve"),
			new Country (190,"Vietnam","Socialist Republic of Vietnam","Independent State",null,null,"Hanoi","VND","Dong","+84","VN","VNM","704",".vn"),
			new Country (191,"Yemen","Republic of Yemen","Independent State",null,null,"Sanaa","YER","Rial","+967","YE","YEM","887",".ye"),
			new Country (192,"Zambia","Republic of Zambia","Independent State",null,null,"Lusaka","ZMK","Kwacha","+260","ZM","ZMB","894",".zm"),

			new Country (193,"Zimbabwe","Republic of Zimbabwe","Independent State",null,null,"Harare","ZWD","Dollar","+263","ZW","ZWE","716",".zw"),
			new Country (194,"Abkhazia","Republic of Abkhazia","Proto Independent State",null,null,"Sokhumi","RUB","Ruble","+995","GE","GEO","268",".ge"),
			new Country (195,"China, Republic of (Taiwan)","Republic of China","Proto Independent State",null,null,"Taipei","TWD","Dollar","+886","TW","TWN","158",".tw"),
			new Country (196,"Nagorno-Karabakh","Nagorno-Karabakh Republic","Proto Independent State",null,null,"Stepanakert","AMD","Dram","+374-97","AZ","AZE","031",".az"),
			new Country (197,"Northern Cyprus","Turkish Republic of Northern Cyprus","Proto Independent State",null,null,"Nicosia","TRY","Lira","+90-392","CY","CYP","196",".nc.tr"),
			new Country (198,"Pridnestrovie (Transnistria)","Pridnestrovian Moldavian Republic","Proto Independent State",null,null,"Tiraspol",null,"Ruple","+373-533","MD","MDA","498",".md"),
			new Country (199,"Somaliland","Republic of Somaliland","Proto Independent State",null,null,"Hargeisa",null,"Shilling","+252","SO","SOM","706",".so"),
			new Country (200,"South Ossetia","Republic of South Ossetia","Proto Independent State",null,null,"Tskhinvali","RUB and GEL","Ruble and Lari","+995","GE","GEO","268",".ge"),
			new Country (201,"Ashmore and Cartier Islands","Territory of Ashmore and Cartier Islands","Dependency","External Territory","Australia",null,null,null,null,"AU","AUS","036",".au"),
			new Country (202,"Christmas Island","Territory of Christmas Island","Dependency","External Territory","Australia","The Settlement (Flying Fish Cove)","AUD","Dollar","+61","CX","CXR","162",".cx"),
			new Country (203,"Cocos (Keeling) Islands","Territory of Cocos (Keeling) Islands","Dependency","External Territory","Australia","West Island","AUD","Dollar","+61","CC","CCK","166",".cc"),
			new Country (204,"Coral Sea Islands","Coral Sea Islands Territory","Dependency","External Territory","Australia",null,null,null,null,"AU","AUS","036",".au"),
			new Country (205,"Heard Island and McDonald Islands","Territory of Heard Island and McDonald Islands","Dependency","External Territory","Australia",null,null,null,null,"HM","HMD","334",".hm"),
			new Country (206,"Norfolk Island","Territory of Norfolk Island","Dependency","External Territory","Australia","Kingston","AUD","Dollar","+672","NF","NFK","574",".nf"),
			new Country (207,"New Caledonia",null,"Dependency","Sui generis Collectivity","France","Noumea","XPF","Franc","+687","NC","NCL","540",".nc"),
			new Country (208,"French Polynesia","Overseas Country of French Polynesia","Dependency","Overseas Collectivity","France","Papeete","XPF","Franc","+689","PF","PYF","258",".pf"),
			new Country (209,"Mayotte","Departmental Collectivity of Mayotte","Dependency","Overseas Collectivity","France","Mamoudzou","EUR","Euro","+262","YT","MYT","175",".yt"),
			new Country (210,"Saint Barthelemy","Collectivity of Saint Barthelemy","Dependency","Overseas Collectivity","France","Gustavia","EUR","Euro","+590","GP","GLP","312",".gp"),
			new Country (211,"Saint Martin","Collectivity of Saint Martin","Dependency","Overseas Collectivity","France","Marigot","EUR","Euro","+590","GP","GLP","312",".gp"),
			new Country (212,"Saint Pierre and Miquelon","Territorial Collectivity of Saint Pierre and Miquelon","Dependency","Overseas Collectivity","France","Saint-Pierre","EUR","Euro","+508","PM","SPM","666",".pm"),
			new Country (213,"Wallis and Futuna","Collectivity of the Wallis and Futuna Islands","Dependency","Overseas Collectivity","France","Mata'utu","XPF","Franc","+681","WF","WLF","876",".wf"),
			new Country (214,"French Southern and Antarctic Lands","Territory of the French Southern and Antarctic Lands","Dependency","Overseas Territory","France","Martin-de-Viviès",null,null,null,"TF","ATF","260",".tf"),
			new Country (215,"Clipperton Island",null,"Dependency","Possession","France",null,null,null,null,"PF","PYF","258",".pf"),
			new Country (216,"Bouvet Island",null,"Dependency","Territory","Norway",null,null,null,null,"BV","BVT","074",".bv"),
			new Country (217,"Cook Islands",null,"Dependency","Self-Governing in Free Association","New Zealand","Avarua","NZD","Dollar","+682","CK","COK","184",".ck"),
			new Country (218,"Niue",null,"Dependency","Self-Governing in Free Association","New Zealand","Alofi","NZD","Dollar","+683","NU","NIU","570",".nu"),
			new Country (219,"Tokelau",null,"Dependency","Territory","New Zealand",null,"NZD","Dollar","+690","TK","TKL","772",".tk"),
			new Country (220,"Guernsey","Bailiwick of Guernsey","Dependency","Crown Dependency","United Kingdom","Saint Peter Port","GGP","Pound","+44","GG","GGY","831",".gg"),
			new Country (221,"Isle of Man",null,"Dependency","Crown Dependency","United Kingdom","Douglas","IMP","Pound","+44","IM","IMN","833",".im"),
			new Country (222,"Jersey","Bailiwick of Jersey","Dependency","Crown Dependency","United Kingdom","Saint Helier","JEP","Pound","+44","JE","JEY","832",".je"),
			new Country (223,"Anguilla",null,"Dependency","Overseas Territory","United Kingdom","The Valley","XCD","Dollar","+1-264","AI","AIA","660",".ai"),
			new Country (224,"Bermuda",null,"Dependency","Overseas Territory","United Kingdom","Hamilton","BMD","Dollar","+1-441","BM","BMU","060",".bm"),
			new Country (225,"British Indian Ocean Territory",null,"Dependency","Overseas Territory","United Kingdom",null,null,null,"+246","IO","IOT","086",".io"),
			new Country (226,"British Sovereign Base Areas",null,"Dependency","Overseas Territory","United Kingdom","Episkopi","CYP","Pound","+357",null,null,null,null),
			new Country (227,"British Virgin Islands",null,"Dependency","Overseas Territory","United Kingdom","Road Town","USD","Dollar","+1-284","VG","VGB","092",".vg"),
			new Country (228,"Cayman Islands",null,"Dependency","Overseas Territory","United Kingdom","George Town","KYD","Dollar","+1-345","KY","CYM","136",".ky"),
			new Country (229,"Falkland Islands (Islas Malvinas)",null,"Dependency","Overseas Territory","United Kingdom","Stanley","FKP","Pound","+500","FK","FLK","238",".fk"),
			new Country (230,"Gibraltar",null,"Dependency","Overseas Territory","United Kingdom","Gibraltar","GIP","Pound","+350","GI","GIB","292",".gi"),
			new Country (231,"Montserrat",null,"Dependency","Overseas Territory","United Kingdom","Plymouth","XCD","Dollar","+1-664","MS","MSR","500",".ms"),
			new Country (232,"Pitcairn Islands",null,"Dependency","Overseas Territory","United Kingdom","Adamstown","NZD","Dollar",null,"PN","PCN","612",".pn"),
			new Country (233,"Saint Helena",null,"Dependency","Overseas Territory","United Kingdom","Jamestown","SHP","Pound","+290","SH","SHN","654",".sh"),
			new Country (234,"South Georgia and the South Sandwich Islands",null,"Dependency","Overseas Territory","United Kingdom",null,null,null,null,"GS","SGS","239",".gs"),
			new Country (235,"Turks and Caicos Islands",null,"Dependency","Overseas Territory","United Kingdom","Grand Turk","USD","Dollar","+1-649","TC","TCA","796",".tc"),
			new Country (236,"Northern Mariana Islands","Commonwealth of The Northern Mariana Islands","Dependency","Commonwealth","United States","Saipan","USD","Dollar","+1-670","MP","MNP","580",".mp"),
			new Country (237,"Puerto Rico","Commonwealth of Puerto Rico","Dependency","Commonwealth","United States","San Juan","USD","Dollar","+1-787 and 1-939","PR","PRI","630",".pr"),
			new Country (238,"American Samoa","Territory of American Samoa","Dependency","Territory","United States","Pago Pago","USD","Dollar","+1-684","AS","ASM","016",".as"),
			new Country (239,"Baker Island",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (240,"Guam","Territory of Guam","Dependency","Territory","United States","Hagatna","USD","Dollar","+1-671","GU","GUM","316",".gu"),
			new Country (241,"Howland Island",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (242,"Jarvis Island",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (243,"Johnston Atoll",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (244,"Kingman Reef",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (245,"Midway Islands",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (246,"Navassa Island",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (247,"Palmyra Atoll",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","581",null),
			new Country (248,"U.S. Virgin Islands","United States Virgin Islands","Dependency","Territory","United States","Charlotte Amalie","USD","Dollar","+1-340","VI","VIR","850",".vi"),
			new Country (249,"Wake Island",null,"Dependency","Territory","United States",null,null,null,null,"UM","UMI","850",null),
			new Country (250,"Hong Kong","Hong Kong Special Administrative Region","Proto Dependency","Special Administrative Region","China",null,"HKD","Dollar","+852","HK","HKG","344",".hk"),
			new Country (251,"Macau","Macau Special Administrative Region","Proto Dependency","Special Administrative Region","China","Macau","MOP","Pataca","+853","MO","MAC","446",".mo"),
			new Country (252,"Faroe Islands",null,"Proto Dependency",null,"Denmark","Torshavn","DKK","Krone","+298","FO","FRO","234",".fo"),
			new Country (253,"Greenland",null,"Proto Dependency",null,"Denmark","Nuuk (Godthab)","DKK","Krone","+299","GL","GRL","304",".gl"),
			new Country (254,"French Guiana","Overseas Region of Guiana","Proto Dependency","Overseas Region","France","Cayenne","EUR","Euro","+594","GF","GUF","254",".gf"),
			new Country (255,"Guadeloupe","Overseas Region of Guadeloupe","Proto Dependency","Overseas Region","France","Basse-Terre","EUR","Euro","+590","GP","GLP","312",".gp"),
			new Country (256,"Martinique","Overseas Region of Martinique","Proto Dependency","Overseas Region","France","Fort-de-France","EUR","Euro","+596","MQ","MTQ","474",".mq"),
			new Country (257,"Reunion","Overseas Region of Reunion","Proto Dependency","Overseas Region","France","Saint-Denis","EUR","Euro","+262","RE","REU","638",".re"),
			new Country (258,"Aland",null,"Proto Dependency",null,"Finland","Mariehamn","EUR","Euro","+358-18","AX","ALA","248",".ax"),
			new Country (259,"Aruba",null,"Proto Dependency",null,"Netherlands","Oranjestad","AWG","Guilder","+297","AW","ABW","533",".aw"),
			new Country (260,"Netherlands Antilles",null,"Proto Dependency",null,"Netherlands","Willemstad","ANG","Guilder","+599","AN","ANT","530",".an"),
			new Country (261,"Svalbard",null,"Proto Dependency",null,"Norway","Longyearbyen","NOK","Krone","+47","SJ","SJM","744",".sj"),
			new Country (262,"Ascension",null,"Proto Dependency","Dependency of Saint Helena","United Kingdom","Georgetown","SHP","Pound","+247","AC","ASC",null,".ac"),
			new Country (263,"Tristan da Cunha",null,"Proto Dependency","Dependency of Saint Helena","United Kingdom","Edinburgh","SHP","Pound","+290","TA","TAA",null,null),
			new Country (264,"Antarctica",null,"Disputed Territory",null,"Undetermined",null,null,null,null,"AQ","ATA","010",".aq"),
			new Country (265,"Kosovo",null,"Disputed Territory",null,"Administrated by the UN","Pristina","CSD and EUR","Dinar and Euro","+381","CS","SCG","891",".cs and .yu"),
			new Country (266,"Palestinian Territories (Gaza Strip and West Bank)",null,"Disputed Territory",null,"Administrated by Israel","Gaza City (Gaza Strip) and Ramallah (West Bank)","ILS","Shekel","+970","PS","PSE","275",".ps"),
			new Country (267,"Western Sahara",null,"Disputed Territory",null,"Administrated by Morocco","El-Aaiun","MAD","Dirham","+212","EH","ESH","732",".eh"),
			new Country (268,"Australian Antarctic Territory",null,"Antarctic Territory","External Territory","Australia",null,null,null,null,"AQ","ATA","010",".aq"),
			new Country (269,"Ross Dependency",null,"Antarctic Territory","Territory","New Zealand",null,null,null,null,"AQ","ATA","010",".aq"),
			new Country (270,"Peter I Island",null,"Antarctic Territory","Territory","Norway",null,null,null,null,"AQ","ATA","010",".aq"),
			new Country (271,"Queen Maud Land",null,"Antarctic Territory","Territory","Norway",null,null,null,null,"AQ","ATA","010",".aq"),
			new Country (272,"British Antarctic Territory",null,"Antarctic Territory","Overseas Territory","United Kingdom",null,null,null,null,"AQ","ATA","010",".aq")
	};		
	
	private JobProfile[] sampleJobProfiles = {
			new JobProfile("Solution Architect"),
			new JobProfile("Application Delivery Manager"),
			new JobProfile("Human Resources Manager"),
	};
	
	private Position[] samplePositions = {
			new Position("SA-101", sampleJobProfiles[0]),
			new Position("SA-102", sampleJobProfiles[0]),
			new Position("AD-099", sampleJobProfiles[1]),
			new Position("HR-042", sampleJobProfiles[2])
	};
}
