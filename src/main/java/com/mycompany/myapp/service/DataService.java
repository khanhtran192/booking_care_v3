package com.mycompany.myapp.service;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enumeration.Gender;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.security.AuthoritiesConstants;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//@Transactional
public class DataService {

    private final Logger log = LoggerFactory.getLogger(DataService.class);

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PackRepository packageRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final TimeSlotRepository timeSlotRepository;
    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final TimeSlotService timeSlotService;
    private final DiagnoseRepository diagnoseRepository;
    private static Random random = new Random();

    private static String[] firstNames = { "Trần", "Lê", "Nguyễn", "Lý", "Vũ", "Võ", "Hoàng", "Đào", "Phạm", "Trương" };
    private static String[] surNames = { "Duy", "Văn", "Đình", "Hải", "Trung", "Nhật" };

    private static String[] lastNames = {
        "Khánh",
        "Thảo",
        "Giang",
        "Minh",
        "Dũng",
        "Thịnh",
        "Quang",
        "Chung",
        "Tiến",
        "Thái",
        "Dương",
        "Đức",
        "Chiến",
        "Hải",
        "An",
        "Hưng",
        "Yến",
        "Quân",
        "Nam",
        "Huy",
    };

    public DataService(
        HospitalRepository hospitalRepository,
        DoctorRepository doctorRepository,
        DepartmentRepository departmentRepository,
        PackRepository packageRepository,
        UserService userService,
        UserRepository userRepository,
        OrderRepository orderRepository,
        TimeSlotRepository timeSlotRepository,
        AuthorityRepository authorityRepository,
        PasswordEncoder passwordEncoder,
        CustomerRepository customerRepository,
        TimeSlotService timeSlotService,
        DiagnoseRepository diagnoseRepository
    ) {
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.packageRepository = packageRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.timeSlotService = timeSlotService;
        this.diagnoseRepository = diagnoseRepository;
    }

    public void createAccountHospital() {
        List<Hospital> hospitals = hospitalRepository.findAllByUserIdNull();
        for (Hospital hospital : hospitals) {
            try {
                User user = userService.createHospitalData(hospital, "benhvien" + hospital.getId());
                hospital.setUserId(user.getId());
                hospital.setEmail(user.getEmail());
                hospitalRepository.save(hospital);
            } catch (Exception e) {}
        }
    }

