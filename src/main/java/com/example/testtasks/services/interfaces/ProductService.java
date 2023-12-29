package  com.example.testtasks.services.interfaces;

import com.example.testtasks.dto.ProductDTO;
import com.example.testtasks.entity.Product;
import java.util.List;

public interface ProductService {
    void saveProducts(ProductDTO request);
    List<Product> getAllProducts();
}
