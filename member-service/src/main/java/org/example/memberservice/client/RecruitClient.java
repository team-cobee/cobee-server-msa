package org.example.memberservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RECRUIT_SERVICE")
public interface RecruitClient {

    // recruit-service에 구현할 내부 API 엔드포인트
    @DeleteMapping("/recruit/{memberId}")
    void deleteAllRecruitDataByMemberId(@PathVariable("memberId") Long memberId);
}
