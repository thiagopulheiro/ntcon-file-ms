package com.ntconsult.challenge.thiago.core;

import com.ntconsult.challenge.thiago.core.data.NewFileEventRequest;
import com.ntconsult.challenge.thiago.domain.Customer;
import com.ntconsult.challenge.thiago.domain.Item;
import com.ntconsult.challenge.thiago.domain.MapUtils;
import com.ntconsult.challenge.thiago.domain.Sale;
import com.ntconsult.challenge.thiago.domain.SalesPerson;
import com.ntconsult.challenge.thiago.port.EventAggregatorException;
import com.ntconsult.challenge.thiago.port.IllegalEventStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Component
@Slf4j
public class NewFileEventAggregator {

    @Value("${file.dir.out}")
    private String dirOut;

    @Value("${file.dir.in}")
    private String dirIn;

    private static final String FILE_BREAKER_CHAR = "ç";

    private static final String SALE_CODE = "003";

    private static final String CUSTOMER_CODE = "002";

    private static final String SALESPERSON_CODE = "001";

    @Async
    public void aggregateAndCreateResult(NewFileEventRequest newFileEventRequest) {
        final Set<SalesPerson> salesPeople = new HashSet<>();
        final Set<Customer> customers = new HashSet<>();
        final List<Sale> sales = new ArrayList<>();
        log.info("Received: {}, reading...", newFileEventRequest.getFileName());
        readInputFile(newFileEventRequest, salesPeople, customers, sales);
        log.info("Read. Writing...");
        writeOutputFile(newFileEventRequest, salesPeople, customers, sales);
        log.info("Done: {}", newFileEventRequest.getFileName());
    }

    private void writeOutputFile(NewFileEventRequest newFileEventRequest, Set<SalesPerson> salesPeople,
                                 Set<Customer> customers, List<Sale> sales) {

        final String absoluteOutFileName = dirOut + File.separator + newFileEventRequest.getFileName();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(absoluteOutFileName))) {
            writer.write("Number of customers: " + customers.size());
            writer.newLine();
            writer.write("Number of sales person: " + salesPeople.size());
            writer.newLine();
            writer.write("Best sale id: " + getBestSaleId(sales));
            writer.newLine();
            writer.write("Worst sales person: " + getWorstSalesPerson(sales));
            writer.newLine();
        } catch (IOException e) {
            throw new EventAggregatorException("Can not create output file: " + newFileEventRequest.getFileName(), e);
        }
    }

    private void readInputFile(NewFileEventRequest newFileEventRequest, Set<SalesPerson> salesPeople,
                               Set<Customer> customers, List<Sale> sales) {

        final String absoluteInFileName = dirIn + File.separator + newFileEventRequest.getFileName();

        try (Stream<String> stream = Files.lines(Paths.get(absoluteInFileName))) {
            stream
                .map(line -> line.split(FILE_BREAKER_CHAR))
                .forEach(splitLine -> {
                    if (splitLine.length < 4) {
                        throw new IllegalEventStateException("Can not parse line.");
                    }
                    switch (splitLine[0]) {
                        case SALE_CODE: {
                            Sale sale = buildSale(splitLine);
                            sales.add(sale);
                            break;
                        }
                        case CUSTOMER_CODE: {
                            Customer customer = buildCustomer(splitLine);
                            customers.add(customer);
                            break;
                        }
                        case SALESPERSON_CODE: {
                            SalesPerson salesPerson = buildSalesPerson(splitLine);
                            salesPeople.add(salesPerson);
                            break;
                        }
                        default: {
                            throw new IllegalEventStateException("Can not recognize code: " + splitLine[0]);
                        }
                    }
                });

        } catch (IOException e) {
            throw new EventAggregatorException("Can not read input file: " + newFileEventRequest.getFileName(), e);
        }
    }

    protected Sale buildSale(String[] splitLine) {
        List<Item> items = new ArrayList<>();

        Arrays.stream(splitLine[2].split(","))
            .map(line -> line.replace("[", ""))
            .map(line -> line.replace("]", ""))
            .forEach(line -> {
                items.add(buildItem(line.split("-")));
            });

        return new Sale(splitLine[1], items, splitLine[3]);
    }

    protected Customer buildCustomer(String[] splitLine) {
        return new Customer(splitLine[1], splitLine[2], splitLine[3]);
    }

    protected SalesPerson buildSalesPerson(String[] splitLine) {
        final BigDecimal salary = new BigDecimal(splitLine[3]);
        return new SalesPerson(splitLine[1], splitLine[2], salary);
    }

    protected Item buildItem(String[] splitItem) {
        int quantity = Integer.parseInt(splitItem[1]);
        BigDecimal price = new BigDecimal(splitItem[2]);
        return new Item(splitItem[0], quantity, price);
    }

    protected String getBestSaleId(List<Sale> sales) {
        if (sales.isEmpty()) {
            return null;
        }
        sales.sort(Comparator.comparing(Sale::totalAmount).reversed());
        return sales.get(0).getId();
    }

    protected String getWorstSalesPerson(List<Sale> sales) {
        if (sales.isEmpty()) {
            return null;
        }
        Map<String, BigDecimal> salesPersonSaleAmountMap = new HashMap<>();
        sales.forEach(sale -> {
            salesPersonSaleAmountMap.merge(sale.getName().toUpperCase(), sale.totalAmount(), BigDecimal::add);
        });
        return MapUtils.sortByValue(salesPersonSaleAmountMap).entrySet().iterator().next().getKey();
    }

}