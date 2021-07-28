package me.becycled.backend.aop;

import me.becycled.backend.model.dao.mybatis.DaoFactory;
import me.becycled.backend.model.entity.user.User;
import me.becycled.backend.service.AccessService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author I1yi4
 */
@Aspect
public class UserPrivacyAspect {

    private final UserFieldPrivacyFilter userFieldPrivacyFilter;
    private final AccessService accessService;

    @Autowired
    public UserPrivacyAspect(final DaoFactory daoFactory, final AccessService accessService) {
        this.userFieldPrivacyFilter = new UserFieldPrivacyFilter(daoFactory);
        this.accessService = accessService;
    }

    @Pointcut("execution(* me.becycled.backend.controller.UserController.*(..))")
    void userControllerMethod() {
        //
    }

    @Pointcut("execution(* me.becycled.backend.controller.UserController.getMe(..))")
    void userCurUserMethod() {
        //
    }

    @Pointcut("execution(* me.becycled.backend.controller.UserController.update(..))")
    void userUpdateMethod() {
        //
    }

    @AfterReturning(pointcut = "userControllerMethod() && !(userCurUserMethod() || userUpdateMethod())", returning = "entity")
    public Object hideNotAccessibleField(final ResponseEntity<?> entity) {
        final Object body = entity.getBody();
        if (body == null || !HttpStatus.OK.equals(entity.getStatusCode())) {
            return entity;
        }

        final User curUser = accessService.getCurrentAuthUser();
        userFieldPrivacyFilter.filterNotAccessibleField(body, curUser);
        return entity;
    }
}
