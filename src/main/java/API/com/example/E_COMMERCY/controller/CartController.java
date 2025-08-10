package API.com.example.E_COMMERCY.controller;


import API.com.example.E_COMMERCY.dto.cart.CartResponseDto;
import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.UpdateCartItemRequestDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CartItemNotFoundException;
import API.com.example.E_COMMERCY.service.cart.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/CartSection")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/Cart")
    public ResponseEntity<?> GetCurrentCart (){
        CartResponseDto cartResponseDto = cartService.displayCart();
        return ResponseEntity.ok().body(cartResponseDto);
    }

    @PostMapping("/Cart/Add")
    public ResponseEntity<?> AddToCart(@RequestBody @Valid AddCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
//        try {
            cartService.AddCartItem(cartItemRequestDto);
            return ResponseEntity.ok("CartItem Added successfully");
//        }
//        catch (Exception e) {
//            e.printStackTrace(); //
//            return ResponseEntity.status(500).body("Error Adding user: " + e.getMessage());
//        }

    }

    @PostMapping("/Cart/UpdateQuantity")
    public ResponseEntity<?> UpdateCartItemQuantity(@RequestBody @Valid UpdateCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException {
        cartService.UpdateCartItem(cartItemRequestDto);
        return ResponseEntity.ok("CartItem quantity Updated successfully");
    }

    @DeleteMapping("/Cart/deleteCartItem/{Id}")
    public ResponseEntity<?> DeleteTheCartItem(@PathVariable @Valid Long Id) throws CartItemNotFoundException {
        cartService.DeleteCartItem(Id);
        return ResponseEntity.ok("CartItem deleted successfully");
    }

    @DeleteMapping("/Cart/ClearCart")
    public ResponseEntity<?> ClearTheCart() throws CartItemNotFoundException {
        cartService.ClearCart();
        return ResponseEntity.ok("Cart cleared successfully");
    }

}
