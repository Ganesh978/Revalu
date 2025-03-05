import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SpringBootApplication
public class EpdApplication {
    public static void main(String[] args) {
        SpringApplication.run(EpdApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ProductRepository repository) {
        return args -> {
            repository.deleteAll();
            repository.save(new Product("0d835b0d-4d4e-46cc-88e1-5169f638f6dc", "Paint",
                    List.of(new Category(1, "Coverings"), new Category(3, "End product")),
                    List.of("new-product")));
            repository.save(new Product("57f318aa-75ee-4b4b-a7c0-e17400b91897", "Mortar",
                    List.of(new Category(2, "Building products"), new Category(3, "End product")),
                    null));
            repository.save(new Product("75ee-4baa-0d835bb-a7c0-e174005169f7", "Expanded cork",
                    List.of(new Category(4, "Insulation"), new Category(2, "Building products")),
                    List.of("bio-based")));
        };
    }
}

// Models
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "epd_data")
class Product {
    @Id
    private String id;
    private String uuid;
    private String name;
    private List<Category> categories;
    private List<String> tags;

    public Product(String uuid, String name, List<Category> categories, List<String> tags) {
        this.uuid = uuid;
        this.name = name;
        this.categories = categories;
        this.tags = tags;
    }
}

class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

// Repository
interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategories_Name(String name);
    List<Product> findByTagsContaining(String tag);
}

// Controller
@RestController
@RequestMapping("/api")
class ProductController {
    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/show_all")
    public List<Product> showAll() {
        return repository.findAll();
    }

    @GetMapping("/categories/{cat_name}")
    public List<Product> getByCategory(@PathVariable String cat_name) {
        return repository.findByCategories_Name(cat_name);
    }

    @GetMapping("/tags/{tagname}")
    public List<Product> getByTag(@PathVariable String tagname) {
        return repository.findByTagsContaining(tagname);
    }
}
