package cab.com.rideversitydriver.Models;

/**
 * Created by CIPL0372 on 12/27/2016.
 */

public class ReportReasonModel {

    String reportReason;

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    @Override
    public String toString() {
        return getReportReason();
    }
}
