package com.baby_io.baby_io_app.service;

import java.util.Optional;
import static java.util.regex.Pattern.matches;

import com.baby_io.baby_io_app.dto.UserDTO;
import com.baby_io.baby_io_app.dto.UserLoginDTO;
import com.baby_io.baby_io_app.dto.UserSignUpDTO;
import com.baby_io.baby_io_app.entity.LullabyPlayer;
import com.baby_io.baby_io_app.entity.Sensor;
import com.baby_io.baby_io_app.entity.User;
import com.baby_io.baby_io_app.exception.EmailAlreadyUsedException;
import com.baby_io.baby_io_app.exception.UsernameAlreadyUsedException;
import com.baby_io.baby_io_app.exception.UserNotFoundException;
import com.baby_io.baby_io_app.exception.WrongPasswordException;
import com.baby_io.baby_io_app.repository.LullabyPlayerRepository;
import com.baby_io.baby_io_app.repository.SensorRepository;
import com.baby_io.baby_io_app.repository.UserRepository;

import com.baby_io.baby_io_app.types.SensorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final SensorRepository sensorRepository;
  private final LullabyPlayerRepository lullabyPlayerRepository;

  @Autowired
  public UserService(UserRepository userRepository, SensorRepository sensorRepository, LullabyPlayerRepository lullabyPlayerRepository) {
    this.userRepository = userRepository;
    this.sensorRepository = sensorRepository;
    this.lullabyPlayerRepository = lullabyPlayerRepository;
  }

  public Optional<UserDTO> findUserById(Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isEmpty()) {
      return Optional.empty();
    }
    User user = userOptional.get();
    return Optional.of(new UserDTO(user.getId(), user.getUsername(), user.getEmail()));
  }


  public UserDTO signUp(UserSignUpDTO dto) {
    String username = dto.getUsername();
    String email = dto.getEmail();
    String password = dto.getPassword();

    if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyUsedException();
    } else if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyUsedException();
    }

    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    userRepository.save(user);

    for (SensorType type : SensorType.values()) {
      Sensor sensor = new Sensor(type);
      sensor.setUser(user);
      sensorRepository.save(sensor);
    }

    LullabyPlayer lullabyPlayer = new LullabyPlayer();
    lullabyPlayer.setUser(user);
    lullabyPlayerRepository.save(lullabyPlayer);

    return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
  }

  public void deleteUser(Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      userRepository.deleteById(id);
    }
  }

  public UserDTO login(UserLoginDTO dto) {
    Optional<User> userOptional;

    if (dto.getCredential().contains("@")) {
      userOptional = userRepository.findByEmail(dto.getCredential());
    } else {
      userOptional = userRepository.findByUsername(dto.getCredential());
    }

    if (userOptional.isEmpty()) {
      throw new UserNotFoundException();
    }

    User user = userOptional.get();
    boolean passwordMatches = matches(dto.getPassword(), user.getPassword());

    if (!passwordMatches) {
      throw new WrongPasswordException();
    }

    return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
  }

}