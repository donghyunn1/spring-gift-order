package gift.product.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping(value = {"/admin/products", "/"})
    public String productAdmin() {
        return "admin/products";  // templates/admin/products.html
    }
}
