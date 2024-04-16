package com.nhnacademy.shop.annotation.aspect;

import com.nhnacademy.shop.member.adapter.MemberAdapter;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-09
 */
@Slf4j
@Aspect
@Component
public class AuthorizationAspect {
    private final MemberAdapter memberAdapter;
    public AuthorizationAspect(MemberAdapter memberAdapter) {
        this.memberAdapter = memberAdapter;
    }

    @Around("@annotation(com.nhnacademy.shop.annotation.AdminAuth)")
    public Object loginCheck(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        Member member = memberAdapter.getMember(request);

        if(Objects.isNull(member)) {
            throw new MemberNotFoundException();
        }
        return  pjp.proceed();
    }
}