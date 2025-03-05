import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    @Test
    void testFindByCategories_Name() {
        repository.save(new Product("1234", "Test Paint",
                List.of(new Category(1, "Coverings")),
                List.of("new-product")));
        List<Product> results = repository.findByCategories_Name("Coverings");
        assertThat(results).isNotEmpty();
    }

    @Test
    void testFindByTagsContaining() {
        repository.save(new Product("5678", "Test Mortar",
                List.of(new Category(2, "Building products")),
                List.of("bio-based")));
        List<Product> results = repository.findByTagsContaining("bio-based");
        assertThat(results).isNotEmpty();
    }
}
