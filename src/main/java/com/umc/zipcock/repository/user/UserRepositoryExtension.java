package com.umc.zipcock.repository.user;

import com.umc.zipcock.model.entity.user.User;
import java.util.List;

public interface UserRepositoryExtension {
    List<User> getTodayProfile(User currentUser);

    List<User> getAroundProfile(User currentUser);
}
