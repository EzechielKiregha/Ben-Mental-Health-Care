package drg.mentalhealth.support.dto;

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

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public long getUpcomingAppointments() {
        return upcomingAppointments;
    }

    public void setUpcomingAppointments(long upcomingAppointments) {
        this.upcomingAppointments = upcomingAppointments;
    }

    public long getSavedResources() {
        return savedResources;
    }

    public void setSavedResources(long savedResources) {
        this.savedResources = savedResources;
    }

    public long getSelfCheckCount() {
        return selfCheckCount;
    }

    public void setSelfCheckCount(long selfCheckCount) {
        this.selfCheckCount = selfCheckCount;
    }
}
