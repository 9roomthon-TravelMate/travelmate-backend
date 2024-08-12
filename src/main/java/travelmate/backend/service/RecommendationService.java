package travelmate.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelmate.backend.entity.Member;
import travelmate.backend.entity.Visited;
import travelmate.backend.repository.MemberRepository;
import travelmate.backend.repository.VisitedRepository;

import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VisitedRepository visitedRepository;

    @Transactional
    public void saveVisitedPlaces(Long travelerId, List<String> contentIds) {
        Member traveler = memberRepository.findById(travelerId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        for (String contentId : contentIds) {
            Visited visited = new Visited();
            visited.setTraveler(traveler);
            visited.setContentId(contentId);
            visitedRepository.save(visited);
        }
    }
}