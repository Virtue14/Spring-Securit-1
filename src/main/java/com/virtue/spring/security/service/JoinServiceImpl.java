package com.virtue.spring.security.service;

import com.virtue.spring.security.dto.JoinDTO;
import com.virtue.spring.security.entity.UserEntity;
import com.virtue.spring.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinServiceImpl implements JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public JoinServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void JoinProcess(JoinDTO joinDTO) {

        // DB에 이미 동일한 username을 가진 회원이 존재하는지 검증
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());

        if (isUser) {
            return;
        }

        UserEntity data = new UserEntity();
        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_USER");

        userRepository.save(data);

    }

}
