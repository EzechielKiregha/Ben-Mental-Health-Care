package drg.mentalhealth.support.service;

import drg.mentalhealth.support.dto.DashboardStatsDto;
import drg.mentalhealth.support.repository.AppointmentRepository;
import drg.mentalhealth.support.repository.MentalHealthResourceRepository;
import drg.mentalhealth.support.repository.SelfCheckResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DashboardService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MentalHealthResourceRepository resourceRepository;

    @Autowired
    private SelfCheckResultRepository selfCheckResultRepository;

    public DashboardStatsDto getStats(Long userId) {
        long totalAppointments = appointmentRepository.count();
        long upcomingAppointments = appointmentRepository.countByAppointmentTimeAfter(LocalDateTime.now());
        long savedResources = resourceRepository.countBySavedByUserId(userId);
        long selfCheckCount = selfCheckResultRepository.countByUserId(userId);

        return new DashboardStatsDto(totalAppointments, upcomingAppointments, savedResources, selfCheckCount);
    }
}
