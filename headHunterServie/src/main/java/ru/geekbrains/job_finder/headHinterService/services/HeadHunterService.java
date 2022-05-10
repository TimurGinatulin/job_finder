package ru.geekbrains.job_finder.headHinterService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.job_finder.cor_lib.exception.models.Forbidden;
import ru.geekbrains.job_finder.cor_lib.exception.models.NotFoundException;
import ru.geekbrains.job_finder.headHinterService.models.Vacancy;
import ru.geekbrains.job_finder.headHinterService.models.entity.*;
import ru.geekbrains.job_finder.headHinterService.repositories.*;
import ru.geekbrains.job_finder.routing_lib.dtos.FilterDto;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;
import ru.geekbrains.job_finder.routing_lib.dtos.ResumeDTO;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;
import ru.geekbrains.job_finder.routing_lib.feigns.UserFeignClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HeadHunterService {
    @Autowired
    private HeadHunterApiRepository apiRepository;
    @Autowired
    private HeadHunterDBRepository dbRepository;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private JobMemoryRepository jobMemoryRepository;
    @Autowired
    private FilterDBRepository filterRepository;
    @Autowired
    private CurrencyDBRepository currencyDBRepository;
    @Autowired
    private AreaDBRepository areaDBRepository;
    @Autowired
    private SpecializationDBRepository specializationDBRepository;
    @Autowired
    private EmploymentDBRepository employmentDBRepository;
    @Autowired
    private ExperienceDBRepository experienceDBRepository;
    @Autowired
    private ScheduleDBRepository scheduleDBRepository;
    @Autowired
    private IndustryDBRepository industryDBRepository;

    public UserDto getUserByCode(String code) {
        HHUserSummary hhUserProperties = apiRepository.getHHUserPropertiesByCode(code);
        UserToken userToken = dbRepository.findById(hhUserProperties.getId()).orElse(null);
        UserDto userDto = UserDto.builder()
                .id(hhUserProperties.getId())
                .firstName(hhUserProperties.getFirstName())
                .middleName(hhUserProperties.getMiddleName())
                .email(hhUserProperties.getEmail())
                .lastName(hhUserProperties.getLastName())
                .build();
        if (userToken != null) {
            userDto.setIsNew(false);
        } else {
            userDto.setIsNew(true);
            userFeignClient.addUser(userDto);
        }
        dbRepository.save(new UserToken(hhUserProperties));
        return userDto;
    }

    public void sendResumeResponse(Filter filter) {
        List<Vacancy> filteredVacancy = apiRepository.getFilteredVacancy(filter);
        UserToken userToken = dbRepository.findById(filter.getUserId()).orElse(null);
        List<Long> idVacancyListByIdSummary =
                jobMemoryRepository.findIdVacancyListByIdSummary(filter.getSummaryId());
        if (userToken != null) {
            List<Vacancy> newVacancy = filteredVacancy.stream()
                    .filter(vacancy -> idVacancyListByIdSummary.contains(vacancy.getId()))
                    .toList();
            newVacancy.forEach(vacancy -> {
                try {
                    apiRepository.sendResumeResponse(userToken.getAccessToken(), vacancy, filter);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public List<ResumeDTO> getResumeList(Long id) {
        UserToken userToken = dbRepository.findById(id).orElse(null);
        if (userToken != null) {
            return apiRepository.getResumeList(userToken.getAccessToken());
        }
        return null;
    }

    public List<Filter> getUserFilters(Long userId) {
        return filterRepository.getAllByUserId(userId);
    }

    public Filter getFilterById(Long id) {
        return filterRepository.findById(id).orElse(null);
    }

    public void create(FilterDto filterDto, Long userId) {
        if (filterDto == null) {
            throw new IllegalArgumentException();
        }
        Filter filter = new Filter();
        filter.setUserId(userId);
        fillFilter(filter, filterDto);
        filterRepository.save(filter);
    }

    public void update(FilterDto filterDto, Long userId) {
        if (filterDto == null || filterDto.getId() == null) {
            throw new IllegalArgumentException();
        }

        Filter filter = filterRepository.getById(filterDto.getId());
        if (!filter.getUserId().equals(userId)) {
            throw new Forbidden("У пользователя нет доступа к данному фильтру");
        }
        fillFilter(filter, filterDto);
        filterRepository.save(filter);
    }

    private void fillFilter(Filter filter, FilterDto filterDto) {
        List<Employment> employments = filterDto.getEmployment()
                .stream()
                .map(emp -> employmentDBRepository.getById(emp))
                .collect(Collectors.toList());
        List<Experience> experiences = filterDto.getExperience()
                .stream()
                .map(emp -> experienceDBRepository.getById(emp))
                .collect(Collectors.toList());
        List<Schedule> schedules = filterDto.getSchedule()
                .stream()
                .map(emp -> scheduleDBRepository.getById(emp))
                .collect(Collectors.toList());

        filter.setFilterName(filter.getFilterName());
        filter.setText(filterDto.getText());
        filter.setSalary(filterDto.getSalary());
        filter.setCurrency(currencyDBRepository.findById(filterDto.getCurrencyCode()).orElse(null));
        filter.setArea(areaDBRepository.findById(filterDto.getArea()).orElse(null));
        filter.setSpecialization(specializationDBRepository.findById(Double.parseDouble(filterDto.getSpecializations())).orElse(null));
        filter.setEmployment(employments);
        filter.setExperience(experiences);
        filter.setSchedule(schedules);
        filter.setIndustry(industryDBRepository.findById(Double.parseDouble(filterDto.getIndustry())).orElse(null));
        filter.setUpdatedAt(LocalDateTime.now());
    }

    public void delete(Long id, Long userId) {
        Optional<Filter> filter = filterRepository.findById(id);

        if (filter.isEmpty()) {
            throw new NotFoundException("Фильтр не найден");
        }

        if (!filter.get().getUserId().equals(userId)) {
            throw new Forbidden("У пользователя нет доступа к данному фильтру");
        }

        Filter filterEntity = filter.get();
        filterEntity.setDeletedAt(LocalDateTime.now());
    }
}
