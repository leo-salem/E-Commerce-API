package API.com.example.E_COMMERCY.service.cart;

import API.com.example.E_COMMERCY.dto.cart.CartResponseDto;
import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.UpdateCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.order.OrderResponseDto;
import API.com.example.E_COMMERCY.exception.customExceptions.CartItemNotFoundException;

public interface CartInterface {
    public CartResponseDto displayCart();
    public void AddCartItem(AddCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException;
    public void UpdateCartItem(UpdateCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException;
    public void DeleteCartItem(Long Id) throws CartItemNotFoundException;
    public OrderResponseDto convertCartToOrder() throws CartItemNotFoundException;
    public void ClearCart() throws CartItemNotFoundException;
}
