package com.umc.zipcock.repository.user;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.umc.zipcock.model.dto.resposne.profile.TodayProfileResDto;
import com.umc.zipcock.model.entity.user.QUser;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.enumClass.user.Role;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

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
}
