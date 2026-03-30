package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private FakeProductRepository fakeRepo;
    private ProductService productService;

    private final int VALID_ID = 1;
    private final CategorieBautura VALID_CAT = CategorieBautura.JUICE;
    private final TipBautura VALID_TIP = TipBautura.PLANT_BASED;

    @BeforeAll
    static void start() {
        System.out.println("Starting tests");
    }

    @AfterAll
    static void end() {
        System.out.println("Ending tests");
    }

    @BeforeEach
    void setUp() {

        fakeRepo = new FakeProductRepository();
        productService = new ProductService(fakeRepo);
    }

    @AfterEach
    void tearDown() {
        fakeRepo.database.clear();
    }

    @Test
    @DisplayName("ECP 1 (Valid): Valid Name and Valid Price")
    @Tag("ECP")
    void updateProduct_ValidNameValidPrice_ShouldUpdateState() {

        String validName = "Fanta";
        double validPrice = 5.5;

        fakeRepo.save(new Product(VALID_ID, "OldName", 1.0, VALID_CAT, VALID_TIP));

        productService.updateProduct(VALID_ID, validName, validPrice, VALID_CAT, VALID_TIP);

        Product updatedProduct = fakeRepo.database.get(VALID_ID);
        assertEquals(validName, updatedProduct.getNume());
        assertEquals(validPrice, updatedProduct.getPret());
    }

    @Test
    @DisplayName("ECP 2 (Invalid): Invalid Name (Too short), Valid Price")
    @Tag("ECP")
    void updateProduct_InvalidName_ShouldThrowException() {

        String invalidName = "A";
        double validPrice = 5.5;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, invalidName, validPrice, VALID_CAT, VALID_TIP)
        );
    }

    @Test
    @DisplayName("ECP 3 (Invalid): Valid Name, Invalid Price (Negative)")
    @Tag("ECP")
    void updateProduct_InvalidPrice_ShouldThrowException() {

        String validName = "Fanta";
        double invalidPrice = -10.0;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, validName, invalidPrice, VALID_CAT, VALID_TIP)
        );
    }

    @Test
    @DisplayName("ECP 4 (Valid): Alternative Valid Name and Valid Price")
    @Tag("ECP")
    void updateProduct_ValidNameValidPrice_Alternative_ShouldUpdateState() {

        String validName = "Strawberry Juice";
        double validPrice = 500.50;
        fakeRepo.save(new Product(VALID_ID, "OldName", 1.0, VALID_CAT, VALID_TIP));

        productService.updateProduct(VALID_ID, validName, validPrice, VALID_CAT, VALID_TIP);

        Product updatedProduct = fakeRepo.database.get(VALID_ID);
        assertEquals(validName, updatedProduct.getNume());
        assertEquals(validPrice, updatedProduct.getPret());
    }

    @Test
    @DisplayName("ECP 5 (Invalid): Invalid Name (Too long), Valid Price")
    @Tag("ECP")
    void updateProduct_InvalidNameTooLong_ShouldThrowException() {

        String invalidName = "This Name Is Way Too Long To Be Valid";
        double validPrice = 5.5;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, invalidName, validPrice, VALID_CAT, VALID_TIP)
        );
    }

    @Test
    @DisplayName("BVA 1 (Invalid): Price exactly at lower excluded boundary (0.0)")
    @Tag("BVA")
    @Timeout(1)
    void updateProduct_PriceLowerBoundaryInvalid_ShouldThrowException() {

        String validName = "Cola";
        double boundaryPrice = 0.0;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, validName, boundaryPrice, VALID_CAT, VALID_TIP)
        );
    }


    @Test
    @DisplayName("BVA 2 (Valid): Price just above lower boundary (0.01)")
    @Tag("BVA")
    void updateProduct_PriceJustAboveLowerBoundary_ShouldUpdateState() {

        String validName = "Cola";
        double boundaryPrice = 0.01;
        fakeRepo.save(new Product(VALID_ID, "OldName", 1.0, VALID_CAT, VALID_TIP));

        productService.updateProduct(VALID_ID, validName, boundaryPrice, VALID_CAT, VALID_TIP);

        assertEquals(boundaryPrice, fakeRepo.database.get(VALID_ID).getPret());
    }

    @Test
    @DisplayName("BVA 3 (Valid): Price exactly at upper included boundary (1000.0)")
    @Tag("BVA")
    void updateProduct_PriceUpperBoundaryValid_ShouldUpdateState() {

        String validName = "Premium Wine";
        double boundaryPrice = 1000.0;
        fakeRepo.save(new Product(VALID_ID, "OldName", 1.0, VALID_CAT, VALID_TIP));

        productService.updateProduct(VALID_ID, validName, boundaryPrice, VALID_CAT, VALID_TIP);

        assertEquals(boundaryPrice, fakeRepo.database.get(VALID_ID).getPret());
    }




    @Test
    @DisplayName("BVA 4 (Invalid): Price just above upper boundary (1000.01)")
    @Tag("BVA")
    void updateProduct_PriceAboveUpperBoundary_ShouldThrowException() {

        String validName = "Premium Wine";
        double boundaryPrice = 1000.01;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, validName, boundaryPrice, VALID_CAT, VALID_TIP)
        );
    }

    @Test
    @DisplayName("BVA 5 (Valid): Name exactly at lower included boundary (Length 3)")
    @Tag("BVA")
    void updateProduct_NameLowerBoundaryValid_ShouldUpdateState() {
        String validName = "Tea";
        double validPrice = 10.0;
        fakeRepo.save(new Product(VALID_ID, "OldName", 1.0, VALID_CAT, VALID_TIP));

        productService.updateProduct(VALID_ID, validName, validPrice, VALID_CAT, VALID_TIP);

        assertEquals(validName, fakeRepo.database.get(VALID_ID).getNume());
    }

    @Test
    @DisplayName("BVA 6 (Invalid): Name just below lower boundary (Length 2)")
    @Tag("BVA")
    void updateProduct_NameBelowLowerBoundary_ShouldThrowException() {
        String invalidName = "Te";
        double validPrice = 10.0;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, invalidName, validPrice, VALID_CAT, VALID_TIP)
        );
    }

    @Test
    @DisplayName("BVA 7 (Valid): Name exactly at upper included boundary (Length 20)")
    @Tag("BVA")
    void updateProduct_NameUpperBoundaryValid_ShouldUpdateState() {
        String validName = "TwentyCharsLongDrink";
        double validPrice = 10.0;
        fakeRepo.save(new Product(VALID_ID, "OldName", 1.0, VALID_CAT, VALID_TIP));

        productService.updateProduct(VALID_ID, validName, validPrice, VALID_CAT, VALID_TIP);

        assertEquals(validName, fakeRepo.database.get(VALID_ID).getNume());
    }

    @Test
    @DisplayName("BVA 8 (Invalid): Name just above upper boundary (Length 21)")
    @Tag("BVA")
    void updateProduct_NameAboveUpperBoundary_ShouldThrowException() {

        String invalidName = "TwentyOneCharsLongDrk";
        double validPrice = 10.0;

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(VALID_ID, invalidName, validPrice, VALID_CAT, VALID_TIP)
        );
    }
}