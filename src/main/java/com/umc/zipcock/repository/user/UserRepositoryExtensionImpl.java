package com.umc.zipcock.repository.user;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.umc.zipcock.model.entity.user.QUser;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.enumClass.user.Role;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryExtensionImpl extends QuerydslRepositorySupport implements UserRepositoryExtension {

    public UserRepositoryExtensionImpl( ) {
        super(User.class);
    }

    @Override
    public List<User> getTodayProfile(User currentUser) {
        QUser user = QUser.user;

        JPQLQuery<User> query = from(user)
                .where(user.id.ne(currentUser.getId()).and(user.roleList.contains(Role.MEMBER.getTitle())))
                .orderBy(user.createdDate.desc())
                .limit(10);

        QueryResults<User> results = query.fetchResults();
        return results.getResults();
    }

    @Override
    public List<User> getAroundProfile(User currentUser) {
        QUser user = QUser.user;

        String currentUserCity = Arrays.stream(currentUser.getResidence().split(",")).collect(Collectors.toList()).get(0);

        NumberExpression<Integer> rankPath = new CaseBuilder()
                // 시, 구가 모두 일치하면 우선 순위 1 부여
                .when(user.residence.eq(currentUser.getResidence())).then(1)
                // 시만 일치하면 우선 순위 2 부여
                .when(user.residence.ne(currentUser.getResidence()).and(user.residence.contains(currentUserCity))).then(2)
                .otherwise(3);

        JPQLQuery<User> query = from(user)
                // 본인이 아니고, "회원" 권한을 가졌으며, 시,구가 모두 일치하거나, 시만 일치하는 사용자를 조회하는 쿼리
                .where(user.id.ne(currentUser.getId()).and(user.roleList.contains(Role.MEMBER.getTitle())).and(user.residence.eq(currentUser.getResidence()).or(user.residence.ne(currentUser.getResidence()).and(user.residence.contains(currentUserCity)))))
                .orderBy(rankPath.asc())
                .limit(10);

        QueryResults<User> results = query.fetchResults();
        return results.getResults();
    }
}
