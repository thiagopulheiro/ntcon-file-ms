package com.ntconsult.challenge.thiago.core;

import com.ntconsult.challenge.thiago.domain.Customer;
import com.ntconsult.challenge.thiago.domain.Item;
import com.ntconsult.challenge.thiago.domain.Sale;
import com.ntconsult.challenge.thiago.domain.SalesPerson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NewFileEventAggregatorTest {

    private final NewFileEventAggregator newFileEventAggregator = new NewFileEventAggregator();

    @Test
    public void buildCustomer_createNew() {
        final String[] line = "002ç2345675433444345çEduardo PereiraçRural".split("ç");
        Customer customer = newFileEventAggregator.buildCustomer(line);
        Assert.assertNotNull(customer);
        Assert.assertEquals("2345675433444345", customer.getId());
        Assert.assertEquals("Rural", customer.getBusinessArea());
        Assert.assertEquals("Eduardo Pereira", customer.getName());
    }

    @Test
    public void buildSalesPerson_createNew() {
        final String[] line = "001ç3245678865434çPauloç40000.99".split("ç");
        SalesPerson salesPerson = newFileEventAggregator.buildSalesPerson(line);
        Assert.assertNotNull(salesPerson);
        Assert.assertEquals("3245678865434", salesPerson.getId());
        Assert.assertEquals("Paulo", salesPerson.getName());
        Assert.assertEquals(new BigDecimal("40000.99"), salesPerson.getSalary());
    }

    @Test
    public void buildSale_createNew() {
        final String[] line = "003ç05ç[1-34-10,2-33-1.50,3-40-100]çfabio".split("ç");
        Sale sale = newFileEventAggregator.buildSale(line);

        Assert.assertNotNull(sale);
        Assert.assertEquals("05", sale.getId());
        Assert.assertEquals("fabio", sale.getName());
        Assert.assertNotNull(sale.getItems());
        Assert.assertEquals(3, sale.getItems().size());
        Assert.assertEquals(new BigDecimal("10"), sale.getItems().get(0).getPrice());
        Assert.assertEquals(34, sale.getItems().get(0).getQuantity());
        Assert.assertEquals("1", sale.getItems().get(0).getId());
        Assert.assertEquals(new BigDecimal("1.50"), sale.getItems().get(1).getPrice());
        Assert.assertEquals(33, sale.getItems().get(1).getQuantity());
        Assert.assertEquals("2", sale.getItems().get(1).getId());
        Assert.assertEquals(new BigDecimal("100"), sale.getItems().get(2).getPrice());
        Assert.assertEquals(40, sale.getItems().get(2).getQuantity());
        Assert.assertEquals("3", sale.getItems().get(2).getId());

        Assert.assertEquals(new BigDecimal("4389.50"), sale.totalAmount());
    }

    @Test
    public void buildItem_createNew() {
        final String[] line = "1-10-100".split("-");
        Item item = newFileEventAggregator.buildItem(line);
        Assert.assertNotNull(item);
        Assert.assertEquals("1", item.getId());
        Assert.assertEquals(Integer.parseInt("10"), item.getQuantity());
        Assert.assertEquals(new BigDecimal("100"), item.getPrice());
    }

    @Test
    public void getBestSaleId_getId() {
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item("2", 10, new BigDecimal("32.5")));

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item("2", 10, new BigDecimal("32.5")));
        items2.add(new Item("3", 25, new BigDecimal("50.4")));

        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale("1", items1, "paulo"));
        sales.add(new Sale("6", items2, "joao"));

        String id = newFileEventAggregator.getBestSaleId(sales);
        Assert.assertNotNull(id);
        Assert.assertEquals("6", id);
    }

    @Test
    public void getWorstSalesPerson_getName() {
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item("2", 10, new BigDecimal("32.5")));

        List<Item> items2 = new ArrayList<>();
        items2.add(new Item("2", 10, new BigDecimal("32.5")));
        items2.add(new Item("3", 25, new BigDecimal("50.4")));

        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale("1", items1, "paulo"));
        sales.add(new Sale("6", items2, "joao"));

        String name = newFileEventAggregator.getWorstSalesPerson(sales);
        Assert.assertNotNull(name);
        Assert.assertEquals("PAULO", name);
    }
}
