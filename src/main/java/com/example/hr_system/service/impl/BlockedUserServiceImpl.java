package com.example.hr_system.service.impl;

import com.example.hr_system.dto.admin.BlockedUserRequest;
import com.example.hr_system.dto.admin.BlockedUserResponses;
import com.example.hr_system.entities.BlockedUser;
import com.example.hr_system.entities.User;
import com.example.hr_system.enums.Role;
import com.example.hr_system.exception.BlockedException;
import com.example.hr_system.mapper.BlockedUserMapper;
import com.example.hr_system.repository.BlockedRepository;
import com.example.hr_system.repository.UserRepository;
import com.example.hr_system.service.BlockedUserService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class BlockedUserServiceImpl implements BlockedUserService {

    private final UserRepository userRepository;
    private final BlockedUserMapper blockedUserMapper;
    private final BlockedRepository blockedRepository;


    @Override
    public BlockedUserResponses isBlockedUser(Long userId, BlockedUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user can be null"));
//        System.out.println("getViewingCompanyData " + request.getViewingCompanyData());
//        System.out.println("getViewingAndSearchingForVacancies " + request.getViewingAndSearchingForVacancies());
//        System.out.println("getViewTheStatusOfResponded " + request.getViewTheStatusOfResponded());
//        System.out.println("getCommunicationWithEmployers " + request.getCommunicationWithEmployers());
//        System.out.println("getViewingCandidateData " + request.getViewingCandidateData());
//        System.out.println("getVacancyAndHiringManagement " + request.getVacancyAndHiringManagement());
//        System.out.println("getCommunicationWithJobSeekers " + request.getCommunicationWithJobSeekers());

        if (request.getViewingCompanyData() != null){
            user.getBlockedUser().setViewingCompanyData(request.getViewingCompanyData());
        }
        if (request.getViewingAndSearchingForVacancies()!= null){
            user.getBlockedUser().setViewingAndSearchingForVacancies(request.getViewingAndSearchingForVacancies());
        }
        if (request.getViewTheStatusOfResponded()!= null){
            user.getBlockedUser().setViewTheStatusOfResponded(request.getViewTheStatusOfResponded());
        }
        if (request.getCommunicationWithEmployers()!= null){
            user.getBlockedUser().setCommunicationWithEmployers(request.getCommunicationWithEmployers());
        }
        if (request.getViewingCandidateData()!= null){
            user.getBlockedUser().setViewingCandidateData(request.getViewingCandidateData());
        }
        if (request.getVacancyAndHiringManagement()!= null){
            user.getBlockedUser().setVacancyAndHiringManagement(request.getVacancyAndHiringManagement());
        }
        if (request.getCommunicationWithJobSeekers() !=null){
            user.getBlockedUser().setCommunicationWithJobSeekers(request.getCommunicationWithJobSeekers());
        }
            blockedRepository.save(user.getBlockedUser());

          userRepository.save(user);
        return blockedUserMapper.toDto(user.getBlockedUser());
    }

    @Override
    public User getUsernameFromToken(String token) {

        String[] chunks = token.substring(7).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        JSONParser jsonParser = new JSONParser();
        JSONObject object = null;
        try {
            object = (JSONObject) jsonParser.parse(decoder.decode(chunks[1]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return userRepository.findByEmail(String.valueOf(object.get("sub"))).orElseThrow(() -> new RuntimeException("user can be null"));
    }


}
