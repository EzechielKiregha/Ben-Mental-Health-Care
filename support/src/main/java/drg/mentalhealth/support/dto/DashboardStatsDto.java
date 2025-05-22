package drg.mentalhealth.support.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashboardStatsDto {
    private long totalAppointments;
    private long upcomingAppointments;
    private long savedResources;
    private long selfCheckCount;

    public DashboardStatsDto(long totalAppointments, long upcomingAppointments, long savedResources, long selfCheckCount) {
        this.totalAppointments = totalAppointments;
        this.upcomingAppointments = upcomingAppointments;
        this.savedResources = savedResources;
        this.selfCheckCount = selfCheckCount;
    }
}
