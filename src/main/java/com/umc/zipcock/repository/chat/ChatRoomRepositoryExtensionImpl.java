package com.umc.zipcock.repository.chat;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.umc.zipcock.model.entity.chat.ChatRoom;
import com.umc.zipcock.model.entity.chat.QChatRoom;
import com.umc.zipcock.model.entity.user.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ChatRoomRepositoryExtensionImpl extends QuerydslRepositorySupport implements ChatRoomRepositoryExtension {

    public ChatRoomRepositoryExtensionImpl( ) {
        super(User.class);
    }

    @Override
    public List<ChatRoom> findAllIncludeUser(User currentUser) {
        QChatRoom chatRoom = QChatRoom.chatRoom;

        JPQLQuery<ChatRoom> query = from(chatRoom)
                .where(chatRoom.receiver.eq(currentUser).or(chatRoom.sender.eq(currentUser)))
                .orderBy(chatRoom.modifiedDate.desc());

        QueryResults<ChatRoom> results = query.fetchResults();
        return results.getResults();
    }
}
