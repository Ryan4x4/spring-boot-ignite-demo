package com.sageburner.api.creditcard.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import com.sageburner.api.creditcard.model.Address;
import com.sageburner.api.creditcard.model.CreditCard;
import org.apache.ignite.cache.store.CacheStore;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.cache.store.CacheStoreSession;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.CacheStoreSessionResource;

/**
 * Example of {@link CacheStore} implementation that uses JDBC
 * transaction with cache transactions and maps {@link Long} to {@link CreditCard}.
 */
public class CacheJdbcCreditCardStore extends CacheStoreAdapter<Long, CreditCard> {
    /**
     * Store session.
     */
    @CacheStoreSessionResource
    private CacheStoreSession ses;

    /**
     * {@inheritDoc}
     */
    @Override
    public CreditCard load(Long key) {
        Connection conn = ses.attachment();

        try (PreparedStatement st = conn.prepareStatement("select * from credit_card where cc_number = ?")) {
            st.setString(1, key.toString());

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Address address = new Address();
                address.setLine1(rs.getString("line_1"));
                address.setLine2(rs.getString("line_2"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setPostalCode(rs.getString("postal_code"));
                address.setCountryCode(rs.getString("country_code"));
                address.setPhone(rs.getString("phone"));
                address.setStatus(rs.getString("status"));

                CreditCard creditCard = new CreditCard();
                creditCard.setNumber(rs.getString("cc_number"));
                creditCard.setType(rs.getString("cc_type"));
                creditCard.setFirstName(rs.getString("first_name"));
                creditCard.setLastName(rs.getString("last_name"));
                creditCard.setBillingAddress(address);
                creditCard.setExpireMonth(rs.getInt("expire_month"));
                creditCard.setExpireYear(rs.getInt("expire_year"));
                creditCard.setCvv2(rs.getInt("cvv2"));
                creditCard.setExternalCustomerId(rs.getString("external_customer_id"));
                creditCard.setFundingState(rs.getString("funding_state"));
                creditCard.setPayerId(rs.getString("payer_id"));
                creditCard.setValidUntil(rs.getString("valid_until"));

                return creditCard;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CacheLoaderException("Failed to load object [key=" + key + ']', e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(Cache.Entry<? extends Long, ? extends CreditCard> entry) {
        Long key = entry.getKey();
        CreditCard val = entry.getValue();

        try {
            Connection conn = ses.attachment();

            int updated;

            // Try update first. If it does not work, then try insert.
            // Some databases would allow these to be done in one 'upsert' operation.
            try (PreparedStatement st = conn.prepareStatement(
                    "update credit_card set " +
                            "line_1 = ?, " +
                            "line_2 = ?, " +
                            "city = ?, " +
                            "state = ?, " +
                            "postal_code = ?, " +
                            "country_code = ?, " +
                            "phone = ?, " +
                            "status = ?, " +
                            "cc_type = ?, " +
                            "first_name = ?, " +
                            "last_name = ?, " +
                            "expire_month = ?, " +
                            "expire_year = ?, " +
                            "cvv2 = ?, " +
                            "external_customer_id = ?, " +
                            "funding_state = ?, " +
                            "payer_id = ?, " +
                            "valid_until = ? " +
                            "where cc_number = ?")) {
                st.setString(1, val.getBillingAddress().getLine1());
                st.setString(2, val.getBillingAddress().getLine2());
                st.setString(3, val.getBillingAddress().getCity());
                st.setString(4, val.getBillingAddress().getState());
                st.setString(5, val.getBillingAddress().getPostalCode());
                st.setString(6, val.getBillingAddress().getCountryCode());
                st.setString(7, val.getBillingAddress().getPhone());
                st.setString(8, val.getBillingAddress().getStatus());

                st.setString(9, val.getType());
                st.setString(10, val.getFirstName());
                st.setString(11, val.getLastName());
                st.setInt(12, val.getExpireMonth());
                st.setInt(13, val.getExpireYear());
                st.setInt(14, val.getCvv2());
                st.setString(15, val.getExternalCustomerId());
                st.setString(16, val.getFundingState());
                st.setString(17, val.getPayerId());
                st.setString(18, val.getValidUntil());
                st.setString(19, val.getNumber());

                updated = st.executeUpdate();
            }

            // If update failed, try to insert.
            if (updated == 0) {
                try (PreparedStatement st = conn.prepareStatement(
                        "insert into credit_card (" +
                                "line_1, " +
                                "line_2, " +
                                "city, " +
                                "state, " +
                                "postal_code, " +
                                "country_code, " +
                                "phone, " +
                                "status, " +
                                "cc_number, " +
                                "cc_type, " +
                                "first_name, " +
                                "last_name, " +
                                "expire_month, " +
                                "expire_year, " +
                                "cvv2, " +
                                "external_customer_id, " +
                                "funding_state, " +
                                "payer_id, " +
                                "valid_until) " +
                                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    st.setString(1, val.getBillingAddress().getLine1());
                    st.setString(2, val.getBillingAddress().getLine2());
                    st.setString(3, val.getBillingAddress().getCity());
                    st.setString(4, val.getBillingAddress().getState());
                    st.setString(5, val.getBillingAddress().getPostalCode());
                    st.setString(6, val.getBillingAddress().getCountryCode());
                    st.setString(7, val.getBillingAddress().getPhone());
                    st.setString(8, val.getBillingAddress().getStatus());

                    st.setString(9, val.getNumber());
                    st.setString(10, val.getType());
                    st.setString(11, val.getFirstName());
                    st.setString(12, val.getLastName());
                    st.setInt(13, val.getExpireMonth());
                    st.setInt(14, val.getExpireYear());
                    st.setInt(15, val.getCvv2());
                    st.setString(16, val.getExternalCustomerId());
                    st.setString(17, val.getFundingState());
                    st.setString(18, val.getPayerId());
                    st.setString(19, val.getValidUntil());

                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new CacheWriterException("Failed to write object [key=" + key + ", val=" + val + ']', e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Object key) {
        Connection conn = ses.attachment();

        try (PreparedStatement st = conn.prepareStatement("delete from credit_card where cc_number=?")) {
            st.setLong(1, (Long) key);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new CacheWriterException("Failed to delete object [key=" + key + ']', e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadCache(IgniteBiInClosure<Long, CreditCard> clo, Object... args) {
        Connection conn = ses.attachment();

        try (PreparedStatement stmt = conn.prepareStatement("select * from credit_card")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Address address = new Address();
                address.setLine1(rs.getString("line_1"));
                address.setLine2(rs.getString("line_2"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setPostalCode(rs.getString("postal_code"));
                address.setCountryCode(rs.getString("country_code"));
                address.setPhone(rs.getString("phone"));
                address.setStatus(rs.getString("status"));

                CreditCard creditCard = new CreditCard();
                creditCard.setNumber(rs.getString("cc_number"));
                creditCard.setType(rs.getString("cc_type"));
                creditCard.setFirstName(rs.getString("first_name"));
                creditCard.setLastName(rs.getString("last_name"));
                creditCard.setBillingAddress(address);
                creditCard.setExpireMonth(rs.getInt("expire_month"));
                creditCard.setExpireYear(rs.getInt("expire_year"));
                creditCard.setCvv2(rs.getInt("cvv2"));
                creditCard.setExternalCustomerId(rs.getString("external_customer_id"));
                creditCard.setFundingState(rs.getString("funding_state"));
                creditCard.setPayerId(rs.getString("payer_id"));
                creditCard.setValidUntil(rs.getString("valid_until"));

                clo.apply(Long.parseLong(creditCard.getNumber()), creditCard);
            }
        } catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }
}
