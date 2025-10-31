package org.example.alarmservice.domain.Enum;

public enum AlarmType {
    COMMENT,  // 댓글에서
    CHAT,  // 채팅에서 채팅이 올때
    INVITED,  // 초대됨 (지원 수락이 됐을 경우 초대알림이 발송되도록 : 메시지는 ~구인글 지원수락이 되었습니다. 채팅방에서 대화를 시작해보세요
    NEW_APPLY, // 새로운 지원왔다고 알림
    MATCH_COMPLETE  // 지원 완료됨
}
