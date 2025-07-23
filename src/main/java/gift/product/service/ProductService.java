package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.model.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product createProduct(ProductRequestDto productRequestDto);
    Page<Product> getAllProducts(Pageable pageable);
    Product getProductById(Long id);
    Product updateProduct(Long id, ProductRequestDto productRequestDto);
    void deleteProduct(Long id);
}
