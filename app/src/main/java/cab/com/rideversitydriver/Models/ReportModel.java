package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by CIPL0372 on 12/27/2016.
 */

public class ReportModel {

    String message,result;

    ArrayList<ReportReasonModel> reportResonArray=new ArrayList<ReportReasonModel>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ReportReasonModel> getReportResonArray() {
        return reportResonArray;
    }

    public void setReportResonArray(ArrayList<ReportReasonModel> reportResonArray) {
        this.reportResonArray = reportResonArray;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
