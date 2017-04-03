package com.sageburner.api.creditcard.dao.jdbc;

import com.sageburner.api.creditcard.model.CreditCard;
import com.sun.prism.impl.FactoryResetException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.store.CacheStoreSessionListener;
import org.apache.ignite.cache.store.jdbc.CacheJdbcStoreSessionListener;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.transactions.Transaction;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Component;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;

import java.util.*;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

@Component
public class CacheJdbcStore {
    Ignite ignite = Ignition.start("/usr/app/ignite/example-ignite.xml");

    /**
     * Cache name.
     */
    private static final String CACHE_NAME = "CreditCardCache";

    CacheConfiguration<Long, CreditCard> cacheCfg = new CacheConfiguration<>(CACHE_NAME);

    private static IgniteCache<Long, CreditCard> cache;

    HashSet<String> prepositions = new HashSet<>();
    HashSet<String> months = new HashSet<>();
    HashSet<String> years = new HashSet<>();

    public CacheJdbcStore() {
        Ignition.setClientMode(true);

        cacheCfg.setCacheMode(CacheMode.REPLICATED);

        // Set atomicity as transaction, since we are showing transactions in example.
        cacheCfg.setAtomicityMode(TRANSACTIONAL);

        // Configure JDBC store.
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(CacheJdbcCreditCardStore.class));

        // Configure JDBC session listener.
        cacheCfg.setCacheStoreSessionListenerFactories(new myCacheStoreSessionListenerFactory());

        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);

        cacheCfg.setIndexedTypes(Long.class, CreditCard.class);

        initPrepositions();
        initMonths();
        initYears();

        cache = ignite.getOrCreateCache(cacheCfg);

        cache.loadCache(null);
    }

    private static class myCacheStoreSessionListenerFactory implements Factory {

        private String jdbcHost = "db";
        private String jdbcDatabase = "ignite";
        private String jdbcUser = "ignite";
        private String jdbcPassword = "ignite";

        @Override
        public CacheStoreSessionListener create() {
            // Data Source
            HikariConfig config = new HikariConfig();
            config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
            config.addDataSourceProperty("serverName", jdbcHost);
            config.addDataSourceProperty("databaseName", jdbcDatabase);
            config.addDataSourceProperty("user", jdbcUser);
            config.addDataSourceProperty("password", jdbcPassword);
            config.setMaximumPoolSize(30);

            HikariDataSource dataSource = new HikariDataSource(config);
            CacheJdbcStoreSessionListener listener = new CacheJdbcStoreSessionListener();
            listener.setDataSource(dataSource);

            return listener;
        }
    }

    public CreditCard createCreditCard(CreditCard creditCard) {
        CreditCard result;

        try (Transaction tx = Ignition.ignite().transactions().txStart()) {
            result = cache.getAndPut(Long.parseLong(creditCard.getNumber()), creditCard);
            tx.commit();
        }

        return result;
    }

    public CreditCard getCreditCard(String number) {
        CreditCard result;

        try (Transaction tx = Ignition.ignite().transactions().txStart()) {
            result = cache.get(Long.parseLong(number));
            tx.commit();
        }

        return result;
    }

    public List<CreditCard> getCreditCardsExpiring(String preposition, String month, String year) {
        List<CreditCard> result = new ArrayList<>();

        if (isValidPreposition(preposition) && isValidMonth(month) && isValidYear(year)) {
            SqlQuery<Long, CreditCard> sql = null;

            int intMonth = Integer.parseInt(month);
            int intYear = Integer.parseInt(year);

            if (preposition.equalsIgnoreCase(Preposition.BEFORE.value)) {
                sql = new SqlQuery(CreditCard.class, "expireYear < " + intYear + " OR (expireYear = " + intYear + " AND expireMonth < " + intMonth + ")");
            } else if (preposition.equalsIgnoreCase(Preposition.AFTER.value)) {
                sql = new SqlQuery(CreditCard.class, "expireYear > " + intYear + " OR (expireYear = " + intYear + " AND expireMonth > " + intMonth + ")");
            }

            try (QueryCursor<Cache.Entry<Long, CreditCard>> cursor = cache.query(sql)) {
                for (Cache.Entry<Long, CreditCard> e : cursor)
                    result.add(e.getValue());
            }
        }

        return result;
    }

    private boolean isValidPreposition(String preposition) {
        return prepositions.contains(preposition.toUpperCase());
    }

    private boolean isValidMonth(String month) {
        return months.contains(month);
    }

    private boolean isValidYear(String year) {
        return years.contains(year);
    }

    public List<CreditCard> getCreditCardList() {
        List<CreditCard> result = new ArrayList<>();

        SqlQuery<Long, CreditCard> sql = new SqlQuery(CreditCard.class, "number IS NOT NULL");

        try (QueryCursor<Cache.Entry<Long, CreditCard>> cursor = cache.query(sql)) {
            for (Cache.Entry<Long, CreditCard> e : cursor)
                result.add(e.getValue());
        }

        return result;
    }

    private enum Preposition {
        BEFORE("BEFORE"),
        AFTER("AFTER");

        private final String value;

        Preposition(String value) {
            this.value = value;
        }
    }

    private void initPrepositions() {
        prepositions.add("BEFORE");
        prepositions.add("AFTER");
    }

    private void initMonths() {
        months.add("1");
        months.add("2");
        months.add("3");
        months.add("4");
        months.add("5");
        months.add("6");
        months.add("7");
        months.add("8");
        months.add("9");
        months.add("10");
        months.add("11");
        months.add("12");
    }

    private void initYears() {
        years.add("2017");
        years.add("2018");
        years.add("2019");
        years.add("2020");
        years.add("2021");
        years.add("2022");
    }

    /**
     * Compute Grid Stuff (Only here for Demo!)
     */

    public Double calculateAverge(List<List<Double>> numLists) {
        Collection<Double> distributedAverage = ignite.compute().apply(
                new IgniteClosure<List<Double>, Double>() {
                    @Override public Double apply(List<Double> numList) {
                        Double sum = 0.0;
                        Double count = Double.valueOf(numList.size());
                        Double average = 0.0;
                        StringBuffer numListString = new StringBuffer();

                        for (Double num : numList) {
                            numListString.append(String.valueOf(num));
                            numListString.append(" ");
                            sum += num;
                        }

                        average = sum / count;

                        System.out.println(">>> Average of: " + numListString.toString());
                        System.out.println(">>>  is: " + average);

                        return average;
                    }
                },

                // Job parameters. Ignite will create as many jobs as there are parameters.
                numLists
        );

        Double avgSum = 0.0;
        Double count = Double.valueOf(numLists.size());

        // Add up individual averages received from remote nodes
        for (Double avg : distributedAverage) {
            avgSum += avg;
        }

        System.out.println(">>> Avg Sum: " + avgSum + " Avg Count: " + count);

        return avgSum / count;
    }
}
