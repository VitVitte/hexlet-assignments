package exercise.component;

import exercise.dto.UserCreateDTO;
import exercise.mapper.UserMapper;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateDTO();
        userData.setName("hexlet");
        userData.setEmail("hexlet@example.com");
        userData.setPasswordDigest("123");
        var user = userMapper.map(userData);
        userRepository.save(user);
    }
}