    public void createDepartment() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        for (Hospital hospital : hospitals) {
            List<Department> sublist = new ArrayList<>(departments);
            int randomNumber = random.nextInt(39) + 1;
            for (int i = sublist.size() - 1; i >= randomNumber; i--) {
                int randomIndex = random.nextInt(i + 1);
                Department temp = sublist.get(i);
                sublist.set(i, sublist.get(randomIndex));
                sublist.set(randomIndex, temp);
            }
            List<Department> newList = sublist.subList(sublist.size() - randomNumber, sublist.size());
            for (Department department : newList) {
                Department newDepartment = new Department();
                newDepartment.setActive(true);
                newDepartment.setDescription(department.getDescription());
                newDepartment.setDepartmentName(department.getDepartmentName());
                newDepartment.setHospital(hospital);
                departmentRepository.save(newDepartment);
            }
        }
    }

    public void createPack() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        List<Pack> packs = packageRepository.findAll();
        for (Hospital hospital : hospitals) {
            for (Pack pack : packs) {
                Pack newPack = new Pack();
                newPack.setActive(true);
                newPack.setDescription(pack.getDescription());
                newPack.setName(pack.getName());
                newPack.setHospital(hospital);
                newPack.setPrice(pack.getPrice());
                packageRepository.save(newPack);
            }
        }
    }

    public void createDocTor() {
        List<Hospital> hospitals = hospitalRepository.findAll();
        List<Doctor> doctors = doctorRepository.findAll();
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            List<Doctor> sublist = new ArrayList<>(doctors);
            int randomNumber = random.nextInt(5) + 1;
            for (int i = sublist.size() - 1; i >= 3; i--) {
                int randomIndex = random.nextInt(i + 1);
                Doctor temp = sublist.get(i);
                sublist.set(i, sublist.get(randomIndex));
                sublist.set(randomIndex, temp);
            }
            List<Doctor> newList = sublist.subList(sublist.size() - randomNumber, sublist.size());
            for (Doctor doctor : newList) {
                try {
                    String randomName =
                        firstNames[random.nextInt(firstNames.length)] +
                        " " +
                        surNames[random.nextInt(surNames.length)] +
                        " " +
                        lastNames[random.nextInt(lastNames.length)];
                    Doctor newDoctor = new Doctor();
                    newDoctor.setName(randomName);
                    newDoctor.setStar(doctor.getStar());
                    newDoctor.setRate(doctor.getRate());
                    newDoctor.setSpecialize(doctor.getSpecialize());
                    newDoctor.setDateOfBirth(doctor.getDateOfBirth());
                    newDoctor.setPhoneNumber(doctor.getPhoneNumber());
                    newDoctor.setHospitalId(Math.toIntExact(department.getHospital().getId()));
                    newDoctor.setDepartment(department);
                    newDoctor.setEmail("");
                    doctorRepository.save(newDoctor);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void createAccountDoctor() {
        List<Doctor> doctors = doctorRepository.findAll();
        for (Doctor doctor : doctors) {
            User user = userService.createDoctorData(doctor, "doctor" + doctor.getId());
            doctor.setUserId(user.getId());
            doctor.setEmail(user.getEmail());
            doctorRepository.save(doctor);
        }
    }

    public void createDataTimeSlot() {
        TimeSlot timeSlot1 = new TimeSlot(TimeSlotValue.EIGHT_AM, TimeSlotValue.HALF_PAST_ELEVENT_AM, 300000.0);
        TimeSlot timeSlot2 = new TimeSlot(TimeSlotValue.SEVEN_AM, TimeSlotValue.EIGHT_PM, 250000.0);
        TimeSlot timeSlot3 = new TimeSlot(TimeSlotValue.NINE_AM, TimeSlotValue.TEN_AM, 200000.0);
        TimeSlot timeSlot4 = new TimeSlot(TimeSlotValue.ELEVENT_AM, TimeSlotValue.TWELVE_AM, 250000.0);
        TimeSlot timeSlot5 = new TimeSlot(TimeSlotValue.TWO_PM, TimeSlotValue.HALF_PAST_TWO_PM, 200000.0);
        TimeSlot timeSlot6 = new TimeSlot(TimeSlotValue.THREE_AM, TimeSlotValue.HALF_PAST_THREE_PM, 200000.0);
        TimeSlot timeSlot7 = new TimeSlot(TimeSlotValue.FOUR_PM, TimeSlotValue.FIVE_PM, 200000.0);
        List<TimeSlot> timeSlots = List.of(timeSlot1, timeSlot2, timeSlot3, timeSlot4, timeSlot5, timeSlot6, timeSlot7);
        List<Pack> packs = packageRepository.findAll();
        packs
            .parallelStream()
            .forEach(pack -> {
                List<TimeSlot> sublist = new ArrayList<>(timeSlots);
                for (int i = sublist.size() - 1; i >= 3; i--) {
                    int randomIndex = random.nextInt(i + 1);
                    TimeSlot temp = sublist.get(i);
                    sublist.set(i, sublist.get(randomIndex));
                    sublist.set(randomIndex, temp);
                }
                List<TimeSlot> newList = sublist.subList(sublist.size() - 3, sublist.size());
                for (TimeSlot timeSlot : newList) {
                    timeSlot.setPack(pack);
                    timeSlotRepository.save(timeSlot);
                }
            });
    }

    public void createUser() {
        for (int i = 0; i <= 3000; i++) {
            List<Authority> authorities = authorityRepository.findAllById(Arrays.asList(AuthoritiesConstants.USER));
            User user = new User();
            user.setLogin("user" + i);
            user.setFirstName(firstNames[random.nextInt(firstNames.length)]);
            user.setLastName(lastNames[random.nextInt(lastNames.length)]);
            user.setEmail("user" + i + "@gmail.com");
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
            user.setPassword(passwordEncoder.encode("user"));
            user.setActivated(true);
            user.setAuthorities((new HashSet<>(authorities)));
            user.setResetDate(LocalDate.now());
            user.setCreatedDate(LocalDate.now());
            user.setCreatedBy("admin");
            userRepository.save(user);
        }
    }

    public void createCustomer() {
        Authority roleUser = authorityRepository.findAllById(Arrays.asList(AuthoritiesConstants.USER)).get(0);
        List<User> users = userRepository
            .findAll()
            .stream()
            .filter(user -> !Objects.equals(user.getLogin(), "user"))
            .filter(user -> user.getAuthorities().contains(roleUser) && user.getAuthorities().size() == 1)
            .collect(Collectors.toList());
        for (User user : users) {
            LocalDate startDate = LocalDate.of(1960, 1, 1);
            LocalDate endDate = LocalDate.of(2023, 12, 31);

            // Tính toán số ngày trong khoảng thời gian
            long daysRange = ChronoUnit.DAYS.between(startDate, endDate);

            // Tạo ngẫu nhiên một số nguyên trong khoảng số ngày
            long randomDays = (long) (Math.random() * daysRange);

            // Tính toán ngày ngẫu nhiên
            LocalDate randomDate = startDate.plusDays(randomDays);

            long min = 100_000_000_000L; // Số nhỏ nhất có 12 chữ số
            long max = 999_999_999_999L; // Số lớn nhất có 12 chữ số

            long randomNumber = min + (long) (random.nextDouble() * (max - min + 1));

            Customer customer = new Customer();
            customer.setEmail(user.getEmail());
            customer.setAddress("");
            customer.setPhoneNumber("0961532911");
            customer.setFirstName(user.getFirstName());
            customer.setLastName(user.getLastName());
            customer.setFullName(user.getFirstName() + " " + user.getLastName());
            customer.setGender(Gender.NAM);
            customer.setDateOfBirth(randomDate);
            customer.setUserBooking(user.getId());
            customer.setIdCard(String.valueOf(randomNumber));
            customerRepository.save(customer);
        }
    }

    public void createOrder() {
        List<Customer> customers = customerRepository.findAll();
        Hospital hospital1 = hospitalRepository.findById(1L).orElse(null);
        Hospital hospital2 = hospitalRepository.findById(2L).orElse(null);
        List<Doctor> doctors1 = doctorRepository.findDoctorByHospitalId(Math.toIntExact(hospital1.getId()));
        List<Doctor> doctors2 = doctorRepository.findDoctorByHospitalId(Math.toIntExact(hospital2.getId()));
        List<Doctor> doctor = new ArrayList<>(doctors1);
        doctor.addAll(doctors2);
        List<OrderStatus> orderStatuses = List.of(OrderStatus.PENDING, OrderStatus.APPROVED, OrderStatus.REJECTED, OrderStatus.CANCELED);
        for (Customer customer : customers) {
            LocalDate startDate = LocalDate.of(2021, 1, 1);
            LocalDate endDate = LocalDate.of(2023, 12, 31);

            // Tính toán số ngày trong khoảng thời gian
            long daysRange = ChronoUnit.DAYS.between(startDate, endDate);

            // Tạo ngẫu nhiên một số nguyên trong khoảng số ngày
            long randomDays = (long) (Math.random() * daysRange);

            // Tính toán ngày ngẫu nhiên
            LocalDate randomDate = startDate.plusDays(randomDays);
            int i = random.nextInt(doctor.size() - 1);
            int j = random.nextInt(orderStatuses.size() - 1);
            Doctor d = doctor.get(i);
            List<TimeSlot> timeSlots = timeSlotService.timeSlotFreeOfDoctor(d.getId(), randomDate);
            if (timeSlots.size() != 0) {
                int k = random.nextInt(timeSlots.size());
                TimeSlot timeSlot = timeSlots.get(k);
                Order order = new Order();
                order.setDoctor(d);
                if (randomDate.isBefore(LocalDate.now())) {
                    order.setStatus(OrderStatus.COMPLETE);
                } else {
                    order.setStatus(orderStatuses.get(j));
                }
                order.setCustomer(customer);
                order.setDate(randomDate);
                order.setTimeslot(timeSlot);
                order.setPrice(timeSlots.get(k).getPrice());
                //                order.setSymptom("mệt");
                order.setAddress("");
                orderRepository.save(order);
            }
        }
    }

    public void createDianose() {
        List<Order> orders = orderRepository
            .findAll()
            .stream()
            .filter(order -> order.getStatus() == OrderStatus.COMPLETE)
            .collect(Collectors.toList());
        for (Order order : orders) {
            Diagnose diagnose = new Diagnose();
            diagnose.setOrder(order);
            diagnoseRepository.save(diagnose);
        }
    }

    public void addHospitalId() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setHospitalId(Long.valueOf(order.getDoctor().getHospitalId()));
            orderRepository.save(order);
        }
    }
}
