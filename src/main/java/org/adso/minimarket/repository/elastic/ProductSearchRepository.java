package org.adso.minimarket.repository.elastic;

import org.adso.minimarket.models.product.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {
}
