package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.web.rest.HospitalResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataService {

    private final Logger log = LoggerFactory.getLogger(DataService.class);

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PackRepository packageRepository;
    private final UserService userService;
    private final UserRepository userRepository;

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
        UserRepository userRepository
    ) {
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.packageRepository = packageRepository;
        this.userService = userService;
        this.userRepository = userRepository;
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
            for (int i = sublist.size() - 1; i >= 15; i--) {
                int randomIndex = random.nextInt(i + 1);
                Department temp = sublist.get(i);
                sublist.set(i, sublist.get(randomIndex));
                sublist.set(randomIndex, temp);
            }
            for (Department department : sublist) {
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
            for (int i = sublist.size() - 1; i >= 3; i--) {
                int randomIndex = random.nextInt(i + 1);
                Doctor temp = sublist.get(i);
                sublist.set(i, sublist.get(randomIndex));
                sublist.set(randomIndex, temp);
            }
            for (Doctor doctor : sublist) {
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
}
