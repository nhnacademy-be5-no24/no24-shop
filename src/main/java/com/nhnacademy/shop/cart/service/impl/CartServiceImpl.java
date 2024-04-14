package com.nhnacademy.shop.cart.service.impl;

import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.cart.domain.Cart;
import com.nhnacademy.shop.cart.dto.request.CartRequestDto;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;
import com.nhnacademy.shop.cart.exception.InsufficientQuantityException;
import com.nhnacademy.shop.cart.repository.CartRepository;
import com.nhnacademy.shop.cart.service.CartService;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    // 장바구니 조회
    @Override
    @Transactional(readOnly = true)
    public Page<CartResponseDto> getCarts(Long customerNo, int page, int size) {
        Page<Cart> carts = cartRepository.findByMemberCustomerNo(customerNo, PageRequest.of(page, size));
        return carts.map(this::mapToDto);
    }

    private CartResponseDto mapToDto(Cart cart) {
        return CartResponseDto.builder()
                .cartId(cart.getCartId())
                .bookTitle(cart.getBook().getBookTitle())
                .bookFixedPrice(cart.getBook().getBookFixedPrice())
                .bookSalePrice(cart.getBook().getBookSalePrice())
                .bookStatus(cart.getBook().getBookStatus())
                .bookQuantity(cart.getBookQuantity())
                .bookImage(cart.getBook().getBookImage())
                .customerNo(cart.getMember().getCustomerNo())
                .build();
    }

    // 장바구니 등록
    @Override
    @Transactional
    public CartResponseDto saveCart(CartRequestDto cartRequestDto) {
        Member member = memberRepository.findMemberByCustomerNo(cartRequestDto.getCustomerNo());
        Book book = bookRepository.findByBookIsbn(cartRequestDto.getBookIsbn());

        if (book.getBookQuantity() < cartRequestDto.getBookQuantity())
            throw new InsufficientQuantityException();

        Cart newCart = Cart.builder()
                .bookQuantity(cartRequestDto.getBookQuantity())
                .book(book)
                .member(member)
                .build();

        cartRepository.save(newCart);

        return mapToDto(newCart);
    }

    // 장바구니 수정
    @Override
    @Transactional
    public CartResponseDto modifyCart(Long cartId, CartRequestDto cartRequestDto) {
        Cart originCart = cartRepository.findById(cartId).orElseThrow();
        Member member = memberRepository.findMemberByCustomerNo(cartRequestDto.getCustomerNo());
        Book book = bookRepository.findByBookIsbn(cartRequestDto.getBookIsbn());

        Cart updatedCart = Cart.builder()
                .cartId(originCart.getCartId())
                .bookQuantity(cartRequestDto.getBookQuantity())
                .book(book)
                .member(member)
                .build();

        cartRepository.save(updatedCart);

        return mapToDto(updatedCart);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
