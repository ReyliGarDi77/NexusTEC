package data.model;

import java.io.Serializable;

/**
 * Entidad de usuario para la comunidad académica Halcones en Nexus-Tec.
 */
public class HalconUser implements Serializable {
    private String id;
    private String institutionalEmail;
    private String fullName;
    private String studentIdCode;
    private String career;
    private int semester;
    private double halconCreditsBalance;
    private double karmaRating;

    public HalconUser() {
    }

    public HalconUser(String id, String institutionalEmail, String fullName, 
                      String studentIdCode, String career, int semester, 
                      double halconCreditsBalance, double karmaRating) {
        this.id = id;
        this.institutionalEmail = institutionalEmail;
        this.fullName = fullName;
        this.studentIdCode = studentIdCode;
        this.career = career;
        this.semester = semester;
        this.halconCreditsBalance = halconCreditsBalance;
        this.karmaRating = karmaRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentIdCode() {
        return studentIdCode;
    }

    public void setStudentIdCode(String studentIdCode) {
        this.studentIdCode = studentIdCode;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getHalconCreditsBalance() {
        return halconCreditsBalance;
    }

    public void setHalconCreditsBalance(double halconCreditsBalance) {
        this.halconCreditsBalance = halconCreditsBalance;
    }

    public double getKarmaRating() {
        return karmaRating;
    }

    public void setKarmaRating(double karmaRating) {
        this.karmaRating = karmaRating;
    }

    @Override
    public String toString() {
        return "HalconUser{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", studentIdCode='" + studentIdCode + '\'' +
                ", career='" + career + '\'' +
                ", halconCreditsBalance=" + halconCreditsBalance +
                ", karmaRating=" + karmaRating +
                '}';
    }
}

