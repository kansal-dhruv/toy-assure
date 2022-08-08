package com.increff.ta.dao;

import com.increff.ta.pojo.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
public class ProductDao extends AbstractDao{

    private final String select_by_clientSkuId = "SELECT e from Product e where e.clientSkuId=:clientSkuId";

    private final String select_by_globalSkuId = "SELECT e FROM Product e where e.globalSkuId=:globalSkuId";

    @Transactional
    public void addProduct(Product product){em.merge(product);}

    public Product findByClientSkuId(String clientSkuId) {
        TypedQuery<Product> query = getQuery(select_by_clientSkuId, Product.class);
        query.setParameter("clientSkuId", clientSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public Product findByGlobalSkuid(Long globalSkuid){
        TypedQuery<Product> query = getQuery(select_by_globalSkuId, Product.class);
        query.setParameter("globalSkuid", globalSkuid);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
