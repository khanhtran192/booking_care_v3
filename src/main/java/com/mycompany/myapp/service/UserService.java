package com.mycompany.myapp.service;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.enumeration.FacilityType;
import com.mycompany.myapp.exception.AlreadyExistedException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.AdminUserDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.service.dto.request.CreateDoctorDTO;
import com.mycompany.myapp.service.dto.request.CreateHospitalDTO;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private static final String CREATE_USER_SUCCESS = "Created Information for User: {}";

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final HospitalRepository hospitalRepository;

    private final MailService mailService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        DoctorRepository doctorRepository,
        DepartmentRepository departmentRepository,
        HospitalRepository hospitalRepository,
        MailService mailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.hospitalRepository = hospitalRepository;
        this.mailService = mailService;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(LocalDate.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(LocalDate.now());
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug(CREATE_USER_SUCCESS, newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(LocalDate.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        log.debug(CREATE_USER_SUCCESS, user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDate.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public List<Long> createDoctor(List<CreateDoctorDTO> doctorDTOs) {
        List<Long> doctorCreated = new ArrayList<>();
        User user = new User();
        Doctor doctor = new Doctor();
        List<Authority> authorities = authorityRepository.findAllById(
            Arrays.asList(AuthoritiesConstants.USER, AuthoritiesConstants.DOCTOR)
        );
        for (CreateDoctorDTO doctorDTO : doctorDTOs) {
            boolean newAccount = true;
            if (
                !hospitalRepository.existsById(doctorDTO.getHospitalId()) || !departmentRepository.existsById(doctorDTO.getDepartmentId())
            ) {
                throw new NotFoundException("hospital or department does not exist");
            }
            if (doctorDTO.getEmail() == null) {
                throw new NotFoundException("Email can not be null");
            }
            if (Boolean.TRUE.equals(userRepository.existsByEmail(doctorDTO.getEmail()))) {
                log.debug("User already exists, add role doctor for user: {}", doctorDTO.getEmail());
                newAccount = false;
                user = userRepository.findByEmail(doctorDTO.getEmail());
                user.setAuthorities((new HashSet<>(authorities)));
                userRepository.save(user);
            } else {
                user.setLogin(doctorDTO.getLogin());
                user.setEmail(doctorDTO.getEmail().toLowerCase());
                user.setFirstName(doctorDTO.getFirstName());
                user.setLastName(doctorDTO.getLastName());
                user.setLangKey(Constants.DEFAULT_LANGUAGE);
                user.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
                user.setResetKey(RandomUtil.generateResetKey());
                user.setActivated(true);
                user.setAuthorities((new HashSet<>(authorities)));
                user.setResetDate(LocalDate.now());
                userRepository.save(user);
                log.debug(CREATE_USER_SUCCESS, user);
            }
            if (doctorRepository.findDoctorByUserId(user.getId()) != null) {
                doctor = doctorRepository.findDoctorByUserId(user.getId());
            }
            doctor.setName(doctorDTO.getName());
            doctor.setEmail(doctorDTO.getEmail().toLowerCase());
            doctor.setHospitalId(Math.toIntExact(doctorDTO.getHospitalId()));
            doctor.setDepartment(departmentRepository.findById(doctorDTO.getDepartmentId()).orElse(null));
            doctor.setUserId(user.getId());
            doctor.setActive(true);
            doctorRepository.save(doctor);
            log.debug("Created Information for doctor: {}", doctor);
            doctorCreated.add(doctor.getUserId());
            if (Boolean.TRUE.equals(newAccount)) {
                mailService.sendCreationEmail(user);
            }
        }
        return doctorCreated;
    }

    public void deleteDoctor(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " does not exist"));
        Set<Authority> authorities = new HashSet<>();
        authorities.add(
            authorityRepository
                .findById(AuthoritiesConstants.USER)
                .orElseThrow(() -> new NotFoundException("Authority " + AuthoritiesConstants.USER + " does not exist"))
        );
        user.setAuthorities(authorities);
        userRepository.save(user);
        log.debug("Delete role doctor of user: {}", user);
    }

    public void createHospital(CreateHospitalDTO hospital) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(hospital.getEmail()))) {
            throw new AlreadyExistedException("user " + hospital.getEmail() + " already exists");
        } else if (Boolean.TRUE.equals(hospitalRepository.existsByEmail(hospital.getEmail()))) {
            throw new AlreadyExistedException("hospital " + hospital.getEmail() + " already exists");
        } else {
            List<Authority> authorities = authorityRepository.findAllById(
                Arrays.asList(AuthoritiesConstants.USER, AuthoritiesConstants.HOSPITAL)
            );
            User user = new User();
            user.setLogin(hospital.getLogin());
            user.setEmail(hospital.getEmail().toLowerCase());
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
            user.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
            user.setResetKey(RandomUtil.generateResetKey());
            user.setActivated(true);
            user.setAuthorities((new HashSet<>(authorities)));
            user.setResetDate(LocalDate.now());
            userRepository.save(user);
            log.debug(CREATE_USER_SUCCESS, user);

            Hospital newHospital = new Hospital();
            newHospital.setName(hospital.getName());
            newHospital.setEmail(hospital.getEmail());
            newHospital.setAddress(hospital.getAddress());
            newHospital.setDescription(hospital.getDescription());
            newHospital.setPhoneNumber(hospital.getPhoneNumber());
            newHospital.setUserId(user.getId());
            newHospital.setType(FacilityType.valueOf(hospital.getType()));
            hospitalRepository.save(newHospital);
            log.debug("Created successfully new hospital with name: {}", newHospital.getName());

            mailService.sendCreationEmail(user);
        }
    }

    public void deleteHospital(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " does not exist"));
        Set<Authority> authorities = new HashSet<>();
        authorities.add(
            authorityRepository
                .findById(AuthoritiesConstants.USER)
                .orElseThrow(() -> new NotFoundException("Authority " + AuthoritiesConstants.USER + " does not exist"))
        );
        user.setAuthorities(authorities);
        userRepository.save(user);
        log.debug("Delete role hospital of user: {}", user);
    }

    public void activeHospital(Long id) {
        List<Authority> authorities = authorityRepository.findAllById(
            Arrays.asList(AuthoritiesConstants.USER, AuthoritiesConstants.HOSPITAL)
        );
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " does not exist"));
        user.setAuthorities(new HashSet<>(authorities));
        userRepository.save(user);
        log.debug("Delete role hospital of user: {}", user);
    }

    public void activeDoctor(Long id) {
        List<Authority> authorities = authorityRepository.findAllById(
            Arrays.asList(AuthoritiesConstants.USER, AuthoritiesConstants.DOCTOR)
        );
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " does not exist"));
        user.setAuthorities(new HashSet<>(authorities));
        userRepository.save(user);
        log.debug("Delete role doctor of user: {}", user);
    }
}
