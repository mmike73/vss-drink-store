package mydrinkshop;

import drinkshop.domain.*;
import drinkshop.service.validator.*;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private final OrderValidator validator =
            new OrderValidator();

    private Product validProduct() {
        return new Product(
                1,
                "cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.PLANT_BASED
        );
    }

    private Product invalidProduct() {
        return new Product(
                0,
                "cola",
                10.0,
                CategorieBautura.JUICE,
                TipBautura.PLANT_BASED
        );
    }

    // TC1: ID invalid
    @Test
    public void TC1_invalidId() {
        OrderItem item = new OrderItem(validProduct(), 1);
        Order order = new Order(-1, List.of(item), 10);

        Exception ex = assertThrows(ValidationException.class,
                () -> validator.validate(order));

        assertTrue(ex.getMessage().contains("ID comanda invalid"));
    }

    // TC2: items = null
    @Test
    public void TC2_nullItems() {
        Order order = new Order(1);
        order.setItems(null);
        order.setTotalPrice(10);

        Exception ex = assertThrows(ValidationException.class,
                () -> validator.validate(order));

        assertTrue(ex.getMessage().contains("Comanda fara produse"));
    }

    // TC3: lista goală (loop = 0)
    @Test
    public void TC3_emptyItems() {
        Order order = new Order(1, new ArrayList<>(), 10);

        Exception ex = assertThrows(ValidationException.class,
                () -> validator.validate(order));

        assertTrue(ex.getMessage().contains("Comanda fara produse"));
    }

    // TC4: total negativ
    @Test
    public void TC4_negativeTotal() {
        OrderItem item = new OrderItem(validProduct(), 1);
        Order order = new Order(1, List.of(item), -5);

        Exception ex = assertThrows(ValidationException.class,
                () -> validator.validate(order));

        assertTrue(ex.getMessage().contains("Total invalid"));
    }

    // TC5: item invalid (product id invalid → catch)
    @Test
    public void TC5_invalidProductInItem() {
        OrderItem item = new OrderItem(invalidProduct(), 1);
        Order order = new Order(1, List.of(item), 10);

        Exception ex = assertThrows(ValidationException.class,
                () -> validator.validate(order));

        assertTrue(ex.getMessage().contains("Product ID invalid"));
    }

    // TC6: item invalid (quantity <= 0 → catch)
    @Test
    public void TC6_invalidQuantityInItem() {
        OrderItem item = new OrderItem(validProduct(), 0);
        Order order = new Order(1, List.of(item), 10);

        Exception ex = assertThrows(ValidationException.class,
                () -> validator.validate(order));

        assertTrue(ex.getMessage().contains("Cantitate invalida"));
    }

    // TC7: mai multe iteme (loop > 1)
    @Test
    public void TC7_multipleItems() {
        OrderItem item1 = new OrderItem(validProduct(), 1);
        OrderItem item2 = new OrderItem(validProduct(), 2);

        Order order = new Order(1, List.of(item1, item2), 30);

        assertDoesNotThrow(() -> validator.validate(order));
    }

    // TC8: caz complet valid
    @Test
    public void TC8_validOrder() {
        OrderItem item = new OrderItem(validProduct(), 2);
        Order order = new Order(1, List.of(item), 20);

        assertDoesNotThrow(() -> validator.validate(order));
    }
}
