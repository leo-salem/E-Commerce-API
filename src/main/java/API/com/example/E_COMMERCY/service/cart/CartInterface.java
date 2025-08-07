package API.com.example.E_COMMERCY.service.cart;

import API.com.example.E_COMMERCY.dto.cart.CartResponseDto;
import API.com.example.E_COMMERCY.dto.cart.request.AddCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.DeleteCartItemRequestDto;
import API.com.example.E_COMMERCY.dto.cart.request.UpdateCartItemRequestDto;
import API.com.example.E_COMMERCY.exception.CartItemNotFoundException;

public interface CartInterface {
    public CartResponseDto displayCart();
    public void AddCartItem(AddCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException;
    public void UpdateCartItem(UpdateCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException;
    public void DeleteCartItem(DeleteCartItemRequestDto cartItemRequestDto) throws CartItemNotFoundException;
    public void convertCartToOrder();
    public void ClearCart();
}
